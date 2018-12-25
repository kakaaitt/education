/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.eduhomeworkarrange.entity;


import com.jeeplus.core.persistence.DataEntity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 作业布置管理Entity
 * @author 乔功
 * @version 2018-10-07
 */
public class EduHomeworkArrange extends DataEntity<EduHomeworkArrange> {
	
	private static final long serialVersionUID = 1L;
	private String homework;		// 作业
	private String classes;		// 班级
	private String student;		// 学生
	private String status;		// 状态（0：未提交；1：已提交；2：已批改）
	private String score;		// 成绩
	private String comment;		// 评语
	
	private String studentName;
	
	private String headImg;
	
	private Date time;	// 提交时间
	
	public EduHomeworkArrange() {
		super();
	}

	public EduHomeworkArrange(String id){
		super(id);
	}

	@ExcelField(title="作业", align=2, sort=1)
	public String getHomework() {
		return homework;
	}

	public void setHomework(String homework) {
		this.homework = homework;
	}
	
	@ExcelField(title="班级", align=2, sort=2)
	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}
	
	@ExcelField(title="学生", align=2, sort=3)
	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}
	
	@ExcelField(title="状态（0：未提交；1：已提交；2：已批改）", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="成绩", align=2, sort=5)
	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	
	@ExcelField(title="评语", align=2, sort=6)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
}