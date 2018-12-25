<%@ page contentType="text/html;charset=UTF-8" %>
	<script>
		var chapterId = "";
		$(document).ready(function() {
			$('#materialjsTree').jstree({
				'core' : {
					"multiple" : false,
					"animation" : 0,
					"themes" : { "variant" : "large", "icons":true , "stripes":true},
					'data' : {
						"url" : "${ctx}/school/textbook/textbook/treeData",
						"dataType" : "json" ,
						"data" : {
							"id" : $('#search_q').val()
						}
					}
				},
				"conditionalselect" : function (node, event) {
					return false;
				},
				'plugins': ['types', 'wholerow', "search"],
				"types":{
					'default' : { 'icon' : 'fa fa-folder' },
					'1' : {'icon' : 'fa fa-home'},
					'2' : {'icon' : 'fa fa-umbrella' },
					'3' : { 'icon' : 'fa fa-group'},
					'4' : { 'icon' : 'fa fa-file-text-o' }
				}

			}).bind("activate_node.jstree", function (obj, e) {
				var node = $('#materialjsTree').jstree(true).get_selected(true)[0];
				//只有第三级，即二级章节才可以添加资源
				jp.get("${ctx}/school/textbook/textbook/queryById?id=" + node.id, function(data){
         	  		if(data.success){
         	  			var textbook = data.body.textbook;
         	  			if(textbook && textbook.remarks == '2'){
         	  				chapterId = node.id;
         	  				var opt = {
     	  						silent: true,
     	  						query:{
     	  							'chapterId':chapterId
     	  						}
     	  					};
     	  					$("#resourceChapterId").val(node.id);
     	  					$("#resourceName").val(node.text);
     	  					$('#eduMaterialResourceTable').bootstrapTable('refresh',opt);
         	  			}else{
         	  				chapterId = "";
         	  			}
         	  		}else{
         	  			console.log(data.msg);
         	  		}
         	  	})
			}).on('loaded.jstree', function() {
				$("#materialjsTree").jstree('open_all');
			});
		});

		function refreshTree() {
            $('#materialjsTree').jstree("refresh");
        }
	</script>