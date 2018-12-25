/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachmaterial.tresource.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 资源管理Entity
 * @author 李海军
 * @version 2018-09-04
 */
public class EduTeacheResource extends DataEntity<EduTeacheResource> {
	
	private static final long serialVersionUID = 1L;
	private String resourcetype;		// 资源类型
	private String resourcename;		// 资源类型名称
	private String filetype;		// 文件类型
	private String filename;		// 文件名称
	private String shareflag;		// 是否分享
	
	private String chapterId;		// 章节
	
	private Integer browse;		  //资源浏览次数
	private Integer glanceCount;  //返回的浏览次数
	private String iconLink; 	//文件类型图标链接
	
	private String subject;
	
	private String subjectIcon;
	
	private String[] loginNames;
	
	private String isStudent;

	public EduTeacheResource() {
		super();
	}

	public EduTeacheResource(String id){
		super(id);
	}

	@ExcelField(title="资源类型", dictType="resource_type", align=2, sort=1)
	public String getResourcetype() {
		return resourcetype;
	}

	public void setResourcetype(String resourcetype) {
		this.resourcetype = resourcetype;
	}
	
	@ExcelField(title="资源类型名称", align=2, sort=2)
	public String getResourcename() {
		return resourcename;
	}

	public void setResourcename(String resourcename) {
		this.resourcename = resourcename;
	}
	
	@ExcelField(title="文件类型", dictType="file_type", align=2, sort=3)
	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	
	@ExcelField(title="文件名称", align=2, sort=4)
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@ExcelField(title="是否分享", dictType="yes_no", align=2, sort=5)
	public String getShareflag() {
		return shareflag;
	}

	public void setShareflag(String shareflag) {
		this.shareflag = shareflag;
	}

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	public Integer getBrowse() {
		return browse;
	}

	public void setBrowse(Integer browse) {
		this.browse = browse;
	}

	public Integer getGlanceCount() {
		return glanceCount;
	}

	public void setGlanceCount(Integer glanceCount) {
		this.glanceCount = glanceCount;
	}

	public String getIconLink() {
		return iconLink;
	}

	public void setIconLink(String iconLink) {
		this.iconLink = iconLink;
	}

	public String[] getLoginNames() {
		return loginNames;
	}

	public void setLoginNames(String[] loginNames) {
		this.loginNames = loginNames;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubjectIcon() {
		return subjectIcon;
	}

	public void setSubjectIcon(String subjectIcon) {
		this.subjectIcon = subjectIcon;
	}

	public String getIsStudent() {
		return isStudent;
	}

	public void setIsStudent(String isStudent) {
		this.isStudent = isStudent;
	}
	
}