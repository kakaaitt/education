/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.knowledge.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.school.knowledge.entity.EduKnowledge;
import com.jeeplus.modules.school.knowledge.mapper.EduKnowledgeMapper;

/**
 * 知识点管理Service
 * @author 乔功
 * @version 2018-09-01
 */
@Service
@Transactional(readOnly = true)
public class EduKnowledgeService extends CrudService<EduKnowledgeMapper, EduKnowledge> {

	public EduKnowledge get(String id) {
		return super.get(id);
	}
	
	public List<EduKnowledge> findList(EduKnowledge eduKnowledge) {
		return super.findList(eduKnowledge);
	}
	
	public Page<EduKnowledge> findPage(Page<EduKnowledge> page, EduKnowledge eduKnowledge) {
		return super.findPage(page, eduKnowledge);
	}
	
	@Transactional(readOnly = false)
	public void save(EduKnowledge eduKnowledge) {
		super.save(eduKnowledge);
	}
	
	@Transactional(readOnly = false)
	public void delete(EduKnowledge eduKnowledge) {
		super.delete(eduKnowledge);
	}
	
}