/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.teacherclass.entity;

import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.school.classes.entity.EduClasses;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 老师授课分配管理Entity
 * @author 乔功
 * @version 2018-09-01
 */
public class EduTeacherClass extends DataEntity<EduTeacherClass> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 教师
	private EduClasses classes;		// 班级
	private String subject;		// 学科
	private String mainSubject;		// 是否主学科
	private String studyYear;		// 学年
	
	private String status;
	
	private String loginName;
	
	private Office office;
	
	public EduTeacherClass() {
		super();
	}

	public EduTeacherClass(String id){
		super(id);
	}

	@NotNull(message="教师不能为空")
	@ExcelField(title="教师", fieldType=User.class, value="user.name", align=2, sort=1)
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
	
	@ExcelField(title="学科", dictType="subject", align=2, sort=3)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@ExcelField(title="是否主学科", dictType="is_main_subject", align=2, sort=4)
	public String getMainSubject() {
		return mainSubject;
	}

	public void setMainSubject(String mainSubject) {
		this.mainSubject = mainSubject;
	}

	public String getStudyYear() {
		return studyYear;
	}

	public void setStudyYear(String studyYear) {
		this.studyYear = studyYear;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
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