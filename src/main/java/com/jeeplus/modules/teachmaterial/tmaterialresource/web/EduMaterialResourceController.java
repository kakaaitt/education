/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachmaterial.tmaterialresource.web;

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
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.teachmaterial.tmaterialresource.entity.EduMaterialResource;
import com.jeeplus.modules.teachmaterial.tmaterialresource.service.EduMaterialResourceService;

/**
 * 教材资源管理Controller
 * @author 乔功
 * @version 2018-09-03
 */
@Controller
@RequestMapping(value = "${adminPath}/teachmaterial/tmaterialresource/eduMaterialResource")
public class EduMaterialResourceController extends BaseController {

	@Autowired
	private EduMaterialResourceService eduMaterialResourceService;
	
	@ModelAttribute
	public EduMaterialResource get(@RequestParam(required=false) String id) {
		EduMaterialResource entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eduMaterialResourceService.get(id);
		}
		if (entity == null){
			entity = new EduMaterialResource();
		}
		return entity;
	}
	
	/**
	 * 教材资源管理列表页面
	 */
	@RequiresPermissions("teachmaterial:tmaterialresource:eduMaterialResource:list")
	@RequestMapping(value = {"list", ""})
	public String list(EduMaterialResource eduMaterialResource, Model model) {
		model.addAttribute("eduMaterialResource", eduMaterialResource);
		return "modules/teachmaterial/tmaterialresource/eduMaterialResourceList";
	}
	
		/**
	 * 教材资源管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tmaterialresource:eduMaterialResource:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EduMaterialResource eduMaterialResource, HttpServletRequest request, HttpServletResponse response, Model model) {
		eduMaterialResource.setCreateBy(UserUtils.getUser());
		Page<EduMaterialResource> page = eduMaterialResourceService.findPage(new Page<EduMaterialResource>(request, response), eduMaterialResource); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑教材资源管理表单页面
	 */
	@RequiresPermissions(value={"teachmaterial:tmaterialresource:eduMaterialResource:view","teachmaterial:tmaterialresource:eduMaterialResource:add","teachmaterial:tmaterialresource:eduMaterialResource:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(EduMaterialResource eduMaterialResource, Model model) {
		model.addAttribute("eduMaterialResource", eduMaterialResource);
		return "modules/teachmaterial/tmaterialresource/eduMaterialResourceForm";
	}

	/**
	 * 保存教材资源管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"teachmaterial:tmaterialresource:eduMaterialResource:add","teachmaterial:tmaterialresource:eduMaterialResource:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(EduMaterialResource eduMaterialResource, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(eduMaterialResource);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		eduMaterialResourceService.save(eduMaterialResource);//保存
		j.setSuccess(true);
		j.setMsg("保存教材资源管理成功");
		return j;
	}
	
	/**
	 * 删除教材资源管理
	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tmaterialresource:eduMaterialResource:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(EduMaterialResource eduMaterialResource) {
		AjaxJson j = new AjaxJson();
		eduMaterialResourceService.delete(eduMaterialResource);
		j.setMsg("删除教材资源管理成功");
		return j;
	}
	
	/**
	 * 批量删除教材资源管理
	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tmaterialresource:eduMaterialResource:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			eduMaterialResourceService.delete(eduMaterialResourceService.get(id));
		}
		j.setMsg("删除教材资源管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tmaterialresource:eduMaterialResource:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(EduMaterialResource eduMaterialResource, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "教材资源管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EduMaterialResource> page = eduMaterialResourceService.findPage(new Page<EduMaterialResource>(request, response, -1), eduMaterialResource);
    		new ExportExcel("教材资源管理", EduMaterialResource.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出教材资源管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tmaterialresource:eduMaterialResource:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EduMaterialResource> list = ei.getDataList(EduMaterialResource.class);
			for (EduMaterialResource eduMaterialResource : list){
				try{
					eduMaterialResourceService.save(eduMaterialResource);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条教材资源管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条教材资源管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入教材资源管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入教材资源管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tmaterialresource:eduMaterialResource:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "教材资源管理数据导入模板.xlsx";
    		List<EduMaterialResource> list = Lists.newArrayList(); 
    		new ExportExcel("教材资源管理数据", EduMaterialResource.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}