/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.teachmaterial.tmaterial.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.school.textbook.entity.Textbook;
import com.jeeplus.modules.school.textbook.service.TextbookService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.teachmaterial.tmaterial.entity.EduTeachMaterial;
import com.jeeplus.modules.teachmaterial.tmaterial.service.EduTeachMaterialService;
import com.jeeplus.modules.teachmaterial.tresource.entity.EduTeacheResource;
import com.jeeplus.modules.teachmaterial.tresource.service.EduTeacheResourceService;

/**
 * 教材管理移动端接口Controller
 * @author 乔功
 * @version 2018-09-10
 */
@Controller
@RequestMapping(value = "${adminPath}/teachmaterial")
public class EduTeachMaterialInterfaceController extends BaseController {

	@Autowired
	private EduTeachMaterialService eduTeachMaterialService;
	
	@Autowired
	private TextbookService textbookService;
	
	@Autowired
	private EduTeacheResourceService eduTeacheResourceService;
	
	/**
	 * 教材首页数据
	 */
	@ResponseBody
	@RequestMapping(value = "index")
	public AjaxJson index(EduTeachMaterial eduTeachMaterial, HttpServletRequest request, HttpServletResponse response, Model model) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		if(StringUtils.isEmpty(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		User loginUser = UserUtils.getUser();
		if("1".equals(loginType)){
//		Page<EduTeachMaterial> page = eduTeachMaterialService.findPage(new Page<EduTeachMaterial>(request, response), eduTeachMaterial); 
			eduTeachMaterial.setCreateBy(loginUser);
		}else if("2".equals(loginType)){
			eduTeachMaterial.setIds(getLoginIds(loginUser));
		}
		//不分页
		List<EduTeachMaterial> list = eduTeachMaterialService.findList(eduTeachMaterial);
		for(EduTeachMaterial material : list){
			material.setIconLink(Global.getConfig("fileUrl") + Global.getSubjectUrl() + material.getSubject() + ".png");
			material.setSubject(DictUtils.getDictLabel(material.getSubject(), "subject", ""));
			material.setPublisher(DictUtils.getDictLabel(material.getPublisher(), "publisher", ""));
		}
		
		j.put("rows", list);
		j.put("total", list.size());
		
		return j;
	}
	
	/**
	 * 教材及章节详情
	 */
	@ResponseBody
	@RequestMapping(value = "detail")
	public AjaxJson detail(EduTeachMaterial eduTeachMaterial,@RequestParam(required=false) String extId, HttpServletRequest request, HttpServletResponse response, Model model) {
		AjaxJson j = new AjaxJson();
		EduTeachMaterial material = eduTeachMaterialService.get(eduTeachMaterial);
		if(null != material){
			Textbook tb = new Textbook();
			tb.setParentIds(material.getTextbook().getId());
			tb.setRemarks("1");
//			List<Map<String, Object>> mapList = Lists.newArrayList();
			List<Textbook> list = textbookService.findList(tb);
			for(Textbook textBook : list){
				tb = new Textbook();
				tb.setParentIds(textBook.getId());
				tb.setRemarks("2");
				textBook.setChild(textbookService.findList(tb));
			}
//			for (int i=0; i<list.size(); i++){
//				Textbook e = list.get(i);
//				if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
//					Map<String, Object> map = Maps.newHashMap();
//					map.put("id", e.getId());
//					map.put("text", e.getName());
//					if(StringUtils.isBlank(e.getParentId()) || "0".equals(e.getParentId())){
//						map.put("parent", "#");
//						Map<String, Object> state = Maps.newHashMap();
//						state.put("opened", true);
//						map.put("state", state);
//					}else{
//						map.put("parent", e.getParentId());
//					}
//					mapList.add(map);
//				}
//			}
			j.put("data", list);
		}else{
			j.setSuccess(false);
			j.setErrorCode("0");
			j.setMsg("该教材不存在！");
			j.put("data", "");
		}
		return j;
	}

	/**
	 * 教材章节资源
	 */
	@ResponseBody
	@RequestMapping(value = "resources")
	public AjaxJson resources(EduTeacheResource eduTeacheResource, HttpServletRequest request, HttpServletResponse response, Model model) {
		AjaxJson j = new AjaxJson();
		
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		if(StringUtils.isEmpty(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		User loginUser = UserUtils.getUser();
		if("1".equals(loginType)){
			eduTeacheResource.setCreateBy(loginUser);
		}else if("2".equals(loginType)){
			eduTeacheResource.setLoginNames(getLoginNames(loginUser));
			eduTeacheResource.setIsStudent("1");
		}
		String subject = eduTeacheResourceService.getSubjectByTextId(eduTeacheResource.getChapterId());
//		Page<EduTeacheResource> page = eduMaterialResourceService.findResourcePage(new Page<EduTeacheResource>(request, response), eduTeacheResource);
		List<EduTeacheResource> list = eduTeacheResourceService.findList(eduTeacheResource);
		for(EduTeacheResource resource : list){
			User createUser = resource.getCreateBy();
			createUser.setPhoto(Global.getConfig("fileUrl") + createUser.getPhoto());
			resource.setCreateBy(createUser);
			resource.setSubject(DictUtils.getDictLabel(subject, "subject", ""));
			resource.setSubjectIcon(Global.getConfig("fileUrl") + Global.getSubjectUrl() + subject + ".png");
			resource.setFilename(Global.getConfig("fileUrl") + resource.getFilename());
			resource.setIconLink(Global.getConfig("fileUrl") + Global.getFileTypeUrl() + resource.getFiletype() + ".png");
			resource.setGlanceCount(0);
			if(null != resource.getBrowse()){
				resource.setGlanceCount(resource.getBrowse());
			}
		}
		j.put("rows", list);
		j.put("total", list.size());
		
		return j;
	}
	
	/**
	 * 资源浏览次数增加
	 */
	@ResponseBody
	@RequestMapping(value = "browse/add")
	public AjaxJson browseAdd(EduTeacheResource eduTeacheResource, HttpServletRequest request, HttpServletResponse response, Model model) {
		AjaxJson j = new AjaxJson();
		//loginType:1-教师, 2-学生
		String loginType = request.getHeader("loginType");
		if(!"2".equals(loginType)){
			j.setErrorCode("-2");
			j.setSuccess(false);
			j.setMsg("登录权限错误！");
			return j;
		}
		String user = UserUtils.getUser().getId();
		//更新浏览次数
		eduTeacheResourceService.updateBrowse(eduTeacheResource);
		//新增资源浏览记录表
		eduTeacheResourceService.insertResourceLog(user,eduTeacheResource.getId(),eduTeacheResource.getChapterId());
		return j;
	}
}