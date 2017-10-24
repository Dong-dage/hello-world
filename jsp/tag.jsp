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
<title>组织标签</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery-1.9.1.js"></script>

<style type="text/css">
	body{
		background-color: #dfedf5;
		margin: 0;
		padding: 0;
		border: 0;
		outline: 0;
	}
	#level1{		
		background-color: #dfedf5;
		margin: 14px 14px 5px 14px;
		border:1px solid #93b2d0;
		height: 600px;
	} 
	#txl{
		height: 24px;
		background-color: #cbddee;
		color: #000000;
		font-size: 12px;
		padding-left:5px;
	}
	#choose{
		height: 40px;
		color: #000000;
		font-size: 12px;
		border:1px solid #93b2d0;		
		padding-left:20px;
		padding-top:15px;
	}
	.choose1{
		width: 10px;
	}
	a{ 
		text-decoration:none;
		color:#000000;
	}
	#chooseExport{
		font-size: 12px;
		padding-left:20px;
		padding-top:15px;
		height: 35px;
	}
	#status{
		font-size: 12px;
		height: 20px;
		width: 100px;
		/* text-align: center; */
		appearance:none;
	  	-moz-appearance:none;	
	  	-webkit-appearance:none;	
	 	border:none;	
	  	margin:-2;
	  	border-radius:5px;	
	  	background-image:url(images/dyg.png);	
	 	background-size:100% 100%; 	
	  	padding-left:1%;
	}
	#biaotidiv{
		border:1px solid lightgray;
		height: 20px;
		width: 65%;
		overflow :auto;	
		position:absolute;
		left: 295px;
		top: 115px;
		background-color: #FFFFFF;
		border-radius:5px;
	} 
	#exportExcel1{
		position: absolute;
		right: 50px;
		top: 115px;	
		height: 25px;
		width: 90px;
	}
	#left,#right {float:left;}  		
	#left{
		background-color: #dfedf5;
		margin: 0px 10px 0px 20px;
		border: 1px solid #93b2d0;
		width: 21%;
		height: 440px;
		overflow-y: auto;
		overflow-x: auto;
		font-size: 12px;
		line-height:30px;
		border-radius:5px;
	}
	li{
		list-style: none;
	}
	#imgbox1,#imgbox2,li{
		float:left;
	}
	#imgbox1{
		position: relative;
  		left: 15px;
  		width: 12px;
  		height: 12px;
  		margin: 8px 0;
		padding: 0;
		border: 0;
	} 
	#imgbox2{
		position: relative;
  		left: 20px;
  		margin: 8px 0;
		padding: 0;
		border: 0;
	} 
	li{
		position: relative;
  		left: 25px;
	}
	#right{
		background-color: #dfedf5;
		margin: 0px 10px 10px 5px;
		border: 1px solid #93b2d0;
		width: 74%;
		height: 440px;
		font-size: 12px;
		overflow-y:scroll;
		border-radius:5px;
	}
	
	
	/* .tab{
		text-align:center;
		width:100%;	
		font-size:12px; 
	}
	#th{
		width:100%;
		
		border: 1px solid #93b2d0;
    	border-collapse: separate;
	    *border-collapse: collapse;
	    background:#ddd;
	}	
	th,td{
		text-align:center;
		border:1px solid #a9aeb4;
		height: 20px;			
	} */
	
	#th1,#th2,#th3,#th4,#th5{
		width:20%;
	}
	
	table.tab{
		text-align:center;
		width:100%;	
		padding-top: 0px;
		margin-left: 0px;
		border-spacing:0;
		font-size:12px;
	}
	table.tab > thead > tr > th{
		border: 1px solid #a9aeb4;
	    border-collapse: separate;
	    *border-collapse: collapse;
	    background:#ddd;
	    font-size:12px;
	    color:#000000;
	    height:28px;
	    padding: 0px;
	    cellspacing:0;
	    border-right: 0;
	}
	table.tab > thead > tr > th:first-child{
		-webkit-border-radius: 4px 0 0px 0;
	    -moz-border-radius: 4px 0 0px 0;
	    border-radius: 4px 0 0px 0;
	}
	table.tab > thead > tr > th:last-child{
		-webkit-border-radius: 0 4px 0 0;
	    -moz-border-radius: 0 4px  0 0;
	    border-radius:  0 4px 0 0;
	}
	table.tab > tbody > tr > td{
		padding: 0px;
	    /* line-height: 30px; */
	    text-align: left;
	    vertical-align: top;
	    border: 1px solid #989da3;
	    text-align:center;
	    vertical-align:middle;
	    height:29px; 
	    background:#dfebf5;
	    cellspacing:0;
	    border-top: 0;
	    border-right: 0;
	}
	table.tab thead tr th:last-child,table.seas tbody tr td:last-child{
		border-right: 1px solid #989da3;
	}
	table.tab thead tr th:last-child,table.seas tbody tr td:last-child,table.seas thead tr th:last-child,table.seas tbody tr td:nth-child(8){
		border-right: 1px solid #989da3;
	}
	#loading{
		text-align: center;
	}

