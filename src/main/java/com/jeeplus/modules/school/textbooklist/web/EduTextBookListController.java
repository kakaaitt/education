/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.textbooklist.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.school.textbook.entity.Textbook;
import com.jeeplus.modules.school.textbook.service.TextbookService;
import com.jeeplus.modules.school.textbooklist.entity.EduTextBookList;
import com.jeeplus.modules.school.textbooklist.service.EduTextBookListService;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 课本管理列表Controller
 * @author 李海军
 * @version 2018-11-06
 */
@Controller
@RequestMapping(value = "${adminPath}/school/textbooklist/eduTextBookList")
public class EduTextBookListController extends BaseController {

	@Autowired
	private EduTextBookListService eduTextBookListService;
	
	@Autowired
	private TextbookService textbookService;
	
	@ModelAttribute
	public EduTextBookList get(@RequestParam(required=false) String id) {
		EduTextBookList entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eduTextBookListService.get(id);
		}
		if (entity == null){
			entity = new EduTextBookList();
		}
		return entity;
	}
	
	/**
	 * 课本管理列表列表页面
	 */
//	@RequiresPermissions("school:textbooklist:eduTextBookList:list")
	@RequestMapping(value = {"list", ""})
	public String list(EduTextBookList eduTextBookList, Model model) {
		model.addAttribute("eduTextBookList", eduTextBookList);
		//判断该登录用户是否教师
		User loginUser = UserUtils.getUser();
		List<Role> roleList = loginUser.getRoleList();
		for(Role role : roleList){
			//1:教师,2:学生,3:家长
			if("teacher".equals(role.getEnname())){
				model.addAttribute("office", loginUser.getOffice());
			}
		}
		return "modules/school/textbooklist/eduTextBookListList";
	}
	
		/**
	 * 课本管理列表列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("school:textbooklist:eduTextBookList:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EduTextBookList eduTextBookList, HttpServletRequest request, HttpServletResponse response, Model model) {
		//获取当前人员所在学校
		User user=UserUtils.getUser();
		if(!user.isAdmin()){
			eduTextBookList.setOffice(user.getOffice());
		}
		Page<EduTextBookList> page = eduTextBookListService.findPage(new Page<EduTextBookList>(request, response), eduTextBookList); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑课本管理列表表单页面
	 */
//	@RequiresPermissions(value={"school:textbooklist:eduTextBookList:view","school:textbooklist:eduTextBookList:add","school:textbooklist:eduTextBookList:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(EduTextBookList eduTextBookList, Model model) {
		model.addAttribute("eduTextBookList", eduTextBookList);
		return "modules/school/textbooklist/eduTextBookListForm";
	}

	/**
	 * 保存课本管理列表
	 */
	@ResponseBody
//	@RequiresPermissions(value={"school:textbooklist:eduTextBookList:add","school:textbooklist:eduTextBookList:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(EduTextBookList eduTextBookList, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(eduTextBookList);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
//		eduTextBookListService.save(eduTextBookList);//保存
		Textbook textbook=new Textbook();
		textbook.setName(eduTextBookList.getName());
		textbook.setPressid(eduTextBookList.getPressid());
		textbook.setSort(1);
		textbook.setCreateBy(eduTextBookList.getCreateBy());
		textbook.setCreateDate(eduTextBookList.getCreateDate());
		textbook.setUpdateBy(eduTextBookList.getUpdateBy());
		textbook.setUpdateDate(eduTextBookList.getUpdateDate());
	    textbook.setRemarks("0");
		textbook.setGrade(eduTextBookList.getGrade());
		textbook.setSubjectid(eduTextBookList.getSubjectid());
		textbook.setSchoolid(eduTextBookList.getOffice().getId());
		
		textbookService.save(textbook);
		j.setSuccess(true);
		j.setMsg("保存课本管理列表成功");
		return j;
	}
	
	/**
	 * 删除课本管理列表
	 */
	@ResponseBody
	@RequiresPermissions("school:textbooklist:eduTextBookList:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(EduTextBookList eduTextBookList) {
		AjaxJson j = new AjaxJson();
		eduTextBookListService.delete(eduTextBookList);
		j.setMsg("删除课本管理列表成功");
		return j;
	}
	
	/**
	 * 批量删除课本管理列表
	 */
	@ResponseBody
//	@RequiresPermissions("school:textbooklist:eduTextBookList:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			eduTextBookListService.delete(eduTextBookListService.get(id));
		}
		j.setMsg("删除课本管理列表成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("school:textbooklist:eduTextBookList:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(EduTextBookList eduTextBookList, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "课本管理列表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EduTextBookList> page = eduTextBookListService.findPage(new Page<EduTextBookList>(request, response, -1), eduTextBookList);
    		new ExportExcel("课本管理列表", EduTextBookList.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出课本管理列表记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("school:textbooklist:eduTextBookList:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EduTextBookList> list = ei.getDataList(EduTextBookList.class);
			for (EduTextBookList eduTextBookList : list){
				try{
					eduTextBookListService.save(eduTextBookList);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条课本管理列表记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条课本管理列表记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入课本管理列表失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入课本管理列表数据模板
	 */
	@ResponseBody
	@RequiresPermissions("school:textbooklist:eduTextBookList:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "课本管理列表数据导入模板.xlsx";
    		List<EduTextBookList> list = Lists.newArrayList(); 
    		new ExportExcel("课本管理列表数据", EduTextBookList.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}