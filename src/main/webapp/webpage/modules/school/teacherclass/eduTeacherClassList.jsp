<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>老师授课分配管理管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="eduTeacherClassList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">教师授课班级列表</h3>
	</div>
	<div class="panel-body">
	
	<!-- 搜索 -->
	<div id="search-collapse" class="collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="eduTeacherClass" class="form form-horizontal well clearfix">
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="学校：">学校：</label>
				<c:if test="${office == null}">
					<sys:gridselect url="${ctx}/sys/office/data" id="office" name="office.id" value="${eduClasses.office.id}" labelName="office.name" labelValue="${eduClasses.office.name}"
						title="选择学校" cssClass="form-control required" fieldLabels="学校名称|备注" fieldKeys="name|remarks" searchLabels="学校名称|备注" searchKeys="name|remarks" ></sys:gridselect>
				</c:if>
				<c:if test="${office != null}">
					<input id="officeId" name="office.id" type="hidden" value="${office.id}">
					<input id="officeName" name="office.name" readonly="readonly" type="text" value="${office.name}" data-msg-required="" class="form-control required" style="">
				</c:if>
<%-- 				<sys:gridselect url="${ctx}/sys/office/schools" id="office" name="office.id" value="${eduStudentClass.office.id}" labelName="office.name" labelValue="${eduStudentClass.office.name}" --%>
<%-- 					title="选择学校" cssClass="form-control required" fieldLabels="学校|备注" fieldKeys="name|remarks" searchLabels="学校|备注" searchKeys="name|remarks" ></sys:gridselect> --%>

			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="班级：">班级：</label>
<%-- 				<sys:gridselect url="${ctx}/school/classes/eduClasses/data" id="classes" name="classes.id" value="${eduStudentClass.classes.id}" labelName="classes.name" labelValue="${eduStudentClass.classes.name}" --%>
<%-- 					title="选择班级" cssClass="form-control required" fieldLabels="班级|备注" fieldKeys="name|remarks" searchLabels="班级|备注" searchKeys="name|remarks" ></sys:gridselect> --%>
					<input id="classesId" name="classes.id" type="hidden" value="">
						<div class="input-group" style="width: 100%">
							<input id="classesName" name="classes.name" readonly="readonly" type="text" value="${eduStudentClass.classes.id}" data-msg-required="" class="form-control required" style="">
					       		 <span class="input-group-btn">
						       		 <button type="button" id="classesButton" class="btn   btn-primary  "><i class="fa fa-search"></i>
						             </button> 
						               <button type="button" id="classesDelButton" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
					       		 </span>
					    </div>
				<label id="classesName-error" class="error" for="classesName" style="display:none"></label>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="教师：">教师：</label>
<%-- 				<sys:gridselect url="${ctx}/sys/user/data" id="user" name="user.id" value="${eduTeacherClass.user.id}" labelName="user.name" labelValue="${eduTeacherClass.user.name}" --%>
<%-- 					title="选择教师" cssClass="form-control required" fieldLabels="教师|备注" fieldKeys="name|remarks" searchLabels="教师|备注" searchKeys="name|remarks" ></sys:gridselect> --%>
				<form:input path="user.name" htmlEscape="false" class=" form-control"/>
			</div>
<!-- 			 <div class="col-xs-12 col-sm-6 col-md-4"> -->
<!-- 				<label class="label-item single-overflow pull-left" title="班级：">班级：</label> -->
<%-- 				<sys:gridselect url="${ctx}/school/classes/eduClasses/data" id="classes" name="classes.id" value="${eduTeacherClass.classes.id}" labelName="classes.name" labelValue="${eduTeacherClass.classes.name}" --%>
<%-- 					title="选择班级" cssClass="form-control required" fieldLabels="班级名称|备注" fieldKeys="name|remarks" searchLabels="班级名称|备注" searchKeys="name|remarks" ></sys:gridselect> --%>
<!-- 			</div> -->
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="学科：">学科：</label>
				<form:select path="subject"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('subject')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="学年：">学年：</label>
				<form:select path="studyYear"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('study_year')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="年级：">年级：</label>
				<form:select path="classes.grade"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('grade')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<div class="form-group">
					<label class="label-item single-overflow pull-left" title="是否主学科：">&nbsp;是否主学科：</label>
					<div class="col-xs-12">
						<form:radiobuttons class="i-checks" path="mainSubject" items="${fns:getDictList('is_main_subject')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</div>
				</div>
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
			<shiro:hasPermission name="school:teacherclass:eduTeacherClass:add">
				<button id="add" class="btn btn-primary" onclick="add()">
					<i class="glyphicon glyphicon-plus"></i> 新建
				</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="school:teacherclass:eduTeacherClass:edit">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="school:teacherclass:eduTeacherClass:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			</shiro:hasPermission>
<%-- 			<shiro:hasPermission name="school:teacherclass:eduTeacherClass:import">
				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="school:teacherclass:eduTeacherClass:export">
	        		<button id="export" class="btn btn-warning">
					<i class="fa fa-file-excel-o"></i> 导出
				</button>
			 </shiro:hasPermission>
	                 <shiro:hasPermission name="school:teacherclass:eduTeacherClass:view">
				<button id="view" class="btn btn-default" disabled onclick="view()">
					<i class="fa fa-search-plus"></i> 查看
				</button>
			</shiro:hasPermission> --%>
		    </div>
		
	<!-- 表格 -->
	<table id="eduTeacherClassTable"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
    <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="school:teacherclass:eduTeacherClass:view">
        <li data-item="view"><a>查看</a></li>
        </shiro:hasPermission>
    	<shiro:hasPermission name="school:teacherclass:eduTeacherClass:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="school:teacherclass:eduTeacherClass:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  
	</div>
	</div>
	</div>
</body>
</html>