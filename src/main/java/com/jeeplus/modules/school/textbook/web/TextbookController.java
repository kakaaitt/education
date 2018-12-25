/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.school.textbook.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.config.Global;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.school.textbook.entity.Textbook;
import com.jeeplus.modules.school.textbook.service.TextbookService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 课本管理Controller
 * @author 李海军
 * @version 2018-09-25
 */
@Controller
@RequestMapping(value = "${adminPath}/school/textbook/textbook")
public class TextbookController extends BaseController {

	@Autowired
	private TextbookService textbookService;
	
	@ModelAttribute
	public Textbook get(@RequestParam(required=false) String id) {
		Textbook entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = textbookService.get(id);
		}
		if (entity == null){
			entity = new Textbook();
		}
		return entity;
	}
	
	
	@ResponseBody
	@RequiresPermissions("school:textbook:textbook:view")
	@RequestMapping(value = "queryById")
	public AjaxJson queryById(@RequestParam(required=true) String id) {
		AjaxJson j = new AjaxJson();
		Textbook textbook = null;
		if (StringUtils.isNotBlank(id)){
			textbook = textbookService.get(id);
		}
		if (textbook == null){
			textbook = new Textbook();
		}
		j.setSuccess(true);
		j.put("textbook", textbook);
		j.setMsg("查询成功！");
		return j;
	}
	
	/**
	 * 课本管理列表页面
	 */
//	@RequiresPermissions("school:textbook:textbook:list")
	@RequestMapping(value = {"list", ""})
	public String list(Textbook textbook,  HttpServletRequest request, HttpServletResponse response, Model model) {
	
		String textid=request.getParameter("textId");
		
		System.out.println("----------------------00000000=="+textid);
		textbook.setId(textid);
		model.addAttribute("textbook", textbook);
		return "modules/school/textbook/textbookList";
	}

	/**
	 * 课本管理列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("school:textbook:textbook:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Textbook textbook,String publisher, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(!StringUtils.isEmpty(publisher)){
			textbook.setPressid(publisher);
		}
		Page<Textbook> page = textbookService.findPage(new Page<Textbook>(request, response), textbook); 
		return getBootstrapData(page);
	}
	
	/**
	 * 课本管理一级目录列表数据
	 */
	@ResponseBody
