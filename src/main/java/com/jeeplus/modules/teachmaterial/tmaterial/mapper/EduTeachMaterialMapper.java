/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachmaterial.tmaterial.mapper;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.teachmaterial.tmaterial.entity.EduTeachMaterial;

/**
 * 教材管理MAPPER接口
 * @author 乔功
 * @version 2018-09-02
 */
@MyBatisMapper
public interface EduTeachMaterialMapper extends BaseMapper<EduTeachMaterial> {
	
	public EduTeachMaterial getByTextbook(@Param("textbook") String textbook,@Param("userId") String userId);
}