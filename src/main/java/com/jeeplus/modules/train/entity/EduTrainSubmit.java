/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.train.entity;

import java.util.List;

import com.jeeplus.core.persistence.DataEntity;

/**
 * 学生练习题目提交Entity
 * @author jeeplus
 * @version 2017-05-16
 */
public class EduTrainSubmit extends DataEntity<EduTrainSubmit> {
	
	private static final long serialVersionUID = 1L;
	private String train;		// 练习id
	private String question;	// 题目id
	private String answer;		// 学生答案
	private String isCorrect;	// 是否正确（0：错误；1：正确）
	
	private String studentAnswer;
	
	private List<EduTrainSubmit> answers;
	public EduTrainSubmit() {
		super();
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

	public String getIsCorrect() {
		return isCorrect;
	}

	public void setIsCorrect(String isCorrect) {
		this.isCorrect = isCorrect;
	}

	public void setTrain(String train) {
		this.train = train;
	}

	public String getTrain() {
		return train;
	}

	public String getStudentAnswer() {
		return studentAnswer;
	}

	public void setStudentAnswer(String studentAnswer) {
		this.studentAnswer = studentAnswer;
	}

	public List<EduTrainSubmit> getAnswers() {
		return answers;
	}

	public void setAnswers(List<EduTrainSubmit> answers) {
		this.answers = answers;
	}

}