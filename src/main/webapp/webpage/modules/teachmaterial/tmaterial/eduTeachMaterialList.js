<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#eduTeachMaterialTable').bootstrapTable({
		 
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
//               pagination: true,   
                //排序方式 
               sortOrder: "asc",  
               //初始化加载第一页，默认第一页
               pageNumber:1,   
               //每页的记录行数（*）   
               pageSize: 10,  
               //可供选择的每页的行数（*）    
               pageList: [10, 25, 50, 100],
               //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
               url: "${ctx}/teachmaterial/tmaterial/eduTeachMaterial/data",
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
                        jp.confirm('确认要删除该教材管理记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/teachmaterial/tmaterial/eduTeachMaterial/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#eduTeachMaterialTable').bootstrapTable('refresh');
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
		       
		    },{
		        field: 'textbook.name',
		        title: '教材名称',
		        sortable: true,
		        sortName: 'textbook.name',
		        formatter:function(value, row , index){
		        	if(value == null || value ==""){
						   value = "-";
					   }
					   <c:choose>
						   <c:when test="${fns:hasPermission('teachmaterial:tmaterial:eduTeachMaterial:edit')}">
						      return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
					      </c:when>
						  <c:when test="${fns:hasPermission('teachmaterial:tmaterial:eduTeachMaterial:view')}">
						      return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
					      </c:when>
						  <c:otherwise>
						      return value;
					      </c:otherwise>
					   </c:choose>
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
		    }
			,{
		        field: 'grade',
		        title: '年级',
		        sortable: true,
		        sortName: 'grade',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('grade'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'publisher',
		        title: '出版社',
		        sortable: true,
		        sortName: 'publisher',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('publisher'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'textbook.name',
		        title: '课本',
		        sortable: true,
		        sortName: 'textbook.name'
		       
		    },{
		        field: 'office.name',
		        title: '学校',
		        sortable: true,
		        sortName: 'office.name'
		       
		    }
			,{
		        field: 'updateDate',
		        title: '更新时间',
		        sortable: true,
		        sortName: 'updateDate'
		       
		    }
			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true,
		        sortName: 'remarks'
		       
		    },
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
		    result += "<a onclick=\"bindResources('" + row.id + "')\" title='添加教学资源'><i class=\"fa fa fa-barcode\"></i></a>";
		    return result;
	  }
	  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#eduTeachMaterialTable').bootstrapTable("toggleView");
		}
	  
	  $('#eduTeachMaterialTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#eduTeachMaterialTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#eduTeachMaterialTable').bootstrapTable('getSelections').length!=1);
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
					  jp.downloadFile('${ctx}/teachmaterial/tmaterial/eduTeachMaterial/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/teachmaterial/tmaterial/eduTeachMaterial/import', function (data) {
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
			jp.downloadFile('${ctx}/teachmaterial/tmaterial/eduTeachMaterial/export');
	  });

		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#eduTeachMaterialTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#eduTeachMaterialTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#eduTeachMaterialTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该教材管理记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/teachmaterial/tmaterial/eduTeachMaterial/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#eduTeachMaterialTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

    //刷新列表
  function refresh(){
  	$('#eduTeachMaterialTable').bootstrapTable('refresh');
  }
  
   function add(){
	  jp.openSaveDialog('新增教材管理', "${ctx}/teachmaterial/tmaterial/eduTeachMaterial/form",'800px', '500px');
  }


  
   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑教材管理', "${ctx}/teachmaterial/tmaterial/eduTeachMaterial/form?id=" + id, '800px', '500px');
  }
  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看教材管理', "${ctx}/teachmaterial/tmaterial/eduTeachMaterial/form?id=" + id, '800px', '500px');
 }
 
 function bindResources(id){
	 window.location.href = "${ctx}/teachmaterial/tmaterial/eduTeachMaterial/materialResource?id=" + id;
 }



</script>