/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.publisher.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 出版社管理Entity
 * @author 乔功
 * @version 2018-08-22
 */
public class EduPublisher extends DataEntity<EduPublisher> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 出版社名称
	
	public EduPublisher() {
		super();
	}

	public EduPublisher(String id){
		super(id);
	}

	@ExcelField(title="出版社名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}