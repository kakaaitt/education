/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.studentclass.web;

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
import com.jeeplus.modules.school.studentclass.entity.EduStudentClass;
import com.jeeplus.modules.school.studentclass.service.EduStudentClassService;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 学生班级关联Controller
 * @author 乔功
 * @version 2018-08-30
 */
@Controller
@RequestMapping(value = "${adminPath}/school/studentclass/eduStudentClass")
public class EduStudentClassController extends BaseController {

	@Autowired
	private EduStudentClassService eduStudentClassService;
	
	@ModelAttribute
	public EduStudentClass get(@RequestParam(required=false) String id) {
		EduStudentClass entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eduStudentClassService.get(id);
		}
		if (entity == null){
			entity = new EduStudentClass();
		}
		return entity;
	}
	
	/**
	 * 学生班级关联列表页面
	 */
	@RequiresPermissions("school:studentclass:eduStudentClass:list")
	@RequestMapping(value = {"list", ""})
	public String list(EduStudentClass eduStudentClass, Model model) {
		//判断该登录用户是否教师
		User loginUser = UserUtils.getUser();
		List<Role> roleList = loginUser.getRoleList();
		for(Role role : roleList){
			//1:教师,2:学生,3:家长
			if("teacher".equals(role.getEnname())){
				model.addAttribute("office", loginUser.getOffice());
			}
		}
		model.addAttribute("eduStudentClass", eduStudentClass);
		return "modules/school/studentclass/eduStudentClassList";
	}
	
		/**
	 * 学生班级关联列表数据
	 */
	@ResponseBody
	@RequiresPermissions("school:studentclass:eduStudentClass:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EduStudentClass eduStudentClass, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EduStudentClass> page = eduStudentClassService.findPage(new Page<EduStudentClass>(request, response), eduStudentClass); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑学生班级关联表单页面
	 */
	@RequiresPermissions(value={"school:studentclass:eduStudentClass:view","school:studentclass:eduStudentClass:add","school:studentclass:eduStudentClass:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(EduStudentClass eduStudentClass, Model model) {
		model.addAttribute("eduStudentClass", eduStudentClass);
		return "modules/school/studentclass/eduStudentClassForm";
	}

	/**
	 * 保存学生班级关联
	 */
	@ResponseBody
	@RequiresPermissions(value={"school:studentclass:eduStudentClass:add","school:studentclass:eduStudentClass:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(EduStudentClass eduStudentClass, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(eduStudentClass);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//学生同一学年不能重复分配班级，若存在，则不能再次添加,id为空时是添加，不为空时是编辑
		if(StringUtils.isEmpty(eduStudentClass.getId())){
			EduStudentClass studentClass = eduStudentClassService.selectByStudent(eduStudentClass);
			if(null != studentClass){
				j.setSuccess(false);
				j.setMsg("该学生已分配该班级，不能重复添加！");
				return j;
			}
		}
		//新增或编辑表单保存
		eduStudentClassService.save(eduStudentClass);//保存
		j.setSuccess(true);
		j.setMsg("保存学生班级关联成功");
		return j;
	}
	
	/**
	 * 删除学生班级关联
	 */
	@ResponseBody
	@RequiresPermissions("school:studentclass:eduStudentClass:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(EduStudentClass eduStudentClass) {
		AjaxJson j = new AjaxJson();
		eduStudentClassService.delete(eduStudentClass);
		j.setMsg("删除学生班级关联成功");
		return j;
	}
	
	/**
	 * 批量删除学生班级关联
	 */
	@ResponseBody
	@RequiresPermissions("school:studentclass:eduStudentClass:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			eduStudentClassService.delete(eduStudentClassService.get(id));
		}
		j.setMsg("删除学生班级关联成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("school:studentclass:eduStudentClass:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(EduStudentClass eduStudentClass, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "学生班级关联"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EduStudentClass> page = eduStudentClassService.findPage(new Page<EduStudentClass>(request, response, -1), eduStudentClass);
    		new ExportExcel("学生班级关联", EduStudentClass.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出学生班级关联记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("school:studentclass:eduStudentClass:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EduStudentClass> list = ei.getDataList(EduStudentClass.class);
			for (EduStudentClass eduStudentClass : list){
				try{
					eduStudentClassService.save(eduStudentClass);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条学生班级关联记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条学生班级关联记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入学生班级关联失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入学生班级关联数据模板
	 */
	@ResponseBody
	@RequiresPermissions("school:studentclass:eduStudentClass:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "学生班级关联数据导入模板.xlsx";
    		List<EduStudentClass> list = Lists.newArrayList(); 
    		new ExportExcel("学生班级关联数据", EduStudentClass.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}