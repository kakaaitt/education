/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.classes.mapper;

import java.util.List;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.school.classes.entity.EduClasses;

/**
 * 班级管理MAPPER接口
 * @author 乔功
 * @version 2018-08-30
 */
@MyBatisMapper
public interface EduClassesMapper extends BaseMapper<EduClasses> {
	
	public List<EduClasses> findListByUser(EduClasses eduClasses);
}