<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课本管理管理</title>
	<meta name="decorator" content="ani"/>
	<%@include file="textbookList.js" %>
</head>
<body>

	<div class="wrapper wrapper-content">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">课本管理列表  </h3>
			</div>
			
			<div class="panel-body">
	
			<!-- 工具栏 -->
			<div class="row">
			<div class="col-sm-12">
				<div class="pull-left treetable-bar">
					<shiro:hasPermission name="school:textbook:textbook:add">
						<a id="add" class="btn btn-primary" onclick="jp.openSaveDialog('新建课本管理', '${ctx}/school/textbook/textbook/form','800px', '500px')"><i class="glyphicon glyphicon-plus"></i> 新建</a><!-- 增加按钮 -->
					</shiro:hasPermission>
			      <!--  <button class="btn btn-default" data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button> -->
				<a id="back" class="btn btn-info" href="${ctx}/school/textbooklist/eduTextBookList/list"><i class="fa fa-folder-open-o"></i> 返回</a>
				</div>
			</div>
			</div>
			<table id="textbookTreeTable" class="table table-hover">
				<thead>
					<tr>
						<th>章节名称</th>
						<th>章节等级</th>
						<th>排序</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="textbookTreeTableList"></tbody>
			</table>
			<br/>
			</div>
			</div>
	</div>
</body>
</html>