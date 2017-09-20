<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>EasyUI Demo</title>
<link rel="stylesheet" type="text/css" href="js/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="js/themes/icon.css"/>
<link rel="stylesheet" type="text/css" href="js/tree_themes/SimpleTree.css"/>
<script type="text/javascript" src="js/jquery-1.6.min.js"></script>
<script type="text/javascript" src="js/noContextMenu.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/tabs.js"></script>
<script type="text/javascript" src="js/SimpleTree.js"></script>

<script type="text/javascript">

$(function(){
	//初始化树形菜单
	$(".st_tree").SimpleTree({
		click:function(a){
			  if(!$(a).attr("hasChild")) 
			  {
				  var title=$(a).text();
				  var url=$(a).attr("rel");
				  var icon=$(a).attr("icon");
				  parent.OpenTab(title,url,icon);
			  }
		}
	});	
});

$(function(){
 	/*为选项卡绑定右键*/
	$(".tabs li").live('contextmenu',function(e){
		
		/* 选中当前触发事件的选项卡 */
		var subtitle =$(this).text();
		$('#tabs').tabs('select',subtitle);
		
		//显示快捷菜单
		$('#menu').menu('show', {
			left: e.pageX,
			top: e.pageY
		});
		
		return false;
	});
});

</script>
<style></style>
</head>

<body class="easyui-layout" >
<div region="north" style="height:80px;">
	<!-- 页面头部 -->
	<h1>***管理系统</h1>
</div>

<div region="west" split="true" style="width:220px;" title="导航菜单">
<div class="st_tree">
	<ul>
    	<li>搜索引擎</li>
        <ul>
    		<li><a href="#" rel="userInfo/">用户管理</a></li>
            <li><a href="#" rel="http://www.google.com">Google搜索</a></li>
        </ul>
    	<li>博客</li>
        <ul>
    		<li><a href="#" rel="http://www.cnblogs.com">cnblogs</a></li>
            <li><a href="#" rel="http://blog.csdn.net">CSDN</a></li>
        </ul>
    </ul>
</div>
</div>

<div region="center">
	<div id="tabs" class="easyui-tabs" fit="true" border="false">
        <div title="欢迎使用">
        	<button id="btn">BTN</button>
        </div>
	</div>
    
</div>

<div id="menu" class="easyui-menu" style="width:150px;">
    <div id="m-refresh">刷新</div>
    <div class="menu-sep"></div>
    <div id="m-closeall">全部关闭</div>
    <div id="m-closeother">除此之外全部关闭</div>
    <div class="menu-sep"></div>
    <div id="m-close">关闭</div>
</div>
</body>
</html>

<!-- 
       <!--欢迎标签 START - ->

        <!--欢迎标签 END- ->
<script type="text/javascript" src="js/noContextMenu.js"></script>

<div id="menu">
<a href="#" rel="http://www.baidu.com">百度搜索</a>
<a href="#" rel="http://www.google.com">Google搜索</a>
<a href="#" rel="http://www.cnblogs.com">cnblogs</a>
</div>

		$("#menu a").click(function(a){
			  if(!$(a).attr("hasChild")) 
			  {
				  var title=$(a).text();
				  var url=$(a).attr("rel");
				  var icon=$(a).attr("icon");
				  OpenTab(title,url,icon);
			  }
		});
-->