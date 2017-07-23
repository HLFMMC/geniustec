<%@ page contentType="text/html; charset=UTF-8"%>

<title>API</title>


<meta name="viewport" content="width=device-width, initial-scale=1">


<script src="http://cdn.bootcss.com/jquery/1.12.3/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.css"
	rel="stylesheet">


<style type="text/css">
body {
	font-family: Verdana, Consolas, Cambria, Courier;
}

textarea {
	font-family: Consolas, Verdana, Cambria, Courier;
}

pre {
	font-family: Consolas, Verdana, Cambria, Courier;
	border-width: 0px;
}
ul>li {
	font-size: 12px;
	color: blue;
}
</style>

<script>
	var requestDate;

	function clearMessage() {

		$("#responseMessage").html("");

	}

	function showAction(obj) {

		$("#actionInput").val(JSON.stringify(obj, null, 4));

	}

	function submitAction() {

		var actionMsg = $("#actionInput").val();
		requestDate = new Date();
		var jsonData = JSON.parse(actionMsg);

		$.ajax({

			url : "../webAction",
			type : 'POST',
			dataType : 'text',
			data : jsonData,
			success : function(data) {
				var jData = JSON.parse(data);
				var str = JSON.stringify(JSON.parse(data), null, 4);
				appendMessage(jData.ServerCode, str);

			}
		});

	}

	//导入api菜单
	function loadApiMenu(){
		$.ajax({   
		    url: 'api_admin_menu.html',
		    type: 'GET',
		    dataType: 'text',
		    async: false,
		    success: function (data){ 	               	
		    	$("#apiMenu").html(data);
		 	 }
	     });
	}

	$(document).ready(function() {
		loadApiMenu();
	});

	function init() {

	}

	function appendMessage(title, msg) {
		var date0 = new Date();
		var costSec = (date0.getTime() - requestDate.getTime()) / 1000;
		var outStr = "";
		outStr += "<div  class='panel panel-default area'>";
		outStr += "<div  class='panel-heading'>";
		outStr += "<span class='text-left'>" + title + "  (" + costSec + " sec)</span>";
		outStr += "</div>";
		outStr += "<div class='panel-body '>";
		outStr += "<pre>" + msg + "</pre>";
		outStr += "</div>";
		outStr += "</div>";

		$('#responseMessage').prepend(outStr);
		$('.area:eq(0)').hide();
		$('.area:eq(0)').slideDown();
	}
	
	
	
	/* managerLogin */
	function managerLogin() {
		var jsonData = {
			"serverCode" : "managerLogin",
			"email" : "1191929983@qq.com",
			"password" : "123qwe"
		}
		showAction(jsonData);
	}
	
	function managerInfo() {
		var jsonData = {
			"serverCode" : "managerInfo"
		}
		showAction(jsonData);
	}
	// 登录者信息
	function managerLogout() {
		var jsonData = {
			"serverCode" : "managerLogout"
		}
		showAction(jsonData);
	}
	/*用户注册*/
	function managerRegister() {
		var jsonData = {
			"serverCode" : "managerRegister",
			"email" : "", 
			"userName" : "",
			"password" : ""
		}
		showAction(jsonData);
	}
	
	// 修改头像
	function managerUpdatePic() {
		var jsonData = {
			"serverCode" : "managerUpdatePic",
			"base64": ""
		}
		showAction(jsonData);
	}
	
	/*修改个人信息*/
	function managerUpdateInfo() {
		var jsonData = {
			"serverCode" : "managerUpdateInfo",
			"userName": "",
			"sex" : "",
			"personality" : ""
		}
		showAction(jsonData);
	}
	
	// 修改头像
	function uploadUserImage() {
		var jsonData = {
			"serverCode" : "uploadUserImage",
			"base64": ""
		}
		showAction(jsonData);
	}
	/*修改个人信息*/
	function updateUserInfo() {
		var jsonData = {
			"serverCode" : "updateUserInfo",
			"nikeName": "",
			"sex" : "",
			"signature" : ""
		}
		showAction(jsonData);
	}
	/*文章列表*/
	function articleList() {
		var jsonData = {
			"serverCode" : "articleList",
			"search" : "", 
			"articleId" : ""
		}
		showAction(jsonData);
	}

	/*文章新增*/
	function articleAdd() {
		var picList = [];
		var item = {};
		item.base64="";
		picList.push(item);
		var picStr = JSON.stringify(picList);
		var jsonData = {
			"serverCode" : "articleAdd",
			"title" : "",
			"content" : "",
			"categoryId" : "",
			"picList" : picStr
		}
		showAction(jsonData);
	}
	
	//文章回复
	function articleReplay() {
		var jsonData = {
			"serverCode" : "articleReplay",
			"articleId" : "",
			"content" : "",
			"base64" : ""
		}
		showAction(jsonData);
	}
	
	//文章删除
	function articleDelete() {
		var jsonData = {
			"serverCode" : "articleDelete",
			"articleId" : "1"
		}
		showAction(jsonData);
	}

	//文章回复列表
	function articleReplayList(){
		var jsonData = {
			"serverCode" : "articleReplayList",
			"articleId" : "",
			"replayId" : ""
		}
		showAction(jsonData);
	}
	
	//我的回复列表
	function myArticleReplayList(){
		var jsonData = {
			"serverCode" : "myArticleReplayList"
		}
		showAction(jsonData);
	}
	
	//问题列表
	function problemList(){
		var jsonData = {
			"serverCode" : "problemList",
			"text" : "",
			"problemId" : ""
		}
		showAction(jsonData);
	}//问题列表
	function myProblemList(){
		var jsonData = {
			"serverCode" : "myProblemList"
		}
		showAction(jsonData);
	}
	//问题新增
	function problemAdd(){
		var jsonData = {
			"serverCode" : "problemAdd",
			"title" : "",
			"content" : "",
			"score" : ""
		}
		showAction(jsonData);
	}
	// 问题回复新增
	function problemReplayAdd(){
		var jsonData = {
			"serverCode" : "problemReplayAdd",
			"problemId" : "",
			"content" : ""
		}
		showAction(jsonData);
	}
	// 问题回复列表
	function problemReplayList(){
		var jsonData = {
			"serverCode" : "problemReplayList",
			"problemId" : "",
			"replayId" : ""
		}
		showAction(jsonData);
	}
	function myProblemReplayList(){
		var jsonData = {
			"serverCode" : "myProblemReplayList"
		}
		showAction(jsonData);
	}
	/*采纳问题*/
	function adoptionReplay() {
		var jsonData = {
			"serverCode" : "adoptionReplay",
			"problemId" : "",
			"replayId" : ""
		}
		showAction(jsonData);
	}
	function closeProblem(){
		var jsonData = {
			"serverCode" : "closeProblem",
			"problemId" : ""
		}
		showAction(jsonData);
	}
	function disciplineList(){
		var jsonData = {
			"serverCode" : "disciplineList"
		}
		showAction(jsonData);
	}
	function categoryList(){
		var jsonData = {
			"serverCode" : "categoryList",
			"disciplineId" : ""
		}
		showAction(jsonData);
	}
	
	/*我的文章列表*/
	function myArticleList() {
		var jsonData = {
			"serverCode" : "myArticleList"
		}
		showAction(jsonData);
	}
