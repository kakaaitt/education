<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>作业班级选择</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
		homework = "${param.homeworkId}";
		$(document).ready(function() {

		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                $("#homework").val(homework);
                jp.post("${ctx}/homework/eduhomeworkarrange/eduHomeworkArrange/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="eduHomeworkArrange" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="homework"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>班级：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/school/classes/eduClasses/data2" id="classes" name="classes" value="${eduStudentClass.classes.id}" labelName="classes.name" labelValue="${eduStudentClass.classes.name}"
							 title="选择班级" cssClass="form-control required" fieldLabels="班级|备注" fieldKeys="name|remarks" searchLabels="班级|备注" searchKeys="name|remarks" isMultiSelected="true"></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>