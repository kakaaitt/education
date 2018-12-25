/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.questionbank.questions.web;

import java.util.Date;
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
import com.jeeplus.common.utils.word.ImportWord;
import com.jeeplus.modules.questionbank.questions.entity.EduQuestion;
import com.jeeplus.modules.questionbank.questions.service.EduQuestionService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 题目管理Controller
 * @author 李海军
 * @version 2018-09-16
 */
@Controller
@RequestMapping(value = "${adminPath}/questionbank/questions/eduQuestion")
public class EduQuestionController extends BaseController {

	@Autowired
	private EduQuestionService eduQuestionService;
	
	@ModelAttribute
	public EduQuestion get(@RequestParam(required=false) String id) {
		EduQuestion entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eduQuestionService.get(id);
		}
		if (entity == null){
			entity = new EduQuestion();
		}
		return entity;
	}
	
	/**
	 * 题目管理列表页面
	 */
	@RequiresPermissions("questionbank:questions:eduQuestion:list")
	@RequestMapping(value = {"list", ""})
	public String list(EduQuestion eduQuestion, Model model) {
		model.addAttribute("eduQuestion", eduQuestion);
		return "modules/questionbank/questions/eduQuestionList";
	}
	
		/**
	 * 题目管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("questionbank:questions:eduQuestion:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EduQuestion eduQuestion, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EduQuestion> page = eduQuestionService.findPage(new Page<EduQuestion>(request, response), eduQuestion); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑题目管理表单页面
	 */
	@RequiresPermissions(value={"questionbank:questions:eduQuestion:view","questionbank:questions:eduQuestion:add","questionbank:questions:eduQuestion:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(EduQuestion eduQuestion, Model model) {
		model.addAttribute("eduQuestion", eduQuestion);
		return "modules/questionbank/questions/eduQuestionForm";
	}

	/**
	 * 保存题目管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"questionbank:questions:eduQuestion:add","questionbank:questions:eduQuestion:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(EduQuestion eduQuestion, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(eduQuestion);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		eduQuestionService.save(eduQuestion);//保存
		j.setSuccess(true);
		j.setMsg("保存题目管理成功");
		return j;
	}
	
	/**
	 * 删除题目管理
	 */
	@ResponseBody
	@RequiresPermissions("questionbank:questions:eduQuestion:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(EduQuestion eduQuestion) {
		AjaxJson j = new AjaxJson();
		eduQuestionService.delete(eduQuestion);
		j.setMsg("删除题目管理成功");
		return j;
	}
	
	/**
	 * 批量删除题目管理
	 */
	@ResponseBody
	@RequiresPermissions("questionbank:questions:eduQuestion:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		User user =UserUtils.getUser();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			//查询如果是系统题库 
			EduQuestion question=eduQuestionService.get(id);
			if(question.getCreateBy().getLoginName().equals(user.getLoginName())){ //只能删除自己的
				eduQuestionService.delete(question);
			}else{
				j.setMsg("只能删除自己上传的题目");
				break;
			}
			j.setMsg("删除题目管理成功");
		}
		
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("questionbank:questions:eduQuestion:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(EduQuestion eduQuestion, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "题目管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EduQuestion> page = eduQuestionService.findPage(new Page<EduQuestion>(request, response, -1), eduQuestion);
    		new ExportExcel("题目管理", EduQuestion.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出题目管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Word数据

	 */
	@ResponseBody
	@RequiresPermissions("questionbank:questions:eduQuestion:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			
			String shareType="0";//默认不分享
			String questionBankType="2";//默认老师题库
			
			String subject=request.getParameter("subject");
			String grade=request.getParameter("grade");
			
			
			StringBuilder failureMsg = new StringBuilder();
			ImportWord ei = new ImportWord();
			List<Map<String,Object>> questions=ei.ImportWordByDocx(file);
			//判断是否是管理员--来判断是否是教师题库或管理员题库，管理员题库默认设置共享
			User user=UserUtils.getUser();
			if(user.isAdmin()){
				questionBankType="1";
				shareType="1";
			}
			for (Map<String,Object> qMap : questions){
				System.out.println(qMap.get("id"));
				EduQuestion eduQuestion=new EduQuestion();
//				eduQuestion.setId(qMap.get("id").toString());
				eduQuestion.setQuestionType(DictUtils.getDictValue(qMap.get("questiontype").toString(), "questiontype", "0"));
				eduQuestion.setQuestionBankType(questionBankType);
				eduQuestion.setQuestionText(qMap.get("question").toString());
				eduQuestion.setOptions(qMap.get("options")==null?"":qMap.get("options").toString());
				eduQuestion.setAnswer(qMap.get("answer").toString());
				eduQuestion.setAnaly(qMap.get("explain")==null?"":qMap.get("explain").toString());
				eduQuestion.setSubject(subject);
				eduQuestion.setGrade(grade);
				eduQuestion.setShareType(shareType);
				eduQuestion.setPublicnNum(0);
				eduQuestion.setCreateBy(user);
				eduQuestion.setCreateDate(new Date());
				
				
				try{
					eduQuestionService.save(eduQuestion);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条题目管理记录。");
			}
			j.setSuccess(true);
			j.setMsg( "已成功导入 "+successNum+" 条题目管理记录"+failureMsg);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg("导入题目管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入题目管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("questionbank:questions:eduQuestion:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "题目管理数据导入模板.xlsx";
    		List<EduQuestion> list = Lists.newArrayList(); 
    		new ExportExcel("题目管理数据", EduQuestion.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}