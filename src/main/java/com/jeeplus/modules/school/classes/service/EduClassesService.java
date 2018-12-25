/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.classes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.school.classes.entity.EduClasses;
import com.jeeplus.modules.school.classes.mapper.EduClassesMapper;

/**
 * 班级管理Service
 * @author 乔功
 * @version 2018-08-30
 */
@Service
@Transactional(readOnly = true)
public class EduClassesService extends CrudService<EduClassesMapper, EduClasses> {

	public EduClasses get(String id) {
		return super.get(id);
	}
	
	public List<EduClasses> findList(EduClasses eduClasses) {
		return super.findList(eduClasses);
	}
	
	public List<EduClasses> findListByUser(EduClasses eduClasses){
		return mapper.findListByUser(eduClasses);
	}
	public Page<EduClasses> findPage(Page<EduClasses> page, EduClasses eduClasses) {
		return super.findPage(page, eduClasses);
	}
	
	@Transactional(readOnly = false)
	public void save(EduClasses eduClasses) {
		super.save(eduClasses);
	}
	
	@Transactional(readOnly = false)
	public void delete(EduClasses eduClasses) {
		super.delete(eduClasses);
	}
	
}