/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.studentparent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.school.studentparent.entity.EduStudentParent;
import com.jeeplus.modules.school.studentparent.mapper.EduStudentParentMapper;

/**
 * 学生家长管理Service
 * @author 乔功
 * @version 2018-08-30
 */
@Service
@Transactional(readOnly = true)
public class EduStudentParentService extends CrudService<EduStudentParentMapper, EduStudentParent> {

	@Autowired
	private EduStudentParentMapper eduStudentParentMapper;
	
	public EduStudentParent get(String id) {
		return super.get(id);
	}
	
	public List<EduStudentParent> findList(EduStudentParent eduStudentParent) {
		return super.findList(eduStudentParent);
	}
	
	public Page<EduStudentParent> findPage(Page<EduStudentParent> page, EduStudentParent eduStudentParent) {
		return super.findPage(page, eduStudentParent);
	}
	
	@Transactional(readOnly = false)
	public void save(EduStudentParent eduStudentParent) {
		super.save(eduStudentParent);
	}
	
	@Transactional(readOnly = false)
	public void delete(EduStudentParent eduStudentParent) {
		super.delete(eduStudentParent);
	}
	
	public EduStudentParent selectByStudentAndParent(EduStudentParent eduStudentParent){
		return eduStudentParentMapper.selectByStudentAndParent(eduStudentParent);
	}
	@Transactional(readOnly = false)
	public int deleteByStudentAndParent(EduStudentParent eduStudentParent){
		return eduStudentParentMapper.deleteByStudentAndParent(eduStudentParent);
	}
	
}