</script>

<div id="test"></div>
<div class="container-fluid"></div>

<nav class="navbar navbar-default">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="#" id="messages">API</a>
			<div id="messages"></div>
		</div>

		<div id="apiMenu"></div>

		<ul class="nav navbar-nav navbar-right">
			<li><a href="#"><span class="glyphicon glyphicon-member"></span>admin</a></li>
		</ul>
	</div>
</nav>
<hr>


<div class="container-fluid">
	<div class="col-md-0"></div>
	
	<div class="col-md-3">
		<div class="panel-group">
			<div class="panel panel-default">
			
			
				<div class="panel-heading">manager</div>
				<ul class="list-group">
					<li class="list-group-item"><a href="javascript:managerLogin()">managerLogin 登录</a></li>
					<li class="list-group-item"><a href="javascript:managerInfo()">managerInfo 用户信息 </a></li>
					<li class="list-group-item"><a href="javascript:managerLogout()">managerLogout 登出 </a></li>
					<li class="list-group-item"><a href="javascript:managerRegister()">managerRegister 注册 </a></li>
					<li class="list-group-item"><a href="javascript:managerUpdatePic()">managerUpdatePic 修改头像 </a></li>
					<li class="list-group-item"><a href="javascript:managerUpdateInfo()">managerUpdateInfo 修改个人信息 </a></li>
					
					<li class="list-group-item"></li>
					<li class="list-group-item"><a href="javascript:uploadUserImage()">uploadUserImage 修改头像 </a></li>
					<li class="list-group-item"><a href="javascript:updateUserInfo()">updateUserInfo 修改个人信息 </a></li>

				</ul>
				<div class="panel-heading">文章相关</div>
				<ul class="list-group">
					<li class="list-group-item"><a href="javascript:articleList()">articleList 文章列表</a></li>
					<li class="list-group-item"><a href="javascript:myArticleList()">myArticleList 我的文章列表</a></li>
					<li class="list-group-item"><a href="javascript:articleAdd()">articleAdd 文章新增</a></li>
					<li class="list-group-item"><a href="javascript:articleReplay()">articleReplay 文章回复新增</a></li>
					<li class="list-group-item"><a href="javascript:articleDelete()">articleDelete 文章删除</a></li>
				
				</ul>
				<div class="panel-heading">问题相关</div>
				<ul class="list-group">
					<li class="list-group-item"><a href="javascript:problemList()">problemList 问题列表</a></li>
					<li class="list-group-item"><a href="javascript:myProblemList()">myProblemList 我的问题列表</a></li>
					<li class="list-group-item"><a href="javascript:problemAdd()">problemAdd 问题新增</a></li>
					<li class="list-group-item"><a href="javascript:problemReplayAdd()">problemReplayAdd 回答问题</a></li>
					<li class="list-group-item"><a href="javascript:problemReplayList()">problemReplayList 问题回复列表</a></li>
					<li class="list-group-item"><a href="javascript:myProblemReplayList()">myProblemReplayList 我的问题回复列表</a></li>
					<li class="list-group-item"><a href="javascript:adoptionReplay()">adoptionReplay 采纳回复</a></li>
					<li class="list-group-item"><a href="javascript:closeProblem()">closeProblem 关闭问题</a></li>
				
				</ul>
				<div class="panel-heading">分类</div>
				<ul class="list-group">
					<li class="list-group-item"><a href="javascript:categoryList()">categoryList列表</a></li>
					<li class="list-group-item"><a href="javascript:disciplineList()">disciplineList 问题新增</a></li>
				
				</ul>

			</div>
		</div>
	</div>
	
	<div class="col-md-9">
		<div class="form-group">
			<label>request</label>
			<textarea class="form-control" rows="8" id="actionInput"></textarea>
		</div>
		<div class="form-group">
			<button class="btn btn-default" onclick="submitAction()">request</button>
			<button class="btn btn-default" onclick="clearMessage()">clear</button>
		</div>
		<label for="exampleInputEmail1">response</label> <br>
		<div id="responseMessage" class="panel-group"></div>
	</div>
</div>
<footer class="container-fluid text-center">
	<p>
		<a href="javascript:refreshVerifyPic();"><img  class="control-label"  id="verifyPic"  src="../verifyCode" /></a><br> Copyright 2016
	</p>
</footer>
</body>
</body>
</html>
