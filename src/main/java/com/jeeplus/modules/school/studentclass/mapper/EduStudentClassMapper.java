/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.studentclass.mapper;

import java.util.List;
import java.util.Map;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.school.classes.entity.EduClasses;
import com.jeeplus.modules.school.studentclass.entity.EduStudentClass;

/**
 * 学生班级关联MAPPER接口
 * @author 乔功
 * @version 2018-08-30
 */
@MyBatisMapper
public interface EduStudentClassMapper extends BaseMapper<EduStudentClass> {
	
	public EduStudentClass selectByStudent(EduStudentClass studentClass);
	
	public EduClasses selectClassesByStudent(String studentId);
	
	public List<String> selectStudentByClasses(String classesId);
	
	public List<Map<String,String>> selectClassesByTeacher(String teacherId);
	
}