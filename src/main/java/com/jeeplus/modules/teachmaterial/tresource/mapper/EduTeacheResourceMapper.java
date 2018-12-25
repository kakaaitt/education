/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachmaterial.tresource.mapper;

import java.util.Map;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.teachmaterial.tresource.entity.EduTeacheResource;

/**
 * 资源管理MAPPER接口
 * @author 李海军
 * @version 2018-09-04
 */
@MyBatisMapper
public interface EduTeacheResourceMapper extends BaseMapper<EduTeacheResource> {
	
	public int updateBrowse(EduTeacheResource eduTeacheResource);
	
	public String getSubjectByTextId(String textid);
	
	public void insertResourceLog(Map<String,Object> params);
	
}