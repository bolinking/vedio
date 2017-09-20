<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
String path = request.getContextPath(); 
// 获得本项目的地址(例如: http://localhost:8080/MyApp/)赋值给basePath变量 
String basePath = request.getScheme()+"://"+request.getServerName()
+":"+request.getServerPort()+path+"/"; 
// 将 "项目路径basePath" 放入pageContext中，待以后用EL表达式读出。 
pageContext.setAttribute("basePath",basePath); 
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="../jquery-easyui-1.4.4/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="../jquery-easyui-1.4.4/themes/icon.css"/>
<script type="text/javascript" src="../jquery-easyui-1.4.4/jquery.min.js"></script>
<script type="text/javascript" src="../jquery-easyui-1.4.4/jquery.easyui.min.js"></script>
<title>userInfo</title>
</head>
<body>
<div style="padding:5px;background:#fafafa;width:1116px;border:1px solid #ccc">
姓名：<input class="easyui-textbox" type="text" id="name" name="name" data-options="required:true"></input>
</div>
<div style="padding:5px;background:#fafafa;width:1116px;border:1px solid #ccc">
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px" onclick="find()">查询</a>
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="$('#w').window('open')">新增</a>
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="deleteuser()">删除</a>
</div>
<div data-options="fit:true,border:false" style="height:100%" >  
       <table id="dataTable" cellpadding="0" cellspacing="0"></table>  
</div>
<div id="w" class="easyui-window" title="新增弹框" data-options="iconCls:'icon-add'" closed="true"  style="width:500px;height:200px;padding:5px;">
		<div class="easyui-layout" data-options="fit:true">
			<div id='user' data-options="region:'center'" style="padding:10px;">
			姓名：<input class="easyui-textbox" type="text" id="name1" name="name" data-options="required:true"></input>
			年龄：<input class="easyui-textbox" type="text" id="age" name="age" data-options="required:true"></input>
			</div>
			<div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
				<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" onclick="add()" style="width:80px">保存</a>
				<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" onclick="javascript:alert('cancel')" style="width:80px">Cancel</a>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript"> 
$(document).ready(function() {
    page_init();
});
function page_init() {

    $("#dataTable").datagrid({
    	title:'',iconCls:'icon-edit',width:'auto',height: 'auto',loadMsg:'正在加载...',
        url: "showInfos", //actionName
        queryParams:{pageNumber:1,pageSize:10},//查询参数
        pagePosition:'bottom',/*分页栏显示位置top,bottom,both*/
        checkOnSelect:true,/*点击行选择复选框,false只有点击复选框才选中行*/
        singleSelect:false,pagination:true,
        rownumbers:true,//查询结果在表格中显示行号
        fitColumns:true,//列的宽度填满表格，防止下方出现滚动条。
        pageNumber:1,   //初始页码，得在这设置才效果，pagination设置没效果。
        pageSize:10,
        pagination: true,//分页控件
        //如果后端返回的json的格式直接是data={total:xx,rows:{xx}},不需要设置loadFilter了，
        //如果有多层封装，比如data.jsonMap = {total:xx,rows:{xx}}，则需要在loadFilter处理一下。
        /*
        loadFilter: function(data){
            if(data.jsonMap) {
                return data.jsonMap;
            }
        }*/
        columns:[[
                  {field:'ck',checkbox:true},

                  {field:'id',title:'Item ID',width:'10%'},

                  {field:'name',title:'姓名',width:'10%'},

                  {field:'age',title:'年龄',width:'10%',align:'right'}

              ]]
    });

    var p = $('#dataTable').datagrid('getPager');
    $(p).pagination({
        pageSize: 10,//每页显示的记录条数，默认为10
        pageList: [5,10,15,20],//可以设置每页记录条数的列表
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '共 {total} 条记录',
        onSelectPage: function (pageNumber,pageSize) {//分页触发
            find(pageNumber, pageSize);
        }
    });

}

