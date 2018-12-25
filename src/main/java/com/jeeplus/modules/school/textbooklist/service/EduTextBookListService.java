/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.textbooklist.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.school.textbooklist.entity.EduTextBookList;
import com.jeeplus.modules.school.textbooklist.mapper.EduTextBookListMapper;

/**
 * 课本管理列表Service
 * @author 李海军
 * @version 2018-11-06
 */
@Service
@Transactional(readOnly = true)
public class EduTextBookListService extends CrudService<EduTextBookListMapper, EduTextBookList> {

	public EduTextBookList get(String id) {
		return super.get(id);
	}
	
	public List<EduTextBookList> findList(EduTextBookList eduTextBookList) {
		return super.findList(eduTextBookList);
	}
	
	public Page<EduTextBookList> findPage(Page<EduTextBookList> page, EduTextBookList eduTextBookList) {
		return super.findPage(page, eduTextBookList);
	}
	
	@Transactional(readOnly = false)
	public void save(EduTextBookList eduTextBookList) {
		super.save(eduTextBookList);
	}
	
	@Transactional(readOnly = false)
	public void delete(EduTextBookList eduTextBookList) {
		super.delete(eduTextBookList);
	}
	
}