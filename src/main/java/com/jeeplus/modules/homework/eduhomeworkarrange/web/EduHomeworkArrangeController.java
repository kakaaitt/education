/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.eduhomeworkarrange.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

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
import com.jeeplus.modules.homework.eduhomeworkarrange.entity.EduHomeworkArrange;
import com.jeeplus.modules.homework.eduhomeworkarrange.service.EduHomeworkArrangeService;

/**
 * 作业布置管理Controller
 * @author 乔功
 * @version 2018-10-07
 */
@Controller
@RequestMapping(value = "${adminPath}/homework/eduhomeworkarrange/eduHomeworkArrange")
public class EduHomeworkArrangeController extends BaseController {

	@Autowired
	private EduHomeworkArrangeService eduHomeworkArrangeService;
	
	@ModelAttribute
	public EduHomeworkArrange get(@RequestParam(required=false) String id) {
		EduHomeworkArrange entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = eduHomeworkArrangeService.get(id);
		}
		if (entity == null){
			entity = new EduHomeworkArrange();
		}
		return entity;
	}
	
	/**
	 * 作业布置管理列表页面
	 */
//	@RequiresPermissions("homework:eduhomeworkarrange:eduHomeworkArrange:list")
	@RequestMapping(value = {"list", ""})
	public String list(EduHomeworkArrange eduHomeworkArrange, Model model) {
		model.addAttribute("eduHomeworkArrange", eduHomeworkArrange);
		return "modules/homework/eduhomeworkarrange/eduHomeworkArrangeList";
	}
	
		/**
	 * 作业布置管理列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("homework:eduhomeworkarrange:eduHomeworkArrange:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(EduHomeworkArrange eduHomeworkArrange, HttpServletRequest request, HttpServletResponse response, Model model) {
		String homeworkid=request.getParameter("homeworkId");
		eduHomeworkArrange.setHomework(homeworkid);
		Page<EduHomeworkArrange> page = eduHomeworkArrangeService.findPage(new Page<EduHomeworkArrange>(request, response), eduHomeworkArrange); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑作业布置管理表单页面
	 */
	@RequestMapping(value = "form")
	public String form(EduHomeworkArrange eduHomeworkArrange, Model model) {
		model.addAttribute("eduHomeworkArrange", eduHomeworkArrange);
		return "modules/homework/eduhomeworkarrange/eduHomeworkArrangeForm";
	}

	/**
	 * 保存作业布置管理
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(EduHomeworkArrange eduHomeworkArrange, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		if(StringUtils.isEmpty(eduHomeworkArrange.getClasses())){
			j.setSuccess(false);
			j.setMsg("班级不能为空");
			return j;
		}
		//布置作业
		int count = eduHomeworkArrangeService.arrangeHomework(eduHomeworkArrange);
		if(count == 0){
			j.setSuccess(false);
			j.setMsg("所选班级无学生");
			return j;
		}
		j.setSuccess(true);
		j.setMsg("作业布置成功");
		return j;
	}
	
	/**
	 * 删除作业布置管理
	 */
	@ResponseBody
//	@RequiresPermissions("homework:eduhomeworkarrange:eduHomeworkArrange:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(EduHomeworkArrange eduHomeworkArrange) {
		AjaxJson j = new AjaxJson();
		eduHomeworkArrangeService.delete(eduHomeworkArrange);
		j.setMsg("删除作业布置管理成功");
		return j;
	}
	
	/**
	 * 批量删除作业布置管理
	 */
	@ResponseBody
	@RequiresPermissions("homework:eduhomeworkarrange:eduHomeworkArrange:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			eduHomeworkArrangeService.delete(eduHomeworkArrangeService.get(id));
		}
		j.setMsg("删除作业布置管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("homework:eduhomeworkarrange:eduHomeworkArrange:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(EduHomeworkArrange eduHomeworkArrange, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "作业布置管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EduHomeworkArrange> page = eduHomeworkArrangeService.findPage(new Page<EduHomeworkArrange>(request, response, -1), eduHomeworkArrange);
    		new ExportExcel("作业布置管理", EduHomeworkArrange.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出作业布置管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("homework:eduhomeworkarrange:eduHomeworkArrange:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EduHomeworkArrange> list = ei.getDataList(EduHomeworkArrange.class);
			for (EduHomeworkArrange eduHomeworkArrange : list){
				try{
					eduHomeworkArrangeService.save(eduHomeworkArrange);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条作业布置管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条作业布置管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入作业布置管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入作业布置管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("homework:eduhomeworkarrange:eduHomeworkArrange:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "作业布置管理数据导入模板.xlsx";
    		List<EduHomeworkArrange> list = Lists.newArrayList(); 
    		new ExportExcel("作业布置管理数据", EduHomeworkArrange.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}