//	@RequiresPermissions("school:textbook:textbook:list")
	@RequestMapping(value = "firstData")
	public Map<String, Object> firstData(Textbook textbook,String publisher, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(!StringUtils.isEmpty(publisher)){
			textbook.setPressid(publisher);
		}
		Page<Textbook> page = textbookService.findFirstList(new Page<Textbook>(request, response), textbook); 
		return getBootstrapData(page);
	}
	
	/**
	 * 查看，增加，编辑课本管理表单页面
	 */
	@RequiresPermissions(value={"school:textbook:textbook:view","school:textbook:textbook:add","school:textbook:textbook:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Textbook textbook, Model model) {
		if (textbook.getParent()!=null && StringUtils.isNotBlank(textbook.getParent().getId())){
			textbook.setParent(textbookService.get(textbook.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(textbook.getId())){
				Textbook textbookChild = new Textbook();
				textbookChild.setParent(new Textbook(textbook.getParent().getId()));
				List<Textbook> list = textbookService.findList(textbook); 
				if (list.size() > 0){
					textbook.setSort(list.get(list.size()-1).getSort());
					if (textbook.getSort() != null){
						textbook.setSort(textbook.getSort() + 30);
					}
				}
			}
		}
		if (textbook.getSort() == null){
			textbook.setSort(30);
		}
		model.addAttribute("textbook", textbook);
		return "modules/school/textbook/textbookForm";
	}

	/**
	 * 保存课本管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"school:textbook:textbook:add","school:textbook:textbook:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Textbook textbook, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(textbook);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}

		//新增或编辑表单保存
		textbookService.save(textbook);//保存
		j.setSuccess(true);
		j.put("textbook", textbook);
		j.setMsg("保存课本管理成功");
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "getChildren")
	public List<Textbook> getChildren(String parentId,String checkid){
//		System.out.println("checkid="+checkid+"==parentId=="+parentId);
		if("-1".equals(parentId)){//如果是-1，没指定任何父节点，就从根节点开始查找
			parentId = checkid;
		}
		return textbookService.getChildren(parentId);
	}
	
	/**
	 * 删除课本管理
	 */
	@ResponseBody
	@RequiresPermissions("school:textbook:textbook:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Textbook textbook) {
		AjaxJson j = new AjaxJson();
		textbookService.delete(textbook);
		j.setSuccess(true);
		j.setMsg("删除课本管理成功");
		return j;
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId,String id, HttpServletResponse response) {
		System.out.println("教材过来的========");
		Textbook tb = new Textbook();
		tb.setParentIds(id);
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Textbook> list = textbookService.findTreeList(tb);
		for (int i=0; i<list.size(); i++){
			Textbook e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("text", e.getName());
				if(StringUtils.isBlank(e.getParentId()) || "0".equals(e.getParentId())){
					map.put("parent", "#");
					Map<String, Object> state = Maps.newHashMap();
					state.put("opened", true);
					map.put("state", state);
				}else{
					map.put("parent", e.getParentId());
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	
	/**
	 * 通过题库跳转到这个章节的树形结构
	 * @param extId
	 * @param id
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData2")
	public List<Map<String, Object>> treeData2(@RequestParam(required=false) String extId,String id, HttpServletResponse response) {
		System.out.println("题库过来的========");
		Textbook tb = new Textbook();
		List<Textbook> list =new ArrayList<Textbook>();
		
		//获得当前登录人员
		User user=UserUtils.getUser();
		//根据当前人员信息查询所在学科
		if(user.isAdmin()){
			tb.setParentIds(id);
			list = textbookService.findTreeList(tb);
		}else{
			//根据登录者的权限
//			List<Map<String, String>> texts=textbookService.findTextByUserId(user.getId());
//			System.out.println("=text subjectid=="+texts);
//			for(int i=0;i<texts.size();i++){
//				List<Textbook> list1 =new ArrayList<Textbook>();
//				String oneId=texts.get(i).get("id").toString();
//				tb.setParentIds(oneId);
//				list1 = textbookService.findTreeList(tb);
//				list.addAll(list1);
//			}
			
			//根据点击进来的章节
			tb.setParentIds(id);
			list = textbookService.findTreeList(tb);
		}
		
		List<Map<String, Object>> mapList = Lists.newArrayList();
		
		for (int i=0; i<list.size(); i++){
			Textbook e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("text", e.getName());
				if(StringUtils.isBlank(e.getParentId()) || "0".equals(e.getParentId())){
					map.put("parent", "#");
					Map<String, Object> state = Maps.newHashMap();
					state.put("opened", true);
					map.put("state", state);
				}else{
					map.put("parent", e.getParentId());
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 通过作业跳转到这个章节的树形结构
	 * @param extId
	 * @param id
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData3")
	public List<Map<String, Object>> treeData3(@RequestParam(required=false) String extId,String id, HttpServletResponse response) {
		System.out.println("作业过来的========");
		Textbook tb = new Textbook();
		List<Textbook> list =new ArrayList<Textbook>();
		
		//获得当前登录人员
		User user=UserUtils.getUser();
		//根据当前人员信息查询所在学科
		if(user.isAdmin()){
			tb.setParentIds(id);
			list = textbookService.findTreeList(tb);
		}else{
			//SELECT a.id,a.subjectid,b.teacher,b.main_subject from edu_textbook a ,edu_teacher_class b 
			//where a.remarks=0 and a.del_flag=0 
			//		and a.subjectid=b.subject 
			List<Map<String, String>> texts=textbookService.findTextByUserId(user.getId());
			System.out.println("==="+texts);
//			id="2aa222dbb70746c1b60d57ff5a9f8b8c,38be908ec19d45ea8a12fefc7e5cef32";
//			String[] ids=id.split(",");
			for(int i=0;i<texts.size();i++){
				List<Textbook> list1 =new ArrayList<Textbook>();
				String oneId=texts.get(i).get("id").toString();
				tb.setParentIds(oneId);
				list1 = textbookService.findTreeList(tb);
				list.addAll(list1);
			}
			
		}
		
		List<Map<String, Object>> mapList = Lists.newArrayList();
		
		for (int i=0; i<list.size(); i++){
			Textbook e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("text", e.getName());
				if(StringUtils.isBlank(e.getParentId()) || "0".equals(e.getParentId())){
					map.put("parent", "#");
					Map<String, Object> state = Maps.newHashMap();
					state.put("opened", true);
					map.put("state", state);
				}else{
					map.put("parent", e.getParentId());
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
	
}