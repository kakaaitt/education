/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.publisher.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.school.publisher.entity.EduPublisher;
import com.jeeplus.modules.school.publisher.mapper.EduPublisherMapper;

/**
 * 出版社管理Service
 * @author 乔功
 * @version 2018-08-22
 */
@Service
@Transactional(readOnly = true)
public class EduPublisherService extends CrudService<EduPublisherMapper, EduPublisher> {

	public EduPublisher get(String id) {
		return super.get(id);
	}
	
	public List<EduPublisher> findList(EduPublisher eduPublisher) {
		return super.findList(eduPublisher);
	}
	
	public Page<EduPublisher> findPage(Page<EduPublisher> page, EduPublisher eduPublisher) {
		return super.findPage(page, eduPublisher);
	}
	
	@Transactional(readOnly = false)
	public void save(EduPublisher eduPublisher) {
		super.save(eduPublisher);
	}
	
	@Transactional(readOnly = false)
	public void delete(EduPublisher eduPublisher) {
		super.delete(eduPublisher);
	}
	
}