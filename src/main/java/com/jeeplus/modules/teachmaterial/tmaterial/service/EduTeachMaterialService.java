/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachmaterial.tmaterial.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.teachmaterial.tmaterial.entity.EduTeachMaterial;
import com.jeeplus.modules.teachmaterial.tmaterial.mapper.EduTeachMaterialMapper;

/**
 * 教材管理Service
 * @author 乔功
 * @version 2018-09-02
 */
@Service
@Transactional(readOnly = true)
public class EduTeachMaterialService extends CrudService<EduTeachMaterialMapper, EduTeachMaterial> {

	@Autowired
	private EduTeachMaterialMapper eduTeachMaterialMapper;
	
	public EduTeachMaterial get(String id) {
		return super.get(id);
	}
	
	public List<EduTeachMaterial> findList(EduTeachMaterial eduTeachMaterial) {
		return super.findList(eduTeachMaterial);
	}
	
	public Page<EduTeachMaterial> findPage(Page<EduTeachMaterial> page, EduTeachMaterial eduTeachMaterial) {
		return super.findPage(page, eduTeachMaterial);
	}
	
	@Transactional(readOnly = false)
	public void save(EduTeachMaterial eduTeachMaterial) {
		super.save(eduTeachMaterial);
	}
	
	@Transactional(readOnly = false)
	public void delete(EduTeachMaterial eduTeachMaterial) {
		super.delete(eduTeachMaterial);
	}
	
	public EduTeachMaterial getByTextbook(String textbook,String userId) {
		return eduTeachMaterialMapper.getByTextbook(textbook,userId);
	}
	
	
}