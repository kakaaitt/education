/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachmaterial.tresource.web;

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
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.teachmaterial.tresource.entity.EduTeacheResource;
import com.jeeplus.modules.teachmaterial.tresource.service.EduTeacheResourceService;

/**
 * 资源管理Controller
 * @author 李海军
 * @version 2018-09-04
 */
@Controller
@RequestMapping(value = "${adminPath}/teachmaterial/tresource/eduTeacheResource")
public class EduTeacheResourceController extends BaseController {

	@Autowired
	private EduTeacheResourceService eduTeacheResourceService;
	
	@ModelAttribute
	public EduTeacheResource get(@RequestParam(required=false) String id) {
		EduTeacheResource entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eduTeacheResourceService.get(id);
		}
		if (entity == null){
			entity = new EduTeacheResource();
		}
		return entity;
	}
	
	/**
	 * 资源管理列表页面
	 */
	@RequiresPermissions("teachmaterial:tresource:eduTeacheResource:list")
	@RequestMapping(value = {"list", ""})
	public String list(EduTeacheResource eduTeacheResource, Model model) {
		model.addAttribute("eduTeacheResource", eduTeacheResource);
		model.addAttribute("pptPlayUrl", Global.getConfig("pptPlayUrl"));
		return "modules/teachmaterial/tresource/eduTeacheResourceList";
	}
	
		/**
	 * 资源管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tresource:eduTeacheResource:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EduTeacheResource eduTeacheResource, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<EduTeacheResource> page = eduTeacheResourceService.findPage(new Page<EduTeacheResource>(request, response), eduTeacheResource); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑资源管理表单页面
	 */
	@RequiresPermissions(value={"teachmaterial:tresource:eduTeacheResource:view","teachmaterial:tresource:eduTeacheResource:add","teachmaterial:tresource:eduTeacheResource:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(EduTeacheResource eduTeacheResource, Model model) {
		model.addAttribute("eduTeacheResource", eduTeacheResource);
		return "modules/teachmaterial/tresource/eduTeacheResourceForm";
	}

	/**
	 * 保存资源管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"teachmaterial:tresource:eduTeacheResource:add","teachmaterial:tresource:eduTeacheResource:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(EduTeacheResource eduTeacheResource, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(eduTeacheResource);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//如果是管理员 默认都是分享的
		User user=UserUtils.getUser();
		if(user.isAdmin()){
			eduTeacheResource.setShareflag("1");
		}
		//新增或编辑表单保存
		eduTeacheResourceService.save(eduTeacheResource);//保存
		j.setSuccess(true);
		j.setMsg("保存资源管理成功");
		return j;
	}
	
	/**
	 * 删除资源管理
	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tresource:eduTeacheResource:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(EduTeacheResource eduTeacheResource) {
		AjaxJson j = new AjaxJson();
		eduTeacheResourceService.delete(eduTeacheResource);
		j.setMsg("删除资源管理成功");
		return j;
	}
	
	/**
	 * 批量删除资源管理
	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tresource:eduTeacheResource:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			eduTeacheResourceService.delete(eduTeacheResourceService.get(id));
		}
		j.setMsg("删除资源管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tresource:eduTeacheResource:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(EduTeacheResource eduTeacheResource, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "资源管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EduTeacheResource> page = eduTeacheResourceService.findPage(new Page<EduTeacheResource>(request, response, -1), eduTeacheResource);
    		new ExportExcel("资源管理", EduTeacheResource.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出资源管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tresource:eduTeacheResource:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EduTeacheResource> list = ei.getDataList(EduTeacheResource.class);
			for (EduTeacheResource eduTeacheResource : list){
				try{
					eduTeacheResourceService.save(eduTeacheResource);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条资源管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条资源管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入资源管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入资源管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tresource:eduTeacheResource:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "资源管理数据导入模板.xlsx";
    		List<EduTeacheResource> list = Lists.newArrayList(); 
    		new ExportExcel("资源管理数据", EduTeacheResource.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}