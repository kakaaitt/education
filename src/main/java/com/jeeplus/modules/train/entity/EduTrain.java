/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.train.entity;

import java.util.List;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 学生练习Entity
 * @author jeeplus
 * @version 2017-05-16
 */
public class EduTrain extends DataEntity<EduTrain> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private Integer number;		// 次数
	private String classes;		// 班级
	private String student;		// 学生id
	private String subject;		// 学科
	private String status;		//状态
	private String score;		//得分
	
	private String iconLink;    //学科图片
	private String textid; 		//章节id
	private Integer questionCount = 10; //默认随机10个题目
	private Integer correctCount;
	private String questiontype;
	private String teacher;
	
	private String studentName;//学生姓名
	private String classesName;//班级名称
	private List<String> classesIds;
	
	private Office office;
	public EduTrain() {
		super();
	}

	public EduTrain(String id){
		super(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getIconLink() {
		return iconLink;
	}

	public void setIconLink(String iconLink) {
		this.iconLink = iconLink;
	}

	public String getTextid() {
		return textid;
	}

	public void setTextid(String textid) {
		this.textid = textid;
	}

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public Integer getCorrectCount() {
		return correctCount;
	}

	public void setCorrectCount(Integer correctCount) {
		this.correctCount = correctCount;
	}

	public String getQuestiontype() {
		return questiontype;
	}

	public void setQuestiontype(String questiontype) {
		this.questiontype = questiontype;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getClassesName() {
		return classesName;
	}

	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}

	public List<String> getClassesIds() {
		return classesIds;
	}

	public void setClassesIds(List<String> classesIds) {
		this.classesIds = classesIds;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
}