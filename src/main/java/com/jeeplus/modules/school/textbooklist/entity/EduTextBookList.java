/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.textbooklist.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 课本管理列表Entity
 * @author 李海军
 * @version 2018-11-06
 */
public class EduTextBookList extends DataEntity<EduTextBookList> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 课本名称
	private String pressid;		// 出版社
	private String subjectid;		// 学科
	private String grade;		// 年级
	private Office office;		// 学校

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public EduTextBookList() {
		super();
	}

	public EduTextBookList(String id){
		super(id);
	}

	@ExcelField(title="课本名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="出版社", dictType="publisher", align=2, sort=2)
	public String getPressid() {
		return pressid;
	}

	public void setPressid(String pressid) {
		this.pressid = pressid;
	}
	
	@ExcelField(title="学科", dictType="subject", align=2, sort=3)
	public String getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(String subjectid) {
		this.subjectid = subjectid;
	}
	
	@ExcelField(title="年级", dictType="grade", align=2, sort=4)
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
}