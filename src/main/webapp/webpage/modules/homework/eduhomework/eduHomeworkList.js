<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#eduHomeworkTable').bootstrapTable({
		 
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
//               showToggle: true,
               //显示 内容列下拉框
    	       showColumns: true,
    	       //显示到处按钮
//    	       showExport: true,
    	       //显示切换分页按钮
//    	       showPaginationSwitch: true,
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
               url: "${ctx}/homework/eduhomework/eduHomework/data",
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
                        jp.confirm('确认要删除该作业管理记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/homework/eduhomework/eduHomework/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#eduHomeworkTable').bootstrapTable('refresh');
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
		        field: 'name',
		        title: '作业名称',
		        sortable: true,
		        sortName: 'name'
		        ,formatter:function(value, row , index){
		        	value = jp.unescapeHTML(value);
				   <c:choose>
					   <c:when test="${fns:hasPermission('homework:eduhomework:eduHomework:edit')}">
					      return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:when test="${fns:hasPermission('homework:eduhomework:eduHomework:view')}">
					      return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:otherwise>
					      return value;
				      </c:otherwise>
				   </c:choose>
		         }
		       
		    }
			,{
		        field: 'requirement',
		        title: '作业要求',
		        sortable: true,
		        sortName: 'requirement'
		       
		    }
			,{
		        field: 'status',
		        title: '作业状态',
		        sortable: true,
		        sortName: 'status',
		        formatter:function(value, row , index){
		        	if(value == 0){
		        		return "未布置 ";
		        	}else if(value == 1){
		        		return "已布置 ";
		        	}else if(value == 2){
		        		return "未批改";
		        	}else if(value == 3){
		        		return "已批改";
		        	}else if(value == 4){
		        		return "作废 ";
		        	}
		        }
		       
		    }
			,{
		        field: 'subject',
		        title: '学科',
		        sortable: true,
		        sortName: 'subject',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('subject'))}, value, "-");
		        }
		       
		    },{
		        field: 'createBy.name',
		        title: '教师',
		        sortable: true,
		        sortName: 'createBy.name'
		    },{
		        field: 'office.name',
		        title: '学校',
		        sortable: true,
		        sortName: 'office.name'
		    }
			,{
		        field: 'remarks',
		        title: '备注信息/作废说明',
		        sortable: true,
		        sortName: 'remarks'
		       
		    }
			,
		    {
			    title: '操作',
			    field: 'operate',
			    align: 'center',
			    valign: 'middle',
			    formatter: operateFormatter //自定义方法，添加操作按钮
			}
		     ]
		
		});
		
		function operateFormatter(value, row, index){
		  	var id = value;
		    var result = "";
//		    result += "<a onclick=\"choseQuestions('" + row.id + "')\" title='选题'><i class=\"fa fa fa-barcode\"></i></a>&nbsp;&nbsp;<a onclick=\"arrangeHomework('" + row.id + "')\" title='布置作业'><i class=\"fa fa fa-user\"></i></a>&nbsp;&nbsp;<a onclick=\"checkHomework('" + row.id + "')\" title='查看作业'><i class=\"fa fa fa-book\"></i></a>&nbsp;&nbsp;<a onclick=\"arrangeCondition('" + row.id + "')\" title='作业提交情况'><i class=\"fa fa fa-file\"></i></a>";
		    result += "<a onclick=\"choseQuestions('" + row.id + "')\" title='选题'><i class=\"fa fa fa-barcode\"></i></a>&nbsp;&nbsp;<a onclick=\"arrangeHomework('" + row.id + "')\" title='布置作业'><i class=\"fa fa fa-user\"></i></a>&nbsp;&nbsp;<a onclick=\"arrangeCondition('" + row.id + "')\" title='作业提交情况'><i class=\"fa fa fa-file\"></i></a>";
		    return result;
		}
		  
	
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#eduHomeworkTable').bootstrapTable("toggleView");
		}
	  
	  $('#eduHomeworkTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#eduHomeworkTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#eduHomeworkTable').bootstrapTable('getSelections').length!=1);
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
					  jp.downloadFile('${ctx}/homework/eduhomework/eduHomework/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/homework/eduhomework/eduHomework/import', function (data) {
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
			jp.downloadFile('${ctx}/homework/eduhomework/eduHomework/export');
	  });

		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#eduHomeworkTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#eduHomeworkTable').bootstrapTable('refresh');
		});
		
		
	});

	function choseQuestions(homeworkId){
		  window.location.href = "${ctx}/questionbank/questiontree/eduQuestionTree/list2?homeworkId=" + homeworkId;
	}
	
	function arrangeHomework(homeworkId){
		  jp.openSaveDialog('作业班级选择', "${ctx}/homework/eduhomeworkarrange/eduHomeworkArrange/form?homeworkId="+homeworkId,'800px', '300px');
	}
	
	function checkHomework(homeworkId){
		  jp.openSaveDialog('检查作业', "${ctx}/homework/eduhomeworkarrange/eduHomeworkArrange/list?homeworkId="+homeworkId,'800px', '800px');
	}
	
	function arrangeCondition(homeworkId){
		  jp.openViewDialog('作业提交情况', "webpage/modules/homework/eduhomework/condition.html?homeworkId=" + homeworkId,'1000px', '740px');
	}
	
		
  function getIdSelections() {
        return $.map($("#eduHomeworkTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该作业管理记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/homework/eduhomework/eduHomework/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#eduHomeworkTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

    //刷新列表
  function refresh(){
  	$('#eduHomeworkTable').bootstrapTable('refresh');
  }
  
   function add(){
	  jp.openSaveDialog('新增作业管理', "${ctx}/homework/eduhomework/eduHomework/form",'800px', '500px');
  }


  
   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑作业管理', "${ctx}/homework/eduhomework/eduHomework/form?id=" + id, '800px', '500px');
  }
  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看作业管理', "${ctx}/homework/eduhomework/eduHomework/form?id=" + id, '800px', '500px');
 }



</script>