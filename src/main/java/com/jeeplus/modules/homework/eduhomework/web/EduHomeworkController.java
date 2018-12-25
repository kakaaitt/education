/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.eduhomework.web;

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
import com.jeeplus.modules.homework.eduhomework.entity.EduHomework;
import com.jeeplus.modules.homework.eduhomework.service.EduHomeworkService;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 作业管理Controller
 * @author 乔功
 * @version 2018-09-27
 */
@Controller
@RequestMapping(value = "${adminPath}/homework/eduhomework/eduHomework")
public class EduHomeworkController extends BaseController {

	@Autowired
	private EduHomeworkService eduHomeworkService;
	
	@ModelAttribute
	public EduHomework get(@RequestParam(required=false) String id) {
		EduHomework entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eduHomeworkService.get(id);
		}
		if (entity == null){
			entity = new EduHomework();
		}
		return entity;
	}
	
	/**
	 * 作业管理列表页面
	 */
	@RequiresPermissions("homework:eduhomework:eduHomework:list")
	@RequestMapping(value = {"list", ""})
	public String list(EduHomework eduHomework, Model model) {
		model.addAttribute("eduHomework", eduHomework);
		//判断该登录用户是否教师
		User loginUser = UserUtils.getUser();
		List<Role> roleList = loginUser.getRoleList();
		for(Role role : roleList){
			//1:教师,2:学生,3:家长
			if("teacher".equals(role.getEnname())){
				model.addAttribute("office", loginUser.getOffice());
			}
		}
		return "modules/homework/eduhomework/eduHomeworkList";
	}
	
		/**
	 * 作业管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("homework:eduhomework:eduHomework:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EduHomework eduHomework, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EduHomework> page = eduHomeworkService.findPage(new Page<EduHomework>(request, response), eduHomework); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑作业管理表单页面
	 */
	@RequiresPermissions(value={"homework:eduhomework:eduHomework:view","homework:eduhomework:eduHomework:add","homework:eduhomework:eduHomework:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(EduHomework eduHomework, Model model) {
		model.addAttribute("eduHomework", eduHomework);
		return "modules/homework/eduhomework/eduHomeworkForm";
	}

	/**
	 * 保存作业管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"homework:eduhomework:eduHomework:add","homework:eduhomework:eduHomework:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(EduHomework eduHomework, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(eduHomework);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		eduHomeworkService.save(eduHomework);//保存
		j.setSuccess(true);
		j.setMsg("保存作业管理成功");
		return j;
	}
	
	/**
	 * 删除作业管理
	 */
	@ResponseBody
	@RequiresPermissions("homework:eduhomework:eduHomework:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(EduHomework eduHomework) {
		AjaxJson j = new AjaxJson();
		eduHomeworkService.delete(eduHomework);
		j.setMsg("删除作业管理成功");
		return j;
	}
	
	/**
	 * 批量删除作业管理
	 */
	@ResponseBody
	@RequiresPermissions("homework:eduhomework:eduHomework:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			eduHomeworkService.delete(eduHomeworkService.get(id));
		}
		j.setMsg("删除作业管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("homework:eduhomework:eduHomework:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(EduHomework eduHomework, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "作业管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EduHomework> page = eduHomeworkService.findPage(new Page<EduHomework>(request, response, -1), eduHomework);
    		new ExportExcel("作业管理", EduHomework.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出作业管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("homework:eduhomework:eduHomework:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EduHomework> list = ei.getDataList(EduHomework.class);
			for (EduHomework eduHomework : list){
				try{
					eduHomeworkService.save(eduHomework);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条作业管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条作业管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入作业管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入作业管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("homework:eduhomework:eduHomework:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "作业管理数据导入模板.xlsx";
    		List<EduHomework> list = Lists.newArrayList(); 
    		new ExportExcel("作业管理数据", EduHomework.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

	/**
	 * 保存作业管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"homework:eduhomework:eduHomework:add","homework:eduhomework:eduHomework:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveQuestions")
	public AjaxJson saveQuestions(EduHomework eduHomework, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		//新增或编辑表单保存
		eduHomeworkService.save(eduHomework);//保存
		j.setSuccess(true);
		j.setMsg("保存作业管理成功");
		return j;
	}
}