</style>

</head>

<body>

<div id="level1">
	<div id="txl">通讯录</div>
	<div id="choose">	
		<!-- <a href="jsp/org.jsp"><img class="choose1" src="images/01.png">&nbsp;&nbsp;组织查询</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		
		<a href="jsp/tag.jsp"><img class="choose1" src="images/00.png">&nbsp;&nbsp;标签查询</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
		
		<!-- <a href="jsp/org.jsp"><img class="choose1" src="images/00.png">&nbsp;&nbsp;组织查询</a> -->	
		<a href='toOrg?loginId=${loginId}'><img class="choose1" src="images/01.png">&nbsp;&nbsp;组织查询</a>	
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		
		<!-- <a href="jsp/tag.jsp"><img class="choose1" src="images/01.png">&nbsp;&nbsp;标签查询</a> -->
		<a href='toTag?loginId=${loginId}'><img class="choose1" src="images/00.png">&nbsp;&nbsp;标签查询</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
	<div id="chooseExport">
		<form action="exportByUserid" name="form1" method="post">	
			<input type="hidden" id="XH" name="XH" style="dislay:none">
			<input type="hidden" id="param" name="param"/>				
			<label for="status">关注状态：<span style="color: red">*</span></label>
			<select id="status" name="status">
				<option value="1,4,2">所有状态</option>
				<option value="1" >已关注</option>
				<option value="4">未关注</option>
				<option value="2">已禁用</option>
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;			
			<label>成员范围:<span style="color: red">*</span></label>		
			<div id="biaotidiv">
				<input type="hidden" id="ids" name="tId">
			</div>
			<!-- <input type="submit" id="exportExcel1"  value="导出Excel表格">	 -->
			<!-- <input type="button" id="exportExcel1" value="导出通讯录" onClick="validate()"> -->
			<img alt="" src="images/LOGOUT.png" id="exportExcel1" onclick="validate()">
		</form>
	</div>
	
	<div>			
		<br/>
		<div class="left" id="left">				
			<div id="navleft">
				
			</div>
		</div>
		
		<div id="right">
			<div class="rightContent">
				<form action="" id="form" method="post">
					<table class="tab" cellpadding="0" cellspacing="0">
					<thead>
						<tr id="th">						
							<th id="th1">姓名</th>
							<th id="th2">账号</th>
							<th id="th3">职位</th>
							<th id="th4">手机</th>
							<!-- <th>标签</th> -->
							<th id="th5">状态</th>
						</tr>
					</thead>
					<tbody id="tbody">						
						<tr class="tdempty"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdempty"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdempty"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdempty"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdempty"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdempty"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdempty"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdempty"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdempty"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdempty"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdempty"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdempty"><td></td><td></td><td></td><td></td><td></td></tr>						
					</tbody>
					
						<tr class="tdemp"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdemp"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdemp"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdemp"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdemp"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdemp"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdemp"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdemp"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdemp"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdemp"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdemp"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdemp"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdemp"><td></td><td></td><td></td><td></td><td></td></tr>
						<tr class="tdemp"><td></td><td></td><td></td><td></td><td></td></tr>
						
					</table>
				</form>
			</div>
		</div>
				
	</div>
	
