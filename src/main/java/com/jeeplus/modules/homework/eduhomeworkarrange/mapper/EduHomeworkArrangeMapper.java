/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.eduhomeworkarrange.mapper;

import java.util.List;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.homework.eduhomeworkarrange.entity.EduHomeworkArrange;

/**
 * 作业布置管理MAPPER接口
 * @author 乔功
 * @version 2018-10-07
 */
@MyBatisMapper
public interface EduHomeworkArrangeMapper extends BaseMapper<EduHomeworkArrange> {
	
	public int insertAll(List<EduHomeworkArrange> list);
}