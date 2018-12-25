<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>首页</title>
	<meta name="decorator" content="ani"/>
	<link href="${ctxStatic}/common/css/base.css" rel="stylesheet" />
	<link href="${ctxStatic}/common/css/index.css" rel="stylesheet" />
	<style>
		#body-container {
			margin-left: 0px !important;
			/**padding: 10px;*/
			margin-top: 0px !important;
			overflow-x: hidden!important;
			transition: all 0.2s ease-in-out !important;
			height: 100% !important;
		}
		.name{
			display:inline-block;
			max-width:80%;
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
		}
	</style>
	<script type="text/javascript">
		function checkStr(str){
			if(str == undefined){
				return "-";
			}else{
				return str;
			}
		}
		function getTeacherIndexs(){
			var jsessionid = "${JSESSIONID}";
			$.ajax({
				url: "/education/a/user/teacher/index;JSESSIONID="+jsessionid+"?__ajax=true&mobileLogin=true",
				type: 'post',
	//				contentType:'application/json',
				dataType:'json',
				data: {},
				success:function(data){
					console.log(data)
					if(data.success){
						var body = data.body;
						//资源总数
						$(".resourceCount").text(body.resourceCount);
						//作业总数
						$(".homeworkCount").text(body.homeworkCount);
						//已交作业学生
						var completeStudents = body.completeStudents;
						var student = 4;
						if(completeStudents.length > 0){
							if(completeStudents.length < 5){
								student = completeStudents.length;
							}
							for(i = 0 ; i <= student;i++){
								$(".completeStudents").append(`<li>
																	<span class="leftspan fontColorBlack">`+(i+1)+`.`+completeStudents[i].name+`</span>
																	<p class="fontColorGray">`+completeStudents[i].subject+`</p>
																	<span class="rightspan fontColorGray">提交时间：`+completeStudents[i].updateDate.substring(0,10)+` </span>
																</li>`);
							}
						}
						//学习最多的教学资源
						var resouce = 4;
						var stduyMostResources = body.stduyMostResources;
						if(stduyMostResources.length > 0){
							if(stduyMostResources.length < 5){
								resouce = stduyMostResources.length;
							}
							for(i = 0 ; i <= resouce;i++){
								$(".stduyMostResources").append(`<li>
																		<span style="width:50%;" class="leftspan fontColorBlack"><span class="name" title="`+(i+1)+`.`+ stduyMostResources[i].resourcename +`">`+(i+1)+`.`+ stduyMostResources[i].resourcename +`</span><i>`+ stduyMostResources[i].browse +`次</i></span>
																		<span class="rightspan fontColorGray">上传时间：`+ stduyMostResources[i].createDate.substring(0,10) +`  </span>
																</li>`);
							}
						}
						//采用次数最多的题目
						var question = 4;
						var userMostQuestions = body.userMostQuestions;
						if(userMostQuestions.length > 0){
							if(userMostQuestions.length < 5){
								question = userMostQuestions.length;
							}
							for(i = 0 ; i <= question;i++){
								$(".userMostQuestions").append(`<li>
																	<span class="leftspan fontColorBlack"><span class="name" title="`+(i+1)+`.`+ userMostQuestions[i].questiontext +`">`+(i+1)+`.`+ userMostQuestions[i].questiontext +`</span><i>`+ userMostQuestions[i].publicnnum +`次</i></span>
																	<p class="fontColorGray">`+ checkStr(userMostQuestions[i].subject) +`</p>
																	<span class="rightspan fontColorGray"> <i>`+userMostQuestions[i].questiontype+`</i> </span>
																</li>`);
							}
						}
					}
				},
				error:function(data){
					console.log(data)
				}
			});
		}
		$(document).ready(function() {
			getTeacherIndexs();
		});
	</script>
</head>
<body class="">
	<!--理想尺寸1180，要适配更大的屏幕-->
	<div class="systemindex_index">
		<div class="systemindex__top">
		<div class="systemindex__tleft">
				<div class="systemindex__tltop bcolor-blue ">
					<img src="${ctxStatic}/common/images/loginLogo.png"/>
				</div>
				<div class="systemindex__tlbottom">
					<div class="systemindex__tlbleft bcolor-cyan">
						<h4>发布教学资源总数</h4>
						<p class="source_count"><span class="resourceCount">0</span>件</p>
					</div>
					<div class="systemindex__tlbleft bcolor-orange">
						<h4>布置作业总数</h4>
						<p><span class="homeworkCount">0</span>件</p>
					</div>
				</div>
			</div>
			<ul class="systemindex-ul systemindex__tright completeStudents">
				<div class="ul-title">
					<p><img src="${ctxStatic}/common/images/icon1.png"/>已交作业学生</p>
				</div>
<!-- 				<li> -->
<!-- 					<a href=""> -->
<!-- 						<span class="leftspan fontColorBlack">1.里丙丙</span> -->
<!-- 						<p class="fontColorGray">语文</p> -->
<!-- 						<span class="rightspan fontColorGray">上传时间：2018/02/12  </span> -->
<!-- 					</a> -->
<!-- 				</li> -->
			</ul>
		</div>
		<div class="systemindex__bottom">
		<ul class="systemindex-ul systemindex__bleft stduyMostResources">
			<div class="ul-title">
				<p><img src="${ctxStatic}/common/images/icon3.png"/>学习最多的教学资源</p>
			</div>
<!-- 			<li> -->
<!-- 				<a href=""> -->
<!-- 					<span class="leftspan fontColorBlack">1.里丙丙  <i>11次</i></span> -->
<!-- 					<p class="fontColorGray">语文</p> -->
<!-- 					<span class="rightspan fontColorGray">上传时间：2018/02/12  </span> -->
<!-- 				</a> -->
<!-- 			</li> -->
		</ul>
		<ul class="systemindex-ul systemindex__bright userMostQuestions">
			<div class="ul-title">
				<p><img src="${ctxStatic}/common/images/icon2.png"/>采用次数最多的题目</p>
			</div>
<!-- 			<li> -->
<!-- 				<a href=""> -->
<!-- 					<span class="leftspan fontColorBlack">1.里丙丙  <i>11次</i></span> -->
<!-- 					<p class="fontColorGray">语文</p> -->
<!-- 					<span class="rightspan fontColorGray"> <i>判断题</i> </span> -->
<!-- 				</a> -->
<!-- 			</li> -->
		</ul>
	</div>
		
 </div>
		
	</body>
</html>