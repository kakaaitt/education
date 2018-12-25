<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>教材管理管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="eduTeachMaterialList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">教材管理列表</h3>
	</div>
	<div class="panel-body">
	
	<!-- 搜索 -->
	<div id="search-collapse" class="collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="eduTeachMaterial" class="form form-horizontal well clearfix">
			<c:if test="${office == null}">
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="学校：">学校：</label>
					<sys:gridselect url="${ctx}/sys/office/data" id="office" name="office.id" value="${eduClasses.office.id}" labelName="office.name" labelValue="${eduClasses.office.name}"
						title="选择学校" cssClass="form-control required" fieldLabels="学校名称|备注" fieldKeys="name|remarks" searchLabels="学校名称|备注" searchKeys="name|remarks" ></sys:gridselect>
<%-- 				<c:if test="${office != null}"> --%>
<%-- 					<input id="officeId" name="office.id" type="hidden" value="${office.id}"> --%>
<%-- 					<input id="officeName" name="office.name" readonly="readonly" type="text" value="${office.name}" data-msg-required="" class="form-control required" style=""> --%>
<%-- 				</c:if> --%>
			</div>
			</c:if>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="学科：">学科：</label>
				<form:select path="subject"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('subject')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="年级：">年级：</label>
				<form:select path="grade"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('grade')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="出版社：">出版社：</label>
				<form:select path="publisher"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('publisher')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="课本：">课本：</label>
				<sys:gridselect url="${ctx}/school/textbook/textbook/treeData" id="textbook" name="textbook.id" value="${eduTeachMaterial.textbook.id}" labelName="textbook.name" labelValue="${eduTeachMaterial.textbook.name}"
					title="选择课本" cssClass="form-control required" fieldLabels="课本|备注" fieldKeys="name|remarks" searchLabels="课本|备注" searchKeys="name|remarks" ></sys:gridselect>
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
			<shiro:hasPermission name="teachmaterial:tmaterial:eduTeachMaterial:add">
				<button id="add" class="btn btn-primary" onclick="add()">
					<i class="glyphicon glyphicon-plus"></i> 新建
				</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="teachmaterial:tmaterial:eduTeachMaterial:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="teachmaterial:tmaterial:eduTeachMaterial:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
			<%-- <shiro:hasPermission name="teachmaterial:tmaterial:eduTeachMaterial:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="teachmaterial:tmaterial:eduTeachMaterial:export">
	        		<button id="export" class="btn btn-warning">
					<i class="fa fa-file-excel-o"></i> 导出
				</button>
			 </shiro:hasPermission> --%>
<%-- 	                 <shiro:hasPermission name="teachmaterial:tmaterial:eduTeachMaterial:view">
				<button id="view" class="btn btn-default" disabled onclick="view()">
					<i class="fa fa-search-plus"></i> 查看
				</button>
			</shiro:hasPermission> --%>
		    </div>
		
	<!-- 表格 -->
	<table id="eduTeachMaterialTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="teachmaterial:tmaterial:eduTeachMaterial:view">
        <li data-item="view"><a>查看</a></li>
        </shiro:hasPermission>
    	<shiro:hasPermission name="teachmaterial:tmaterial:eduTeachMaterial:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="teachmaterial:tmaterial:eduTeachMaterial:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>