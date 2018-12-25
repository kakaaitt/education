<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>教材资源管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="matreialResourceTreeList.js" %>
	<%@include file="matreialResource.js" %>
	
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">教材资源列表</h3>
	</div>
	<div class="panel-body">
		<div class="row">
				<div class="col-sm-4 col-md-3" >
					<div class="form-group" style="margin-bottom: 11px;">
						<div class="row">
							<div class="col-sm-10" >
								<div class="input-search">
									<input id="search_q" value="${eduTeachMaterial.textbook.id}" type="hidden" name="">
								</div>
							</div>
						</div>
					</div>
					<div id="materialjsTree" style="overflow-x:auto; border:0px;"></div>
				</div>
				<div  class="col-sm-8 col-md-9">
	
	<!-- 搜索 -->
	<div id="search-collapse" class="collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="eduMaterialResource" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="章节：">章节：</label>
				<sys:gridselect url="${ctx}/teachmaterial/tresource/eduTeacheResource/data" id="resource" name="resource.chapterId" value="${eduMaterialResource.resource.id}" labelName="resource.resourcename" labelValue="${eduMaterialResource.resource.resourcename}"
					title="选择章节" cssClass="form-control required" fieldLabels="resourcetype|resourcename|remarks" fieldKeys="资源名称|资源类型|备注" searchLabels="资源名称|资源类型|备注" searchKeys="resourcetype|resourcename|remarks" ></sys:gridselect>
			</div>
		 <div class="col-xs-12 col-sm-6 col-md-4">
			<div style="margin-top:26px">
			  <a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
			  <a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
			 </div>
	    </div>	
	</form:form>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div id="toolbar">
			<shiro:hasPermission name="teachmaterial:tmaterialresource:eduMaterialResource:add">
				<button id="add" class="btn btn-primary" onclick="add()">
					<i class="glyphicon glyphicon-plus"></i> 新建
				</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="teachmaterial:tmaterialresource:eduMaterialResource:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="teachmaterial:tmaterialresource:eduMaterialResource:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			 <button id="back" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 返回</button>
<%-- 			<shiro:hasPermission name="teachmaterial:tmaterialresource:eduMaterialResource:import"> --%>
<!-- 				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button> -->
<%-- 			</shiro:hasPermission> --%>
<%-- 			<shiro:hasPermission name="teachmaterial:tmaterialresource:eduMaterialResource:export"> --%>
<!-- 	        		<button id="export" class="btn btn-warning"> -->
<!-- 					<i class="fa fa-file-excel-o"></i> 导出 -->
<!-- 				</button> -->
<%-- 			 </shiro:hasPermission> --%>
	                 <shiro:hasPermission name="teachmaterial:tmaterialresource:eduMaterialResource:view">
				<button id="view" class="btn btn-default" disabled onclick="view()">
					<i class="fa fa-search-plus"></i> 查看
				</button>
			</shiro:hasPermission>
		    </div>
		
	<!-- 表格 -->
	<table id="eduMaterialResourceTable"   data-toolbar="#toolbar"></table>
	
    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="teachmaterial:tmaterialresource:eduMaterialResource:view">
        <li data-item="view"><a>查看</a></li>
        </shiro:hasPermission>
    	<shiro:hasPermission name="teachmaterial:tmaterialresource:eduMaterialResource:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="teachmaterial:tmaterialresource:eduMaterialResource:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
	</div>
</div>
</body>
</html>