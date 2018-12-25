/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.questionbank.questions.mapper;

import java.util.List;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.questionbank.questions.entity.EduQuestion;
import com.jeeplus.modules.questionbank.questions.entity.EduQuestionFiles;
import com.jeeplus.modules.school.knowledge.entity.EduKnowledge;

/**
 * 题目管理MAPPER接口
 * @author 李海军
 * @version 2018-09-16
 */
@MyBatisMapper
public interface EduQuestionMapper extends BaseMapper<EduQuestion> {
	public List<EduQuestionFiles> findFilesList(String questionId);
	
	public List<EduKnowledge> findKnowledgesList(String questionId);
}