/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachmaterial.tmaterial.web;

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
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.teachmaterial.tmaterial.entity.EduTeachMaterial;
import com.jeeplus.modules.teachmaterial.tmaterial.service.EduTeachMaterialService;

/**
 * 教材管理Controller
 * @author 乔功
 * @version 2018-09-02
 */
@Controller
@RequestMapping(value = "${adminPath}/teachmaterial/tmaterial/eduTeachMaterial")
public class EduTeachMaterialController extends BaseController {

	@Autowired
	private EduTeachMaterialService eduTeachMaterialService;
	
	@ModelAttribute
	public EduTeachMaterial get(@RequestParam(required=false) String id) {
		EduTeachMaterial entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eduTeachMaterialService.get(id);
		}
		if (entity == null){
			entity = new EduTeachMaterial();
		}
		return entity;
	}
	
	/**
	 * 教材管理列表页面
	 */
	@RequiresPermissions("teachmaterial:tmaterial:eduTeachMaterial:list")
	@RequestMapping(value = {"list", ""})
	public String list(EduTeachMaterial eduTeachMaterial, Model model) {
		model.addAttribute("eduTeachMaterial", eduTeachMaterial);
		//判断该登录用户是否教师
		User loginUser = UserUtils.getUser();
		List<Role> roleList = loginUser.getRoleList();
		for(Role role : roleList){
			//1:教师,2:学生,3:家长
			if("teacher".equals(role.getEnname())){
				model.addAttribute("office", loginUser.getOffice());
			}
		}
		return "modules/teachmaterial/tmaterial/eduTeachMaterialList";
	}
	
		/**
	 * 教材管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tmaterial:eduTeachMaterial:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EduTeachMaterial eduTeachMaterial, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user=UserUtils.getUser();
		if(!user.isAdmin()){
			eduTeachMaterial.setOffice(user.getOffice());
		}
		
		Page<EduTeachMaterial> page = eduTeachMaterialService.findPage(new Page<EduTeachMaterial>(request, response), eduTeachMaterial); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑教材管理表单页面
	 */
	@RequiresPermissions(value={"teachmaterial:tmaterial:eduTeachMaterial:view","teachmaterial:tmaterial:eduTeachMaterial:add","teachmaterial:tmaterial:eduTeachMaterial:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(EduTeachMaterial eduTeachMaterial, Model model) {
		model.addAttribute("eduTeachMaterial", eduTeachMaterial);
		return "modules/teachmaterial/tmaterial/eduTeachMaterialForm";
	}
	
	/**
	 * 查看，增加教材资源页面
	 */
	@RequiresPermissions(value={"teachmaterial:tmaterial:eduTeachMaterial:view","teachmaterial:tmaterial:eduTeachMaterial:add","teachmaterial:tmaterial:eduTeachMaterial:edit"},logical=Logical.OR)
	@RequestMapping(value = "materialResource")
	public String materialResource(EduTeachMaterial eduTeachMaterial, Model model) {
		model.addAttribute("eduTeachMaterial", eduTeachMaterial);
		return "/modules/teachmaterial/tmaterialresource/matreialResource";
	}

	/**
	 * 保存教材管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"teachmaterial:tmaterial:eduTeachMaterial:add","teachmaterial:tmaterial:eduTeachMaterial:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(EduTeachMaterial eduTeachMaterial, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(eduTeachMaterial);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		if(StringUtils.isEmpty(eduTeachMaterial.getId())){
			User user = UserUtils.getUser();
			
			//此时是添加，同一课本不能添加多个教材
			EduTeachMaterial etm = eduTeachMaterialService.getByTextbook(eduTeachMaterial.getTextbook().getId(),user.getId());
			if(null != etm){
				j.setSuccess(false);
				j.setMsg("同一课本不能重复创建教材！");
				return j;
			}
		}
		//新增或编辑表单保存
		eduTeachMaterialService.save(eduTeachMaterial);//保存
		j.setSuccess(true);
		j.setMsg("保存教材管理成功");
		return j;
	}
	
	/**
	 * 删除教材管理
	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tmaterial:eduTeachMaterial:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(EduTeachMaterial eduTeachMaterial) {
		AjaxJson j = new AjaxJson();
		eduTeachMaterialService.delete(eduTeachMaterial);
		j.setMsg("删除教材管理成功");
		return j;
	}
	
	/**
	 * 批量删除教材管理
	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tmaterial:eduTeachMaterial:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			eduTeachMaterialService.delete(eduTeachMaterialService.get(id));
		}
		j.setMsg("删除教材管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tmaterial:eduTeachMaterial:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(EduTeachMaterial eduTeachMaterial, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "教材管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EduTeachMaterial> page = eduTeachMaterialService.findPage(new Page<EduTeachMaterial>(request, response, -1), eduTeachMaterial);
    		new ExportExcel("教材管理", EduTeachMaterial.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出教材管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tmaterial:eduTeachMaterial:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EduTeachMaterial> list = ei.getDataList(EduTeachMaterial.class);
			for (EduTeachMaterial eduTeachMaterial : list){
				try{
					eduTeachMaterialService.save(eduTeachMaterial);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条教材管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条教材管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入教材管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入教材管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("teachmaterial:tmaterial:eduTeachMaterial:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "教材管理数据导入模板.xlsx";
    		List<EduTeachMaterial> list = Lists.newArrayList(); 
    		new ExportExcel("教材管理数据", EduTeachMaterial.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}