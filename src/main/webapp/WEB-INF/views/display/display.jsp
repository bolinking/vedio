<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Bootstrap 实例 - 两端对齐的导航元素</title>
	<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">  
	<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
	<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<script type="text/javascript">
function main1(){
	$("#main1").css("display","none");
	$("#main2").css("display","block");
}
function main2(){
	$("#main2").css("display","none");
	$("#main1").css("display","block");
}
function main3(id){
	window.URL.createObjectURL=id;
}
</script>
<style type="text/css">
div{margin:5px;border:0;padding:0;}
</style>
<body>
<div style="margin-bottom:20px;">
<p>两端对齐的导航元素</p>
<ul class="nav nav-pills nav-justified" >
	<li class="active"><a href="#">Home</a></li>
	<li><a href="#" onclick="main1()">电影</a></li>
	<li><a href="#" onclick="main2()">音乐</a></li>
	<li><a href="#">VB.Net</a></li>
	<li><a href="#">Java</a></li>
	<li><a href="#">PHP</a></li>
</ul><br><br><br>
</div>
<div id="main1">
<a href="#" onclick="main3(id='${raw.source_url }')">${raw.title }</a>
</div>
<div id="main2" style="display:none">
ssssss
</div>
</body>
</html>