function find(pageNumber,pageSize)
{
	    var name = $("#name").val();
	    if(pageNumber == null){
	    	//获取页码
		    var pageNumber = $("#dataTable" ).datagrid("getPager" ).data("pagination" ).options.pageNumber;
	    }
	    if(pageSize == null){
	    	//获取页面记录数
		    var pageSize = $("#dataTable" ).datagrid("getPager" ).data("pagination" ).options.pageSize;
	    }
        $("#dataTable").datagrid('getPager').pagination({pageSize : pageSize, pageNumber : pageNumber,name:name});//重置
        $("#dataTable").datagrid("loading"); //加屏蔽
        $.ajax({
            type : "POST",
            dataType : "json",
            url : "showInfos",
            data : {
                pageNumber : pageNumber,
                pageSize : pageSize,
                name : name
            },
            success : function(data) {
                $("#dataTable").datagrid('loadData',data);
                $("#dataTable").datagrid("loaded"); //移除屏蔽
            },
            error : function(err) {
                $.messager.alert('操作提示', '获取信息失败...请联系管理员!', 'error');
                $("#dataTable").datagrid("loaded"); //移除屏蔽
            }
        });

}

function add(){
	var name = $("#name1").val();
	var age = $("#age").val();
	$.ajax({
	       cache: false,
	       type: "POST",
	       url:"saveuserInfo",
	       data:{name:name,age:age},
	       success : function(data) {
	    	   if(data){
		              $.messager.alert('操作提示', '保存成功', 'info');
		              $("#dataTable").datagrid('reload');
		              $('#w').window('close');
	    	   }else{
	    		   $.messager.alert('操作提示', '保存失败', 'info');
	    	   }
	          },
	          error : function(err) {
	              $.messager.alert('操作提示', '获取信息失败...请联系管理员!', 'error');
	              $("#dataTable").datagrid("loaded"); //移除屏蔽
	          }
	   });

}

function deleteuser(){
	var obj = $("#dataTable");
	var rows = obj.datagrid("getSelections");
	if(rows.length == 0){
		$.messager.alert('操作提示', '至少选择一条数据', 'warning');
	}
//	for(var i=0;i<rows.length;i++){
//		ids += rows[i].id+",";
//	}
	$.ajax({
	       cache: false,
	       type: "POST",
	       url:"deleteuserInfo",
	       data:{list:jsonToString(rows)},
	       success : function(data) {
	    	   if(data){
		              $.messager.alert('操作提示', '删除成功', 'info');
		              $("#dataTable").datagrid('reload');
	    	   }else{
	    		   $.messager.alert('操作提示', '删除失败', 'info');
	    	   }
	          },
	          error : function(err) {
	              $.messager.alert('操作提示', '获取信息失败...请联系管理员!', 'error');
	              $("#dataTable").datagrid("loaded"); //移除屏蔽
	          }
	   });
}


function jsonToString(obj){  
    var THIS = this;   
    switch(typeof(obj)){  
        case 'string':  
            return '"' + obj.replace(/(["\\])/g, '\\$1') + '"';  
        case 'array':  
            return '[' + obj.map(THIS.jsonToString).join(',') + ']';  
        case 'object':  
             if(THIS.isArray(obj)){
                var strArr = [];  
                var len = obj.length;  
                for(var i=0; i<len; i+=1){  
                    strArr.push(THIS.jsonToString(obj[i]));  
                }  
                return '[' + strArr.join(',') + ']';  
            }else if(obj == null){  
                return 'null';  
            }else{  
                var string = [];  
                for (var property in obj){
                	string.push(THIS.jsonToString(property) + ':' + THIS.jsonToString(obj[property]));
               	}  
                return '{' + string.join(',') + '}';  
            }  
        case 'number':
            return obj;  
        case 'boolean':  
            return obj;
        case 'function':
        	return obj.toString();
    }
};

function isArray( obj ){
	return Object.prototype.toString.call(obj) == '[object Array]';
};
</script>
</html>