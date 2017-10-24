<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<title>权限管理</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery-1.9.1.js">
	
</script>
<style type="text/css">
* {
	border: 0;
}

body {
	font-family: Verdana, Arial, '宋体';
	font-size: 12px;
}

div#wrapper {
	margin: 0 auto;
	padding: 0;
	border: 0;
	width: 100%;
	text-align: left;
}

div#navleft {
	float: left;
	margin: 45px 0 0 8px;
	padding: 0;
	border: 0;
	width: 20%;
	height: 650px;
	overflow: auto;
	list-style: none;
	background-color: #f0f6e4;
}

div#navright {
	position: relative;
	float: right;
	margin: 45px 8px 0 0;
	padding: 0;
	border: 0;
	width: 78%;
	height: 650px;
	overflow: auto;
	background-color: LightGray;
}

#homefirst, #wrapper, .top {
	wodth: 100%;
}

.top {
	height: 60px;
}

.top>div {
	display: inline;
}

</style>
</head>

<body id="homefirst">
	<div id="wrapper">
		<div class="top">
			
		</div>
		<div class="content">
			<div id="navleft">
				<h2>标签权限管理</h2>
			</div>

			<div id="navright">
			
			</div>
		</div>
	</div>
</body>

</html>
