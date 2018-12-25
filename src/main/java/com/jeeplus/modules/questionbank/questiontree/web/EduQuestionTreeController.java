/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.questionbank.questiontree.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.word.ImportWord;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.questionbank.questiontree.entity.EduQuestionTree;
import com.jeeplus.modules.questionbank.questiontree.service.EduQuestionTreeService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
/**
 * 题库管理Controller
 * @author 李海军
 * @version 2018-10-03
 */
@Controller
@RequestMapping(value = "${adminPath}/questionbank/questiontree/eduQuestionTree")
public class EduQuestionTreeController extends BaseController {

	@Autowired
	private EduQuestionTreeService eduQuestionTreeService;
	
	@ModelAttribute
	public EduQuestionTree get(@RequestParam(required=false) String id) {
		EduQuestionTree entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eduQuestionTreeService.get(id);
		}
		if (entity == null){
			entity = new EduQuestionTree();
		}
		return entity;
	}
	
	/**
	 * 题库管理列表页面
	 */
//	@RequiresPermissions("questionbank:questiontree:eduQuestionTree:list")
	@RequestMapping(value ="list2")
	public String tohomework(EduQuestionTree eduQuestionTree, Model model) {
		model.addAttribute("eduQuestionTree", eduQuestionTree);
		return "modules/homework/questiontree/eduQuestionTreeList";
	}
	
	/**
	 * 作业题库管理列表页面
	 */
//	@RequiresPermissions("questionbank:questiontree:eduQuestionTree:list")
	@RequestMapping(value = {"list", ""})
	public String list(EduQuestionTree eduQuestionTree, Model model) {
		model.addAttribute("eduQuestionTree", eduQuestionTree);
		return "modules/questionbank/questiontree/eduQuestionTreeList";
	}
	
		/**
	 * 题库管理列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("questionbank:questiontree:eduQuestionTree:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EduQuestionTree eduQuestionTree, HttpServletRequest request, HttpServletResponse response, Model model) {
		//根据章节获取题目列表
		String textId=request.getParameter("chapterId");
		eduQuestionTree.setTextid(textId==null?"0":textId);
		eduQuestionTree.setCreateBy(UserUtils.getUser());
		Page<EduQuestionTree> page = eduQuestionTreeService.findPage(new Page<EduQuestionTree>(request, response), eduQuestionTree); 
		return getBootstrapData(page);
	}
	
	/**
	 * 题库管理列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("questionbank:questiontree:eduQuestionTree:list")
	@RequestMapping(value = "data2")
	public Map<String, Object> data2(EduQuestionTree eduQuestionTree, HttpServletRequest request, HttpServletResponse response, Model model) {
		//根据章节获取题目列表
		String textId=request.getParameter("chapterId");
		String homeWork=request.getParameter("homework");
		
		eduQuestionTree.setTextid(textId==null?"0":textId);
		eduQuestionTree.setCreateBy(UserUtils.getUser());
		eduQuestionTree.setHomeWork(homeWork);
		
		Page<EduQuestionTree> page = eduQuestionTreeService.findPage2(new Page<EduQuestionTree>(request, response), eduQuestionTree); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑题库管理表单页面
	 */
	@RequiresPermissions(value={"questionbank:questiontree:eduQuestionTree:view","questionbank:questiontree:eduQuestionTree:add","questionbank:questiontree:eduQuestionTree:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(EduQuestionTree eduQuestionTree, Model model) {
		model.addAttribute("eduQuestionTree", eduQuestionTree);
		return "modules/questionbank/questiontree/eduQuestionTreeForm";
	}

	/**
	 * 保存题库管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"questionbank:questiontree:eduQuestionTree:add","questionbank:questiontree:eduQuestionTree:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(EduQuestionTree eduQuestionTree, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(eduQuestionTree);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		eduQuestionTreeService.save(eduQuestionTree);//保存
		//保存知识点数据1对多情况
		eduQuestionTreeService.saveKnowLedge(eduQuestionTree);
		j.setSuccess(true);
		j.setMsg("保存题库管理成功");
		return j;
	}
	
	/**
	 * 删除题库管理
	 */
	@ResponseBody
	@RequiresPermissions("questionbank:questiontree:eduQuestionTree:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(EduQuestionTree eduQuestionTree) {
		AjaxJson j = new AjaxJson();
		eduQuestionTreeService.delete(eduQuestionTree);
		j.setMsg("删除题库管理成功");
		return j;
	}
	
	/**
	 * 批量删除题库管理
	 */
	@ResponseBody
	@RequiresPermissions("questionbank:questiontree:eduQuestionTree:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			eduQuestionTreeService.delete(eduQuestionTreeService.get(id));
		}
		j.setMsg("删除题库管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("questionbank:questiontree:eduQuestionTree:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(EduQuestionTree eduQuestionTree, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "题库管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EduQuestionTree> page = eduQuestionTreeService.findPage(new Page<EduQuestionTree>(request, response, -1), eduQuestionTree);
    		new ExportExcel("题库管理", EduQuestionTree.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出题库管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("questionbank:questiontree:eduQuestionTree:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			
			String shareType="0";//默认不分享
			String questionBankType="2";//默认老师题库
			
			//章节id
			String textId=request.getParameter("textId");
			
			StringBuilder failureMsg = new StringBuilder();
			ImportWord ei = new ImportWord();
			List<Map<String,Object>> questions=ei.ImportWordByDocx(file);
			//判断是否是管理员--来判断是否是教师题库或管理员题库，管理员题库默认设置共享
			User user=UserUtils.getUser();
			if(user.isAdmin()){
				questionBankType="1";
				shareType="1";
			}
			for (Map<String,Object> qMap : questions){
				System.out.println(qMap.get("id"));
				EduQuestionTree eduQuestion=new EduQuestionTree();
//				eduQuestion.setId(qMap.get("id").toString());
				eduQuestion.setQuestionType(DictUtils.getDictValue(qMap.get("questiontype").toString(), "questiontype", "0"));
				eduQuestion.setQuestionBankType(questionBankType);
				eduQuestion.setQuestionText(qMap.get("question").toString());
				eduQuestion.setOptions(qMap.get("options")==null?"":qMap.get("options").toString());
				eduQuestion.setAnswer(qMap.get("answer").toString());
				eduQuestion.setAnaly(qMap.get("explain")==null?"":qMap.get("explain").toString());
				eduQuestion.setShareType(shareType);
				eduQuestion.setPublicnNum(0);
				eduQuestion.setCreateBy(user);
				eduQuestion.setCreateDate(new Date());
				eduQuestion.setTextid(textId);
				
				try{
					eduQuestionTreeService.save(eduQuestion);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条题库管理记录。");
			}
			j.setSuccess(true);
			j.setMsg( "已成功导入 "+successNum+" 条题库管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入题库管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入题库管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("questionbank:questiontree:eduQuestionTree:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "题库管理数据导入模板.docx";
    		List<EduQuestionTree> list = Lists.newArrayList(); 
    		new ExportExcel("题库管理数据", EduQuestionTree.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}