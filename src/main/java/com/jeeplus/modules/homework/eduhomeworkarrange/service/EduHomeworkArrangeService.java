/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.eduhomeworkarrange.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.homework.eduhomework.entity.EduHomework;
import com.jeeplus.modules.homework.eduhomework.mapper.EduHomeworkMapper;
import com.jeeplus.modules.homework.eduhomeworkarrange.entity.EduHomeworkArrange;
import com.jeeplus.modules.homework.eduhomeworkarrange.mapper.EduHomeworkArrangeMapper;
import com.jeeplus.modules.questionbank.questiontree.entity.EduQuestionTree;
import com.jeeplus.modules.questionbank.questiontree.mapper.EduQuestionTreeMapper;
import com.jeeplus.modules.school.studentclass.mapper.EduStudentClassMapper;

/**
 * 作业布置管理Service
 * @author 乔功
 * @version 2018-10-07
 */
@Service
@Transactional(readOnly = true)
public class EduHomeworkArrangeService extends CrudService<EduHomeworkArrangeMapper, EduHomeworkArrange> {

	@Autowired
	private EduStudentClassMapper eduStudentClassMapper;
	
	@Autowired
	private EduHomeworkArrangeMapper eduHomeworkArrangeMapper;
	
	@Autowired
	private EduQuestionTreeMapper eEduQuestionTreeMapper;//更新题库布置次数
	
	@Autowired
	private EduHomeworkMapper eduHomeworkMapper;//布置作业id
	
	public EduHomeworkArrange get(String id) {
		return super.get(id);
	}
	
	public List<EduHomeworkArrange> findList(EduHomeworkArrange eduHomeworkArrange) {
		return super.findList(eduHomeworkArrange);
	}
	
	public Page<EduHomeworkArrange> findPage(Page<EduHomeworkArrange> page, EduHomeworkArrange eduHomeworkArrange) {
		return super.findPage(page, eduHomeworkArrange);
	}
	
	@Transactional(readOnly = false)
	public void save(EduHomeworkArrange eduHomeworkArrange) {
		super.save(eduHomeworkArrange);
	}
	
	@Transactional(readOnly = false)
	public void delete(EduHomeworkArrange eduHomeworkArrange) {
		super.delete(eduHomeworkArrange);
	}
	
	@Transactional(readOnly = false)
	public int arrangeHomework(EduHomeworkArrange eduHomeworkArrange){
		//是否有多个班级
		String[] classesArr = eduHomeworkArrange.getClasses().split(",");
		Date date = new Date();
		List<EduHomeworkArrange> arrangeList = new ArrayList<EduHomeworkArrange>();
		for(String classes : classesArr){
			List<String> studentList = eduStudentClassMapper.selectStudentByClasses(classes);
			//获取学生信息
			for(String student : studentList){
				EduHomeworkArrange arrange = new EduHomeworkArrange();
				arrange.setId(UUID.randomUUID().toString().replace("-", ""));
				arrange.setHomework(eduHomeworkArrange.getHomework());
				arrange.setClasses(classes);
				arrange.setStudent(student);
				arrange.setCreateDate(date);
				arrangeList.add(arrange);
			}
		}
		if(arrangeList != null && arrangeList.size() > 0){
			int renum=eduHomeworkArrangeMapper.insertAll(arrangeList);
			//根据作业id查询关联的作业,更新题目的布置次数
			EduHomework eduHomework = new EduHomework(eduHomeworkArrange.getHomework());
			List<EduQuestionTree> questions= eduHomeworkMapper.findQuestionList(eduHomework);
			
			for(EduQuestionTree edutree:questions){
				edutree.setPublicnNum(edutree.getPublicnNum()+1);
				eEduQuestionTreeMapper.update(edutree);
			}
			//将作业状态更改为已布置
			eduHomework.setStatus("1");
			eduHomeworkMapper.updateStatus(eduHomework);
			return renum;
		}else{
			return 0;
		}
	}
	
}