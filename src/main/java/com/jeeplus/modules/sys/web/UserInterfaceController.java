/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.school.classes.entity.EduClasses;
import com.jeeplus.modules.school.studentclass.service.EduStudentClassService;
import com.jeeplus.modules.school.teacherclass.entity.EduTeacherClass;
import com.jeeplus.modules.school.teacherclass.service.EduTeacherClassService;
import com.jeeplus.modules.sys.entity.EduLoginLog;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.service.UserService;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 用户移动端接口Controller
 * @author 乔功
 * @version 2018-09-10
 */
@Controller
@RequestMapping(value = "${adminPath}/user")
public class UserInterfaceController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EduStudentClassService eduStudentClassService;
	
	@Autowired
	private EduTeacherClassService eduTeacherClassService;
	
	/**
	 * 个人信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkSessionId")
	public AjaxJson checkSessionId() {
		return new AjaxJson();
	}
	
	/**
	 * 个人信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "userDetail")
	public AjaxJson userDetail() {
		AjaxJson j = new AjaxJson();
		User loginUser = UserUtils.getUser();
		String id = loginUser.getId();
		User user = systemService.getUserById(id);
		user.setSex(DictUtils.getDictLabel(user.getSex(),"sex", ""));
		user.setPhoto(Global.getConfig("fileUrl") + user.getPhoto());
		if(null == loginUser.getRole()){
			j.setSuccess(false);
			j.setErrorCode("0");
			j.setMsg("无角色信息！");
			return j;
		}
		String roleName = loginUser.getRole().getEnname();
		//获取老师或者学生的班级
		EduClasses classes = new EduClasses();
		if("teacher".equals(roleName)){
			classes = eduTeacherClassService.selectClassesByTeacher(id);
		}else if("student".equals(roleName)){ //学生
			classes = eduStudentClassService.selectClassesByStudent(id);
		}
		user.setClasses(classes);
		j.put("user", user);
		return j;
	}
	
	/**
	 * 用户上传头像
	 * @param user
	 * @param model
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@ResponseBody
	@RequestMapping(value = "headerImg/upload")
	public AjaxJson imageUpload( HttpServletRequest request, HttpServletResponse response,MultipartFile file) throws IllegalStateException, IOException {
		AjaxJson j = new AjaxJson();
		User currentUser = UserUtils.getUser();
		String fileUrl_prefix = Global.getConfig("fileUrl");
		String fileUrl = Global.getAttachmentUrl()+"/sys/user/images/";
		
		// 判断文件是否为空  
        if (!file.isEmpty()) {  
                // 文件保存路径  
            	String realPath = Global.getAttachmentDir()+"/sys/user/images/";
                // 转存文件  
            	FileUtils.createDirectory(realPath);
            	file.transferTo(new File( realPath +  file.getOriginalFilename()));
            	currentUser.setPhoto(Global.getAttachmentUrl()+ "/sys/user/images/" + file.getOriginalFilename());
    			systemService.updateUserInfo(currentUser);
    			j.put("url",fileUrl_prefix + fileUrl + file.getOriginalFilename());
        }else{
        	j.setSuccess(false);
        	j.setErrorCode("0");
        	j.setMsg("文件为空！");
        }

		return j;
	}
	
	/**
	 * 学生登录记录
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "userLogin")
	public AjaxJson userLogin(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		if(!"2".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		User loginUser = UserUtils.getUser();
		String id = loginUser.getId();
		//查看学生今天是否已经登录过
		EduLoginLog ell = systemService.isLoginToday(id);
		if(null == ell){
			ell = new EduLoginLog();
			ell.setId(UUID.randomUUID().toString().replace("-", ""));
			ell.setUser(id);
			systemService.insertLoginLog(ell);
		}
		return j;
	}
	
	
	/**
	 * 教师首页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "teacher/index")
	public AjaxJson teacherIndex(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		
		//loginType:1-教师, 2-学生
//		String loginType = request.getHeader("loginType");
//		if(!"1".equals(loginType)){
//			j.setErrorCode("-2");
//			j.setSuccess(false);
//			j.setMsg("登录权限错误！");
//			return j;
//		}
		User loginUser = UserUtils.getUser();
		String loginName = loginUser.getLoginName();
		String userId = loginUser.getId();
		//资源总数
		Integer resourceCount = userService.getResourceCount(loginName);
		//作业总数
		Integer homeworkCount = userService.getHomeworkCount(userId);
		//已交作业的学生
		List<Map<String,Object>> completeStudents = userService.getCompleteStudents(userId);
		for(Map<String,Object> map : completeStudents){
			map.put("subject", DictUtils.getDictLabel(map.get("subject").toString(), "subject", "-"));
		}
		//学习最多的教学资源
		List<Map<String,Object>> stduyMostResources = userService.getStduyMostResources(loginName);
		//采用次数最多的题目
		List<Map<String,Object>> userMostQuestions = userService.getUseMostQuestions(loginName);
		for(Map<String,Object> question : userMostQuestions){
			question.put("questiontype", DictUtils.getDictLabel(question.get("questiontype").toString(), "questiontype", "-"));
			question.put("subject", DictUtils.getDictLabel(question.get("subjectid").toString(), "subject", "-"));
		}
		j.put("resourceCount", resourceCount);
		j.put("homeworkCount", homeworkCount);
		j.put("completeStudents", completeStudents);
		j.put("stduyMostResources", stduyMostResources);
		j.put("userMostQuestions", userMostQuestions);
		return j;
	}
	
	/**
	 * 学生首页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "student/index")
	public AjaxJson studentIndex(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		if(!"2".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		User loginUser = UserUtils.getUser();
		String userId = loginUser.getId();
		//学习资源总数
//		Integer resourceCount = userService.getStudyResourceCount(userId);
		//练习总数
		Integer resourceCount = userService.getTrainsCount(userId);
		//已完成作业总数
		Integer homeworkCount = userService.getCompleteHomeworkCount(userId);
		//近期学习内容
		List<Map<String,Object>> recentResources = userService.getRecentStudyResources(userId);
		for(Map<String,Object> map : recentResources){
			map.put("subject", DictUtils.getDictLabel(map.get("subject").toString(), "subject", "-"));
		}
		//学习最多的教学资源
//		List<Map<String,Object>> stduyMostResources = userService.getStudentStduyMostResources(userId);
//		for(Map<String,Object> map : stduyMostResources){
//			map.put("subject", DictUtils.getDictLabel(map.get("subject").toString(), "subject", "-"));
//		}
		//近期完成作业
		List<Map<String,Object>> completeHomeworks = userService.getCompleteHomeworks(userId);
		for(Map<String,Object> map : completeHomeworks){
			map.put("iconLink",Global.getConfig("fileUrl") +  Global.getSubjectUrl() + map.get("subject").toString() + ".png");
		}
		//待完成作业
		List<Map<String,Object>> needCompleteHomeworks = userService.getNeedCompleteHomeworks(userId);
		for(Map<String,Object> map : needCompleteHomeworks){
			map.put("iconLink",Global.getConfig("fileUrl") +  Global.getSubjectUrl() + map.get("subject").toString() + ".png");
		}
		j.put("resourceCount", resourceCount);
		j.put("homeworkCount", homeworkCount);
		j.put("recentResources", recentResources);
		j.put("recentCompleteHomeworks", completeHomeworks);
		j.put("needCompleteHomeworks", needCompleteHomeworks);
		return j;
	}
	
	/**
	 * 学情
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "condition")
	public AjaxJson condition(@RequestBody Map<String,Object> paramMap , HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		if(StringUtils.isEmpty(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		User loginUser = UserUtils.getUser();
		String id = loginUser.getId();
		//查询参数
		Map<String,Object> params = new HashMap<String,Object>();
		if("1".equals(loginType)){
			//获取老师班级学科
			if(null == paramMap.get("subject") && null == paramMap.get("classes")){
				List<EduTeacherClass> teacherClasses = eduTeacherClassService.getByTeacher(id);
				if(null == teacherClasses || teacherClasses.size() == 0){
					j.setErrorCode("0");
					j.setSuccess(false);
					j.setMsg("数据异常！");
					return j;
				}
				//默认第一个班级和学科作为查询条件
				params.put("subject", teacherClasses.get(0).getSubject());
				params.put("classes", teacherClasses.get(0).getClasses().getId());
				List<Map<String,Object>> subject = new ArrayList<Map<String,Object>>();
				List<Map<String,Object>> classes = new ArrayList<Map<String,Object>>();
				for(EduTeacherClass etc : teacherClasses){
					Map<String,Object> subjectMap = new HashMap<String,Object>();
					subjectMap.put("id", etc.getSubject());
					subjectMap.put("name", DictUtils.getDictLabel(etc.getSubject(), "subject", "-"));
					subject.add(subjectMap);
					Map<String,Object> classMap = new HashMap<String,Object>();
					classMap.put("id", etc.getClasses().getId());
					classMap.put("name", etc.getClasses().getName());
					classes.add(classMap);
				}
				j.put("subject", subject);
				j.put("classes", classes);
			}
		}else if("2".equals(loginType)){
			params.put("user", id);
		}
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
		Map<String,Object> loginXAxisMonthMap = new HashMap<String,Object>();
		Map<String,Object> loginSeriesMonthMap = new HashMap<String,Object>();
		loginXAxisMonthMap.put("data", x_month);
		loginSeriesMonthMap.put("data", monthLoginCount);
		monthLoginSystem.put("xAxis", x_month);
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
