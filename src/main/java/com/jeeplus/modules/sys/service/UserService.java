/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;

/**
 * 班级管理Service
 * @author 乔功
 * @version 2018-08-30
 */
@Service
@Transactional(readOnly = true)
public class UserService extends CrudService<UserMapper, User> {

	@Autowired
	private UserMapper userMapper;
	
	public User get(String id) {
		return super.get(id);
	}
	
	public List<User> findList(User user) {
		return super.findList(user);
	}
	
	public Page<User> findPage(Page<User> page, User user) {
		return super.findPage(page, user);
	}
	
	@Transactional(readOnly = false)
	public void save(User user) {
		super.save(user);
	}
	
	@Transactional(readOnly = false)
	public void delete(User user) {
		super.delete(user);
	}
	
	//登录日志
	public List<Map<String,Object>> getLoginCountByYear(Map<String,Object> params){
		return userMapper.getLoginCountByYear(params);
	}
	
	public List<Map<String,Object>> getLoginCountByMonth(Map<String,Object> params){
		return userMapper.getLoginCountByMonth(params);
	}
	
	public List<Map<String,Object>> getLoginCountByDay(Map<String,Object> params){
		return userMapper.getLoginCountByDay(params);
	}
	
	//做题数
	public List<Map<String,Object>> getAnswerCountByYear(Map<String,Object> params){
		return userMapper.getAnswerCountByYear(params);
	}
	
	public List<Map<String,Object>> getAnswerCountByMonth(Map<String,Object> params){
		return userMapper.getAnswerCountByMonth(params);
	}
	
	public List<Map<String,Object>> getAnswerCountByDay(Map<String,Object> params){
		return userMapper.getAnswerCountByDay(params);
	}
	
	//客观题做题数及正确数
	public List<Map<String,Object>> getCorrectAnswerCountByYear(Map<String,Object> params){
		return userMapper.getCorrectAnswerCountByYear(params);
	}
	
	public List<Map<String,Object>> getCorrectAnswerCountByMonth(Map<String,Object> params){
		return userMapper.getCorrectAnswerCountByMonth(params);
	}
	
	public List<Map<String,Object>> getCorrectAnswerCountByDay(Map<String,Object> params){
		return userMapper.getCorrectAnswerCountByDay(params);
	}
	
	//资源学习数
	public List<Map<String,Object>> getKnowledgeCountByYear(Map<String,Object> params){
		return userMapper.getKnowledgeCountByYear(params);
	}
	
	public List<Map<String,Object>> getKnowledgeCountByMonth(Map<String,Object> params){
		return userMapper.getKnowledgeCountByMonth(params);
	}
	
	public List<Map<String,Object>> getKnowledgeCountByDay(Map<String,Object> params){
		return userMapper.getKnowledgeCountByDay(params);
	}
	
	//教师首页
	public Integer getResourceCount(String loginName){
		return userMapper.getResourceCount(loginName);
	}
	public Integer getHomeworkCount(String userId){
		return userMapper.getHomeworkCount(userId);
	}
	public List<Map<String,Object>> getCompleteStudents(String userId){
		return userMapper.getCompleteStudents(userId);
	}
	public List<Map<String,Object>> getStduyMostResources(String loginName){
		return userMapper.getStduyMostResources(loginName);
	}
	public List<Map<String,Object>> getUseMostQuestions(String loginName){
		return userMapper.getUseMostQuestions(loginName);
	}
	
	//学生首页
	public Integer getStudyResourceCount(String userId){
		return userMapper.getStudyResourceCount(userId);
	}
	public Integer getTrainsCount(String userId){
		return userMapper.getTrainsCount(userId);
	}
	public Integer getCompleteHomeworkCount(String userId){
		return userMapper.getCompleteHomeworkCount(userId);
	}
	public List<Map<String,Object>> getRecentStudyResources(String userId){
		return userMapper.getRecentStudyResources(userId);
	}
	public List<Map<String,Object>> getStudentStduyMostResources(String userId){
		return userMapper.getStudentStduyMostResources(userId);
	}
	
	public List<Map<String,Object>> getCompleteHomeworks(String userId){
		return userMapper.getCompleteHomeworks(userId);
	}
	
	public List<Map<String,Object>> getNeedCompleteHomeworks(String userId){
		return userMapper.getNeedCompleteHomeworks(userId);
	}
	
	public User checkUser(User user){
		User user1 = userMapper.getUserByLoginName(user);
		if(user1 == null || !SystemService.validatePassword(user.getPassword(), user1.getPassword())){
			return null;
		}
		return user1;
	}
}