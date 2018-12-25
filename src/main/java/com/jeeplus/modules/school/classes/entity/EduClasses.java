/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.classes.entity;

import com.jeeplus.modules.sys.entity.Office;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 班级管理Entity
 * @author 乔功
 * @version 2018-08-30
 */
public class EduClasses extends DataEntity<EduClasses> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 班级名称
	private String shortName;		// 班级简写
	private Office office;		// 学校
	private String grade;		// 年级
	private String studyYear;		// 学年
	private String sort;		// 排序
	
	private String school;
	
	public EduClasses() {
		super();
	}

	public EduClasses(String id){
		super(id);
	}

	@ExcelField(title="班级名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="班级简写", align=2, sort=2)
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	@NotNull(message="学校不能为空")
	@ExcelField(title="学校", fieldType=Office.class, value="office.name", align=2, sort=3)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="年级", dictType="grade", align=2, sort=4)
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	@ExcelField(title="学年", dictType="study_year", align=2, sort=5)
	public String getStudyYear() {
		return studyYear;
	}

	public void setStudyYear(String studyYear) {
		this.studyYear = studyYear;
	}
	
	@ExcelField(title="排序", align=2, sort=6)
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}
	
}