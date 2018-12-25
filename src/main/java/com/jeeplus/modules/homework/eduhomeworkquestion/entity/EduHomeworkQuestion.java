/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.eduhomeworkquestion.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 作业题目管理Entity
 * @author 乔功
 * @version 2018-10-07
 */
public class EduHomeworkQuestion extends DataEntity<EduHomeworkQuestion> {
	
	private static final long serialVersionUID = 1L;
	private String homework;		// 作业
	private String question;		// 题目
	private String status;		// 状态
	
	private String questions;
	
	private String[] questionIds;
	
	public EduHomeworkQuestion() {
		super();
	}

	public EduHomeworkQuestion(String id){
		super(id);
	}

	@ExcelField(title="作业", align=2, sort=1)
	public String getHomework() {
		return homework;
	}

	public void setHomework(String homework) {
		this.homework = homework;
	}
	
	@ExcelField(title="题目", align=2, sort=2)
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	@ExcelField(title="状态", align=2, sort=3)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getQuestions() {
		return questions;
	}

	public void setQuestions(String questions) {
		this.questions = questions;
	}

	public String[] getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(String[] questionIds) {
		this.questionIds = questionIds;
	}
	
}