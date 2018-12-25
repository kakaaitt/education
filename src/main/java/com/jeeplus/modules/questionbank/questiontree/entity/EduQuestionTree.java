/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.questionbank.questiontree.entity;


import java.util.List;
import java.util.Map;

import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.school.knowledge.entity.EduKnowledge;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 题库管理Entity
 * @author 李海军
 * @version 2018-10-03
 */
public class EduQuestionTree extends DataEntity<EduQuestionTree> {
	
	private static final long serialVersionUID = 1L;
	private String questionType;		// 题目类型
	private String questionBankType;		// 题库类型
	private String questionText;		// 题目内容
	private String options;		// 选项
	private String answer;		// 答案
	private String analy;		// 解析
	private String textid;		// 章节
	private String shareType;		// 分享状态
	private Integer publicnNum;		// 布置次数
	private String files;		// 附件信息
	private EduKnowledge konwledges;		// 知识点
	
	private String studentAnswer; //学生答案
	
	private String isCorrect; //是否正确
	
	private String[] optionList;
	
	private String subject;
	
	//查询条件
	private String questiontype;		// 题目类型
	private String questionbanktype;		// 题库类型
	private String isChosen; //是否已选
	private String homeWork;//作业id
	
	private Office office;
	
	private List<Map<String,Object>> filesList;
	
	private List<EduKnowledge> konwledgeList;
	
	private String knowledgeNames;
	
	private Integer correctCount;
	
	public EduQuestionTree() {
		super();
	}

	public EduQuestionTree(String id){
		super(id);
	}
	
	public String getHomeWork() {
		return homeWork;
	}
	
	public void setHomeWork(String homeWork) {
		this.homeWork = homeWork;
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
	
	@ExcelField(title="章节", align=2, sort=7)
	public String getTextid() {
		return textid;
	}

	public void setTextid(String textid) {
		this.textid = textid;
	}
	
	@ExcelField(title="分享状态", dictType="yes_no", align=2, sort=8)
	public String getShareType() {
		return shareType;
	}

	public void setShareType(String shareType) {
		this.shareType = shareType;
	}
	
	@ExcelField(title="布置次数", align=2, sort=9)
	public Integer getPublicnNum() {
		return publicnNum;
	}

	public void setPublicnNum(Integer publicnNum) {
		this.publicnNum = publicnNum;
	}
	
	@ExcelField(title="附件信息", align=2, sort=16)
	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	@ExcelField(title="知识点", fieldType=EduKnowledge.class, value="konwledges.name", align=2, sort=17)
	public EduKnowledge getKonwledges() {
		return konwledges;
	}

	public void setKonwledges(EduKnowledge konwledges) {
		this.konwledges = konwledges;
	}
	
	public String getQuestiontype() {
		return questiontype;
	}

	public void setQuestiontype(String questiontype) {
		this.questiontype = questiontype;
	}

	public String getQuestionbanktype() {
		return questionbanktype;
	}

	public void setQuestionbanktype(String questionbanktype) {
		this.questionbanktype = questionbanktype;
	}

	public String getIsChosen() {
		return isChosen;
	}

	public void setIsChosen(String isChosen) {
		this.isChosen = isChosen;
	}

	public List<EduKnowledge> getKonwledgeList() {
		return konwledgeList;
	}

	public void setKonwledgeList(List<EduKnowledge> konwledgeList) {
		this.konwledgeList = konwledgeList;
	}

	public String getStudentAnswer() {
		return studentAnswer;
	}

	public void setStudentAnswer(String studentAnswer) {
		this.studentAnswer = studentAnswer;
	}

	public String getIsCorrect() {
		return isCorrect;
	}

	public void setIsCorrect(String isCorrect) {
		this.isCorrect = isCorrect;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public List<Map<String, Object>> getFilesList() {
		return filesList;
	}

	public void setFilesList(List<Map<String, Object>> filesList) {
		this.filesList = filesList;
	}

	public String[] getOptionList() {
		return optionList;
	}

	public void setOptionList(String[] optionList) {
		this.optionList = optionList;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getKnowledgeNames() {
		return knowledgeNames;
	}

	public void setKnowledgeNames(String knowledgeNames) {
		this.knowledgeNames = knowledgeNames;
	}

	public Integer getCorrectCount() {
		return correctCount;
	}

	public void setCorrectCount(Integer correctCount) {
		this.correctCount = correctCount;
	}
	
}