<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课本管理列表管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {

		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/school/textbooklist/eduTextBookList/save",$('#inputForm').serialize(),function(data){
                    if(data.success){
                        jp.getParent().refresh();
                        var dialogIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                        parent.layer.close(dialogIndex);
                        jp.success(data.msg)

                    }else{
                        jp.error(data.msg);
                    }
                })
			}

        }
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="eduTextBookList" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">课本名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">出版社：</label></td>
					<td class="width-35">
						<form:select path="pressid" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('publisher')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">学科：</label></td>
					<td class="width-35">
						<form:select path="subjectid" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('subject')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">年级：</label></td>
					<td class="width-35">
						<form:select path="grade" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('grade')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>学校：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/sys/office/data" id="office" name="office.id" value="${eduTextBookList.office.id}" labelName="office.name" labelValue="${eduTextBookList.office.name}"
							 title="选择学校" cssClass="form-control required" fieldLabels="学校名称|备注" fieldKeys="name|remarks" searchLabels="学校名称|备注" searchKeys="name|remarks" ></sys:gridselect>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>