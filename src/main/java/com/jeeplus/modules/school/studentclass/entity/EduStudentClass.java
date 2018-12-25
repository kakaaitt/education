/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.studentclass.entity;

import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.school.classes.entity.EduClasses;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 学生班级关联Entity
 * @author 乔功
 * @version 2018-08-30
 */
public class EduStudentClass extends DataEntity<EduStudentClass> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 学生
	private EduClasses classes;		// 班级
	private String studyYear;		// 学年
	
	private Office office;
	
	private String status;
	
	public EduStudentClass() {
		super();
	}

	public EduStudentClass(String id){
		super(id);
	}

	@NotNull(message="学生不能为空")
	@ExcelField(title="学生", fieldType=User.class, value="user.name", align=2, sort=1)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@NotNull(message="班级不能为空")
	@ExcelField(title="班级", fieldType=EduClasses.class, value="classes.name", align=2, sort=2)
	public EduClasses getClasses() {
		return classes;
	}

	public void setClasses(EduClasses classes) {
		this.classes = classes;
	}
	
	@ExcelField(title="学年", dictType="study_year", align=2, sort=3)
	public String getStudyYear() {
		return studyYear;
	}

	public void setStudyYear(String studyYear) {
		this.studyYear = studyYear;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}