/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.questionbank.questions.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.school.knowledge.entity.EduKnowledge;
import com.jeeplus.modules.sys.entity.Office;

import java.util.List;

import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 题目管理Entity
 * @author 李海军
 * @version 2018-09-16
 */
public class EduQuestion extends DataEntity<EduQuestion> {
	
	private static final long serialVersionUID = 1L;
	private String questionType;		// 题目类型
	private String questionBankType;		// 题库类型
	private String questionText;		// 题目内容
	private String options;		// 选项
	private String answer;		// 答案
	private String analy;		// 解析
	private String subject;		// 学科
	private String grade;		// 年级
	private String shareType;		// 分享状态
	private Integer publicnNum;		// 布置次数
	
	private String chapterId;		// 章节
	
	private String[] loginNames;
	
	private List<EduQuestionFiles> filesList;
	
	private List<EduKnowledge> konwledgeList;
	
	private Office office;
	
	public EduQuestion() {
		super();
	}

	public EduQuestion(String id){
		super(id);
	}

	@ExcelField(title="题目类型", dictType="questiontype", align=2, sort=1)
	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	
	@ExcelField(title="题库类型", dictType="questionbanktype", align=2, sort=2)
	public String getQuestionBankType() {
		return questionBankType;
	}

	public void setQuestionBankType(String questionBankType) {
		this.questionBankType = questionBankType;
	}
	
	@ExcelField(title="题目内容", align=2, sort=3)
	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	
	@ExcelField(title="选项", align=2, sort=4)
	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}
	
	@ExcelField(title="答案", align=2, sort=5)
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	@ExcelField(title="解析", align=2, sort=6)
	public String getAnaly() {
		return analy;
	}

	public void setAnaly(String analy) {
		this.analy = analy;
	}
	
	@ExcelField(title="学科", dictType="subject", align=2, sort=7)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@ExcelField(title="年级", dictType="grade", align=2, sort=8)
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	@ExcelField(title="分享状态", dictType="yes_no", align=2, sort=9)
	public String getShareType() {
		return shareType;
	}

	public void setShareType(String shareType) {
		this.shareType = shareType;
	}
	
	@ExcelField(title="布置次数", align=2, sort=10)
	public Integer getPublicnNum() {
		return publicnNum;
	}

	public void setPublicnNum(Integer publicnNum) {
		this.publicnNum = publicnNum;
	}

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	public List<EduQuestionFiles> getFilesList() {
		return filesList;
	}

	public void setFilesList(List<EduQuestionFiles> filesList) {
		this.filesList = filesList;
	}

	public String[] getLoginNames() {
		return loginNames;
	}

	public void setLoginNames(String[] loginNames) {
		this.loginNames = loginNames;
	}

	public List<EduKnowledge> getKonwledgeList() {
		return konwledgeList;
	}

	public void setKonwledgeList(List<EduKnowledge> konwledgeList) {
		this.konwledgeList = konwledgeList;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

}