/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.questionbank.questiontree.mapper;

import java.util.List;
import java.util.Map;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.questionbank.questiontree.entity.EduQuestionTree;

/**
 * 题库管理MAPPER接口
 * @author 李海军
 * @version 2018-10-03
 */
@MyBatisMapper
public interface EduQuestionTreeMapper extends BaseMapper<EduQuestionTree> {
	
	public List<EduQuestionTree> findList2(EduQuestionTree entity);
	
	public void saveKnowLedge(Map<String, String> saveMap);
	
	public void deleteKnowLedge(Map<String, String> delMap);
	
	public Map<String,Object> getCorrectCountById(String questionId);
}