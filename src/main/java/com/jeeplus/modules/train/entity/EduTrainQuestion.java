/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.train.entity;

import com.jeeplus.core.persistence.DataEntity;

/**
 * 学生练习题目Entity
 * @author jeeplus
 * @version 2017-05-16
 */
public class EduTrainQuestion extends DataEntity<EduTrainQuestion> {
	
	private static final long serialVersionUID = 1L;
	private String train;		// 练习id
	private String question;	// 题目id
	private String status;		// 状态
	public EduTrainQuestion() {
		super();
	}

	public String getTrain() {
		return train;
	}

	public void setTrain(String train) {
		this.train = train;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public EduTrainQuestion(String id){
		super(id);
	}
}