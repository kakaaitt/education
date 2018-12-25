<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>题库管理管理</title>
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
                jp.post("${ctx}/questionbank/questiontree/eduQuestionTree/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="eduQuestionTree" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">题目类型：</label></td>
					<td class="width-35">
						<form:select path="questionType" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('questiontype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">题库类型：</label></td>
					<td class="width-35">
						<form:select path="questionBankType" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('questionbanktype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">题目内容：</label></td>
					<td class="width-35">
						<form:input path="questionText" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">选项：</label></td>
					<td class="width-35">
						<form:input path="options" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">答案：</label></td>
					<td class="width-35">
						<form:input path="answer" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">解析：</label></td>
					<td class="width-35">
						<form:input path="analy" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">分享状态：</label></td>
					<td class="width-35">
						<form:select path="shareType" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">布置次数：</label></td>
					<td class="width-35">
						<form:input path="publicnNum" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">附件信息：</label></td>
					<td class="width-35">
						<sys:fileUpload path="files"  value="${eduQuestionTree.files}" type="file" uploadPath="/questionbank/questiontree/eduQuestionTree"/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>