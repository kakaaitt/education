/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.questionbank.questionlist.web;

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
import com.jeeplus.modules.questionbank.questionlist.entity.Eduquestionlist;
import com.jeeplus.modules.questionbank.questionlist.service.EduquestionlistService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 题库章节关联Controller
 * @author 李海军
 * @version 2018-10-23
 */
@Controller
@RequestMapping(value = "${adminPath}/questionbank/questionlist/eduquestionlist")
public class EduquestionlistController extends BaseController {

	@Autowired
	private EduquestionlistService eduquestionlistService;
	
	@ModelAttribute
	public Eduquestionlist get(@RequestParam(required=false) String id) {
		Eduquestionlist entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eduquestionlistService.get(id);
		}
		if (entity == null){
			entity = new Eduquestionlist();
		}
		return entity;
	}
	
	/**
	 * 题目关系管理列表页面
	 */
//	@RequiresPermissions("questionbank:questionlist:eduquestionlist:list")
	@RequestMapping(value = {"list", ""})
	public String list(Eduquestionlist eduquestionlist, Model model) {
		model.addAttribute("eduquestionlist", eduquestionlist);
		return "modules/questionbank/questionlist/eduquestionlistList";
	}
	
		/**
	 * 题目关系管理列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("questionbank:questionlist:eduquestionlist:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Eduquestionlist eduquestionlist, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user=UserUtils.getUser();
		if(!user.isAdmin()){
			eduquestionlist.setTeacher(user.getId());
			eduquestionlist.setCreateBy(user);
		}else{
			eduquestionlist.setTeacher("admin");
		}
		
		Page<Eduquestionlist> page = eduquestionlistService.findPage(new Page<Eduquestionlist>(request, response), eduquestionlist); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑题目关系管理表单页面
	 */
	@RequiresPermissions(value={"questionbank:questionlist:eduquestionlist:view","questionbank:questionlist:eduquestionlist:add","questionbank:questionlist:eduquestionlist:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Eduquestionlist eduquestionlist, Model model) {
		model.addAttribute("eduquestionlist", eduquestionlist);
		return "modules/questionbank/questionlist/eduquestionlistForm";
	}

	/**
	 * 保存题目关系管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"questionbank:questionlist:eduquestionlist:add","questionbank:questionlist:eduquestionlist:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Eduquestionlist eduquestionlist, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(eduquestionlist);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		eduquestionlistService.save(eduquestionlist);//保存
		j.setSuccess(true);
		j.setMsg("保存题目关系管理成功");
		return j;
	}
	
	/**
	 * 删除题目关系管理
	 */
	@ResponseBody
	@RequiresPermissions("questionbank:questionlist:eduquestionlist:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Eduquestionlist eduquestionlist) {
		AjaxJson j = new AjaxJson();
		eduquestionlistService.delete(eduquestionlist);
		j.setMsg("删除题目关系管理成功");
		return j;
	}
	
	/**
	 * 批量删除题目关系管理
	 */
	@ResponseBody
	@RequiresPermissions("questionbank:questionlist:eduquestionlist:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			eduquestionlistService.delete(eduquestionlistService.get(id));
		}
		j.setMsg("删除题目关系管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("questionbank:questionlist:eduquestionlist:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Eduquestionlist eduquestionlist, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "题目关系管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Eduquestionlist> page = eduquestionlistService.findPage(new Page<Eduquestionlist>(request, response, -1), eduquestionlist);
    		new ExportExcel("题目关系管理", Eduquestionlist.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出题目关系管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("questionbank:questionlist:eduquestionlist:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Eduquestionlist> list = ei.getDataList(Eduquestionlist.class);
			for (Eduquestionlist eduquestionlist : list){
				try{
					eduquestionlistService.save(eduquestionlist);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条题目关系管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条题目关系管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入题目关系管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入题目关系管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("questionbank:questionlist:eduquestionlist:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "题目关系管理数据导入模板.xlsx";
    		List<Eduquestionlist> list = Lists.newArrayList(); 
    		new ExportExcel("题目关系管理数据", Eduquestionlist.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}