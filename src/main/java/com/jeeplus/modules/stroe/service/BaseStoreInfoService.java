/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.stroe.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.stroe.entity.BaseStoreInfo;
import com.jeeplus.modules.stroe.mapper.BaseStoreInfoMapper;

/**
 * 商户信息Service
 * @author liangzai
 * @version 2018-07-20
 */
@Service
@Transactional(readOnly = true)
public class BaseStoreInfoService extends CrudService<BaseStoreInfoMapper, BaseStoreInfo> {

	public BaseStoreInfo get(String id) {
		return super.get(id);
	}
	
	public List<BaseStoreInfo> findList(BaseStoreInfo baseStoreInfo) {
		return super.findList(baseStoreInfo);
	}
	
	public Page<BaseStoreInfo> findPage(Page<BaseStoreInfo> page, BaseStoreInfo baseStoreInfo) {
		return super.findPage(page, baseStoreInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(BaseStoreInfo baseStoreInfo) {
		super.save(baseStoreInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(BaseStoreInfo baseStoreInfo) {
		super.delete(baseStoreInfo);
	}
	
}