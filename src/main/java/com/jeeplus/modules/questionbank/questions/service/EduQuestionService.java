/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.questionbank.questions.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.questionbank.questions.entity.EduQuestion;
import com.jeeplus.modules.questionbank.questions.entity.EduQuestionFiles;
import com.jeeplus.modules.questionbank.questions.mapper.EduQuestionMapper;
import com.jeeplus.modules.school.knowledge.entity.EduKnowledge;

/**
 * 题目管理Service
 * @author 李海军
 * @version 2018-09-16
 */
@Service
@Transactional(readOnly = true)
public class EduQuestionService extends CrudService<EduQuestionMapper, EduQuestion> {
	
	@Autowired
	private EduQuestionMapper eduQuestionMapper;

	public EduQuestion get(String id) {
		return super.get(id);
	}
	
	public List<EduQuestion> findList(EduQuestion eduQuestion) {
		return super.findList(eduQuestion);
	}
	
	public Page<EduQuestion> findPage(Page<EduQuestion> page, EduQuestion eduQuestion) {
		return super.findPage(page, eduQuestion);
	}
	
	@Transactional(readOnly = false)
	public void save(EduQuestion eduQuestion) {
		super.save(eduQuestion);
	}
	
	@Transactional(readOnly = false)
	public void delete(EduQuestion eduQuestion) {
		super.delete(eduQuestion);
	}
	
	public List<EduQuestionFiles> findFilesList(String questionId){
		return eduQuestionMapper.findFilesList(questionId);
	}
	
	public List<EduKnowledge> findKnowledgesList(String questionId){
		return eduQuestionMapper.findKnowledgesList(questionId);
	}
}