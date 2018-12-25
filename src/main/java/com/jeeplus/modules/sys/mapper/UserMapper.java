package com.jeeplus.modules.sys.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.sys.entity.EduLoginLog;
import com.jeeplus.modules.sys.entity.User;

/**
 * 用户MAPPER接口
 * @version 2017-05-16
 */
@MyBatisMapper
public interface UserMapper extends BaseMapper<User> {
	
	/**
	 * 根据登录名称查询用户
	 * @param loginName
	 * @return
	 */
	public User getByLoginName(User user);

	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	public List<User> findUserByOfficeId(User user);
	
	/**
	 * 查询全部用户数目
	 * @return
	 */
	public long findAllCount(User user);
	
	/**
	 * 更新用户密码
	 * @param user
	 * @return
	 */
	public int updatePasswordById(User user);
	
	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * @param user
	 * @return
	 */
	public int updateLoginInfo(User user);

	/**
	 * 删除用户角色关联数据
	 * @param user
	 * @return
	 */
	public int deleteUserRole(User user);
	
	/**
	 * 插入用户角色关联数据
	 * @param user
	 * @return
	 */
	public int insertUserRole(User user);
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);
	
	/**
	 * 插入好友
	 */
	public int insertFriend(@Param("id")String id, @Param("userId")String userId, @Param("friendId")String friendId);
	
	/**
	 * 查找好友
	 */
	public User findFriend(@Param("userId")String userId, @Param("friendId")String friendId);
	/**
	 * 删除好友
	 */
	public void deleteFriend(@Param("userId")String userId, @Param("friendId")String friendId);
	
	/**
	 * 
	 * 获取我的好友列表
	 * 
	 */
	public List<User> findFriends(User currentUser);
	
	/**
	 * 
	 * 查询用户-->用来添加到常用联系人
	 * 
	 */
	public List<User> searchUsers(User user);
	
	/**
	 * 
	 */
	
	public List<User>  findListByOffice(User user);
	
	public EduLoginLog isLoginToday(String user);
	
	public void insertLoginLog(EduLoginLog eduLoginLog);
	
	//登录日志
	public List<Map<String,Object>> getLoginCountByYear(Map<String,Object> params);
	
	public List<Map<String,Object>> getLoginCountByMonth(Map<String,Object> params);
	
	public List<Map<String,Object>> getLoginCountByDay(Map<String,Object> params);
	
	//做题
	public List<Map<String,Object>> getAnswerCountByYear(Map<String,Object> params);
	
	public List<Map<String,Object>> getAnswerCountByMonth(Map<String,Object> params);
	
	public List<Map<String,Object>> getAnswerCountByDay(Map<String,Object> params);
	
	//客观题做题数及正确数
	public List<Map<String,Object>> getCorrectAnswerCountByYear(Map<String,Object> params);
	
	public List<Map<String,Object>> getCorrectAnswerCountByMonth(Map<String,Object> params);
	
	public List<Map<String,Object>> getCorrectAnswerCountByDay(Map<String,Object> params);
	
	//资源学习
	public List<Map<String,Object>> getKnowledgeCountByYear(Map<String,Object> params);
	
	public List<Map<String,Object>> getKnowledgeCountByMonth(Map<String,Object> params);
	
	public List<Map<String,Object>> getKnowledgeCountByDay(Map<String,Object> params);
	
	//教师首页
	public Integer getResourceCount(String loginName);
	public Integer getHomeworkCount(String userId);
	public List<Map<String,Object>> getCompleteStudents(String userId);
	public List<Map<String,Object>> getStduyMostResources(String loginName);
	public List<Map<String,Object>> getUseMostQuestions(String loginName);
	
	//学生首页
	public Integer getStudyResourceCount(String userId);
	public Integer getTrainsCount(String userId);
	public Integer getCompleteHomeworkCount(String userId);
	public List<Map<String,Object>> getRecentStudyResources(String userId);
	public List<Map<String,Object>> getStudentStduyMostResources(String userId);
	public List<Map<String,Object>> getCompleteHomeworks(String userId);
	public List<Map<String,Object>> getNeedCompleteHomeworks(String userId);
	
	//家长端
	public User getStudentByParent(String parentid);
	
	public User getUserByLoginName(User user);
}
