/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.knowledge.web;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.school.knowledge.entity.EduKnowledge;
import com.jeeplus.modules.school.knowledge.service.EduKnowledgeService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 知识点管理Controller
 * @author 乔功
 * @version 2018-09-01
 */
@Controller
@RequestMapping(value = "${adminPath}/school/knowledge/eduKnowledge")
public class EduKnowledgeController extends BaseController {

	@Autowired
	private EduKnowledgeService eduKnowledgeService;
	
	@ModelAttribute
	public EduKnowledge get(@RequestParam(required=false) String id) {
		EduKnowledge entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eduKnowledgeService.get(id);
		}
		if (entity == null){
			entity = new EduKnowledge();
		}
		return entity;
	}
	
	/**
	 * 知识点管理列表页面
	 */
	@RequiresPermissions("school:knowledge:eduKnowledge:list")
	@RequestMapping(value = {"list", ""})
	public String list(EduKnowledge eduKnowledge, Model model) {
		model.addAttribute("eduKnowledge", eduKnowledge);
		return "modules/school/knowledge/eduKnowledgeList";
	}
	
		/**
	 * 知识点管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("school:knowledge:eduKnowledge:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EduKnowledge eduKnowledge, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		eduKnowledge.setCreateBy(UserUtils.getUser());
		Page<EduKnowledge> page = eduKnowledgeService.findPage(new Page<EduKnowledge>(request, response), eduKnowledge); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑知识点管理表单页面
	 */
	@RequiresPermissions(value={"school:knowledge:eduKnowledge:view","school:knowledge:eduKnowledge:add","school:knowledge:eduKnowledge:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(EduKnowledge eduKnowledge, Model model) {
		model.addAttribute("eduKnowledge", eduKnowledge);
		return "modules/school/knowledge/eduKnowledgeForm";
	}

	/**
	 * 保存知识点管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"school:knowledge:eduKnowledge:add","school:knowledge:eduKnowledge:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(EduKnowledge eduKnowledge, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(eduKnowledge);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		eduKnowledgeService.save(eduKnowledge);//保存
		j.setSuccess(true);
		j.setMsg("保存知识点管理成功");
		return j;
	}
	
	/**
	 * 删除知识点管理
	 */
	@ResponseBody
	@RequiresPermissions("school:knowledge:eduKnowledge:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(EduKnowledge eduKnowledge) {
		AjaxJson j = new AjaxJson();
		eduKnowledgeService.delete(eduKnowledge);
		j.setMsg("删除知识点管理成功");
		return j;
	}
	
	/**
	 * 批量删除知识点管理
	 */
	@ResponseBody
	@RequiresPermissions("school:knowledge:eduKnowledge:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			eduKnowledgeService.delete(eduKnowledgeService.get(id));
		}
		j.setMsg("删除知识点管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("school:knowledge:eduKnowledge:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(EduKnowledge eduKnowledge, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "知识点管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EduKnowledge> page = eduKnowledgeService.findPage(new Page<EduKnowledge>(request, response, -1), eduKnowledge);
    		new ExportExcel("知识点管理", EduKnowledge.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出知识点管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("school:knowledge:eduKnowledge:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EduKnowledge> list = ei.getDataList(EduKnowledge.class);
			for (EduKnowledge eduKnowledge : list){
				try{
					eduKnowledgeService.save(eduKnowledge);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条知识点管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条知识点管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入知识点管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入知识点管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("school:knowledge:eduKnowledge:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "知识点管理数据导入模板.xlsx";
    		List<EduKnowledge> list = Lists.newArrayList(); 
    		new ExportExcel("知识点管理数据", EduKnowledge.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}