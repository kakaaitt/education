/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.AppVersion;
import com.jeeplus.modules.sys.service.SystemService;

/**
 * app版本接口Controller
 * @author 乔功
 * @version 2018-09-10
 */
@Controller
@RequestMapping(value = "${adminPath}/app/config")
public class AppConfigInterfaceController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	/**
	 * 最新版本信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkVersion")
	public AjaxJson checkVersion(AppVersion appVersion) {
		AjaxJson j = new AjaxJson();
		AppVersion version = systemService.getLastVersion(appVersion);
		if(null != version){
			version.setDownloadUrl(Global.getConfig("fileUrl") + version.getDownloadUrl());
			j.put("version", version);
		}else{
			j.setSuccess(false);
			j.setErrorCode("0");
			j.setMsg("无版本信息！");
			j.put("version", "");
		}
		return j;
	}
}
