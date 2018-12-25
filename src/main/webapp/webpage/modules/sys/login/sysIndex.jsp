<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <title>${fns:getConfig('productName')}</title>
	<meta name="decorator" content="ani"/>
	<script src="${ctxStatic}/plugin/js-menu/contabs.js"></script>
	<link id="theme-tab" href="${ctxStatic}/plugin/js-menu/menuTab-${cookie.theme.value==null?'blue':cookie.theme.value}.css" rel="stylesheet" />
	<%@ include file="/webpage/include/systemInfoSocket-init.jsp"%>
<%-- 	<%@ include file="/webpage/include/layIM-init.jsp"%> --%>
<style type="text/css">
	.topnav-navbar{
		border:none;
	}
	#body-container{
		margin-top:0px;
		height:100%;
	}
	.topnav-navbar{
		width:220px;
	}
</style>
</head>

<body class="">
	<nav class="navbar topnav-navbar navbar-fixed-top" role="navigation">
		<div class="navbar-header text-center">
			<button type="button" class="navbar-toggle" id="showMenu" >
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>

			<a class="navbar-brand J_menuItem"  href="${ctx}/home">恒轩汇智在线教育云平台</a>
		</div>
<!-- 		<div class="collapse navbar-collapse"> -->
<!-- 			<ul class="nav navbar-nav pull-right navbar-right">	 -->
<!-- 				<li> -->
<!-- 					<div class="row" style="margin: 10px 10px 0 0;"> -->
<!-- 						<div class="col-sm-4"> -->
<!-- 							<span  style="margin: 12px 0 0 0;">Tab</span> -->
<!-- 						</div> -->
<!-- 						<div class="col-sm-8"> -->
<!-- 							<div class="onoffswitch"> -->
<%-- 								<input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="switchTab" ${cookie.tab.value!=false?'checked':''} > --%>
<!-- 								<label class="onoffswitch-label" for="switchTab"> -->
<!-- 									<span class="onoffswitch-inner"></span> -->
<!-- 									<span class="onoffswitch-switch"></span> -->
<!-- 								</label> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
				
<!-- 				</li> -->
<!-- 				<li> -->
<!-- 					<a href="#"  onClick="toggleFullScreen()"> -->
<!-- 						<span>全屏 </span> -->
<!-- 					</a> -->

<!-- 				</li> -->
			
				
<!-- 				<li class="dropdown admin-dropdown"> -->
<!-- 					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"> -->
<%-- 						<img src="<c:if test="${fns:getUser().photo == null || fns:getUser().photo==''}">${ctxStatic}/common/images/flat-avatar.png</c:if> <c:if test="${fns:getUser().photo != null && fns:getUser().photo!=''}">${fns:getUser().photo}</c:if>" class="topnav-img" alt=""><span class="hidden-sm">${fns:getUser().name}</span> --%>
<!-- 					</a> -->
<!-- 					<ul class="dropdown-menu animated fadeIn" role="menu"> -->
<%-- 						  <li><a class="J_menuItem" href="${ctx}/sys/user/imageEdit">修改头像</a> --%>
<!--                           </li> -->
<%--                           <li><a class="J_menuItem" href="${ctx }/sys/user/info">个人资料</a> --%>
<!--                           </li> -->
<%--                           <li><a href="${ctx}/logout">安全退出</a> --%>
<!--                           </li> -->
<!-- 					</ul> -->
<!-- 				</li> -->
<!-- 			</ul> -->
<!-- 		</div> -->
		<ul class="nav navbar-nav pull-right hidd">	
			<li class="dropdown admin-dropdown" >
				<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
					<img src="${fns:getUser().photo }" class="topnav-img" alt=""><span class="hidden-sm">${fns:getUser().name}</span> 
				</a>
				<ul class="dropdown-menu animated fadeIn" role="menu">
					<li><a class="J_menuItem" href="${ctx}/sys/user/imageEdit">修改头像</a>
                    </li>
                    <li><a class="J_menuItem" href="${ctx }/sys/user/info">个人资料</a>
                    </li>
                    <li><a class="J_menuItem" href="${ctx }/iim/contact/index">我的通讯录</a>
                    </li>
                    <li><a class="J_menuItem" href="${ctx }/iim/mailBox/list">信箱</a>
                    </li> 
                    <li><a href="${ctx}/logout">安全退出</a>
                    </li>
				</ul>
			</li>	
		</ul>
	</nav>
	<aside id="sidebar">
	<div class="sidenav-outer">
		<div class="side-widgets">
			<div class="text-center" style="padding-top:10px;"> 
				<a  href="#"><img class="img-circle user-avatar"  src="<c:if test="${fns:getUser().photo == null || fns:getUser().photo==''}">${ctxStatic}/common/images/flat-avatar.png</c:if> <c:if test="${fns:getUser().photo != null && fns:getUser().photo!=''}">${fns:getUser().photo}</c:if>" class="user-avatar" /></a>
				<li class="dropdown admin-dropdown" style="padding-top:10px;">
					<a href="#" class="dropdown-toggle"  data-toggle="dropdown" role="button" aria-expanded="false">
						<font color="white">${fns:getUser().name}<b class="caret"></b></font>
					</a>
					<ul class="dropdown-menu animated fade in" style="left:35px" role="menu">
						  <li><a class="J_menuItem" href="${ctx}/sys/user/imageEdit">修改头像</a>
                          </li>
                          <li><a class="J_menuItem" href="${ctx }/sys/user/info">个人资料</a>
                          </li>
