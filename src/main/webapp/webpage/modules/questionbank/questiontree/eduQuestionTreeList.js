<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#eduQuestionTreeTable').bootstrapTable({
		 
		  //请求方法
               method: 'post',
               //类型json
               dataType: "json",
               contentType: "application/x-www-form-urlencoded",
               //显示检索按钮
	           showSearch: false,
               //显示刷新按钮
               showRefresh: false,
               //显示切换手机试图按钮
               showToggle: false,
               //显示 内容列下拉框
    	       showColumns: false,
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
               url: "${ctx}/questionbank/questiontree/eduQuestionTree/data",
               //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
               //queryParamsType:'',   
               ////查询参数,每次调用是会带上这个参数，可自定义                         
               queryParams : function(params) {
               
               	var searchParam = $("#searchForm").serializeJSON();
               	searchParam.chapterId=chapterId;
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
                        jp.confirm('确认要删除该题库管理记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/questionbank/questiontree/eduQuestionTree/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#eduQuestionTreeTable').bootstrapTable('refresh');
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
		        field: 'questionType',
		        title: '题目类型',
		        sortable: true,
		        sortName: 'questionType',
		        formatter:function(value, row , index){
		        	   value = jp.getDictLabel(${fns:toJson(fns:getDictList('questiontype'))}, value, "-");
				   <c:choose>
					   <c:when test="${fns:hasPermission('questionbank:questiontree:eduQuestionTree:edit')}">
					      return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:when test="${fns:hasPermission('questionbank:questiontree:eduQuestionTree:view')}">
					      return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
				      </c:when>
					  <c:otherwise>
					      return value;
				      </c:otherwise>
				   </c:choose>
		        }
		       
		    }
			,{
		        field: 'questionbanktype',
		        title: '题库类型',
		        sortable: true,
		        sortName: 'questionbanktype',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('questionbanktype'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'questionText',
		        title: '题目内容',
		        sortable: true,
		        sortName: 'questionText'
		       
		    }
			,{
		        field: 'options',
		        title: '选项',
		        sortable: true,
		        sortName: 'options'
		       
		    }
			,{
		        field: 'answer',
		        title: '答案',
		        sortable: true,
		        sortName: 'answer'
		       
		    }
			,{
		        field: 'analy',
		        title: '解析',
		        sortable: true,
		        sortName: 'analy'
		       
		    }
			,{
		        field: 'shareType',
		        title: '分享状态',
		        sortable: true,
		        sortName: 'shareType',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'publicnNum',
		        title: '布置次数',
		        sortable: true,
		        sortName: 'publicnNum'
		       
		    }
			,{
		        field: 'createBy.loginName',
		        title: '创建者',
		        sortable: true,
		        sortName: 'createBy.loginName'
		       
		    }
		    ,{
		        field: 'files',
		        title: '附件信息',
		        sortable: true,
		        sortName: 'files',
		        formatter:function(value, row , index){
		        	var labelArray = [];
		        	if(value!=undefined){
		        	var valueArray = value.split("|");
		        	for(var i =0 ; i<valueArray.length; i++){
		        		//if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(valueArray[i]))
		        		//{
		        			labelArray[i] = "<a href=\""+valueArray[i]+"\" url=\""+valueArray[i]+"\" target=\"_blank\">"+decodeURIComponent(valueArray[i].substring(valueArray[i].lastIndexOf("/")+1))+"</a>"
		        		//}else{
		        		//	labelArray[i] = '<img   onclick="jp.showPic(\''+valueArray[i]+'\')"'+' height="50px" src="'+valueArray[i]+'">';
		        		//}
		        	}
		        	return labelArray.join(" ");
		        	}else{
		        	return labelArray.join(" ");
		        	}
		        }
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#eduQuestionTreeTable').bootstrapTable("toggleView");
		}
	  
	  $('#eduQuestionTreeTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#eduQuestionTreeTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#eduQuestionTreeTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
		if(chapterId){
			jp.open({
			    type: 2,
                area: [500, 200],
                auto: true,
			    title:"导入数据",
			    content: "${ctx}/tag/importExcel" ,
			    btn: ['下载模板','确定'],
				    btn1: function(index, layero){
					  window.location.href="/education/static/common/template/questions.docx";
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/questionbank/questiontree/eduQuestionTree/import?textId='+chapterId, function (data) {
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
			}else{
				jp.warning("请选择章节！");
			}
		});
		
		
	 $("#export").click(function(){//导出Excel文件
			jp.downloadFile('${ctx}/questionbank/questiontree/eduQuestionTree/export');
	  });
	 $("#back").click(function(){//返回前一页
		 window.location.href = "${ctx}/questionbank/questionlist/eduquestionlist/list";
	  });
		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#eduQuestionTreeTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#eduQuestionTreeTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#eduQuestionTreeTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该题库管理记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/questionbank/questiontree/eduQuestionTree/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#eduQuestionTreeTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

    //刷新列表
  function refresh(){
  	$('#eduQuestionTreeTable').bootstrapTable('refresh');
  }
  
   function add(){
	  jp.openSaveDialog('新增题库管理', "${ctx}/questionbank/questiontree/eduQuestionTree/form",'800px', '500px');
  }


  
   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑题库管理', "${ctx}/questionbank/questiontree/eduQuestionTree/form?id=" + id, '800px', '500px');
  }
  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看题库管理', "${ctx}/questionbank/questiontree/eduQuestionTree/form?id=" + id, '800px', '500px');
 }



</script>