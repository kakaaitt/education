/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.studentparent.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.school.studentparent.entity.EduStudentParent;

/**
 * 学生家长管理MAPPER接口
 * @author 乔功
 * @version 2018-08-30
 */
@MyBatisMapper
public interface EduStudentParentMapper extends BaseMapper<EduStudentParent> {
	
	public EduStudentParent selectByStudentAndParent(EduStudentParent eduStudentParent);
	
	public int deleteByStudentAndParent(EduStudentParent eduStudentParent);
}