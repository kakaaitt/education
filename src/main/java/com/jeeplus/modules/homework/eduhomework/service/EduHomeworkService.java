/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.eduhomework.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.utils.misc.IdGenerator;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.homework.eduhomework.entity.EduHomework;
import com.jeeplus.modules.homework.eduhomework.entity.EduHomeworkSubmit;
import com.jeeplus.modules.homework.eduhomework.mapper.EduHomeworkMapper;
import com.jeeplus.modules.homework.eduhomeworkarrange.entity.EduHomeworkArrange;
import com.jeeplus.modules.questionbank.questiontree.entity.EduQuestionTree;

/**
 * 作业管理Service
 * @author 乔功
 * @version 2018-09-27
 */
@Service
@Transactional(readOnly = true)
public class EduHomeworkService extends CrudService<EduHomeworkMapper, EduHomework> {

	@Autowired
	private EduHomeworkMapper eduHomeworkMapper;
	
	public EduHomework get(String id) {
		return super.get(id);
	}
	
	public List<EduHomework> findList(EduHomework eduHomework) {
		return super.findList(eduHomework);
	}
	
	public List<EduHomework> findList2(EduHomework eduHomework) {
		return eduHomeworkMapper.findList2(eduHomework);
	}
	
	public Page<EduHomework> findPage(Page<EduHomework> page, EduHomework eduHomework) {
		return super.findPage(page, eduHomework);
	}
	
	@Transactional(readOnly = false)
	public void save(EduHomework eduHomework) {
		super.save(eduHomework);
	}
	
	@Transactional(readOnly = false)
	public void delete(EduHomework eduHomework) {
		super.delete(eduHomework);
	}
	
	public List<String> findClasses(String homeworkId){
		return eduHomeworkMapper.findClasses(homeworkId);
	}
	
	public List<Map<String,Object>> findQuestionsCount(String homeworkId){
		return eduHomeworkMapper.findQuestionsCount(homeworkId);
	}
	public List<Map<String,Object>> findStudentsCount(String homeworkId){
		return eduHomeworkMapper.findStudentsCount(homeworkId);
	}
	
	public List<EduQuestionTree> findQuestionList(EduHomework eduHomework){
		return eduHomeworkMapper.findQuestionList(eduHomework);
	}
	
	public EduHomeworkSubmit findIfAnswered(EduHomeworkSubmit homeworkSubmit){
		return eduHomeworkMapper.findIfAnswered(homeworkSubmit);
	}
	@Transactional(readOnly = false)
	public void updateHomeworkSubmit(EduHomeworkSubmit homeworkSubmit) {
		//查看是否已答过该题
		EduHomeworkSubmit ehws = eduHomeworkMapper.findIfAnswered(homeworkSubmit);
		if(null != ehws){
			homeworkSubmit.setId(ehws.getId());
			eduHomeworkMapper.updateHomeworkSubmit(homeworkSubmit);
		}else{
			homeworkSubmit.setId(UUID.randomUUID().toString().replace("-", ""));
			homeworkSubmit.setDelFlag("0");
			eduHomeworkMapper.insertHomeworkSubmit(homeworkSubmit);
		}
		
	}
	
	@Transactional(readOnly = false)
	public void objectSubmit(EduHomeworkSubmit homeworkSubmit){
		homeworkSubmit.setId(UUID.randomUUID().toString().replace("-", ""));
		homeworkSubmit.setDelFlag("0");
		if(homeworkSubmit.getAnswer().equals(homeworkSubmit.getStudentAnswer())){
			homeworkSubmit.setIsCorrect("1");
		}else{
			homeworkSubmit.setIsCorrect("0");
		}
		eduHomeworkMapper.insertHomeworkSubmit(homeworkSubmit);
	}
	
	@Transactional(readOnly = false)
	public void subjectSubmit(EduHomeworkSubmit homeworkSubmit){
		homeworkSubmit.setId(UUID.randomUUID().toString().replace("-", ""));
		homeworkSubmit.setDelFlag("0");
		eduHomeworkMapper.insertHomeworkSubmit(homeworkSubmit);
	}
	
