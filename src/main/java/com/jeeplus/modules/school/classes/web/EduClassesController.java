/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.classes.web;

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
import com.jeeplus.modules.school.classes.entity.EduClasses;
import com.jeeplus.modules.school.classes.service.EduClassesService;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 班级管理Controller
 * @author 乔功
 * @version 2018-08-30
 */
@Controller
@RequestMapping(value = "${adminPath}/school/classes/eduClasses")
public class EduClassesController extends BaseController {

	@Autowired
	private EduClassesService eduClassesService;
	
	@ModelAttribute
	public EduClasses get(@RequestParam(required=false) String id) {
		EduClasses entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eduClassesService.get(id);
		}
		if (entity == null){
			entity = new EduClasses();
		}
		return entity;
	}
	
	/**
	 * 班级管理列表页面
	 */
	@RequiresPermissions("school:classes:eduClasses:list")
	@RequestMapping(value = {"list", ""})
	public String list(EduClasses eduClasses, Model model) {
		//判断该登录用户是否教师
		User loginUser = UserUtils.getUser();
		List<Role> roleList = loginUser.getRoleList();
		for(Role role : roleList){
			//1:教师,2:学生,3:家长
			if("teacher".equals(role.getEnname())){
				model.addAttribute("office", loginUser.getOffice());
			}
		}
		model.addAttribute("eduClasses", eduClasses);
		return "modules/school/classes/eduClassesList";
	}
	
		/**
	 * 班级管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("school:classes:eduClasses:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EduClasses eduClasses, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EduClasses> page = eduClassesService.findPage(new Page<EduClasses>(request, response), eduClasses); 
		return getBootstrapData(page);
	}

		/**
	 * 班级管理列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("school:classes:eduClasses:list")
	@RequestMapping(value = "data2")
	public Map<String, Object> data2(EduClasses eduClasses, HttpServletRequest request, HttpServletResponse response, Model model) {
//		System.out.println("00000000000000000000000000000000000000000");
		User user=UserUtils.getUser();
		if(!user.isAdmin()){
			eduClasses.setRemarks(user.getId());
		}
		List<EduClasses> userClass = eduClassesService.findListByUser(eduClasses); 
		Page<EduClasses> page=new Page<>();
		page.setList(userClass);
		return getBootstrapData(page);
	}
	/**
	 * 查看，增加，编辑班级管理表单页面
	 */
	@RequiresPermissions(value={"school:classes:eduClasses:view","school:classes:eduClasses:add","school:classes:eduClasses:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(EduClasses eduClasses, Model model) {
		model.addAttribute("eduClasses", eduClasses);
		return "modules/school/classes/eduClassesForm";
	}

	/**
	 * 保存班级管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"school:classes:eduClasses:add","school:classes:eduClasses:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(EduClasses eduClasses, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(eduClasses);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		eduClassesService.save(eduClasses);//保存
		j.setSuccess(true);
		j.setMsg("保存班级管理成功");
		return j;
	}
	
	/**
	 * 删除班级管理
	 */
	@ResponseBody
	@RequiresPermissions("school:classes:eduClasses:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(EduClasses eduClasses) {
		AjaxJson j = new AjaxJson();
		eduClassesService.delete(eduClasses);
		j.setMsg("删除班级管理成功");
		return j;
	}
	
	/**
	 * 批量删除班级管理
	 */
	@ResponseBody
	@RequiresPermissions("school:classes:eduClasses:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			eduClassesService.delete(eduClassesService.get(id));
		}
		j.setMsg("删除班级管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("school:classes:eduClasses:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(EduClasses eduClasses, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "班级管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EduClasses> page = eduClassesService.findPage(new Page<EduClasses>(request, response, -1), eduClasses);
    		new ExportExcel("班级管理", EduClasses.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出班级管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("school:classes:eduClasses:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EduClasses> list = ei.getDataList(EduClasses.class);
			for (EduClasses eduClasses : list){
				try{
					eduClassesService.save(eduClasses);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条班级管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条班级管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入班级管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入班级管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("school:classes:eduClasses:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "班级管理数据导入模板.xlsx";
    		List<EduClasses> list = Lists.newArrayList(); 
    		new ExportExcel("班级管理数据", EduClasses.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}