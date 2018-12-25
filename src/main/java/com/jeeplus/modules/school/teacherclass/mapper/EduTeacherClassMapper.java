/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.teacherclass.mapper;

import java.util.List;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.school.classes.entity.EduClasses;
import com.jeeplus.modules.school.teacherclass.entity.EduTeacherClass;

/**
 * 老师授课分配管理MAPPER接口
 * @author 乔功
 * @version 2018-09-01
 */
@MyBatisMapper
public interface EduTeacherClassMapper extends BaseMapper<EduTeacherClass> {
	
	public EduTeacherClass selectByTeacher(EduTeacherClass eduTeacherClass);
	
	public EduTeacherClass selectHasMainSubject(EduTeacherClass eduTeacherClass);

	public EduClasses selectClassesByTeacher(String teacherId);
	
	public List<EduTeacherClass> findTeachersByStudent(String studentId);
	
	public List<EduTeacherClass> getByTeacher(String teacherId);
	
	public List<String> getclassesIdsByTeacher(String teacherId);
}