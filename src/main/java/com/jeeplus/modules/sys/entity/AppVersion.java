/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;

import com.jeeplus.core.persistence.DataEntity;

/**
 * app版本Entity
 * @author 乔功
 * @version 2018-09-10
 */
public class AppVersion extends DataEntity<AppVersion> {

	private static final long serialVersionUID = 1L;
	private Integer versionCode;	// 版本号
	private String versionName;	// 版本名称
	private String type;// 类型
	private String downloadUrl;// 版本下载地址
	
	public Integer getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	
}