<%--                           <li><a class="J_menuItem" href="${ctx }/iim/contact/index">我的通讯录</a> --%>
<!--                           </li> -->
<%--                           <li><a class="J_menuItem" href="${ctx }/iim/mailBox/list">信箱</a> --%>
<!--                           </li>  -->
<!--                           <hr> -->
<!-- 							<li><a href="javaScript:changeStyle()">切换成横向菜单</a> -->
<!-- 							</li> -->
<!-- 							<hr> -->
                          <li><a href="${ctx}/logout">安全退出</a>
                          </li>
					</ul>
				</li>
			</div>
				
			<div class="widgets-content">
				<div class="menu">
					<div class="menu-body">
								<ul class="nav nav-pills nav-stacked sidenav" id="1">
							<t:aniMenu  menu="${fns:getTopMenu()}"></t:aniMenu>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</aside>	
<section id="body-container" class="animation">
             
	
		<!--选项卡  -->
		<div class="main-container" id="main-container">
			<div class="main-content">
				<div class="main-content-inner">
					<div id="breadcrumbs" class="${cookie.tab.value!=false?'breadcrumbs':'un-breadcrumbs'}">
						  <div class="content-tabs">
						    <button id="hideMenu" class="roll-nav roll-left-0 J_tabLeft"><i class="fa fa-align-justify"></i>
						    </button>
							<button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i>
							</button>
							<nav class="page-tabs J_menuTabs">
								<div class="page-tabs-content">
									 <a href="javascript:;" class="active J_menuTab" data-id="${ctx}/home">首页</a>
								</div>
							</nav>
							<button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i>
							</button>
							<a href="${ctx}/logout" class="roll-nav roll-right J_tabExit"><i class="fa fa fa-sign-out"></i> 退出</a>
		            	</div>
					</div>

			<div class="J_mainContent"  id="content-main" style="${cookie.tab.value!=false?'height:calc(100% - 40px)':'height:calc(100%)'}">
             <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="${ctx}/home" frameborder="0" data-id="${ctx}/home" seamless></iframe>
            </div>
            </div>
            
            
            </div>
            </div>
            
           <%--<div class="footer">
                <div class="pull-left"><a href="http://www.jeeplus.org">http://www.jeeplus.org</a> &copy; 2015-2025</div>
            </div>--%>
          
</section>
            
            


<script>

		
$(function(){
		$('.theme-picker').click(function() {
			changeTheme($(this).attr('data-theme'));
		});
		$('#showMenu').click(function() {
		    $('body').toggleClass('push-right');
		});
    $('#hideMenu').click(function () {
           $('body').removeClass('push-right')
        $('body').toggleClass('push-left');
    });
		$("#switchTab").change(function(){
			if($("#switchTab").is(':checked')){
				 $("#breadcrumbs").attr("class","breadcrumbs");
				 $("#content-main").css("height","calc(100% - 40px)")
				 $.get('${ctx}/tab/true?url='+window.top.location.href,function(result){  });
			}else{
				 $("#breadcrumbs").attr("class","un-breadcrumbs");
				 $("#content-main").css("height","calc(100%)")
				 $.get('${ctx}/tab/false?url='+window.top.location.href,function(result){  });
			}
		})
		

});
/**
 *切换主题
 */
function changeStyle(){
    $.get('${ctx}/style/jp?url='+window.top.location.href,function(result){
        window.location.reload();
    });
}
/**
 * 切换颜色
 * @param theme
 */
function changeTheme(theme) {
	var link = $('<link>')
	.appendTo('head')
	.attr({type : 'text/css', rel : 'stylesheet'})
	.attr('href', '${ctxStatic}/common/css/app-'+theme+'.css');


	var tabLink = $('<link>')
	.appendTo('head')
	.attr({type : 'text/css', rel : 'stylesheet'})
	.attr('href', '${ctxStatic}/plugin/js-menu/menuTab-'+theme+'.css');

	var childrenLink= $('<link>').appendTo($(".J_iframe").contents().find("head"))
	.attr({type : 'text/css', rel : 'stylesheet'})
	.attr('href', '${ctxStatic}/common/css/app-'+theme+'.css');

	 $.get('${ctx}/theme/'+theme+'?url='+window.top.location.href,function(result){
         setTimeout(function () {
             $('#theme').remove();
             $('#theme-tab').remove();
             $(".J_iframe").contents().find("#theme").remove();
         }, 500);
		 link.attr("id","theme");
		 childrenLink.attr("id","theme");
		 tabLink.attr("id","theme-tab")
	 });
		
}
</script>

</body>
</html>