/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.train.web;

import java.text.NumberFormat;
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.questionbank.questions.service.EduQuestionService;
import com.jeeplus.modules.questionbank.questiontree.entity.EduQuestionTree;
import com.jeeplus.modules.school.knowledge.entity.EduKnowledge;
import com.jeeplus.modules.school.teacherclass.service.EduTeacherClassService;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.train.entity.EduTrain;
import com.jeeplus.modules.train.entity.EduTrainSubmit;
import com.jeeplus.modules.train.service.EduTrainService;

/**
 * 学生练习Controller
 * @author jeeplus
 * @version 2017-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/train")
public class EduTrainController extends BaseController {
	
	@Autowired
	private EduTrainService eduTrainService;
	
	@Autowired
	private EduQuestionService eduQuestionService;
	
	@Autowired
	private EduTeacherClassService eduTeacherClassService;
	
	
	//  -----------------------  后台管理接口开始   -------------------------------
	/**
	 * 学生练习管理列表页面
	 */
	@RequiresPermissions("train:edutrain:eduTrain:list")
	@RequestMapping(value = {"page/list", ""})
	public String list(EduTrain eduTrain, Model model) {
		model.addAttribute("eduTrain", eduTrain);
		//判断该登录用户是否教师
		User loginUser = UserUtils.getUser();
		List<Role> roleList = loginUser.getRoleList();
		for(Role role : roleList){
			//1:教师,2:学生,3:家长
			if("teacher".equals(role.getEnname())){
				model.addAttribute("office", loginUser.getOffice());
			}
		}
		return "modules/train/edutrain/eduTrainList";
	}
	
	/**
	 * 学生练习管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("train:edutrain:eduTrain:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EduTrain eduTrain, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		User loginUser = UserUtils.getUser();
		if(null == loginUser.getRole()){
			resultMap.put("msg", "无角色信息！");
			return resultMap;
		}
		String roleName = loginUser.getRole().getEnname();
		//获取老师的班级
		List<String> classesIds = new ArrayList<String>();
		if("teacher".equals(roleName)){
			String teacherId = loginUser.getId();
			classesIds = eduTeacherClassService.getclassesIdsByTeacher(teacherId);
			eduTrain.setClassesIds(classesIds);
		}
		
		Page<EduTrain> page = eduTrainService.findPage(new Page<EduTrain>(request, response), eduTrain); 
		return getBootstrapData(page);
	}
	//  -----------------------  后台管理接口结束  -------------------------------
	 
	/**
	 * 学生练习列表
	 */
	@ResponseBody
	@RequestMapping(value = "list")
	public AjaxJson list(EduTrain eduTrain ,HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		if(!"2".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		String id = UserUtils.getUser().getId();
		eduTrain.setStudent(id);
		
		List<EduTrain> trains = eduTrainService.selectList(eduTrain);
		for(EduTrain train : trains){
			train.setIconLink(Global.getConfig("fileUrl") + Global.getSubjectUrl() + train.getSubject() + ".png");
			//计算练习的正确率
			if(train.getCorrectCount() == 0 || train.getQuestionCount() == 0){
				train.setCorrectCount(0);
			}else{
				NumberFormat numberFormat = NumberFormat.getInstance();  
				// 设置精确到小数点后2位  
				numberFormat.setMaximumFractionDigits(0);  
				String result = numberFormat.format((float) train.getCorrectCount() / (float) train.getQuestionCount() * 100);
				train.setCorrectCount(Integer.valueOf(result));
			}
		}
		j.put("rows", trains);
		return j;
	}
	
	/**
	 * 学生新增练习
	 */
	@ResponseBody
	@RequestMapping(value = "newTrain")
	public AjaxJson newTrain(EduTrain eduTrain ,HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		if(!"2".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		if(StringUtils.isEmpty(eduTrain.getSubject())){
			j.setErrorCode("0");
			j.setSuccess(false);
			j.setMsg("学科不能为空！");
			return j;
		}
		String id = UserUtils.getUser().getId();
		eduTrain.setStudent(id);
		String trainId = eduTrainService.addNewTrain(eduTrain);
		eduTrain.setId(trainId);
		List<EduQuestionTree> questions = eduTrainService.findQuestionList(eduTrain);
		for(EduQuestionTree question : questions){
			//单选，多选，判断题，返回选项数组
			if( "1".equals(question.getQuestionType()) || 
				"2".equals(question.getQuestionType()) || 
				"3".equals(question.getQuestionType())){
				question.setOptionList(question.getOptions().split("；"));
			}
			//附件
			List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
			if(!StringUtils.isEmpty(question.getFiles())){
				String[] files = question.getFiles().split("\\|");
				for(int i = 0;i < files.length;i++){
					String file = files[i];
					Map<String,Object> fileMap = new HashMap<String,Object>();
					String[] fileNames = file.split("/");
					String[] types = file.split("\\.");
					String type = getFileType(types[types.length - 1]);
					fileMap.put("fileUrl", Global.getConfig("fileUrl") +  file);
					fileMap.put("name", fileNames[fileNames.length - 1]);
					fileMap.put("type", type);
					fileMap.put("iconUrl", Global.getConfig("fileUrl") + Global.getFileTypeUrl() + type + ".png");
					fileList.add(fileMap);
				}
			}
			question.setFilesList(fileList);
			//知识点
			List<EduKnowledge> knowledges = eduQuestionService.findKnowledgesList(question.getId());
			question.setKonwledgeList(knowledges);
		}
		j.put("id", trainId);
		j.put("data", questions);
		return j;
	}
	
	/**
	 * 学生查看练习题目
	 */
	@ResponseBody
	@RequestMapping(value = "questions")
	public AjaxJson questions(EduTrain eduTrain ,HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
//		String loginType = request.getHeader("loginType");
//		if(!"2".equals(loginType)){
//			j.setErrorCode("-2");
//			j.setSuccess(false);
//			j.setMsg("登录权限错误！");
//			return j;
//		}
		List<EduQuestionTree> questions = eduTrainService.findQuestionList(eduTrain);
		for(EduQuestionTree question : questions){
			//单选，多选，判断题，返回选项数组
			if( "1".equals(question.getQuestionType()) || 
				"2".equals(question.getQuestionType()) || 
				"3".equals(question.getQuestionType())){
				question.setOptionList(question.getOptions().split("；"));
			}
			//附件
			List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
			if(!StringUtils.isEmpty(question.getFiles())){
				String[] files = question.getFiles().split("\\|");
				for(int i = 0;i < files.length;i++){
					String file = files[i];
					Map<String,Object> fileMap = new HashMap<String,Object>();
					String[] fileNames = file.split("/");
					String[] types = file.split("\\.");
					String type = getFileType(types[types.length - 1]);
					fileMap.put("fileUrl", Global.getConfig("fileUrl") +  file);
					fileMap.put("name", fileNames[fileNames.length - 1]);
					fileMap.put("type", type);
					fileMap.put("iconUrl", Global.getConfig("fileUrl") + Global.getFileTypeUrl() + type + ".png");
					fileList.add(fileMap);
				}
			}
			question.setFilesList(fileList);
			//知识点
			List<EduKnowledge> knowledges = eduQuestionService.findKnowledgesList(question.getId());
			question.setKonwledgeList(knowledges);
		}
		j.put("data", questions);
		return j;
	}
	
	/**
	 * 客观题提交
	 */
	@ResponseBody
	@RequestMapping(value = "submit")
	public AjaxJson submit(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		if(!"2".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		String answers = request.getParameter("answers");
		String train = request.getParameter("train");
		EduTrainSubmit submit = new EduTrainSubmit();
		List<EduTrainSubmit> submits = JSON.parseArray(answers, EduTrainSubmit.class);  
		submit.setTrain(train);
		submit.setAnswers(submits);
		eduTrainService.submit(submit);
		j.setMsg("提交成功！");
		return j;
	}
	
	/**
	 * 作业完成
	 */
	@ResponseBody
	@RequestMapping(value = "complete")
	public AjaxJson completeTrain(EduTrain eduTrain, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		if(!"2".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		eduTrainService.completeTrain(eduTrain);
		return j;
	}
	
	
}