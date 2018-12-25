/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.eduhomework.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.homework.eduhomework.entity.EduHomework;
import com.jeeplus.modules.homework.eduhomework.entity.EduHomeworkSubmit;
import com.jeeplus.modules.homework.eduhomework.service.EduHomeworkService;
import com.jeeplus.modules.homework.eduhomeworkarrange.entity.EduHomeworkArrange;
import com.jeeplus.modules.questionbank.questions.service.EduQuestionService;
import com.jeeplus.modules.questionbank.questiontree.entity.EduQuestionTree;
import com.jeeplus.modules.school.classes.entity.EduClasses;
import com.jeeplus.modules.school.knowledge.entity.EduKnowledge;
import com.jeeplus.modules.school.studentclass.service.EduStudentClassService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 作业管理Controller
 * @author 乔功
 * @version 2018-09-27
 */
@Controller
@RequestMapping(value = "${adminPath}/homework")
public class EduHomeworkInterfaceController extends BaseController {

	@Autowired
	private EduHomeworkService eduHomeworkService;
	
	@Autowired
	private EduQuestionService eduQuestionService;
	
	@Autowired
	private EduStudentClassService eduStudentClassService; 
	
	
	/**
	 * 获取老师班级信息
	 */
	@ResponseBody
	@RequestMapping(value = "classes")
	public AjaxJson classes(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
		User loginUser = UserUtils.getUser();
		String loginType = request.getHeader("loginType");
		if("2".equals(loginType) || "3".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		String id = loginUser.getId();
		
		List<Map<String,String>> classesList = eduStudentClassService.selectClassesByTeacher(id);
		j.put("classes", classesList);
		return j;
	}
	
	/**
	 * 作业列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "get")
	public AjaxJson get(String id, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		EduHomework homework = eduHomeworkService.get(id);
		//学科图片
		homework.setIconLink(Global.getConfig("fileUrl") + Global.getSubjectUrl() + homework.getSubject() + ".png");
		homework.setSubject(DictUtils.getDictLabel(homework.getSubject(), "subject", ""));
		//获取作业布置班级
		List<String> classNames = eduHomeworkService.findClasses(homework.getId());
		homework.setClassNames(classNames);
		//各题目数量
		List<Map<String,Object>> questionsCount = eduHomeworkService.findQuestionsCount(homework.getId());
		List<Map<String,Object>> counts = new ArrayList<Map<String,Object>>();
		for(int i = 1;i <= 5;i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("questiontype", Integer.valueOf(i).toString());
			map.put("count", 0);
			for(Map<String,Object> count : questionsCount){
				if(Integer.valueOf(i).toString().equals(count.get("questiontype"))){
					map.put("count", count.get("count"));
					break;
				}
			}
			counts.add(map);
		}
		homework.setQuestionsCount(counts);
		//总人数及参与人数
		List<Map<String,Object>> studentsCount = eduHomeworkService.findStudentsCount(homework.getId());
		homework.setStudentsCount(studentsCount);
		j.put("data", homework);
		return j;
	}
	
	/**
	 * 作业列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "list")
	public AjaxJson list(EduHomework eduHomework, HttpServletRequest request, HttpServletResponse response) {
//		Page<EduHomework> page = eduHomeworkService.findPage(new Page<EduHomework>(request, response), eduHomework); 
//		return getBootstrapData(page);
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		User loginUser = UserUtils.getUser();
		if(StringUtils.isEmpty(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		List<EduHomework> list = new ArrayList<EduHomework>();
		if("1".equals(loginType)){
			eduHomework.setCreateBy(loginUser);
			eduHomework.setIsTeacher("1");;//不包括未布置作业
			list = eduHomeworkService.findList(eduHomework);
		}else if("2".equals(loginType)){
			eduHomework.setStudent(loginUser.getId());
			list = eduHomeworkService.findList2(eduHomework);
		}
		
		for(EduHomework homework : list){
			//学科图片
			homework.setIconLink(Global.getConfig("fileUrl") + Global.getSubjectUrl() + homework.getSubject() + ".png");
			homework.setSubject(DictUtils.getDictLabel(homework.getSubject(), "subject", ""));
			//获取作业布置班级
			List<String> classNames = eduHomeworkService.findClasses(homework.getId());
			homework.setClassNames(classNames);
			//各题目数量
			List<Map<String,Object>> questionsCount = eduHomeworkService.findQuestionsCount(homework.getId());
			List<Map<String,Object>> counts = new ArrayList<Map<String,Object>>();
			for(int i = 1;i <= 5;i++){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("questiontype", Integer.valueOf(i).toString());
				map.put("count", 0);
				for(Map<String,Object> count : questionsCount){
					if(Integer.valueOf(i).toString().equals(count.get("questiontype"))){
						map.put("count", count.get("count"));
						break;
					}
				}
				counts.add(map);
			}
			homework.setQuestionsCount(counts);
			//总人数及参与人数
			List<Map<String,Object>> studentsCount = eduHomeworkService.findStudentsCount(homework.getId());
			homework.setStudentsCount(studentsCount);
		}
		j.put("rows", list);
		j.put("total", list.size());
		return j;
	}
	
	/**
	 * 作业题目数据
	 */
	@ResponseBody
	@RequestMapping(value = "question")
	public AjaxJson question(EduHomework eduHomework, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		//如果是学生，并且没有题目类型，则默认只显示单选题（默认也返回全部作业）
		if("2".equals(loginType)){
			eduHomework.setStudent(UserUtils.getUser().getId());
//			if(StringUtils.isEmpty(eduHomework.getQuestiontype())){
//				eduHomework.setQuestiontype("2");
//			}
		}
		List<EduQuestionTree> list = eduHomeworkService.findQuestionList(eduHomework);
		for(EduQuestionTree question : list){
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
		j.put("data", list);
		return j;
	}
	
	/**
	 * 题目批量提交
	 */
	@ResponseBody
	@RequestMapping(value = "jsonSubmit")
	public AjaxJson jsonSubmit(@RequestBody EduHomeworkSubmit eduHomeworks, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		System.out.println(eduHomeworks);
		j.put("eduHomeworks", eduHomeworks);
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		if(!"2".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		String studentId = UserUtils.getUser().getId();
		EduClasses classes = eduStudentClassService.selectClassesByStudent(studentId);
		eduHomeworks.setStudent(studentId);
		if(classes != null){
			eduHomeworks.setClasses(classes.getId());
		}
		eduHomeworkService.submitHomework(eduHomeworks);
		j.setMsg("提交成功！");
		return j;
	}
	
	/**
	 * 题目批量提交
	 */
	@ResponseBody
	@RequestMapping(value = "formSubmit")
	public AjaxJson formSubmit(EduHomeworkSubmit eduHomeworks, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
//		List<EduHomeworkSubmit> list = new ArrayList<EduHomeworkSubmit>();
//		for(int i = 0;i<5;i++){
//			EduHomeworkSubmit submit = new EduHomeworkSubmit();
//			submit.setHomework("homework"+i);
//			submit.setQuestion("question"+i);
//			submit.setAnswer("A");
//			submit.setStudentAnswer("B");
//			list.add(submit);
//		}
//		String jsonstr = JSON.toJSONString(list);
//		System.out.println(jsonstr);
		String answers = request.getParameter("answers");
		List<EduHomeworkSubmit> submits = JSON.parseArray(answers, EduHomeworkSubmit.class);  
		eduHomeworks.setAnswers(submits);
		String studentId = UserUtils.getUser().getId();
		EduClasses classes = eduStudentClassService.selectClassesByStudent(studentId);
		eduHomeworks.setStudent(studentId);
		if(classes != null){
			eduHomeworks.setClasses(classes.getId());
		}
		eduHomeworkService.submitHomework(eduHomeworks);
		j.setMsg("提交成功！");
		return j;
	}
	
	/**
	 * 客观题提交
	 */
	@ResponseBody
	@RequestMapping(value = "objectSubmit")
	public AjaxJson objectSubmit(EduHomeworkSubmit object, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		if(!"2".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		String studentId = UserUtils.getUser().getId();
		EduClasses classes = eduStudentClassService.selectClassesByStudent(studentId);
		object.setStudent(studentId);
		if(classes != null){
			object.setClasses(classes.getId());
		}
		eduHomeworkService.objectSubmit(object);
		j.setMsg("提交成功！");
		return j;
	}
	
	/**
	 * 主观题提交
	 */
	@ResponseBody
	@RequestMapping(value = "subjectSubmit")
	public AjaxJson subjectSubmit(EduHomeworkSubmit subject, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		if(!"2".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		String studentId = UserUtils.getUser().getId();
		EduClasses classes = eduStudentClassService.selectClassesByStudent(studentId);
		subject.setStudent(studentId);
		if(classes != null){
			subject.setClasses(classes.getId());
		}
		eduHomeworkService.subjectSubmit(subject);
		j.setMsg("提交成功！");
		return j;
	}
	
	/**
	 * 学生确认作业提交
	 */
	@ResponseBody
	@RequestMapping(value = "completeSubmit")
	public AjaxJson completeSubmit(EduHomeworkArrange eduHomeworkArrange, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		if(!"2".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		String studentId = UserUtils.getUser().getId();
		eduHomeworkArrange.setStudent(studentId);
		//更新
		eduHomeworkService.updateHomeworkArrange(eduHomeworkArrange);
		j.setMsg("提交成功！");
		return j;
	}
	
	/**
	 * 已交/未交作业学生
	 */
	@ResponseBody
	@RequestMapping(value = "students")
	public AjaxJson students(EduHomeworkArrange eduHomeworkArrange, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
//		String loginType = request.getHeader("loginType");
//		if(!"1".equals(loginType)){
//			j.setErrorCode("-2");
//			j.setSuccess(false);
//			j.setMsg("登录权限错误！");
//			return j;
//		}
		List<EduHomeworkArrange> list = eduHomeworkService.findHomeworkStudents(eduHomeworkArrange);
		for(EduHomeworkArrange arrange : list){
			if(null != arrange.getHeadImg() && !"null".equals(arrange.getHeadImg()) ){
				arrange.setHeadImg(Global.getConfig("fileUrl") + arrange.getHeadImg());
			}
		}
		j.put("rows", list);
		j.put("total", list.size());
		return j;
	}
	
	/**
	 * 主观题批改
	 */
	@ResponseBody
	@RequestMapping(value = "subjectCorrect")
	public AjaxJson subjectCorrect(EduHomeworkSubmit homeworkSubmit, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
//		String loginType = request.getHeader("loginType");
//		if(!"1".equals(loginType)){
//			j.setErrorCode("-2");
//			j.setSuccess(false);
//			j.setMsg("登录权限错误！");
//			return j;
//		}
		//更新
		eduHomeworkService.updateSubjective(homeworkSubmit);
		j.setMsg("批改成功！");
		return j;
	}
	
	/**
	 * 确认批改
	 */
	@ResponseBody
	@RequestMapping(value = "completeCorrect")
	public AjaxJson completeCorrect(EduHomeworkArrange eduHomeworkArrange, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
//		String loginType = request.getHeader("loginType");
//		if(!"1".equals(loginType)){
//			j.setErrorCode("-2");
//			j.setSuccess(false);
//			j.setMsg("登录权限错误！");
//			return j;
//		}
		//更新
		eduHomeworkService.completeCorrect(eduHomeworkArrange);
		return j;
	}
	
	/**
	 * 收作业
	 */
	@ResponseBody
	@RequestMapping(value = "receive")
	public AjaxJson receive(EduHomeworkArrange eduHomeworkArrange, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		if(!"1".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		//更新
		eduHomeworkService.updateHomeworkArrange(eduHomeworkArrange);
		return j;
	}
}