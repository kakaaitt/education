/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.questionbank.questionlist.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.questionbank.questionlist.entity.Eduquestionlist;
import com.jeeplus.modules.questionbank.questionlist.mapper.EduquestionlistMapper;

/**
 * 题库章节关联Service
 * @author 李海军
 * @version 2018-10-23
 */
@Service
@Transactional(readOnly = true)
public class EduquestionlistService extends CrudService<EduquestionlistMapper, Eduquestionlist> {

	public Eduquestionlist get(String id) {
		return super.get(id);
	}
	
	public List<Eduquestionlist> findList(Eduquestionlist eduquestionlist) {
		return super.findList(eduquestionlist);
	}
	
	public Page<Eduquestionlist> findPage(Page<Eduquestionlist> page, Eduquestionlist eduquestionlist) {
		return super.findPage(page, eduquestionlist);
	}
	
	@Transactional(readOnly = false)
	public void save(Eduquestionlist eduquestionlist) {
		super.save(eduquestionlist);
	}
	
	@Transactional(readOnly = false)
	public void delete(Eduquestionlist eduquestionlist) {
		super.delete(eduquestionlist);
	}
	
}