/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.studentparent.entity;

import com.jeeplus.modules.school.classes.entity.EduClasses;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 学生家长管理Entity
 * @author 乔功
 * @version 2018-08-30
 */
public class EduStudentParent extends DataEntity<EduStudentParent> {
	
	private static final long serialVersionUID = 1L;
	private User user1;		// 学生
	private User user2;		// 家长
	private EduClasses classes;		// 班级
	
	private Office office;
	
	public EduStudentParent() {
		super();
	}

	public EduStudentParent(String id){
		super(id);
	}

	@NotNull(message="学生不能为空")
	@ExcelField(title="学生", fieldType=User.class, value="user1.name", align=2, sort=1)
	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}
	
	@NotNull(message="家长不能为空")
	@ExcelField(title="家长", fieldType=User.class, value="user2.name", align=2, sort=2)
	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public EduClasses getClasses() {
		return classes;
	}

	public void setClasses(EduClasses classes) {
		this.classes = classes;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
}