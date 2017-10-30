<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Bootstrap 实例 - 两端对齐的导航元素</title>
	<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">  
	<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
	<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<%response.addHeader("Access-Control-Allow-Origin", "*"); %>
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
.div{ margin:0 auto; width:400px; height:100px; border:0px solid #F00} 
#main1first {
float: left;
height: 400px;
width: 450px;
}
#main1first1 {
float: left;
height: 400px;
width: 450px;
}
#main1first1 {
float: left;
height: 400px;
width: 500px;
}
</style>
<body>
<div style="margin-bottom:20px;">
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
<div id ="main1first">
<c:forEach  var="item" items="${raws1}" varStatus="status">
 <a href="#" onclick="main3(id='${item.source_url }')">${item.title }</a><br>
</c:forEach>
</div>
<div id ="main1first1">
<c:forEach  var="item" items="${raws2}" varStatus="status">
 <a href="#" onclick="main3(id='${item.source_url }')">${item.title }</a><br>
</c:forEach>
</div>
<div id ="main1first2">
<c:forEach  var="item" items="${raws3}" varStatus="status">
 <a href="#" onclick="main3(id='${item.source_url }')">${item.title }</a><br>
</c:forEach>
</div>
<div id = "main1" class="div">
<ul class="pagination">
	<li><a href="#">&laquo;</a></li>
	<li><a href="#">${page }</a></li>
	<li><a href="#">2</a></li>
	<li><a href="#">3</a></li>
	<li><a href="#">4</a></li>
	<li><a href="#">5</a></li>
	<li><a href="#">&raquo;</a></li>
</ul>
</div>
</div>
<div id="main2" style="display:none">
ssssss
</div>
</body>
</html>