<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var objid = "${textbook.id}";
	    var $textbookTreeTable=null;  
		$(document).ready(function() {
			$textbookTreeTable=$('#textbookTreeTable').treeTable({  
		    	   theme:'vsStyle',	           
					expandLevel : 2,
					column:0,
					checkbox: false,
		            url:'${ctx}/school/textbook/textbook/getChildren?checkid='+objid+'&parentId=',  
		            callback:function(item) { 
		            	 var treeTableTpl= $("#textbookTreeTableTpl").html();
		            	 item.dict = {};
						item.dict.pressid = jp.getDictLabel(${fns:toJson(fns:getDictList('publisher'))}, item.pressid, "-");
						item.dict.remarks = jp.getDictLabel(${fns:toJson(fns:getDictList('book_type'))}, item.remarks, "-");
						item.dict.grade = jp.getDictLabel(${fns:toJson(fns:getDictList('grade'))}, item.grade, "-");
						item.dict.subjectid = jp.getDictLabel(${fns:toJson(fns:getDictList('subject'))}, item.subjectid, "-");

		            	 var result = laytpl(treeTableTpl).render({
								  row: item
							});
		                return result;                   
		            },  
		            beforeClick: function($textbookTreeTable, id) { 
		                //异步获取数据 这里模拟替换处理  
		                $textbookTreeTable.refreshPoint(id);  
		            },  
		            beforeExpand : function($textbookTreeTable, id) {   
		            },  
		            afterExpand : function($textbookTreeTable, id) {  
		            },  
		            beforeClose : function($textbookTreeTable, id) {    
		            	
		            }  
		        });
		        
		        $textbookTreeTable.initParents('${parentIds}', "0");//在保存编辑时定位展开当前节点
		       
		});
		
		function del(con,id){  
			jp.confirm('确认要删除课本管理吗？', function(){
				jp.loading();
	       	  	$.get("${ctx}/school/textbook/textbook/delete?id="+id, function(data){
	       	  		if(data.success){
	       	  			$textbookTreeTable.del(id);
	       	  			jp.success(data.msg);
	       	  		}else{
	       	  			jp.error(data.msg);
	       	  		}
	       	  	})
	       	   
       		});
	
		} 
		function refreshNode(data) {//刷新节点
            var current_id = data.body.textbook.id;
			var target = $textbookTreeTable.get(current_id);
			var old_parent_id = target.attr("pid") == undefined?'1':target.attr("pid");
			var current_parent_id = data.body.textbook.parentId;
			var current_parent_ids = data.body.textbook.parentIds;
			if(old_parent_id == current_parent_id){
				if(current_parent_id == '0'){
					$textbookTreeTable.refreshPoint(-1);
				}else{
					$textbookTreeTable.refreshPoint(current_parent_id);
				}
			}else{
				$textbookTreeTable.del(current_id);//刷新删除旧节点
				$textbookTreeTable.initParents(current_parent_ids, "0");
			}
        }
		function refresh(){//刷新
			var index = jp.loading("正在加载，请稍等...");
			$textbookTreeTable.refresh();
			jp.close(index);
		}
</script>
<script type="text/html" id="textbookTreeTableTpl">
			<td>
			<c:choose>
			      <c:when test="${fns:hasPermission('school:textbook:textbook:edit')}">
				    <a  href="#" onclick="jp.openSaveDialog('编辑课本管理', '${ctx}/school/textbook/textbook/form?id={{d.row.id}}','800px', '500px')">
							{{d.row.name === undefined ? "": d.row.name}}
					</a>
			      </c:when>
			      <c:when test="${fns:hasPermission('school:textbook:textbook:view')}">
				    <a  href="#" onclick="jp.openViewDialog('查看课本管理', '${ctx}/school/textbook/textbook/form?id={{d.row.id}}','800px', '500px')">
							{{d.row.name === undefined ? "": d.row.name}}
					</a>
			      </c:when>
			      <c:otherwise>
							{{d.row.name === undefined ? "": d.row.name}}
			      </c:otherwise>
			</c:choose>
			</td>
			<td>
							{{d.row.dict.remarks === undefined ? "": d.row.dict.remarks}}
			</td>
			<td>
			{{d.row.sort === undefined ? "": d.row.sort}}
			</td>
			<td>
				<div class="btn-group">
			 		<button type="button" class="btn  btn-primary btn-xs dropdown-toggle" data-toggle="dropdown">
						<i class="fa fa-cog"></i>
						<span class="fa fa-chevron-down"></span>
					</button>
				  <ul class="dropdown-menu" role="menu">
					<shiro:hasPermission name="school:textbook:textbook:view">
						<li><a href="#" onclick="jp.openViewDialog('查看课本管理', '${ctx}/school/textbook/textbook/form?id={{d.row.id}}','800px', '500px')"><i class="fa fa-search-plus"></i> 查看</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="school:textbook:textbook:edit">
						<li><a href="#" onclick="jp.openSaveDialog('修改课本管理', '${ctx}/school/textbook/textbook/form?id={{d.row.id}}','800px', '500px')"><i class="fa fa-edit"></i> 修改</a></li>
		   			</shiro:hasPermission>
		   			<shiro:hasPermission name="school:textbook:textbook:del">
		   				<li><a  onclick="return del(this, '{{d.row.id}}')"><i class="fa fa-trash"></i> 删除</a></li>
					</shiro:hasPermission>
		   			<shiro:hasPermission name="school:textbook:textbook:add">
						<li><a href="#" onclick="jp.openSaveDialog('添加下级课本管理', '${ctx}/school/textbook/textbook/form?parent.id={{d.row.id}}','800px', '500px')"><i class="fa fa-plus"></i> 添加下级课本管理</a></li>
					</shiro:hasPermission>
				  </ul>
				</div>
			</td>
	</script>