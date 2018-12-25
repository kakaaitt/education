/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.stroe.web;

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
import org.springframework.web.bind.annotation.PathVariable;
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
import com.jeeplus.modules.stroe.entity.BaseStoreInfo;
import com.jeeplus.modules.stroe.service.BaseStoreInfoService;

/**
 * 商户信息Controller
 * @author liangzai
 * @version 2018-07-20
 */
@Controller
@RequestMapping(value = "${adminPath}/stroe/baseStoreInfo")
public class BaseStoreInfoController extends BaseController {

	@Autowired
	private BaseStoreInfoService baseStoreInfoService;
	
	@ModelAttribute
	public BaseStoreInfo get(@RequestParam(required=false) String id) {
		BaseStoreInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = baseStoreInfoService.get(id);
		}
		if (entity == null){
			entity = new BaseStoreInfo();
		}
		return entity;
	}
	
	/**
	 * 商户信息列表页面
	 */
	@RequiresPermissions("stroe:baseStoreInfo:list")
	@RequestMapping(value = {"list", ""})
	public String list(BaseStoreInfo baseStoreInfo, Model model) {
		model.addAttribute("baseStoreInfo", baseStoreInfo);
		return "modules/stroe/baseStoreInfoList";
	}
	
		/**
	 * 商户信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("stroe:baseStoreInfo:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(BaseStoreInfo baseStoreInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BaseStoreInfo> page = baseStoreInfoService.findPage(new Page<BaseStoreInfo>(request, response), baseStoreInfo); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑商户信息表单页面
	 */
	@RequiresPermissions(value={"stroe:baseStoreInfo:view","stroe:baseStoreInfo:add","stroe:baseStoreInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, BaseStoreInfo baseStoreInfo, Model model) {
		model.addAttribute("baseStoreInfo", baseStoreInfo);
		model.addAttribute("mode", mode);
		return "modules/stroe/baseStoreInfoForm";
	}

	/**
	 * 保存商户信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"stroe:baseStoreInfo:add","stroe:baseStoreInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(BaseStoreInfo baseStoreInfo, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(baseStoreInfo);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		baseStoreInfoService.save(baseStoreInfo);//保存
		j.setSuccess(true);
		j.setMsg("保存商户信息成功");
		return j;
	}
	
	/**
	 * 删除商户信息
	 */
	@ResponseBody
	@RequiresPermissions("stroe:baseStoreInfo:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(BaseStoreInfo baseStoreInfo) {
		AjaxJson j = new AjaxJson();
		baseStoreInfoService.delete(baseStoreInfo);
		j.setMsg("删除商户信息成功");
		return j;
	}
	
	/**
	 * 批量删除商户信息
	 */
	@ResponseBody
	@RequiresPermissions("stroe:baseStoreInfo:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			baseStoreInfoService.delete(baseStoreInfoService.get(id));
		}
		j.setMsg("删除商户信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("stroe:baseStoreInfo:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(BaseStoreInfo baseStoreInfo, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商户信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BaseStoreInfo> page = baseStoreInfoService.findPage(new Page<BaseStoreInfo>(request, response, -1), baseStoreInfo);
    		new ExportExcel("商户信息", BaseStoreInfo.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出商户信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("stroe:baseStoreInfo:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BaseStoreInfo> list = ei.getDataList(BaseStoreInfo.class);
			for (BaseStoreInfo baseStoreInfo : list){
				try{
					baseStoreInfoService.save(baseStoreInfo);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条商户信息记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条商户信息记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入商户信息失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入商户信息数据模板
	 */
	@ResponseBody
	@RequiresPermissions("stroe:baseStoreInfo:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "商户信息数据导入模板.xlsx";
    		List<BaseStoreInfo> list = Lists.newArrayList(); 
    		new ExportExcel("商户信息数据", BaseStoreInfo.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}