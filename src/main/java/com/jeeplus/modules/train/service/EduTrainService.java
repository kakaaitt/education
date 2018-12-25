/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.train.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.questionbank.questiontree.entity.EduQuestionTree;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.train.entity.EduTrain;
import com.jeeplus.modules.train.entity.EduTrainQuestion;
import com.jeeplus.modules.train.entity.EduTrainSubmit;
import com.jeeplus.modules.train.mapper.EduTrainMapper;

/**
 * 学生练习Service
 * @author jeeplus
 * @version 2017-05-16
 */
@Service
@Transactional(readOnly = true)
public class EduTrainService extends CrudService<EduTrainMapper, EduTrain> {
	
	@Autowired
	private EduTrainMapper eduTrainMapper;
	
	public List<EduTrain> selectList(EduTrain eduTrain){
		return eduTrainMapper.selectList(eduTrain);
	}
	
	@Transactional(readOnly = false)
	public String addNewTrain(EduTrain eduTrain){
		//插入学生练习信息
		String trainId = UUID.randomUUID().toString().replace("-", "");
		eduTrain.setId(trainId);
		eduTrain.setName("习题练习");
		//查询作业次数
		Integer maxNumber = eduTrainMapper.selectMaxNumber(eduTrain);
		if(maxNumber != null && maxNumber != 0){
			eduTrain.setNumber(maxNumber+1);
		}else{
			eduTrain.setNumber(1);
		}
		String classes = eduTrainMapper.selectClasses(eduTrain);
		eduTrain.setClasses(classes);
		User user = new User(eduTrain.getStudent());
		eduTrain.setCreateBy(user);
		eduTrainMapper.insert(eduTrain);
		//该班级学科教师id
//		String teacher = eduTrainMapper.selectTeacher(eduTrain);
//		eduTrain.setTeacher(teacher);
		//生成练习题
		List<String> questionList = eduTrainMapper.selectRandQuestions(eduTrain);
		for(String questionid : questionList){
			EduTrainQuestion etq = new EduTrainQuestion();
			etq.setId(UUID.randomUUID().toString().replace("-", ""));
			etq.setTrain(trainId);
			etq.setQuestion(questionid);
			eduTrainMapper.insertTrainQuestion(etq);
		}
		return trainId;
	}
	
	public List<EduQuestionTree> findQuestionList(EduTrain eduTrain){
		return eduTrainMapper.findQuestionList(eduTrain);
	}
	
	@Transactional(readOnly = false)
	public void submit(EduTrainSubmit eduTrainSubmit){
//		eduTrainSubmit.setId(UUID.randomUUID().toString().replace("-", ""));
//		eduTrainSubmit.setDelFlag("0");
//		if(eduTrainSubmit.getAnswer().equals(eduTrainSubmit.getStudentAnswer())){
//			eduTrainSubmit.setIsCorrect("1");
//		}else{
//			eduTrainSubmit.setIsCorrect("0");
//		}
//		eduTrainMapper.insertTrainSubmit(eduTrainSubmit);
		
		List<EduTrainSubmit> answers = eduTrainSubmit.getAnswers();
		Date date = new Date();
		for(EduTrainSubmit submit : answers){
			submit.setId(UUID.randomUUID().toString().replace("-", ""));
			submit.setTrain(eduTrainSubmit.getTrain());
			submit.setCreateDate(date);
			//客观题自动批改
			if(submit.getAnswer().equals(submit.getStudentAnswer())){
				submit.setIsCorrect("1");
			}else{
				submit.setIsCorrect("0");
			}
		
		}
		eduTrainMapper.insertAllTrainSubmit(answers);
		//更新练习状态
		EduTrain eduTrain = new EduTrain();
		eduTrain.setId(eduTrainSubmit.getTrain());
		eduTrainMapper.completeTrain(eduTrain);
	}
	
	@Transactional(readOnly = false)
	public void completeTrain(EduTrain eduTrain){
		eduTrainMapper.completeTrain(eduTrain);
	}
}