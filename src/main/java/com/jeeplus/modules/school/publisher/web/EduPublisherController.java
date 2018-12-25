/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.publisher.web;

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
import com.jeeplus.modules.school.publisher.entity.EduPublisher;
import com.jeeplus.modules.school.publisher.service.EduPublisherService;

/**
 * 出版社管理Controller
 * @author 乔功
 * @version 2018-08-22
 */
@Controller
@RequestMapping(value = "${adminPath}/school/publisher/eduPublisher")
public class EduPublisherController extends BaseController {

	@Autowired
	private EduPublisherService eduPublisherService;
	
	@ModelAttribute
	public EduPublisher get(@RequestParam(required=false) String id) {
		EduPublisher entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eduPublisherService.get(id);
		}
		if (entity == null){
			entity = new EduPublisher();
		}
		return entity;
	}
	
	/**
	 * 出版社管理列表页面
	 */
	@RequiresPermissions("school:publisher:eduPublisher:list")
	@RequestMapping(value = {"list", ""})
	public String list(EduPublisher eduPublisher, Model model) {
		model.addAttribute("eduPublisher", eduPublisher);
		return "modules/school/publisher/eduPublisherList";
	}
	
		/**
	 * 出版社管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("school:publisher:eduPublisher:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EduPublisher eduPublisher, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EduPublisher> page = eduPublisherService.findPage(new Page<EduPublisher>(request, response), eduPublisher); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑出版社管理表单页面
	 */
	@RequiresPermissions(value={"school:publisher:eduPublisher:view","school:publisher:eduPublisher:add","school:publisher:eduPublisher:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(EduPublisher eduPublisher, Model model) {
		model.addAttribute("eduPublisher", eduPublisher);
		return "modules/school/publisher/eduPublisherForm";
	}

	/**
	 * 保存出版社管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"school:publisher:eduPublisher:add","school:publisher:eduPublisher:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(EduPublisher eduPublisher, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(eduPublisher);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		eduPublisherService.save(eduPublisher);//保存
		j.setSuccess(true);
		j.setMsg("保存出版社管理成功");
		return j;
	}
	
	/**
	 * 删除出版社管理
	 */
	@ResponseBody
	@RequiresPermissions("school:publisher:eduPublisher:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(EduPublisher eduPublisher) {
		AjaxJson j = new AjaxJson();
		eduPublisherService.delete(eduPublisher);
		j.setMsg("删除出版社管理成功");
		return j;
	}
	
	/**
	 * 批量删除出版社管理
	 */
	@ResponseBody
	@RequiresPermissions("school:publisher:eduPublisher:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			eduPublisherService.delete(eduPublisherService.get(id));
		}
		j.setMsg("删除出版社管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("school:publisher:eduPublisher:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(EduPublisher eduPublisher, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "出版社管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EduPublisher> page = eduPublisherService.findPage(new Page<EduPublisher>(request, response, -1), eduPublisher);
    		new ExportExcel("出版社管理", EduPublisher.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出出版社管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("school:publisher:eduPublisher:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EduPublisher> list = ei.getDataList(EduPublisher.class);
			for (EduPublisher eduPublisher : list){
				try{
					eduPublisherService.save(eduPublisher);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条出版社管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条出版社管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入出版社管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入出版社管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("school:publisher:eduPublisher:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "出版社管理数据导入模板.xlsx";
    		List<EduPublisher> list = Lists.newArrayList(); 
    		new ExportExcel("出版社管理数据", EduPublisher.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}