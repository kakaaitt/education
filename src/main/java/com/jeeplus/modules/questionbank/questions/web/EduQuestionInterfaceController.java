/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.questionbank.questions.web;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.questionbank.questions.service.EduQuestionService;
import com.jeeplus.modules.questionbank.questiontree.entity.EduQuestionTree;
import com.jeeplus.modules.questionbank.questiontree.service.EduQuestionTreeService;
import com.jeeplus.modules.school.knowledge.entity.EduKnowledge;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 题目管理Controller
 * @author 李海军
 * @version 2018-09-16
 */
@Controller
@RequestMapping(value = "${adminPath}/questions")
public class EduQuestionInterfaceController extends BaseController {

	@Autowired
	private EduQuestionService eduQuestionService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private EduQuestionTreeService eduQuestionTreeService;
	
	/**
	 * 题目管理列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public AjaxJson data(EduQuestionTree eduQuestion, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		User loginUser = UserUtils.getUser();
			eduQuestion.setCreateBy(loginUser);
		List<EduQuestionTree> list = eduQuestionTreeService.findList(eduQuestion);
		for(EduQuestionTree question : list){
			//单选，多选，判断题，返回选项数组
			if( "1".equals(question.getQuestionType()) || 
				"2".equals(question.getQuestionType()) || 
				"3".equals(question.getQuestionType())){
				question.setOptionList(question.getOptions().split("；"));
			}
			//单选，多选，判断，填空，返回正确率
			if(!"5".equals(question.getQuestionType())){
				Map<String,Object> counts = eduQuestionTreeService.getCorrectCountById(question.getId());
				if(StringUtils.isEmpty(counts.get("correctCount").toString()) || "0".equals(counts.get("totalCount").toString())){
					question.setCorrectCount(0);
				}else{
					Integer correctCount = Integer.valueOf(counts.get("correctCount").toString());
					Integer totalCount = Integer.valueOf(counts.get("totalCount").toString());
					NumberFormat numberFormat = NumberFormat.getInstance();  
					// 设置精确到小数点后2位  
					numberFormat.setMaximumFractionDigits(0);  
					String result = numberFormat.format((float) correctCount / (float) totalCount * 100);
					question.setCorrectCount(Integer.valueOf(result));
				}
			}
			question.setSubject(DictUtils.getDictLabel(question.getSubject(), "subject", ""));
			//附件
			List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
			if(!StringUtils.isEmpty(question.getFiles())){
				String[] files = question.getFiles().split("\\|");
				for(int i = 0;i < files.length;i++){
					String file = files[i];
					Map<String,Object> fileMap = new HashMap<String,Object>();
					String[] fileNames = file.split("/");
					String[] types = file.split("\\.");
					String type = getFileType(types[types.length - 1]);
					fileMap.put("fileUrl", Global.getConfig("fileUrl") +  file);
					fileMap.put("name", fileNames[fileNames.length - 1]);
					fileMap.put("type", type);
					fileMap.put("iconUrl", Global.getConfig("fileUrl") + Global.getFileTypeUrl() + type + ".png");
					fileList.add(fileMap);
				}
			}
			question.setFilesList(fileList);
			//知识点
			List<EduKnowledge> knowledges = eduQuestionService.findKnowledgesList(question.getId());
			question.setKonwledgeList(knowledges);
			//学校
			question.setOffice(officeService.get(question.getCreateBy().getOffice().getId()));
		}
		j.put("rows", list);
		j.put("total", list.size());
		return j;
	}

}