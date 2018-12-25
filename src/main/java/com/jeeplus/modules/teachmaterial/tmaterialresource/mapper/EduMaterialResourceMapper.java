/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachmaterial.tmaterialresource.mapper;

import java.util.List;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.teachmaterial.tmaterialresource.entity.EduMaterialResource;
import com.jeeplus.modules.teachmaterial.tresource.entity.EduTeacheResource;

/**
 * 教材资源管理MAPPER接口
 * @author 乔功
 * @version 2018-09-03
 */
@MyBatisMapper
public interface EduMaterialResourceMapper extends BaseMapper<EduMaterialResource> {
	
	public List<EduTeacheResource> findResourceList(EduTeacheResource eduTeacheResource);
}