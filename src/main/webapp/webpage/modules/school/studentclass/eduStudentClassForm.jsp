<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学生班级关联管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			
			//班级联动
			var school = $("#officeId").val();
			$("#classesButton, #classesName").click(function(){
				school = $("#officeId").val();
				if(!school){
					jp.warning("请先选择学校")
					return;
				}
				if ($("#classesButton").hasClass("disabled")){
					return true;
				}
				top.layer.open({
					type: 2,  
					area: ['800px', '500px'],
					title:"选择班级",
					auto:true,
					name:'friend',
					content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/school/classes/eduClasses/data?school="+school)+"&fieldLabels="+encodeURIComponent("班级")+"&fieldKeys="+encodeURIComponent("name")+"&searchLabels="+encodeURIComponent("班级")+"&searchKeys="+encodeURIComponent("name")+"&isMultiSelected=false",
					btn: ['确定', '关闭'],
					yes: function(index, layero){
						var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						var items = iframeWin.getSelections();
						if(items == ""){
							jp.warning("必须选择一条数据!");
							return;
						}
						var ids = [];
						var names = [];
						for(var i=0; i<items.length; i++){
							var item = items[i];
							ids.push(item.id);
							names.push(item.name)
						}
						$("#classesId").val(ids.join(","));
						$("#classesName").val(names.join(","));
						top.layer.close(index);//关闭对话框。
					},
					cancel: function(index){ 
					}
				}); 
			})
			$("#classesDelButton").click(function(){
				// 是否限制选择，如果限制，设置为disabled
				if ($("#classesButton").hasClass("disabled")){
					return true;
				}
				// 清除	
				$("#classesId").val("");
				$("#classesName").val("");
				$("#classesName").focus();
			});
			
			//学生联动
			$("#userButton, #userName").click(function(){
				school = $("#officeId").val();
				if(!school){
					jp.warning("请先选择学校")
					return;
				}
				if ($("#userButton").hasClass("disabled")){
					return true;
				}
				top.layer.open({
					type: 2,  
					area: ['800px', '500px'],
					title:"选择学生",
					auto:true,
					name:'friend',
					content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/sys/user/data?type=1&schoolId="+school)+"&fieldLabels="+encodeURIComponent("学生")+"&fieldKeys="+encodeURIComponent("name")+"&searchLabels="+encodeURIComponent("学生")+"&searchKeys="+encodeURIComponent("name")+"&isMultiSelected=false",
					btn: ['确定', '关闭'],
					yes: function(index, layero){
						var iframeWin = layero.find('iframe')[0].contentWindow; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						var items = iframeWin.getSelections();
						if(items == ""){
							jp.warning("必须选择一条数据!");
							return;
						}
						var ids = [];
						var names = [];
						for(var i=0; i<items.length; i++){
							var item = items[i];
							ids.push(item.id);
							names.push(item.name)
						}
						$("#userId").val(ids.join(","));
						$("#userName").val(names.join(","));
						top.layer.close(index);//关闭对话框。
					},
					cancel: function(index){ 
					}
				}); 
			})
			$("#userDelButton").click(function(){
				// 是否限制选择，如果限制，设置为disabled
				if ($("#userButton").hasClass("disabled")){
					return true;
				}
				// 清除	
				$("#userId").val("");
				$("#userName").val("");
				$("#userName").focus();
			});

		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/school/studentclass/eduStudentClass/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="eduStudentClass" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">学校：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/sys/office/schools" id="office" name="office.id" value="${eduStudentClass.office.id}" labelName="office.name" labelValue="${eduStudentClass.office.name}"
					title="选择学校" cssClass="form-control" fieldLabels="学校|备注" fieldKeys="name|remarks" searchLabels="学校|备注" searchKeys="name|remarks" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>学生：</label></td>
					<td class="width-35">
<%-- 						<sys:gridselect url="${ctx}/sys/user/data?type=1" id="user" name="user.id" value="${eduStudentClass.user.id}" labelName="user.name" labelValue="${eduStudentClass.user.name}" --%>
<%-- 							 title="选择学生" cssClass="form-control required" fieldLabels="学生|备注" fieldKeys="name|remarks" searchLabels="学生|备注" searchKeys="name|remarks" ></sys:gridselect> --%>
						<input id="userId" name="user.id" type="hidden" value="${eduStudentClass.user.id}">
						<div class="input-group" style="width: 100%">
							<input id="userName" name="user.name" readonly="readonly" type="text" value="${eduStudentClass.user.name}" data-msg-required="" class="form-control required" style="">
					       		 <span class="input-group-btn">
						       		 <button type="button" id="userButton" class="btn   btn-primary  "><i class="fa fa-search"></i>
						             </button> 
						               <button type="button" id="userDelButton" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
					       		 </span>
					    </div>
						<label id="userName-error" class="error" for="userName" style="display:none"></label>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>班级：</label></td>
					<td class="width-35">
<%-- 						<sys:gridselect url="${ctx}/school/classes/eduClasses/data" id="classes" name="classes.id" value="${eduStudentClass.classes.id}" labelName="classes.name" labelValue="${eduStudentClass.classes.name}" --%>
<%-- 							 title="选择班级" cssClass="form-control required" fieldLabels="班级|备注" fieldKeys="name|remarks" searchLabels="班级|备注" searchKeys="name|remarks" ></sys:gridselect> --%>
						<input id="classesId" name="classes.id" type="hidden" value="${eduStudentClass.classes.id}">
						<div class="input-group" style="width: 100%">
							<input id="classesName" name="classes.name" readonly="readonly" type="text" value="${eduStudentClass.classes.name}" data-msg-required="" class="form-control" style="">
					       		 <span class="input-group-btn">
						       		 <button type="button" id="classesButton" class="btn   btn-primary  "><i class="fa fa-search"></i>
						             </button> 
						               <button type="button" id="classesDelButton" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
					       		 </span>
					    </div>
						<label id="classesName-error" class="error" for="classesName" style="display:none"></label>
					</td>
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