/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.textbook.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.school.textbook.entity.Textbook;
import com.jeeplus.modules.school.textbook.mapper.TextbookMapper;

/**
 * 课本管理Service
 * @author 李海军
 * @version 2018-09-05
 */
@Service
@Transactional(readOnly = true)
public class TextbookService extends TreeService<TextbookMapper, Textbook> {

	@Autowired
	private TextbookMapper textbookMapper;
	
	public Textbook get(String id) {
		return super.get(id);
	}
	
	public List<Textbook> findList(Textbook textbook) {
		if (StringUtils.isNotBlank(textbook.getParentIds())){
			textbook.setParentIds(","+textbook.getParentIds()+",");
		}
		return super.findList(textbook);
	}
	
	public List<Textbook> findTreeList(Textbook textbook) {
		if (StringUtils.isNotBlank(textbook.getParentIds())){
			textbook.setParentIds(textbook.getParentIds());
		}
		return textbookMapper.findTreeList(textbook);
	}
	
	public Page<Textbook> findFirstList(Page<Textbook> page, Textbook entity) {
		dataRuleFilter(entity);
		entity.setPage(page);
		page.setList(mapper.findFirstList(entity));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(Textbook textbook) {
		super.save(textbook);
	}
	
	@Transactional(readOnly = false)
	public void delete(Textbook textbook) {
		super.delete(textbook);
	}
	
	//根据登录者id查询课本章节信息
	public List<Map<String,String>> findTextByUserId(String userId){
		return textbookMapper.findTextByUserId(userId);
	}
}