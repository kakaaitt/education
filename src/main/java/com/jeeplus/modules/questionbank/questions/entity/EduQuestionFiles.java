package com.jeeplus.modules.questionbank.questions.entity;

import com.jeeplus.core.persistence.DataEntity;

public class EduQuestionFiles  extends DataEntity<EduQuestion> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3006958040651889496L;

	private String questionId;
	
	private String filetype;
	
	private String filename;
	
	private String filepath;
	
	private String iconLink;

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getIconLink() {
		return iconLink;
	}

	public void setIconLink(String iconLink) {
		this.iconLink = iconLink;
	}
	
}
