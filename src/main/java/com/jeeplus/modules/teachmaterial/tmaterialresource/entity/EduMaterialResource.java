/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachmaterial.tmaterialresource.entity;

import com.jeeplus.modules.teachmaterial.tresource.entity.EduTeacheResource;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 教材资源管理Entity
 * @author 乔功
 * @version 2018-09-03
 */
public class EduMaterialResource extends DataEntity<EduMaterialResource> {
	
	private static final long serialVersionUID = 1L;
	private String materialId;		// 教材
	private String textbookId;		// 课本
	private String chapterId;		// 章节
	private EduTeacheResource resource;		// 资源
	private String status;		// 状态
	
	public EduMaterialResource() {
		super();
	}

	public EduMaterialResource(String id){
		super(id);
	}

	@ExcelField(title="教材", align=2, sort=1)
	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
	
	@ExcelField(title="课本", align=2, sort=2)
	public String getTextbookId() {
		return textbookId;
	}

	public void setTextbookId(String textbookId) {
		this.textbookId = textbookId;
	}
	
	@ExcelField(title="章节", align=2, sort=3)
	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}
	
	@NotNull(message="资源不能为空")
	@ExcelField(title="资源", fieldType=EduTeacheResource.class, value="resource.resourcename", align=2, sort=4)
	public EduTeacheResource getResource() {
		return resource;
	}

	public void setResource(EduTeacheResource resource) {
		this.resource = resource;
	}
	
	@ExcelField(title="状态", dictType="status", align=2, sort=5)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}