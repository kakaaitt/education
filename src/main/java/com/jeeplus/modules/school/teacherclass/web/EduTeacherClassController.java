/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.teacherclass.web;

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
import com.jeeplus.modules.school.teacherclass.entity.EduTeacherClass;
import com.jeeplus.modules.school.teacherclass.service.EduTeacherClassService;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 老师授课分配管理Controller
 * @author 乔功
 * @version 2018-09-01
 */
@Controller
@RequestMapping(value = "${adminPath}/school/teacherclass/eduTeacherClass")
public class EduTeacherClassController extends BaseController {

	@Autowired
	private EduTeacherClassService eduTeacherClassService;
	
	@ModelAttribute
	public EduTeacherClass get(@RequestParam(required=false) String id) {
		EduTeacherClass entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eduTeacherClassService.get(id);
		}
		if (entity == null){
			entity = new EduTeacherClass();
		}
		return entity;
	}
	
	/**
	 * 老师授课分配管理列表页面
	 */
	@RequiresPermissions("school:teacherclass:eduTeacherClass:list")
	@RequestMapping(value = {"list", ""})
	public String list(EduTeacherClass eduTeacherClass, Model model) {
		//判断该登录用户是否教师
		User loginUser = UserUtils.getUser();
		List<Role> roleList = loginUser.getRoleList();
		for(Role role : roleList){
			//1:教师,2:学生,3:家长
			if("teacher".equals(role.getEnname())){
				model.addAttribute("office", loginUser.getOffice());
			}
		}
		model.addAttribute("eduTeacherClass", eduTeacherClass);
		return "modules/school/teacherclass/eduTeacherClassList";
	}
	
		/**
	 * 老师授课分配管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("school:teacherclass:eduTeacherClass:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EduTeacherClass eduTeacherClass, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EduTeacherClass> page = eduTeacherClassService.findPage(new Page<EduTeacherClass>(request, response), eduTeacherClass); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑老师授课分配管理表单页面
	 */
	@RequiresPermissions(value={"school:teacherclass:eduTeacherClass:view","school:teacherclass:eduTeacherClass:add","school:teacherclass:eduTeacherClass:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(EduTeacherClass eduTeacherClass, Model model) {
		model.addAttribute("eduTeacherClass", eduTeacherClass);
		return "modules/school/teacherclass/eduTeacherClassForm";
	}

	/**
	 * 保存老师授课分配管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"school:teacherclass:eduTeacherClass:add","school:teacherclass:eduTeacherClass:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(EduTeacherClass eduTeacherClass, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(eduTeacherClass);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		
		//老师同一班级同一学科不能重复分配，若存在，则不能再次添加,id为空时是添加，不为空时是编辑
//		if(StringUtils.isEmpty(eduTeacherClass.getId())){
		EduTeacherClass teacherClass = eduTeacherClassService.selectByTeacher(eduTeacherClass);
		if(null != teacherClass){
			j.setSuccess(false);
			j.setMsg("该授课班级已分配，不能重复操作！");
			return j;
		}
		
		//若此次分配是主学科，则查询是否已分配主学科
		if("0".equals(eduTeacherClass.getMainSubject())){
			teacherClass = eduTeacherClassService.selectHasMainSubject(eduTeacherClass);
			if(null != teacherClass){
				j.setSuccess(false);
				j.setMsg("该老师主学科已分配过！");
				return j;
			}
		}
//		}
		//新增或编辑表单保存
		eduTeacherClassService.save(eduTeacherClass);//保存
		j.setSuccess(true);
		j.setMsg("保存老师授课分配管理成功");
		return j;
	}
	
	/**
	 * 删除老师授课分配管理
	 */
	@ResponseBody
	@RequiresPermissions("school:teacherclass:eduTeacherClass:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(EduTeacherClass eduTeacherClass) {
		AjaxJson j = new AjaxJson();
		eduTeacherClassService.delete(eduTeacherClass);
		j.setMsg("删除老师授课分配管理成功");
		return j;
	}
	
	/**
	 * 批量删除老师授课分配管理
	 */
	@ResponseBody
	@RequiresPermissions("school:teacherclass:eduTeacherClass:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			eduTeacherClassService.delete(eduTeacherClassService.get(id));
		}
		j.setMsg("删除老师授课分配管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("school:teacherclass:eduTeacherClass:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(EduTeacherClass eduTeacherClass, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "老师授课分配管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EduTeacherClass> page = eduTeacherClassService.findPage(new Page<EduTeacherClass>(request, response, -1), eduTeacherClass);
    		new ExportExcel("老师授课分配管理", EduTeacherClass.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出老师授课分配管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("school:teacherclass:eduTeacherClass:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EduTeacherClass> list = ei.getDataList(EduTeacherClass.class);
			for (EduTeacherClass eduTeacherClass : list){
				try{
					eduTeacherClassService.save(eduTeacherClass);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条老师授课分配管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条老师授课分配管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入老师授课分配管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入老师授课分配管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("school:teacherclass:eduTeacherClass:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "老师授课分配管理数据导入模板.xlsx";
    		List<EduTeacherClass> list = Lists.newArrayList(); 
    		new ExportExcel("老师授课分配管理数据", EduTeacherClass.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}