package com.jeeplus.modules.sys.mapper;

import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.sys.entity.AppVersion;

/**
 * app版本MAPPER接口
 * @version 2018-09-10
 */
@MyBatisMapper
public interface AppVersionMapper {
	
	/**
	 * 查询最新版本号
	 * @param AppVersion
	 * @return
	 */
	public AppVersion getLastVersion(AppVersion appVersion);
}
