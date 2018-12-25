package com.jeeplus.modules.train.mapper;

import java.util.List;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.questionbank.questiontree.entity.EduQuestionTree;
import com.jeeplus.modules.train.entity.EduTrain;
import com.jeeplus.modules.train.entity.EduTrainQuestion;
import com.jeeplus.modules.train.entity.EduTrainSubmit;

@MyBatisMapper
public interface EduTrainMapper extends BaseMapper<EduTrain> {

	public List<EduTrain> selectList(EduTrain eduTrain);
	
	public Integer selectMaxNumber(EduTrain eduTrain);
	
	public int insert(EduTrain eduTrain);
	
	public String selectClasses(EduTrain eduTrain);
	
	public String selectTeacher(EduTrain eduTrain);
	
	public List<String> selectRandQuestions(EduTrain eduTrain);
	
	public void insertTrainQuestion(EduTrainQuestion eduTrainQuestion);
	
	public List<EduQuestionTree> findQuestionList(EduTrain eduTrain);
	
	public void insertTrainSubmit(EduTrainSubmit eduTrainSubmit);
	
	public void insertAllTrainSubmit(List<EduTrainSubmit> list);
	
	public void completeTrain(EduTrain eduTrain);
	
}
