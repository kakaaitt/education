/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.homework.eduhomework.entity.EduHomework;
import com.jeeplus.modules.homework.eduhomework.service.EduHomeworkService;
import com.jeeplus.modules.school.classes.entity.EduClasses;
import com.jeeplus.modules.school.studentclass.mapper.EduStudentClassMapper;
import com.jeeplus.modules.school.studentclass.service.EduStudentClassService;
import com.jeeplus.modules.school.studentparent.entity.EduStudentParent;
import com.jeeplus.modules.school.studentparent.service.EduStudentParentService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.service.UserService;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.train.entity.EduTrain;
import com.jeeplus.modules.train.service.EduTrainService;

/**
 * 家长端接口Controller
 * @author 乔功
 * @version 2018-09-10
 */
@Controller
@RequestMapping(value = "${adminPath}/parent")
public class ParentAppController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EduStudentClassService eduStudentClassService;
	
	@Autowired
	private EduHomeworkService eduHomeworkService;
	
	@Autowired
	private EduTrainService eduTrainService;
	
	@Autowired
	private EduStudentParentService eduStudentParentService;
	
	@Autowired
	private EduStudentClassMapper eduStudentClassMapper;
	
	/**
	 * 学生信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "child")
	public AjaxJson userDetail(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生，3-家长
		String loginType = request.getHeader("loginType");
		if(!"3".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		User loginUser = UserUtils.getUser();
		//通过家长id获取学生信息
		User user = systemService.getStudentByParent(loginUser.getId());
		if(null == user){
//			j.setErrorCode("0");
//			j.setSuccess(false);
//			j.setMsg("无学生信息！");
			return j;
		}
		user.setSex(DictUtils.getDictLabel(user.getSex(),"sex", ""));
		user.setPhoto(Global.getConfig("fileUrl") + user.getPhoto());
		//获取学生的班级
		EduClasses classes = eduStudentClassService.selectClassesByStudent(user.getId());
		user.setClasses(classes);
		j.put("user", user);
		return j;
	}
	
	
	/**
	 * 家长绑定学生
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "bind")
	public AjaxJson bindStudent(User user,HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生，3-家长
		String loginType = request.getHeader("loginType");
		if(!"3".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		
		User loginUser = UserUtils.getUser();
		//通过家长id获取学生信息
		User student = systemService.getStudentByParent(loginUser.getId());
		if(null != student){
			j.setErrorCode("0");
			j.setSuccess(false);
			j.setMsg("您已绑定过学生！");
			return j;
		}
		
		if(StringUtils.isEmpty(user.getLoginName()) || StringUtils.isEmpty(user.getPassword())){
			j.setErrorCode("0");
			j.setSuccess(false);
			j.setMsg("学生账号密码不能为空！");
			return j;
		}
		
		//查看学生账号密码是否正确
		User user1 = userService.checkUser(user);
		if(null == user1){
			j.setErrorCode("0");
			j.setSuccess(false);
			j.setMsg("学生账号或密码错误！");
			return j;
		}
		EduStudentParent eduStudentParent = new EduStudentParent();
		eduStudentParent.setUser1(user1);
		eduStudentParent.setUser2(loginUser);
		//查看学生是否绑定过家长
		List<EduStudentParent> list = eduStudentParentService.findList(eduStudentParent);
		if(null != list && list.size() > 0){
			j.setErrorCode("0");
			j.setSuccess(false);
			j.setMsg("该学生已绑定过家长！");
			return j;
		}
		eduStudentParent.setCreateDate(new Date());
		//新增家长学生
		eduStudentParentService.save(eduStudentParent);
		j.setMsg("绑定成功！");
		student = systemService.getStudentByParent(loginUser.getId());
		if(null != student){
			EduClasses classes = eduStudentClassMapper.selectClassesByStudent(student.getId());
			student.setClasses(classes);
			j.put("student", student);
		}
		return j;
	}
	
	/**
	 * 家长解绑学生
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "unbind")
	public AjaxJson unbindStudent(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生，3-家长
		String loginType = request.getHeader("loginType");
		if(!"3".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
//		if(StringUtils.isEmpty(user.getLoginName()) || StringUtils.isEmpty(user.getPassword())){
//			j.setErrorCode("0");
//			j.setSuccess(false);
//			j.setMsg("学生账号密码不能为空！");
//			return j;
//		}
		User loginUser = UserUtils.getUser();
		//通过家长id获取学生信息
//		User student = systemService.getStudentByParent(loginUser.getId());
//		if(null == student){
//			j.setErrorCode("0");
//			j.setSuccess(false);
//			j.setMsg("无绑定学生信息！");
//			return j;
//		}
		//若传过来的学生账号密码与查出来的一致，则修改成功
//		if(student.getLoginName().equals(user.getLoginName()) && SystemService.validatePassword(user.getPassword(), student.getPassword())){
			EduStudentParent eduStudentParent = new EduStudentParent();
//			eduStudentParent.setUser1(student);
			eduStudentParent.setUser2(loginUser);
			eduStudentParentService.deleteByStudentAndParent(eduStudentParent);
//			if(count >= 1){
			j.setMsg("解绑成功！");
//			}
//		}else{
//			j.setErrorCode("0");
//			j.setSuccess(false);
//			j.setMsg("学生账号或密码错误！");
//		}
		
		return j;
	}
	
	
	/**
	 * 作业
	 */
	@ResponseBody
	@RequestMapping(value = "homework")
	public AjaxJson list(EduHomework eduHomework, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生，3-家长
		String loginType = request.getHeader("loginType");
		if(!"3".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		User loginUser = UserUtils.getUser();
		//通过家长id获取学生信息
		User user = systemService.getStudentByParent(loginUser.getId());
		if(null == user){
//			j.setErrorCode("0");
//			j.setSuccess(false);
//			j.setMsg("无学生信息！");
			return j;
		}
//		eduHomework.setIds(getLoginIds(user));
		eduHomework.setStudent(user.getId());
		List<EduHomework> list = eduHomeworkService.findList2(eduHomework);
		for(EduHomework homework : list){
			//学科图片
			homework.setIconLink(Global.getConfig("fileUrl") + Global.getSubjectUrl() + homework.getSubject() + ".png");
			homework.setSubject(DictUtils.getDictLabel(homework.getSubject(), "subject", ""));
			//获取作业布置班级
			List<String> classNames = eduHomeworkService.findClasses(homework.getId());
			homework.setClassNames(classNames);
			//各题目数量
			List<Map<String,Object>> questionsCount = eduHomeworkService.findQuestionsCount(homework.getId());
			homework.setQuestionsCount(questionsCount);
			//总人数及参与人数
			List<Map<String,Object>> studentsCount = eduHomeworkService.findStudentsCount(homework.getId());
			homework.setStudentsCount(studentsCount);
		}
		j.put("rows", list);
		j.put("total", list.size());
		return j;
	}
	
	/**
	 * 练习
	 */
	@ResponseBody
	@RequestMapping(value = "train")
	public AjaxJson list(EduTrain eduTrain ,HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生，3-家长
		String loginType = request.getHeader("loginType");
		if(!"3".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		
		User loginUser = UserUtils.getUser();
		//通过家长id获取学生信息
		User user = systemService.getStudentByParent(loginUser.getId());
		if(null == user){
//			j.setErrorCode("0");
//			j.setSuccess(false);
//			j.setMsg("无学生信息！");
			return j;
		}
		eduTrain.setStudent(user.getId());
		List<EduTrain> trains = eduTrainService.selectList(eduTrain);
		for(EduTrain train : trains){
			train.setIconLink(Global.getConfig("fileUrl") + Global.getSubjectUrl() + train.getSubject() + ".png");
		}
		j.put("rows", trains);
		return j;
	}
	
	/**
	 * 微课
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "resource")
	public AjaxJson studentIndex(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生，3-家长
		String loginType = request.getHeader("loginType");
		if(!"3".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		
		User loginUser = UserUtils.getUser();
		//通过家长id获取学生信息
		User user = systemService.getStudentByParent(loginUser.getId());
		if(null == user){
//			j.setErrorCode("0");
//			j.setSuccess(false);
//			j.setMsg("无学生信息！");
			return j;
		}
		String userId = user.getId();
		//近期学习内容
		List<Map<String,Object>> recentResources = userService.getRecentStudyResources(userId);
		for(Map<String,Object> map : recentResources){
			String subject = map.get("subject").toString();
			String filetype = map.get("filetype").toString();
			map.put("subject", DictUtils.getDictLabel(subject, "subject", "-"));
			map.put("subjectLink", Global.getConfig("fileUrl") + Global.getSubjectUrl() + subject + ".png");
			map.put("filetypeLink", Global.getConfig("fileUrl") + Global.getFileTypeUrl() + filetype + ".png");
		}
		j.put("resources", recentResources);
		return j;
	}
	
	/**
	 * 首页学情
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "index")
	public AjaxJson condition(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生，3-家长
		String loginType = request.getHeader("loginType");
		if(!"3".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		
		User loginUser = UserUtils.getUser();
		//通过家长id获取学生信息
		User user = systemService.getStudentByParent(loginUser.getId());
		if(null == user){
//			j.setErrorCode("0");
//			j.setSuccess(false);
//			j.setMsg("无学生信息！");
			return j;
		}
		String id = user.getId();
		//查询参数
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("user", id);
		DecimalFormat df = new DecimalFormat("0");//格式化小数
		//组装返回数据结构
		Map<String,Object> yearMap = new HashMap<String,Object>();
		Map<String,Object> monthMap = new HashMap<String,Object>();
		Map<String,Object> dayMap = new HashMap<String,Object>();
		//获取年月日的x轴
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		String year = format.format(new Date());
		String[] x_year = {year};
		String[] x_month = new String[12];
		for(int i = 1 ; i < 13; i++){
			if(i < 10){
				x_month[i-1] = year + "-0" + i; 
			}else{
				x_month[i-1] = year + "-" + i; 
			}
		}
		String[] x_day = new String[7];
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    	long nowTime = System.currentTimeMillis();
    	long lastTime = nowTime-6*24*60*60*1000;
    	int index = 0;
    	for(long i = lastTime;i<= nowTime; i+=24*60*60*1000){
    		x_day[index] = format1.format(i);
    		index++;
    	}
		params.put("startTime", getPastDate(6) + " 00:00:00");
		//按年登录记录
		List<Map<String,Object>> loginCountByYear = new ArrayList<Map<String,Object>>();//登录
		List<Map<String,Object>> answerCountByYear = new ArrayList<Map<String,Object>>();//做题
		List<Map<String,Object>> answersCountByYear = new ArrayList<Map<String,Object>>();//做题总数
		List<Map<String,Object>> correctAnswerCountByYear = new ArrayList<Map<String,Object>>();//做题正确数
		List<Map<String,Object>> knowledgeCountByYear = new ArrayList<Map<String,Object>>();//资源学习数
		//按月登录记录
		List<Map<String,Object>> loginCountByMonth = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> answerCountByMonth = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> answersCountByMonth = new ArrayList<Map<String,Object>>();//做题总数
		List<Map<String,Object>> correctAnswerCountByMonth = new ArrayList<Map<String,Object>>();//做题正确数
		List<Map<String,Object>> knowledgeCountByMonth = new ArrayList<Map<String,Object>>();//资源学习数
		//按天登录记录
		List<Map<String,Object>> loginCountByDay = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> answerCountByDay = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> answersCountByDay = new ArrayList<Map<String,Object>>();//做题总数
		List<Map<String,Object>> correctAnswerCountByDay = new ArrayList<Map<String,Object>>();//做题正确数
		List<Map<String,Object>> knowledgeCountByDay = new ArrayList<Map<String,Object>>();//资源学习数
		
		//年
		//登录
		Map<String,Object> yearLoginSystem = new HashMap<String,Object>();
		loginCountByYear = userService.getLoginCountByYear(params);
		Integer[] yearLoginCount = {0};
		if(null != loginCountByYear && loginCountByYear.size() > 0){
			yearLoginCount[0] = Integer.valueOf(loginCountByYear.get(0).get("count").toString());
		}
		Map<String,Object> loginXAxisYearMap = new HashMap<String,Object>();
		Map<String,Object> loginSeriesYearMap = new HashMap<String,Object>();
		loginXAxisYearMap.put("data", x_year);
		loginSeriesYearMap.put("data", yearLoginCount);
		yearLoginSystem.put("xAxis", loginXAxisYearMap);
		yearLoginSystem.put("series", loginSeriesYearMap);
		yearMap.put("loginSystem", yearLoginSystem);
		//做题数量
		Map<String,Object> yearAnswer = new HashMap<String,Object>();
		answerCountByYear = userService.getAnswerCountByYear(params);
		Integer[] yearAnswerCount = {0};
		if(null != answerCountByYear && answerCountByYear.size() > 0){
			yearAnswerCount[0] = Integer.valueOf(answerCountByYear.get(0).get("count").toString());
		}
		Map<String,Object> answerXAxisYearMap = new HashMap<String,Object>();
		Map<String,Object> answerSeriesYearMap = new HashMap<String,Object>();
		answerXAxisYearMap.put("data", x_year);
		answerSeriesYearMap.put("data", yearAnswerCount);
		yearAnswer.put("xAxis", answerXAxisYearMap);
		yearAnswer.put("series", answerSeriesYearMap);
		yearMap.put("answer", yearAnswer);
		//做题正确率
		Map<String,Object> yearCorrectAnswer = new HashMap<String,Object>();//总数
		answersCountByYear = userService.getCorrectAnswerCountByYear(params);
		params.put("isCorrect", "1");
		correctAnswerCountByYear = userService.getCorrectAnswerCountByYear(params);//正确数
		Integer[] yearCorrectAnswerCount = {0};
		if(null != answersCountByYear && answersCountByYear.size() > 0 
				&& null != correctAnswerCountByYear && correctAnswerCountByYear.size() > 0){
			if(!"0".equals(answersCountByYear.get(0).get("count").toString())  
					&& !"0".equals(answersCountByYear.get(0).get("count").toString())){
				yearCorrectAnswerCount[0] = Integer.valueOf(df.format((float)Integer.valueOf(correctAnswerCountByYear.get(0).get("count").toString()) * 100/Integer.valueOf(answersCountByYear.get(0).get("count").toString())));
			}
		}
		Map<String,Object> correctAnswerXAxisYearMap = new HashMap<String,Object>();
		Map<String,Object> correctAnswerSeriesYearMap = new HashMap<String,Object>();
		correctAnswerXAxisYearMap.put("data", x_year);
		correctAnswerSeriesYearMap.put("data", yearCorrectAnswerCount);
		yearCorrectAnswer.put("xAxis", correctAnswerXAxisYearMap);
		yearCorrectAnswer.put("series", correctAnswerSeriesYearMap);
		yearMap.put("correctAnswer", yearCorrectAnswer);
		//资源学习数
		Map<String,Object> yearKnowledge = new HashMap<String,Object>();
		knowledgeCountByYear = userService.getKnowledgeCountByYear(params);
		Integer[] yearKnowledgeCount = {0};
		if(null != knowledgeCountByYear && knowledgeCountByYear.size() > 0){
			yearKnowledgeCount[0] = Integer.valueOf(knowledgeCountByYear.get(0).get("count").toString());
		}
		Map<String,Object> knowledgeXAxisYearMap = new HashMap<String,Object>();
		Map<String,Object> knowledgeSeriesYearMap = new HashMap<String,Object>();
		knowledgeXAxisYearMap.put("data", x_year);
		knowledgeSeriesYearMap.put("data", yearKnowledgeCount);
		yearKnowledge.put("xAxis", knowledgeXAxisYearMap);
		yearKnowledge.put("series", knowledgeSeriesYearMap);
		yearMap.put("knowledgeLearn", yearKnowledge);
		
		//月
		//登录
		Map<String,Object> monthLoginSystem = new HashMap<String,Object>();
		monthLoginSystem.put("xAxis", x_month);
		loginCountByMonth = userService.getLoginCountByMonth(params);
		Integer[] monthLoginCount = {0,0,0,0,0,0,0,0,0,0,0,0}; 
		for(int i = 0;i < x_month.length;i++){
			for(Map<String,Object> map : loginCountByMonth){
				if(x_month[i].equals(map.get("time"))){
					monthLoginCount[i] = Integer.valueOf(map.get("count").toString());
					break;
				}
			}
		}
		monthLoginSystem.put("series", monthLoginCount);
		monthMap.put("loginSystem", monthLoginSystem);
		//做题数量
		Map<String,Object> monthAnswer = new HashMap<String,Object>();
		answerCountByMonth = userService.getAnswerCountByMonth(params);
		Integer[] monthAnswerCount = {0,0,0,0,0,0,0,0,0,0,0,0}; 
		for(int i = 0;i < x_month.length;i++){
			for(Map<String,Object> map : answerCountByMonth){
				if(x_month[i].equals(map.get("time"))){
					monthAnswerCount[i] = Integer.valueOf(map.get("count").toString());
					break;
				}
			}
		}
		Map<String,Object> answerXAxisMonthMap = new HashMap<String,Object>();
		Map<String,Object> answerSeriesMonthMap = new HashMap<String,Object>();
		answerXAxisMonthMap.put("data", x_month);
		answerSeriesMonthMap.put("data", monthAnswerCount);
		monthAnswer.put("xAxis", answerXAxisMonthMap);
		monthAnswer.put("series", answerSeriesMonthMap);
		monthMap.put("answer", monthAnswer);
		//做题正确率
		Map<String,Object> monthCorrectAnswer = new HashMap<String,Object>();
		params.put("isCorrect", null);
		answersCountByMonth = userService.getCorrectAnswerCountByMonth(params);
		params.put("isCorrect", "1");
		correctAnswerCountByMonth = userService.getCorrectAnswerCountByMonth(params);
		Integer[] monthCorrectAnswerCount = {0,0,0,0,0,0,0,0,0,0,0,0}; 
		for(int i = 0;i < x_month.length;i++){
			for(Map<String,Object> map : answersCountByMonth){
				if(!"0".equals(map.get("count").toString()) && x_month[i].equals(map.get("time"))){
					for(Map<String,Object> correctMap : correctAnswerCountByMonth){
						if(!"0".equals(correctMap.get("count").toString()) && x_month[i].equals(correctMap.get("time"))){
							monthCorrectAnswerCount[i] = Integer.valueOf(df.format((float)100 * Integer.valueOf(correctMap.get("count").toString())/Integer.valueOf(map.get("count").toString())));
							break;
						}
					}
					break;
				}
			}
		}
		Map<String,Object> correctAnswerXAxisMonthMap = new HashMap<String,Object>();
		Map<String,Object> correctAnswerSeriesMonthMap = new HashMap<String,Object>();
		correctAnswerXAxisMonthMap.put("data", x_month);
		correctAnswerSeriesMonthMap.put("data", monthCorrectAnswerCount);
		monthCorrectAnswer.put("xAxis", correctAnswerXAxisMonthMap);
		monthCorrectAnswer.put("series", correctAnswerSeriesMonthMap);
		monthMap.put("correctAnswer", monthCorrectAnswer);
		//资源学习数
		Map<String,Object> monthKnowledge = new HashMap<String,Object>();
		knowledgeCountByMonth = userService.getKnowledgeCountByMonth(params);
		Integer[] monthKnowledgeCount = {0,0,0,0,0,0,0,0,0,0,0,0}; 
		for(int i = 0;i < x_month.length;i++){
			for(Map<String,Object> map : knowledgeCountByMonth){
				if(x_month[i].equals(map.get("time"))){
					monthKnowledgeCount[i] = Integer.valueOf(map.get("count").toString());
					break;
				}
			}
		}
		Map<String,Object> knowledgeXAxisMonthMap = new HashMap<String,Object>();
		Map<String,Object> knowledgeSeriesMonthMap = new HashMap<String,Object>();
		knowledgeXAxisMonthMap.put("data", x_month);
		knowledgeSeriesMonthMap.put("data", monthKnowledgeCount);
		monthKnowledge.put("xAxis", knowledgeXAxisMonthMap);
		monthKnowledge.put("series", knowledgeSeriesMonthMap);
		monthMap.put("knowledgeLearn", monthKnowledge);
		
		//日
		//登录
		Map<String,Object> dayLoginSystem = new HashMap<String,Object>();
		loginCountByDay = userService.getLoginCountByDay(params);
		Integer[] dayLoginCount = {0,0,0,0,0,0,0}; 
		for(int i = 0;i < x_day.length;i++){
			for(Map<String,Object> map : loginCountByDay){
				if(x_day[i].equals(map.get("time"))){
					dayLoginCount[i] = Integer.valueOf(map.get("count").toString());
					break;
				}
			}
		}
		Map<String,Object> loginXAxisDayMap = new HashMap<String,Object>();
		Map<String,Object> loginSeriesDayMap = new HashMap<String,Object>();
		loginXAxisDayMap.put("data", x_day);
		loginSeriesDayMap.put("data", dayLoginCount);
		dayLoginSystem.put("xAxis", loginXAxisDayMap);
		dayLoginSystem.put("series", loginSeriesDayMap);
		dayMap.put("loginSystem", dayLoginSystem);
		//做题数量
		Map<String,Object> dayAnswer = new HashMap<String,Object>();
		answerCountByDay = userService.getAnswerCountByDay(params);
		Integer[] dayAnswerCount = {0,0,0,0,0,0,0}; 
		for(int i = 0;i < x_day.length;i++){
			for(Map<String,Object> map : answerCountByDay){
				if(x_day[i].equals(map.get("time"))){
					dayAnswerCount[i] = Integer.valueOf(map.get("count").toString());
					break;
				}
			}
		}
		Map<String,Object> answerXAxisDayMap = new HashMap<String,Object>();
		Map<String,Object> answerSeriesDayMap = new HashMap<String,Object>();
		answerXAxisDayMap.put("data", x_day);
		answerSeriesDayMap.put("data", dayAnswerCount);
		dayAnswer.put("xAxis", answerXAxisDayMap);
		dayAnswer.put("series", answerSeriesDayMap);
		dayMap.put("answer", dayAnswer);
		//做题正确率
		Map<String,Object> dayCorrectAnswer = new HashMap<String,Object>();
		params.put("isCorrect", null);
		answersCountByDay = userService.getCorrectAnswerCountByDay(params);
		params.put("isCorrect", "1");
		correctAnswerCountByDay = userService.getCorrectAnswerCountByDay(params);
		Integer[] dayCorrectAnswerCount = {0,0,0,0,0,0,0}; 
		for(int i = 0;i < x_day.length;i++){
			for(Map<String,Object> map : answersCountByDay){
				if(!"0".equals(map.get("count").toString()) && x_day[i].equals(map.get("time"))){
					for(Map<String,Object> correctMap : correctAnswerCountByDay){
						if(!"0".equals(correctMap.get("count").toString()) && x_day[i].equals(correctMap.get("time"))){
							dayCorrectAnswerCount[i] =  Integer.valueOf(df.format((float)100 *Integer.valueOf(correctMap.get("count").toString()) / Integer.valueOf(map.get("count").toString())));
						}
					}
					break;
				}
			}
		}
		Map<String,Object> correctAnswerXAxisDayMap = new HashMap<String,Object>();
		Map<String,Object> correctAnswerSeriesDayMap = new HashMap<String,Object>();
		correctAnswerXAxisDayMap.put("data", x_day);
		correctAnswerSeriesDayMap.put("data", dayCorrectAnswerCount);
		dayCorrectAnswer.put("xAxis", correctAnswerXAxisDayMap);
		dayCorrectAnswer.put("series", correctAnswerSeriesDayMap);
		dayMap.put("correctAnswer", dayCorrectAnswer);
		//资源学习数
		Map<String,Object> dayKnowledge = new HashMap<String,Object>();
		knowledgeCountByDay = userService.getKnowledgeCountByDay(params);
		Integer[] dayKnowledgeCount = {0,0,0,0,0,0,0}; 
		for(int i = 0;i < x_day.length;i++){
			for(Map<String,Object> map : knowledgeCountByDay){
				if(x_day[i].equals(map.get("time"))){
					dayKnowledgeCount[i] = Integer.valueOf(map.get("count").toString());
					break;
				}
			}
		}
		Map<String,Object> knowledgeXAxisDayMap = new HashMap<String,Object>();
		Map<String,Object> knowledgeSeriesDayMap = new HashMap<String,Object>();
		knowledgeXAxisDayMap.put("data", x_day);
		knowledgeSeriesDayMap.put("data", dayKnowledgeCount);
		dayKnowledge.put("xAxis", knowledgeXAxisDayMap);
		dayKnowledge.put("series", knowledgeSeriesDayMap);
		dayMap.put("knowledgeLearn", dayKnowledge);
		
		j.put("year", yearMap);
		j.put("month", monthMap);
		j.put("day", dayMap);
		return j;
	}
	
	/**
     * 获取过去第n天的日期
     * @param days
     * @return
     */
    public String getPastDate(int days) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - days);
    	Date today = calendar.getTime();
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	String result = format.format(today);
    	return result;
    }
	
}
