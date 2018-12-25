/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.eduhomework.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.Office;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 作业管理Entity
 * @author 乔功
 * @version 2018-09-27
 */
public class EduHomework extends DataEntity<EduHomework> {
	
	private static final long serialVersionUID = 1L;
	private String name;
	private String requirement;		// 作业要求
	private String status;		// 作业状态：0-未布置，2-已布置未收，3-已收未批改，4-已批改，5-作废
	private String subject;		// 学科
	
	private String[] loginNames;
	
	private String[] ids;
	
	private String iconLink; //学科图标链接
	
	private List<String> classNames;
	
	private List<Map<String,Object>> questionsCount;
	
	private List<Map<String,Object>> studentsCount;
	
	private String student;
	
	private String questiontype;
	
	private Date arrangeTime;
	
	private String isTeacher;
	
	private Office office;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EduHomework() {
		super();
	}

	public EduHomework(String id){
		super(id);
	}

	@ExcelField(title="作业要求", align=2, sort=1)
	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}
	
	@ExcelField(title="作业状态：0-未布置，2-已布置未收，3-已收未批改，4-已批改，5-作废", align=2, sort=2)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="学科", dictType="subject", align=2, sort=3)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String[] getLoginNames() {
		return loginNames;
	}

	public void setLoginNames(String[] loginNames) {
		this.loginNames = loginNames;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getIconLink() {
		return iconLink;
	}

	public void setIconLink(String iconLink) {
		this.iconLink = iconLink;
	}

	public List<String> getClassNames() {
		return classNames;
	}

	public void setClassNames(List<String> classNames) {
		this.classNames = classNames;
	}

	public List<Map<String,Object>> getQuestionsCount() {
		return questionsCount;
	}

	public void setQuestionsCount(List<Map<String,Object>> questionsCount) {
		this.questionsCount = questionsCount;
	}

	public List<Map<String,Object>> getStudentsCount() {
		return studentsCount;
	}

	public void setStudentsCount(List<Map<String,Object>> studentsCount) {
		this.studentsCount = studentsCount;
	}

	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}

	public String getQuestiontype() {
		return questiontype;
	}

	public void setQuestiontype(String questiontype) {
		this.questiontype = questiontype;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getArrangeTime() {
		return arrangeTime;
	}

	public void setArrangeTime(Date arrangeTime) {
		this.arrangeTime = arrangeTime;
	}

	public String getIsTeacher() {
		return isTeacher;
	}

	public void setIsTeacher(String isTeacher) {
		this.isTeacher = isTeacher;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
}