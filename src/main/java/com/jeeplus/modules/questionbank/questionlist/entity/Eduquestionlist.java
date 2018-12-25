/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.questionbank.questionlist.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 题库章节关联Entity
 * @author 李海军
 * @version 2018-10-23
 */
public class Eduquestionlist extends DataEntity<Eduquestionlist> {
	
	private static final long serialVersionUID = 1L;
	private String subjectid;		// 学科
	private String teacher;		// 老师id
	private String mainsubject;		// 是否主课
	private String loginname;		// 老师
	private String grade;//年级
	private String schoolname;//学校
	
	
	public String getSchoolname() {
		return schoolname;
	}

	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Eduquestionlist() {
		super();
	}

	public Eduquestionlist(String id){
		super(id);
	}

	@ExcelField(title="学科", dictType="subject", align=2, sort=1)
	public String getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(String subjectid) {
		this.subjectid = subjectid;
	}
	
	@ExcelField(title="老师id", align=2, sort=2)
	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getMainsubject() {
		return mainsubject;
	}

	public void setMainsubject(String mainsubject) {
		this.mainsubject = mainsubject;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

	
}