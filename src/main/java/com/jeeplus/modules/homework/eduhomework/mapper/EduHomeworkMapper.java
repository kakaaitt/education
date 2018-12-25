/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.eduhomework.mapper;

import java.util.List;
import java.util.Map;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.homework.eduhomework.entity.EduHomework;
import com.jeeplus.modules.homework.eduhomework.entity.EduHomeworkSubmit;
import com.jeeplus.modules.homework.eduhomeworkarrange.entity.EduHomeworkArrange;
import com.jeeplus.modules.questionbank.questiontree.entity.EduQuestionTree;

/**
 * 作业管理MAPPER接口
 * @author 乔功
 * @version 2018-09-27
 */
@MyBatisMapper
public interface EduHomeworkMapper extends BaseMapper<EduHomework> {
	
	public List<String> findClasses(String homeworkId);
	
	public List<Map<String,Object>> findQuestionsCount(String homeworkId);
	
	public List<Map<String,Object>> findStudentsCount(String homeworkId);
	
	public List<EduQuestionTree> findQuestionList(EduHomework eduHomework);
	
	public EduHomeworkSubmit findIfAnswered(EduHomeworkSubmit homeworkSubmit);
	
	public void updateHomeworkSubmit(EduHomeworkSubmit homeworkSubmit);
	
	public void insertHomeworkSubmit(EduHomeworkSubmit homeworkSubmit);
	
	public void delAnswers(EduHomeworkSubmit homeworkSubmit);
	
	public void insertAllHomeworkSubmit(List<EduHomeworkSubmit> list);
	
	public List<EduHomeworkSubmit> findStudentAnswerList(EduHomeworkSubmit homeworkSubmit);
	
	public List<EduHomeworkArrange> findHomeworkStudents(EduHomeworkArrange eduHomeworkArrange);
	
	public void updateSubjective(EduHomeworkSubmit eduHomeworkSubmit);
	
	public void completeCorrect(EduHomeworkArrange eduHomeworkArrange);
	
	public void updateHomeworkArrange(EduHomeworkArrange eduHomeworkArrange);
	
	public void updateStatus(EduHomework eduHomework);
	
	public List<EduHomework> findList2(EduHomework eduHomework);
	
	public Integer getNoCorrectCount(String homework);
	
	public List<EduHomeworkArrange> getAllNoSubmitStudent(EduHomework eduHomework);
}