	@Transactional(readOnly = false)
	public void submitHomework(EduHomeworkSubmit homeworkSubmit) {
//		eduHomeworkMapper.delAnswers(homeworkSubmit);
		List<EduHomeworkSubmit> answers = homeworkSubmit.getAnswers();
		Date date = new Date();
		for(EduHomeworkSubmit submit : answers){
			submit.setId(UUID.randomUUID().toString().replace("-", ""));
			submit.setStudent(homeworkSubmit.getStudent());
			submit.setClasses(homeworkSubmit.getClasses());
			submit.setHomework(homeworkSubmit.getHomework());
			submit.setCreateDate(date);
			//客观题自动批改
			if("1".equals(submit.getQuestiontype()) || "2".equals(submit.getQuestiontype())
					||"3".equals(submit.getQuestiontype()) || "4".equals(submit.getQuestiontype())){
				if(submit.getAnswer().equals(submit.getStudentAnswer())){
					submit.setIsCorrect("1");
				}else{
					submit.setIsCorrect("0");
				}
			}
		}
		eduHomeworkMapper.insertAllHomeworkSubmit(answers);
		//更新作业状态
		EduHomeworkArrange eduHomeworkArrange = new EduHomeworkArrange();
		eduHomeworkArrange.setHomework(homeworkSubmit.getHomework());
		eduHomeworkArrange.setStudent(homeworkSubmit.getStudent());
		eduHomeworkMapper.updateHomeworkArrange(eduHomeworkArrange);
	}
	
	public List<EduHomeworkSubmit> findStudentAnswerList(EduHomeworkSubmit eduHomeworkSubmit){
		return eduHomeworkMapper.findStudentAnswerList(eduHomeworkSubmit);
	}
	
	public List<EduHomeworkArrange> findHomeworkStudents(EduHomeworkArrange eduHomeworkArrange){
		return eduHomeworkMapper.findHomeworkStudents(eduHomeworkArrange);
	}
	
	@Transactional(readOnly = false)
	public void updateSubjective(EduHomeworkSubmit eduHomeworkSubmit){
		eduHomeworkMapper.updateSubjective(eduHomeworkSubmit);
	}
	
	@Transactional(readOnly = false)
	public void completeCorrect(EduHomeworkArrange eduHomeworkArrange){
		eduHomeworkArrange.setStatus("2");
		eduHomeworkMapper.completeCorrect(eduHomeworkArrange);
		
		//查看是否所有学生都已经批改过，若都批改过，则将作业状态置为已批改
		Integer noCorrectCount = eduHomeworkMapper.getNoCorrectCount(eduHomeworkArrange.getHomework());
		if(null == noCorrectCount || noCorrectCount == 0){
			//将作业状态更改为已布置
			EduHomework eduHomework = new EduHomework();
			eduHomework.setId(eduHomeworkArrange.getHomework());
			eduHomework.setStatus("3");
			eduHomeworkMapper.updateStatus(eduHomework);
		}
	}
	
	@Transactional(readOnly = false)
	public void updateHomeworkArrange(EduHomeworkArrange eduHomeworkArrange){
		Date date = new Date();
		EduHomework eduHomework = new EduHomework(eduHomeworkArrange.getHomework());
		//查询该作业的所有未交的学生
		List<EduHomeworkArrange> students = eduHomeworkMapper.getAllNoSubmitStudent(eduHomework);
		//查询该作业的所有题目
		List<EduQuestionTree> questions = eduHomeworkMapper.findQuestionList(eduHomework);
		//插入该学生的作业题目，客观题为错误，问答题为空
		List<EduHomeworkSubmit> list = new ArrayList<EduHomeworkSubmit>();
		for(EduHomeworkArrange student : students){
			for(EduQuestionTree question : questions){
				EduHomeworkSubmit submit = new EduHomeworkSubmit();
				submit.setId(IdGenerator.uuid2());
				submit.setHomework(eduHomeworkArrange.getHomework());
				submit.setClasses(student.getClasses());
				submit.setStudent(student.getStudent());
				submit.setQuestion(question.getId());
				submit.setCreateDate(date);
				if(!"5".equals(question.getQuestionType())){
					submit.setIsCorrect("0");
				}
				list.add(submit);
			}
		}
		eduHomeworkMapper.insertAllHomeworkSubmit(list);
		//将所有学生作业置位已提交
		eduHomeworkMapper.updateHomeworkArrange(eduHomeworkArrange);
		//将作业状态更改为已布置
		eduHomework.setStatus("2");
		eduHomeworkMapper.updateStatus(eduHomework);
	}
}