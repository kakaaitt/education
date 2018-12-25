/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.studentclass.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.school.classes.entity.EduClasses;
import com.jeeplus.modules.school.studentclass.entity.EduStudentClass;
import com.jeeplus.modules.school.studentclass.mapper.EduStudentClassMapper;

/**
 * 学生班级关联Service
 * @author 乔功
 * @version 2018-08-30
 */
@Service
@Transactional(readOnly = true)
public class EduStudentClassService extends CrudService<EduStudentClassMapper, EduStudentClass> {

	@Autowired
	private EduStudentClassMapper eduStudentClassMapper;
	
	public EduStudentClass get(String id) {
		return super.get(id);
	}
	
	public List<EduStudentClass> findList(EduStudentClass eduStudentClass) {
		return super.findList(eduStudentClass);
	}
	
	public Page<EduStudentClass> findPage(Page<EduStudentClass> page, EduStudentClass eduStudentClass) {
		return super.findPage(page, eduStudentClass);
	}
	
	@Transactional(readOnly = false)
	public void save(EduStudentClass eduStudentClass) {
		super.save(eduStudentClass);
	}
	
	@Transactional(readOnly = false)
	public void delete(EduStudentClass eduStudentClass) {
		super.delete(eduStudentClass);
	}
	
	public EduStudentClass selectByStudent(EduStudentClass studentClass){
		return eduStudentClassMapper.selectByStudent(studentClass);
	}
	
	public EduClasses selectClassesByStudent(String studentId){
		return eduStudentClassMapper.selectClassesByStudent(studentId);
	}
	
	public List<Map<String,String>> selectClassesByTeacher(String teacherId){
		return eduStudentClassMapper.selectClassesByTeacher(teacherId);
	}
	
	
}