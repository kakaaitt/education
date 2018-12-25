/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.teacherclass.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.school.classes.entity.EduClasses;
import com.jeeplus.modules.school.teacherclass.entity.EduTeacherClass;
import com.jeeplus.modules.school.teacherclass.mapper.EduTeacherClassMapper;

/**
 * 老师授课分配管理Service
 * @author 乔功
 * @version 2018-09-01
 */
@Service
@Transactional(readOnly = true)
public class EduTeacherClassService extends CrudService<EduTeacherClassMapper, EduTeacherClass> {

	@Autowired
	private EduTeacherClassMapper eduTeacherClassMapper;
	
	public EduTeacherClass get(String id) {
		return super.get(id);
	}
	
	public List<EduTeacherClass> findList(EduTeacherClass eduTeacherClass) {
		return super.findList(eduTeacherClass);
	}
	
	public Page<EduTeacherClass> findPage(Page<EduTeacherClass> page, EduTeacherClass eduTeacherClass) {
		return super.findPage(page, eduTeacherClass);
	}
	
	@Transactional(readOnly = false)
	public void save(EduTeacherClass eduTeacherClass) {
		super.save(eduTeacherClass);
	}
	
	@Transactional(readOnly = false)
	public void delete(EduTeacherClass eduTeacherClass) {
		super.delete(eduTeacherClass);
	}
	
	public EduTeacherClass selectByTeacher(EduTeacherClass eduTeacherClass){
		return eduTeacherClassMapper.selectByTeacher(eduTeacherClass);
	}
	
	public EduTeacherClass selectHasMainSubject(EduTeacherClass eduTeacherClass){
		return eduTeacherClassMapper.selectHasMainSubject(eduTeacherClass);
	}
	
	public EduClasses selectClassesByTeacher(String teacherId){
		return eduTeacherClassMapper.selectClassesByTeacher(teacherId);
	}
	
	public List<EduTeacherClass> findTeachersByStudent(String studentId){
		return eduTeacherClassMapper.findTeachersByStudent(studentId);
	}
	
	public List<EduTeacherClass> getByTeacher(String teacherId){
		return eduTeacherClassMapper.getByTeacher(teacherId);
	}
	
	
	public List<String> getclassesIdsByTeacher(String teacherId){
		return eduTeacherClassMapper.getclassesIdsByTeacher(teacherId);
	}
}