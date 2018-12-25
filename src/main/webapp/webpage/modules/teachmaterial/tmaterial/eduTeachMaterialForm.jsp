<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>教材管理管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
		var publisher = "${eduTeachMaterial.publisher}";
// 		$("#textbookName").val("${eduTeachMaterial.textbook.name}");
		$(document).ready(function() {
			$("#publisher").change(function(){
				publisher = $("#publisher").val();
			});
			
			$("#textbookButton, #textbookName").click(function(){
				if(!publisher){
					jp.warning("请先选择出版社")
					return;
				}
				if ($("#textbookButton").hasClass("disabled")){
					return true;
				}
				top.layer.open({
				    type: 2,  
				    area: ['800px', '500px'],
				    title:"选择课本",
				    auto:true,
				    name:'friend',
				    content: "${ctx}/tag/gridselect?url="+encodeURIComponent("${ctx}/school/textbook/textbook/firstData?publisher="+publisher)+"&fieldLabels="+encodeURIComponent("课本|年级")+"&fieldKeys="+encodeURIComponent("name|grade")+"&searchLabels="+encodeURIComponent("课本|年级")+"&searchKeys="+encodeURIComponent("name|grade")+"&isMultiSelected=false",
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
				    	 $("#textbookId").val(ids.join(","));
				    	 $("#textbookName").val(names.join(","));
						 top.layer.close(index);//关闭对话框。
					  },
					  cancel: function(index){ 
				       }
				}); 
			})
			$("#${id}DelButton").click(function(){
				// 是否限制选择，如果限制，设置为disabled
				if ($("#${id}Button").hasClass("disabled")){
					return true;
				}
				// 清除	
				$("#${id}Id").val("");
				$("#${id}Name").val("");
				$("#${id}Name").focus();

			});
		});
		function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
			}else{
                jp.loading();
                jp.post("${ctx}/teachmaterial/tmaterial/eduTeachMaterial/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="eduTeachMaterial" class="form-horizontal">
		<form:hidden path="id"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>学科：</label></td>
					<td class="width-35">
						<form:select path="subject" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('subject')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>年级：</label></td>
					<td class="width-35">
						<form:select path="grade" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('grade')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>出版社：</label></td>
					<td class="width-35">
						<form:select path="publisher" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('publisher')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课本：</label></td>
					<td class="width-35">
						<input id="textbookId" name="textbook.id" type="hidden" value="">
						<div class="input-group" style="width: 100%">
							<input id="textbookName" name="textbook.name" readonly="readonly" type="text" value="${eduTeachMaterial.textbook.name}" data-msg-required="" class="form-control required" style="">
					       		 <span class="input-group-btn">
						       		 <button type="button" id="textbookButton" class="btn   btn-primary  "><i class="fa fa-search"></i>
						             </button> 
						               <button type="button" id="textbookDelButton" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
					       		 </span>
					    </div>
						<label id="textbookName-error" class="error" for="textbookName" style="display:none"></label>				
					</td>
				</tr>
				<tr>
				   <td class="width-15 active"><label class="pull-right"><font color="red">*</font>学校：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/sys/office/data" id="office" name="office.id" value="${eduTeachMaterial.office.id}" labelName="office.name" labelValue="${eduTeachMaterial.office.name}"
							 title="选择学校" cssClass="form-control required" fieldLabels="学校名称|备注" fieldKeys="name|remarks" searchLabels="学校名称|备注" searchKeys="name|remarks" ></sys:gridselect>
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