</div>	

</body>


<script type="text/javascript">

	<%-- var loginId = '<%=request.getParameter("loginId") %>'; --%>
	var loginId ='${loginId}';
	//alert(loginId);
	
	$(window).load(
		function getTag() {
			var result = '';
			$.ajax({
				type : "post",
				/* url : 'queryTagList', */
				url : 'queryTagByLoginId?loginId='+loginId,
				dataType : "json",
				/* data: {"usid":userLogin}, */
				success : function(data) {
					//alert(data);
					for (var i = 0; i < data.length; i++) {
						var tagId=data[i].wechat_tagid;
						var tagName=data[i].tagname;
						//alert(tagName);
						//alert(tagId);
						result += '<div id="'+tagId+'"  name ="'+tagName+'" onclick="getTgUser(this)" >'
							+ '<img id="imgbox1" class="tagId'+tagId+'"  src="images/04.png" style="width:5%">'
							+ '<img id="imgbox2" src="images/FOLDER.png" style="width:5%">'
							+ '<li>'+ data[i].tagname + '</li></div>'
							+ '<br/>';							
					}
					$("#navleft").append(result);
				},
			});
		}
	);
						
	var ids = [];	
	var tr=null; 
	function getTgUser(obj) {	
		//debugger;
		var tgId = $(obj).attr("id");
		var tgname = $(obj).attr("name");	
		//向div方框中添加组织名称
		var sp1="<span class='tgId"+tgId+"'>"+tgname+"&nbsp;&nbsp;&nbsp;&nbsp;"+"</span>";	
		//var sp2="<span class='tgId"+tgId+"'>"+tgId+"</span>";
		//alert(tgId);
		var index = ids.indexOf(tgId);
		if(index == -1){
			ids.push(tgId);
			$("#biaotidiv").append(sp1);				
			$('.tagId'+tgId).attr('src','images/03.png');
			
		}else{
			ids.splice(index,1);
			$('.tgId'+tgId).remove();			
			$('.tagId'+tgId).attr('src','images/04.png');
		}				
		$("#ids").val(ids); 
		
		var tIds=$("#ids").val();
		//alert("tIds: "+tIds);
		
		//“导出execl表格”显示与隐藏
		/* if($("#ids").val().length<=0 ){
			$('#exportExcel1').hide();
		}else{
			$('#exportExcel1').show();
		} */
		
		var status = $("#status").val();
		//alert(status);
		$(".tab #tr2").remove();
		
		/* 
		var currentPage=1; //第几页 
		var numPerPage=500; //每页显示条数     
		//分页查询  
		var queryByPage=function(){
			//$(".tab #tr2").remove();
		*/
			$.ajax({
				//async:false,
				type : 'post',
				url : "getTagUser?tId="+tIds+"&status="+status,
				dataType : 'json',
				beforeSend:function(){
					$(".tdempty").hide();
					$(".tdemp").hide();
					$("#tbody").append("<div id='loading'><img src='images/loading.gif' /><div>");
				},
				success : function(data) {	
					
					/* $('.tagIdUser'+tgId).remove(); */
					$("#loading").remove();
					$('#tr2').remove();
					$('.tdempty').show();
					$(".tdemp").show();
					var dataLength=data.length;
					//alert("dataLength: "+dataLength);
					if(dataLength==0){
						alert("查无数据");
					}
					if (dataLength != 0) {
						//alert("1");
						/* if(index == -1){		
							//alert("showhide");
							for (var i = 0; i < data.length; i++) {																			
								var td3 = $("<td>" + data[i].name + "</td>");
								var td4 = $('<td><input type="text" style="background-color:transparent;border:none;font-size:12px;width:65px" name="userid" value="'+data[i].userid+'"/></td>');								
								var td5 = $("<td>" + data[i].position + "</td>");
								var td6 = $("<td>" + data[i].mobile + "</td>");
								var td8 = $("<td>" + data[i].status + "</td>");												
								var tr = $("<tr class='tgId"+tgId+"' id='tr2'></tr>");			
								tr.append(td3).append(td4).append(td5).append(td6).append(td8);
								$('.tab').append(tr);
							}
							$('.tdempty').hide();
						}else{
							$('.tgId'+tgId).remove();
							$('.tdempty').show();				    				
						} */
												
						for (var i = 0; i < data.length; i++) {																			
							var td3 = $("<td>" + data[i].name + "</td>");
							var td4 = $('<td><input type="text" style="background-color:transparent;border:none;font-size:12px;width:100px;text-align:center;" name="userid" value="'+data[i].userid+'"/></td>');								
							var td5 = $("<td>" + data[i].position + "</td>");
							var td6 = $("<td>" + data[i].mobile + "</td>");
							var td8 = $("<td>" + data[i].status + "</td>");												
							/* tr = $("<tr class='tagIdUser"+tgId+"' id='tr2'></tr>");	 */	
							tr = $("<tr class='userDetails' id='tr2'></tr>");
							tr.append(td3).append(td4).append(td5).append(td6).append(td8);
							$('.tab').append(tr);
						}
						$('.tdempty').hide();
														
					}
				}			
			});	
		//} 
	}
	
	//点击关注状态进行异步查询
	$("#status").change(function(){
		//debugger;
		var tIds=$("#ids").val();
    	//alert("tIds: "+tIds);
    	var status=$("#status").val();
    	//alert("status: "+status);
		
    	$(".tab #tr2").remove();
    	
		$.ajax({
			type: "POST",
			url : "getTagUser?tId="+tIds+"&status="+status,
			dataType:"json",
			beforeSend:function(){
				$(".tdempty").hide();
				$(".tdemp").hide();
				$("#tbody").append("<div id='loading'><img src='images/loading.gif' /><div>");
			},
			success: function (data) {
				/* $('.tagIdUser'+tgId).remove(); */
				
				$("#loading").remove();				
				$('.tdempty').show();
				$(".tdemp").show();
				
				$('.userDetails').remove();
				
				var dataLength=data.length;
				//alert("dataLength: "+dataLength);
				if(dataLength==0){
					alert("查无数据");
				}
				if (dataLength != 0) {
    				//alert("have");
    				for (var i = 0; i < data.length; i++) {																			
						var td3 = $("<td>" + data[i].name + "</td>");
						var td4 = $('<td><input type="text" style="background-color:transparent;border:none;font-size:12px;width:100px;text-align:center;" name="userid" value="'+data[i].userid+'"/></td>');								
						var td5 = $("<td>" + data[i].position + "</td>");
						var td6 = $("<td>" + data[i].mobile + "</td>");
						var td8 = $("<td>" + data[i].status + "</td>");												
						/* tr = $("<tr class='tagIdUser"+tgId+"' id='tr2'></tr>");	 */	
						tr = $("<tr class='userDetails' id='tr2'></tr>");
						tr.append(td3).append(td4).append(td5).append(td6).append(td8);
						$('.tab').append(tr);
					}
					$('.tdempty').hide();
    			}				    				
    		},      
    		error: function (XMLHttpRequest, textStatus, errorThrown) {     
    			alert(errorThrown);     
    		}
		});
	});
	
	function validate(){ 
		if(ids!=''){ 				 
			document.forms["form1"].submit(); 
		}else{ 
			alert("请选择要导出的标签"); 
			return false; 
		} 				
	}
</script>

</html>
