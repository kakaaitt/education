package com.jeeplus.core.web;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jeeplus.common.beanvalidator.BeanValidators;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.core.mapper.JsonMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.modules.school.teacherclass.entity.EduTeacherClass;
import com.jeeplus.modules.school.teacherclass.service.EduTeacherClassService;
import com.jeeplus.modules.sys.entity.User;

/**
 * 控制器支持类
 * @version 2016-3-23
 */
public abstract class BaseController {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 管理基础路径
	 */
	@Value("${adminPath}")
	protected String adminPath;
	
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;
	
	@Autowired
	
	private EduTeacherClassService eduTeacherClassService;
	

//	/**
//	 * 服务端参数有效性验证
//	 * @param object 验证的实体对象
//	 * @param groups 验证组
//	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
//	 */
	protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(model, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}

	protected String beanValidator(Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			return getMessage(list.toArray(new String[]{}));
		}
		return "";
	}

	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(redirectAttributes, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 添加Model消息
	 * @param messages
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		model.addAttribute("message", sb.toString());
	}
	/**
	 * @param messages
	 */
	protected String getMessage( String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		return sb.toString();
	}

	/**
	 * 添加Flash消息
	 * @param messages
	 */
	protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}
	
	/**
	 * 客户端返回JSON字符串
	 * @param response
	 * @param object
	 * @return
	 */
	protected String renderString(HttpServletResponse response, Object object) {
		return renderString(response, JsonMapper.toJsonString(object));
	}
	
	/**
	 * 客户端返回字符串
	 * @param response
	 * @param string
	 * @return
	 */
	protected String renderString(HttpServletResponse response, String string) {
		try {
			response.reset();
	        response.setContentType("application/json");
	        response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 参数绑定异常
	 */
	@ExceptionHandler({BindException.class, ConstraintViolationException.class, ValidationException.class})
    public String bindException() {  
        return "error/400";
    }
	
	/**
	 * 授权登录异常
	 */
	@ExceptionHandler({AuthenticationException.class})
    public String authenticationException() {  
        return "error/403";
    }
	
	/**
	 *系统登录异常
	 */
	@ExceptionHandler({Exception.class})
    public String exception() {  
        return "error/500";
    }
	
	/**
	 * 初始化数据绑定
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
	 * 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
//			@Override
//			public String getAsText() {
//				Object value = getValue();
//				return value != null ? DateUtils.formatDateTime((Date)value) : "";
//			}
		});
	}
	
	
	/**
	 * 获取bootstrap data分页数据
	 * @param page
	 * @return map对象
	 */
	public <T> Map<String, Object> getBootstrapData(Page page){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", page.getList());
		map.put("total", page.getCount());
		return map;
	}
	
	//获取该学生的所有老师登录名
	protected String[] getLoginNames(User loginUser){
		List<EduTeacherClass> teachers =  eduTeacherClassService.findTeachersByStudent(loginUser.getId());
		String[] loginNames = new String[teachers.size()];
		for(int i = 0;i < teachers.size();i++){
			loginNames[i] = teachers.get(i).getUser().getLoginName();
		}
		return loginNames;
	}
	//获取该学生的所有老师id
	protected String[] getLoginIds(User loginUser){
		List<EduTeacherClass> teachers =  eduTeacherClassService.findTeachersByStudent(loginUser.getId());
		String[] loginIds = new String[teachers.size()];
		for(int i = 0;i < teachers.size();i++){
			loginIds[i] = teachers.get(i).getUser().getId();
		}
		return loginIds;
	}
	
	protected String getFileType(String type){
		if(type.contains("ppt") || type.contains("pptx")){
			return "20";
		}else if(type.contains("pdf") ||type.contains("doc") || type.contains("docx")){
			return "21";
		}else if(type.contains("mp4")){
			return "22";
		}else if(type.contains("mp3")){
			return "23";
		}else if(type.contains("png") || type.contains("jpg")){
			return "24";
		}
		return "";
	}
	
	public static void main(String[] args) {
		String[] arr = "/jeeplus/userfiles/1/questionbank/questiontree/eduQuestionTree/2018/10/题库模板.docx".split("\\.");
		System.out.println(arr[arr.length - 1]);
	}
}
