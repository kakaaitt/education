<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	
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
	
	
	$('#eduTeacherClassTable').bootstrapTable({
		 
		  //请求方法
               method: 'post',
               //类型json
               dataType: "json",
               contentType: "application/x-www-form-urlencoded",
               //显示检索按钮
	           showSearch: true,
               //显示刷新按钮
               showRefresh: true,
               //显示切换手机试图按钮
               showToggle: false,
               //显示 内容列下拉框
    	       showColumns: true,
    	       //显示到处按钮
    	       showExport: false,
    	       //显示切换分页按钮
    	       showPaginationSwitch: false,
    	       //最低显示2行
    	       minimumCountColumns: 2,
               //是否显示行间隔色
               striped: true,
               //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）     
               cache: false,    
               //是否显示分页（*）  
               pagination: true,   
                //排序方式 
               sortOrder: "asc",  
               //初始化加载第一页，默认第一页
               pageNumber:1,   
               //每页的记录行数（*）   
               pageSize: 10,  
               //可供选择的每页的行数（*）    
               pageList: [10, 25, 50, 100],
               //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
               url: "${ctx}/school/teacherclass/eduTeacherClass/data",
               //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
               //queryParamsType:'',   
               ////查询参数,每次调用是会带上这个参数，可自定义                         
               queryParams : function(params) {
               	var searchParam = $("#searchForm").serializeJSON();
               	searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
               	searchParam.pageSize = params.limit === undefined? -1 : params.limit;
               	searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
                   return searchParam;
               },
               //分页方式：client客户端分页，server服务端分页（*）
               sidePagination: "server",
               contextMenuTrigger:"right",//pc端 按右键弹出菜单
               contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
               contextMenu: '#context-menu',
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   		edit(row.id);
                   }else if($el.data("item") == "view"){
                       view(row.id);
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该老师授课分配管理记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/school/teacherclass/eduTeacherClass/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#eduTeacherClassTable').bootstrapTable('refresh');
                   	  			jp.success(data.msg);
                   	  		}else{
                   	  			jp.error(data.msg);
                   	  		}
                   	  	})
                   	   
                   	});
                      
                   } 
               },
              
               onClickRow: function(row, $el){
               },
               	onShowSearch: function () {
			$("#search-collapse").slideToggle();
		},
               columns: [{
		        checkbox: true
		       
		    }
			,{
		        field: 'user.name',
		        title: '教师',
		        sortable: true,
		        sortName: 'user.name'
		        ,formatter:function(value, row , index){

			   if(value == null || value ==""){
				   value = "-";
			   }
			   <c:choose>
				   <c:when test="${fns:hasPermission('school:teacherclass:eduTeacherClass:edit')}">
				      return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
			      </c:when>
				  <c:when test="${fns:hasPermission('school:teacherclass:eduTeacherClass:view')}">
				      return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
			      </c:when>
				  <c:otherwise>
				      return value;
			      </c:otherwise>
			   </c:choose>

		        }
		       
		    }
			,{
		        field: 'classes.name',
		        title: '班级',
		        sortable: true,
		        sortName: 'classes.name'
		       
		    }
			,{
		        field: 'user.loginName',
		        title: '教师登录名',
		        sortable: true,
		        sortName: 'user.loginName'
		       
		    }
			,{
		        field: 'classes.grade',
		        title: '年级',
		        sortable: true,
		        sortName: 'classes.grade',
	        	formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('grade'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'office.name',
		        title: '学校',
		        sortable: true,
		        sortName: 'office.name'
		    }
			,{
		        field: 'subject',
		        title: '学科',
		        sortable: true,
		        sortName: 'subject',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('subject'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'studyYear',
		        title: '学年',
		        sortable: true,
		        sortName: 'studyYear',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('study_year'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'mainSubject',
		        title: '是否主学科',
		        sortable: true,
		        sortName: 'mainSubject',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('is_main_subject'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'updateDate',
		        title: '更新时间',
		        sortable: true,
		        sortName: 'updateDate'
		       
		    }
			,{
		        field: 'status',
		        title: '状态',
		        sortable: true,
		        sortName: 'status',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('status'))}, value, "-");
		        }
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#eduTeacherClassTable').bootstrapTable("toggleView");
		}
	  
	  $('#eduTeacherClassTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#eduTeacherClassTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#eduTeacherClassTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 2,
                area: [500, 200],
                auto: true,
			    title:"导入数据",
			    content: "${ctx}/tag/importExcel" ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  jp.downloadFile('${ctx}/school/teacherclass/eduTeacherClass/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/school/teacherclass/eduTeacherClass/import', function (data) {
							if(data.success){
								jp.success(data.msg);
								refresh();
							}else{
								jp.error(data.msg);
							}
						});//调用保存事件
						jp.close(index);
				  },
				 
				  btn3: function(index){ 
					  jp.close(index);
	    	       }
			}); 
		});
		
		
	 $("#export").click(function(){//导出Excel文件
			jp.downloadFile('${ctx}/school/teacherclass/eduTeacherClass/export');
	  });

		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#eduTeacherClassTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#eduTeacherClassTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#eduTeacherClassTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该老师授课分配管理记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/school/teacherclass/eduTeacherClass/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#eduTeacherClassTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

    //刷新列表
  function refresh(){
  	$('#eduTeacherClassTable').bootstrapTable('refresh');
  }
  
   function add(){
	  jp.openSaveDialog('新增老师授课分配管理', "${ctx}/school/teacherclass/eduTeacherClass/form",'800px', '500px');
  }


  
   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑老师授课分配管理', "${ctx}/school/teacherclass/eduTeacherClass/form?id=" + id, '800px', '500px');
  }
  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看老师授课分配管理', "${ctx}/school/teacherclass/eduTeacherClass/form?id=" + id, '800px', '500px');
 }



</script>