/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;

import java.util.Date;

import com.jeeplus.core.persistence.DataEntity;

/**
 * 学生登录日志Entity
 * @author 乔功
 * @version 2018-09-10
 */
public class EduLoginLog extends DataEntity<EduLoginLog> {

	private static final long serialVersionUID = 1L;
	
	private String user;
	
	private Date loginTime;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	
}