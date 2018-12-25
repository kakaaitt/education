/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.eduhomework.entity;


import java.util.List;

import com.jeeplus.core.persistence.DataEntity;

/**
 * 作业提交管理Entity
 * @author 乔功
 * @version 2018-09-27
 */
public class EduHomeworkSubmit extends DataEntity<EduHomeworkSubmit> {
	
	private static final long serialVersionUID = 1L;
	
	private String homework;
	
	private String question;
	
	private String answer;
	
	private String student;
	
	private String classes;
	
	private String isCorrect;
	
	private String studentAnswer;
	
	private String questiontype;
	
	private List<EduHomeworkSubmit> answers;
	
	public EduHomeworkSubmit() {
		super();
	}

	public EduHomeworkSubmit(String id){
		super(id);
	}

	public String getHomework() {
		return homework;
	}

	public void setHomework(String homework) {
		this.homework = homework;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getIsCorrect() {
		return isCorrect;
	}

	public void setIsCorrect(String isCorrect) {
		this.isCorrect = isCorrect;
	}

	public List<EduHomeworkSubmit> getAnswers() {
		return answers;
	}

	public void setAnswers(List<EduHomeworkSubmit> answers) {
		this.answers = answers;
	}

	public String getStudentAnswer() {
		return studentAnswer;
	}

	public void setStudentAnswer(String studentAnswer) {
		this.studentAnswer = studentAnswer;
	}

	public String getQuestiontype() {
		return questiontype;
	}

	public void setQuestiontype(String questiontype) {
		this.questiontype = questiontype;
	}

}