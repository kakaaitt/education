/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.studentparent.web;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.school.studentparent.entity.EduStudentParent;
import com.jeeplus.modules.school.studentparent.service.EduStudentParentService;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 学生家长管理Controller
 * @author 乔功
 * @version 2018-08-30
 */
@Controller
@RequestMapping(value = "${adminPath}/school/studentparent/eduStudentParent")
public class EduStudentParentController extends BaseController {

	@Autowired
	private EduStudentParentService eduStudentParentService;
	
	@ModelAttribute
	public EduStudentParent get(@RequestParam(required=false) String id) {
		EduStudentParent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eduStudentParentService.get(id);
		}
		if (entity == null){
			entity = new EduStudentParent();
		}
		return entity;
	}
	
	/**
	 * 学生家长管理列表页面
	 */
	@RequiresPermissions("school:studentparent:eduStudentParent:list")
	@RequestMapping(value = {"list", ""})
	public String list(EduStudentParent eduStudentParent, Model model) {
		//判断该登录用户是否教师
		User loginUser = UserUtils.getUser();
		List<Role> roleList = loginUser.getRoleList();
		for(Role role : roleList){
			//1:教师,2:学生,3:家长
			if("teacher".equals(role.getEnname())){
				model.addAttribute("office", loginUser.getOffice());
			}
		}
		model.addAttribute("eduStudentParent", eduStudentParent);
		return "modules/school/studentparent/eduStudentParentList";
	}
	
		/**
	 * 学生家长管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("school:studentparent:eduStudentParent:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EduStudentParent eduStudentParent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EduStudentParent> page = eduStudentParentService.findPage(new Page<EduStudentParent>(request, response), eduStudentParent); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑学生家长管理表单页面
	 */
	@RequiresPermissions(value={"school:studentparent:eduStudentParent:view","school:studentparent:eduStudentParent:add","school:studentparent:eduStudentParent:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(EduStudentParent eduStudentParent, Model model) {
		model.addAttribute("eduStudentParent", eduStudentParent);
		return "modules/school/studentparent/eduStudentParentForm";
	}

	/**
	 * 保存学生家长管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"school:studentparent:eduStudentParent:add","school:studentparent:eduStudentParent:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(EduStudentParent eduStudentParent, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(eduStudentParent);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//学生家长关系不能重复绑定,id为空时是添加，不为空时是编辑
		if(StringUtils.isEmpty(eduStudentParent.getId())){
			EduStudentParent studentParent = eduStudentParentService.selectByStudentAndParent(eduStudentParent);
			if(null != studentParent){
				j.setSuccess(false);
				j.setMsg("该学生家长已分配，不能重复操作！");
				return j;
			}
		}
		//新增或编辑表单保存
		eduStudentParentService.save(eduStudentParent);//保存
		j.setSuccess(true);
		j.setMsg("保存学生家长成功");
		return j;
	}
	
	/**
	 * 删除学生家长管理
	 */
	@ResponseBody
	@RequiresPermissions("school:studentparent:eduStudentParent:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(EduStudentParent eduStudentParent) {
		AjaxJson j = new AjaxJson();
		eduStudentParentService.delete(eduStudentParent);
		j.setMsg("删除学生家长管理成功");
		return j;
	}
	
	/**
	 * 批量删除学生家长管理
	 */
	@ResponseBody
	@RequiresPermissions("school:studentparent:eduStudentParent:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			eduStudentParentService.delete(eduStudentParentService.get(id));
		}
		j.setMsg("删除学生家长管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("school:studentparent:eduStudentParent:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(EduStudentParent eduStudentParent, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "学生家长管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EduStudentParent> page = eduStudentParentService.findPage(new Page<EduStudentParent>(request, response, -1), eduStudentParent);
    		new ExportExcel("学生家长管理", EduStudentParent.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出学生家长管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("school:studentparent:eduStudentParent:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EduStudentParent> list = ei.getDataList(EduStudentParent.class);
			for (EduStudentParent eduStudentParent : list){
				try{
					eduStudentParentService.save(eduStudentParent);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条学生家长管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条学生家长管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入学生家长管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入学生家长管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("school:studentparent:eduStudentParent:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "学生家长管理数据导入模板.xlsx";
    		List<EduStudentParent> list = Lists.newArrayList(); 
    		new ExportExcel("学生家长管理数据", EduStudentParent.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}