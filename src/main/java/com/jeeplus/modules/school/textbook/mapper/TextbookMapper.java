/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.textbook.mapper;

import java.util.List;
import java.util.Map;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.school.textbook.entity.Textbook;

/**
 * 课本管理MAPPER接口
 * @author 李海军
 * @version 2018-09-25
 */
@MyBatisMapper
public interface TextbookMapper extends TreeMapper<Textbook> {
	//乔功增加用于选择
    List<Textbook> findFirstList(Textbook textbook);
	
	List<Textbook> findTreeList(Textbook textbook);
	
	//根据登录者id查询课本章节信息
	List<Map<String,String>> findTextByUserId(String userId);
}