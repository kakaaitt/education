/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.textbook.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * 课本管理Entity
 * @author 李海军
 * @version 2018-09-25
 */
public class Textbook extends TreeEntity<Textbook> {
	
	private static final long serialVersionUID = 1L;
	private String pressid;		// 出版社
	private String grade;		// 年级
	private List<Textbook> child;
	private String subjectid;		// 学科
	private String schoolid;//学校id
	
	
	public String getSchoolid() {
		return schoolid;
	}

	public void setSchoolid(String schoolid) {
		this.schoolid = schoolid;
	}

	public Textbook() {
		super();
	}

	public Textbook(String id){
		super(id);
	}

	public String getPressid() {
		return pressid;
	}

	public void setPressid(String pressid) {
		this.pressid = pressid;
	}
	
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	public String getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(String subjectid) {
		this.subjectid = subjectid;
	}
	
	public  Textbook getParent() {
			return parent;
	}
	
	@Override
	public void setParent(Textbook parent) {
		this.parent = parent;
		
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
	
	public List<Textbook> getChild() {
		return child;
	}

	public void setChild(List<Textbook> child) {
		this.child = child;
	}
}