/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachmaterial.tresource.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.teachmaterial.tresource.entity.EduTeacheResource;
import com.jeeplus.modules.teachmaterial.tresource.mapper.EduTeacheResourceMapper;

/**
 * 资源管理Service
 * @author 李海军
 * @version 2018-09-04
 */
@Service
@Transactional(readOnly = true)
public class EduTeacheResourceService extends CrudService<EduTeacheResourceMapper, EduTeacheResource> {

	@Autowired
	private EduTeacheResourceMapper eduTeacheResourceMapper;
	
	public EduTeacheResource get(String id) {
		return super.get(id);
	}
	
	public List<EduTeacheResource> findList(EduTeacheResource eduTeacheResource) {
		return super.findList(eduTeacheResource);
	}
	
	public Page<EduTeacheResource> findPage(Page<EduTeacheResource> page, EduTeacheResource eduTeacheResource) {
		return super.findPage(page, eduTeacheResource);
	}
	
	@Transactional(readOnly = false)
	public void save(EduTeacheResource eduTeacheResource) {
		super.save(eduTeacheResource);
	}
	
	@Transactional(readOnly = false)
	public void delete(EduTeacheResource eduTeacheResource) {
		super.delete(eduTeacheResource);
	}
	
	@Transactional(readOnly = false)
	public int updateBrowse(EduTeacheResource eduTeacheResource) {
		return eduTeacheResourceMapper.updateBrowse(eduTeacheResource);
	}
	
	public String getSubjectByTextId(String chapterId){
		return eduTeacheResourceMapper.getSubjectByTextId(chapterId);
	}
	
	@Transactional(readOnly = false)
	public void insertResourceLog(String userid,String reourceid,String chapterId) {
		//获取资源的学科
		String subject = eduTeacheResourceMapper.getSubjectByTextId(chapterId);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", UUID.randomUUID().toString().replace("-", ""));
		params.put("subject", subject);
		params.put("student", userid);
		params.put("resource", reourceid);
		eduTeacheResourceMapper.insertResourceLog(params);
	}
}