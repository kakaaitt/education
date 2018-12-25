/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.knowledge.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 知识点管理Entity
 * @author 乔功
 * @version 2018-09-01
 */
public class EduKnowledge extends DataEntity<EduKnowledge> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 知识点名称
	private String grade;		// 年级
	private String subject;		// 学科
	
	public EduKnowledge() {
		super();
	}

	public EduKnowledge(String id){
		super(id);
	}

	@ExcelField(title="知识点名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="年级", dictType="grade", align=2, sort=2)
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	@ExcelField(title="学科", dictType="subject", align=2, sort=3)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
}