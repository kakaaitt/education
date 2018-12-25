/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachmaterial.tmaterialresource.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.teachmaterial.tmaterial.entity.EduTeachMaterial;
import com.jeeplus.modules.teachmaterial.tmaterialresource.entity.EduMaterialResource;
import com.jeeplus.modules.teachmaterial.tmaterialresource.mapper.EduMaterialResourceMapper;
import com.jeeplus.modules.teachmaterial.tresource.entity.EduTeacheResource;

/**
 * 教材资源管理Service
 * @author 乔功
 * @version 2018-09-03
 */
@Service
@Transactional(readOnly = true)
public class EduMaterialResourceService extends CrudService<EduMaterialResourceMapper, EduMaterialResource> {

	public EduMaterialResource get(String id) {
		return super.get(id);
	}
	
	public List<EduMaterialResource> findList(EduMaterialResource eduMaterialResource) {
		return super.findList(eduMaterialResource);
	}
	
	public Page<EduMaterialResource> findPage(Page<EduMaterialResource> page, EduMaterialResource eduMaterialResource) {
		return super.findPage(page, eduMaterialResource);
	}
	
	@Transactional(readOnly = false)
	public void save(EduMaterialResource eduMaterialResource) {
		super.save(eduMaterialResource);
	}
	
	@Transactional(readOnly = false)
	public void delete(EduMaterialResource eduMaterialResource) {
		super.delete(eduMaterialResource);
	}
	
	public Page<EduTeacheResource> findResourcePage(Page<EduTeacheResource> page, EduTeacheResource eduTeacheResource) {
		dataRuleFilter(eduTeacheResource);
		eduTeacheResource.setPage(page);
		List<EduTeacheResource> list = mapper.findResourceList(eduTeacheResource);
		for(EduTeacheResource resource : list){
			resource.setFiletype(DictUtils.getDictLabel(resource.getFiletype(), "file_type", ""));
			resource.setResourcetype(DictUtils.getDictLabel(resource.getResourcetype(), "resource_type", ""));
		}
		page.setList(list);
		return page;
	}
	
}