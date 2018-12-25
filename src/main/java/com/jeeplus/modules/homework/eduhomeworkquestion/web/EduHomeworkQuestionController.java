/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.eduhomeworkquestion.web;

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
import com.jeeplus.modules.homework.eduhomeworkquestion.entity.EduHomeworkQuestion;
import com.jeeplus.modules.homework.eduhomeworkquestion.service.EduHomeworkQuestionService;

/**
 * 作业题目管理Controller
 * @author 乔功
 * @version 2018-10-07
 */
@Controller
@RequestMapping(value = "${adminPath}/homework/eduhomeworkquestion/eduHomeworkQuestion")
public class EduHomeworkQuestionController extends BaseController {

	@Autowired
	private EduHomeworkQuestionService eduHomeworkQuestionService;
	
	@ModelAttribute
	public EduHomeworkQuestion get(@RequestParam(required=false) String id) {
		EduHomeworkQuestion entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eduHomeworkQuestionService.get(id);
		}
		if (entity == null){
			entity = new EduHomeworkQuestion();
		}
		return entity;
	}
	
	/**
	 * 作业题目管理列表页面
	 */
	@RequiresPermissions("homework:eduhomeworkquestion:eduHomeworkQuestion:list")
	@RequestMapping(value = {"list", ""})
	public String list(EduHomeworkQuestion eduHomeworkQuestion, Model model) {
		model.addAttribute("eduHomeworkQuestion", eduHomeworkQuestion);
		return "modules/homework/eduhomeworkquestion/eduHomeworkQuestionList";
	}
	
		/**
	 * 作业题目管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("homework:eduhomeworkquestion:eduHomeworkQuestion:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EduHomeworkQuestion eduHomeworkQuestion, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EduHomeworkQuestion> page = eduHomeworkQuestionService.findPage(new Page<EduHomeworkQuestion>(request, response), eduHomeworkQuestion); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑作业题目管理表单页面
	 */
	@RequiresPermissions(value={"homework:eduhomeworkquestion:eduHomeworkQuestion:view","homework:eduhomeworkquestion:eduHomeworkQuestion:add","homework:eduhomeworkquestion:eduHomeworkQuestion:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(EduHomeworkQuestion eduHomeworkQuestion, Model model) {
		model.addAttribute("eduHomeworkQuestion", eduHomeworkQuestion);
		return "modules/homework/eduhomeworkquestion/eduHomeworkQuestionForm";
	}

	/**
	 * 保存作业题目管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"homework:eduhomeworkquestion:eduHomeworkQuestion:add","homework:eduhomeworkquestion:eduHomeworkQuestion:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(EduHomeworkQuestion eduHomeworkQuestion, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(eduHomeworkQuestion);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		eduHomeworkQuestionService.save(eduHomeworkQuestion);//保存
		j.setSuccess(true);
		j.setMsg("保存作业题目管理成功");
		return j;
	}
	
	/**
	 * 保存作业题目管理
	 */
	@ResponseBody
	@RequestMapping(value = "saveAll")
	public AjaxJson saveAll(EduHomeworkQuestion eduHomeworkQuestion,String questions, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		//新增或编辑表单保存
		eduHomeworkQuestionService.saveAll(eduHomeworkQuestion,questions);//保存
		j.setSuccess(true);
		j.setMsg("添加成功");
		return j;
	}
	
	/**
	 * 批量删除作业题目管理
	 */
	@ResponseBody
	@RequestMapping(value = "removeAll")
	public AjaxJson removeAll(EduHomeworkQuestion eduHomeworkQuestion,String questions, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		//批量删除
		eduHomeworkQuestionService.removeAll(eduHomeworkQuestion,questions);//删除
		j.setSuccess(true);
		j.setMsg("移除成功");
		return j;
	}
	
	/**
	 * 删除作业题目管理
	 */
	@ResponseBody
	@RequiresPermissions("homework:eduhomeworkquestion:eduHomeworkQuestion:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(EduHomeworkQuestion eduHomeworkQuestion) {
		AjaxJson j = new AjaxJson();
		eduHomeworkQuestionService.delete(eduHomeworkQuestion);
		j.setMsg("删除作业题目管理成功");
		return j;
	}
	
	/**
	 * 批量删除作业题目管理
	 */
	@ResponseBody
	@RequiresPermissions("homework:eduhomeworkquestion:eduHomeworkQuestion:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			eduHomeworkQuestionService.delete(eduHomeworkQuestionService.get(id));
		}
		j.setMsg("删除作业题目管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("homework:eduhomeworkquestion:eduHomeworkQuestion:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(EduHomeworkQuestion eduHomeworkQuestion, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "作业题目管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EduHomeworkQuestion> page = eduHomeworkQuestionService.findPage(new Page<EduHomeworkQuestion>(request, response, -1), eduHomeworkQuestion);
    		new ExportExcel("作业题目管理", EduHomeworkQuestion.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出作业题目管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("homework:eduhomeworkquestion:eduHomeworkQuestion:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EduHomeworkQuestion> list = ei.getDataList(EduHomeworkQuestion.class);
			for (EduHomeworkQuestion eduHomeworkQuestion : list){
				try{
					eduHomeworkQuestionService.save(eduHomeworkQuestion);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条作业题目管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条作业题目管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入作业题目管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入作业题目管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("homework:eduhomeworkquestion:eduHomeworkQuestion:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "作业题目管理数据导入模板.xlsx";
    		List<EduHomeworkQuestion> list = Lists.newArrayList(); 
    		new ExportExcel("作业题目管理数据", EduHomeworkQuestion.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}