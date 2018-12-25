/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.stroe.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 商户信息Entity
 * @author liangzai
 * @version 2018-07-20
 */
public class BaseStoreInfo extends DataEntity<BaseStoreInfo> {
	
	private static final long serialVersionUID = 1L;
	private String stroeName;		// 商户名称
	private String stroeCode;		// 商户编码
	
	public BaseStoreInfo() {
		super();
	}

	public BaseStoreInfo(String id){
		super(id);
	}

	@ExcelField(title="商户名称", align=2, sort=7)
	public String getStroeName() {
		return stroeName;
	}

	public void setStroeName(String stroeName) {
		this.stroeName = stroeName;
	}
	
	@ExcelField(title="商户编码", align=2, sort=8)
	public String getStroeCode() {
		return stroeCode;
	}

	public void setStroeCode(String stroeCode) {
		this.stroeCode = stroeCode;
	}
	
}