/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.eduhomeworkquestion.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.homework.eduhomeworkquestion.entity.EduHomeworkQuestion;
import com.jeeplus.modules.homework.eduhomeworkquestion.mapper.EduHomeworkQuestionMapper;

/**
 * 作业题目管理Service
 * @author 乔功
 * @version 2018-10-07
 */
@Service
@Transactional(readOnly = true)
public class EduHomeworkQuestionService extends CrudService<EduHomeworkQuestionMapper, EduHomeworkQuestion> {

	public EduHomeworkQuestion get(String id) {
		return super.get(id);
	}
	
	public List<EduHomeworkQuestion> findList(EduHomeworkQuestion eduHomeworkQuestion) {
		return super.findList(eduHomeworkQuestion);
	}
	
	public Page<EduHomeworkQuestion> findPage(Page<EduHomeworkQuestion> page, EduHomeworkQuestion eduHomeworkQuestion) {
		return super.findPage(page, eduHomeworkQuestion);
	}
	
	@Transactional(readOnly = false)
	public void save(EduHomeworkQuestion eduHomeworkQuestion) {
		super.save(eduHomeworkQuestion);
	}
	
	@Transactional(readOnly = false)
	public void saveAll(EduHomeworkQuestion eduHomeworkQuestion,String questions) {
		String[] questionIds =questions.split(",");
		for(String id : questionIds){
			eduHomeworkQuestion.setQuestion(id);
			eduHomeworkQuestion.setId(null);
//			super.delete(eduHomeworkQuestion);
			super.save(eduHomeworkQuestion);
		}
	}
	
	@Transactional(readOnly = false)
	public void removeAll(EduHomeworkQuestion eduHomeworkQuestion,String questions) {
		String[] questionIds =questions.split(",");
		for(String id : questionIds){
			eduHomeworkQuestion.setQuestion(id);
			super.delete(eduHomeworkQuestion);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(EduHomeworkQuestion eduHomeworkQuestion) {
		super.delete(eduHomeworkQuestion);
	}
	
}