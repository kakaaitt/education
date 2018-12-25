/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachmaterial.tmaterial.entity;

import com.jeeplus.modules.school.textbook.entity.Textbook;
import com.jeeplus.modules.sys.entity.Office;

import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 教材管理Entity
 * @author 乔功
 * @version 2018-09-02
 */
public class EduTeachMaterial extends DataEntity<EduTeachMaterial> {
	
	private static final long serialVersionUID = 1L;
	private String subject;		// 学科
	private String grade;		// 年级
	private String publisher;		// 出版社
	private Textbook textbook;		// 课本
	private Office office;		// 学校
	private String[] ids;
	
	private String iconLink; //学科图标链接
	
	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public EduTeachMaterial() {
		super();
	}

	public EduTeachMaterial(String id){
		super(id);
	}

	@ExcelField(title="学科", dictType="subject", align=2, sort=1)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@ExcelField(title="年级", dictType="grade", align=2, sort=2)
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	@ExcelField(title="出版社", dictType="publisher", align=2, sort=3)
	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	@NotNull(message="课本不能为空")
	@ExcelField(title="课本", fieldType=Textbook.class, value="textbook.name", align=2, sort=4)
	public Textbook getTextbook() {
		return textbook;
	}

	public void setTextbook(Textbook textbook) {
		this.textbook = textbook;
	}

	public String getIconLink() {
		return iconLink;
	}

	public void setIconLink(String iconLink) {
		this.iconLink = iconLink;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}
	
}