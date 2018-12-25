/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.questionbank.questiontree.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.questionbank.questiontree.entity.EduQuestionTree;
import com.jeeplus.modules.questionbank.questiontree.mapper.EduQuestionTreeMapper;

/**
 * 题库管理Service
 * @author 李海军
 * @version 2018-10-03
 */
@Service
@Transactional(readOnly = true)
public class EduQuestionTreeService extends CrudService<EduQuestionTreeMapper, EduQuestionTree> {

	public EduQuestionTree get(String id) {
		return super.get(id);
	}
	
	public List<EduQuestionTree> findList(EduQuestionTree eduQuestionTree) {
		return super.findList(eduQuestionTree);
	}
	
	public Page<EduQuestionTree> findPage(Page<EduQuestionTree> page, EduQuestionTree eduQuestionTree) {
		return super.findPage(page, eduQuestionTree);
	}
	
	public Page<EduQuestionTree> findPage2(Page<EduQuestionTree> page, EduQuestionTree eduQuestionTree) {
		dataRuleFilter(eduQuestionTree);
		eduQuestionTree.setPage(page);
		page.setList(mapper.findList2(eduQuestionTree));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(EduQuestionTree eduQuestionTree) {
		if(eduQuestionTree.getKonwledges()!=null){
			eduQuestionTree.setRemarks(eduQuestionTree.getKonwledges().getName());
		}else {
			eduQuestionTree.setRemarks("");
		}
	
		super.save(eduQuestionTree);
	}
	
	//新增知识点
	@Transactional(readOnly = false)
	public void saveKnowLedge(EduQuestionTree eduQuestionTree) {
		String[] konwIds=eduQuestionTree.getKonwledges().getId().split(",");
		String[] konwidNames=eduQuestionTree.getKonwledges().getName().split(",");
		
		//先删除对应的知识点--根据question_id唯一
		String questionId=eduQuestionTree.getId();
		Map<String, String> delMap=new HashMap<>();
		delMap.put("question_id", questionId);
		mapper.deleteKnowLedge(delMap);
		//再一条条新增对应的知识点
		for(int i=0;i<konwIds.length;i++){
			Map<String, String> saveMap=new HashMap<>();
			saveMap.put("question_id", eduQuestionTree.getId());
			saveMap.put("knowledge", konwIds[i]);
			saveMap.put("knowledgename", konwidNames[i]);
			saveMap.put("del_flag", "0");
			mapper.saveKnowLedge(saveMap);
		}
	
	}
	
	@Transactional(readOnly = false)
	public void delete(EduQuestionTree eduQuestionTree) {
		super.delete(eduQuestionTree);
	}
	
	public Map<String,Object> getCorrectCountById(String questionId){
		return mapper.getCorrectCountById(questionId);
	}
}