<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%> 
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公司组织架构</title>
	 
<link rel="stylesheet" href="./css/demo.css" type="text/css">
<link rel="stylesheet" href="./css/zTreeStyle.css" type="text/css"> 
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
		border-radius:5px;
		height: 20px;
		width: 65%;
		overflow :auto; 	
		/* overflow-y:scroll;  */		
		position:absolute;
		left: 295px;
		top: 115px;
		background-color: #FFFFFF;		
	} 

	#exportExcel1{
		position: absolute;
		right: 50px;
		top: 115px;			
		height: 25px;
		width: 90px;
	}
	div.content_wrap {
	    width: 100%;
	    height: 470px;
	}
	div.zTreeDemoBackground {
	    width: 100%;
	    height: 430px;
	    text-align: left;
	}
			
	#left{
		background-color: #dfedf5;
		margin: 0px 10px 0px 20px;
		border:0px solid #93b2d0;
		border-radius:5px;
			
	}
	ul.ztree {
		margin-top: 0px;
		border: 1px solid #93b2d0;
		width:100%;
		height:430px;
		overflow-y: auto;
		overflow-x: auto;
		border-radius:5px;
	} 
	#right{
		background-color: #dfedf5;
		margin: 0px 20px 0px 0px;
		/* border:0px; */
		border:1px solid #93b2d0;
		border-radius:5px;
		overflow-y: scroll;
		overflow-x: auto;
	}
	
	div.rightContent{
		margin-top: 0px;
		border: 0px solid #93b2d0;
		width: 100%;
		height: 440px;
		overflow-y: scroll;
		overflow-x: auto;
	} 
	div.content_wrap div.right {
	    float: right;
	    width: 75%;
	    height: 440px;
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
		

</style>	
</head>

<body>
<div id="level1">
	<div id="txl">通讯录</div>
	<div id="choose">
		<!-- <a href="jsp/org.jsp"><img class="choose1" src="images/00.png">&nbsp;&nbsp;组织查询</a> -->	
		<a href='toOrg?loginId=${loginId}'><img class="choose1" src="images/00.png">&nbsp;&nbsp;组织查询</a>	
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		
		<!-- <a href="jsp/tag.jsp"><img class="choose1" src="images/01.png">&nbsp;&nbsp;标签查询</a> -->
		<a href='toTag?loginId=${loginId}'><img class="choose1" src="images/01.png">&nbsp;&nbsp;标签查询</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<!-- <textarea cols="20" rows="5"></textarea> -->
	</div>
			
	<div id="chooseExport">
		<form action="exportFileBydId" name="form1" method="post">					
			<label for="status">关注状态：<span style="color: red">*</span></label>
			<select id="status" name="status" >
				<option class="status" value="1,4,2">所有状态</option>
				<option class="status" value="1" >已关注</option>
				<option class="status" value="4">未关注</option>
				<option class="status" value="2">已禁用</option>
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;			
			<label>成员范围:<span style="color: red">*</span></label>		
			<div id="biaotidiv">
				<input type="hidden" id="ids" name="dId">
			</div>
			<!-- <input type="submit" id="exportExcel1"  value="导出Excel表格">	 -->
			<!-- <input type="button"  id="exportExcel1"  onClick="validate()"> -->
			<img alt="" src="images/LOGOUT.png" id="exportExcel1" onclick="validate()">
		</form>
	</div>
	<div class="content_wrap">			
		<br/>				
		<div class="zTreeDemoBackground left" id="left">				
			<ul id="treeDemo" class="ztree"></ul>
		</div>
		
		<div class="right" id="right">		
			<!-- <div class="rightContent"> -->
				<form action="export2" id="form" method="post">
					<table class="tab"  cellpadding="0" cellspacing="0">
					<thead>
						<tr id="th">
							<!-- <th>分公司</th>
							<th>营业所</th> -->
							<th id="th1">姓名</th>
							<th id="th2">账号</th>
							<th id="th3">职位</th>
							<th id="th4">手机</th>
							<th id="th5">状态</th>
						</tr>	
					</thead>
					<tbody>										
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
			<!-- </div> -->
		</div>
	</div>
	
</div>	
<script type="text/javascript" src="js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="js/jquery.ztree.core.js"></script>
<script type="text/javascript">
		
		var setting = {
			view: {
				selectedMulti: false,
				showLine: false
			},
			data: {
				keep:{
					parent:true
				},
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeAsync: beforeAsync,
				onAsyncError: onAsyncError,
				onAsyncSuccess: onAsyncSuccess,
				onExpand: zTreeOnExpand,
				onCollapse: zTreeOnCollapse,
				onClick: zTreeOnClick
			} 
		};
		
		
		function filter(treeId, parentNode, childNodes) {
			if (!childNodes) return null;
			for (var i=0, l=childNodes.length; i<l; i++) {
				childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			}
			return childNodes;
		} 
		var log, className = "dark";
		function beforeAsync(treeId, treeNode) {
			className = (className === "dark" ? "":"dark");
			showLog("[ "+getTime()+" beforeAsync ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
			return true;
		}
		function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
			showLog("[ "+getTime()+" onAsyncError ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
		}
		function onAsyncSuccess(event, treeId, treeNode, msg) {
			showLog("[ "+getTime()+" onAsyncSuccess ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
		}
		
		function showLog(str) {
			if (!log) log = $("#log");
			log.append("<li class='"+className+"'>"+str+"</li>");
			if(log.children("li").length > 8) {
				log.get(0).removeChild(log.children("li")[0]);
			}
		} 
		function getTime() {
			var now= new Date(),
			h=now.getHours(),
			m=now.getMinutes(),
			s=now.getSeconds(),
			ms=now.getMilliseconds();
			return (h+":"+m+":"+s+ " " +ms);
		}		
		
		$(document).ready(function(){
			var havenodes = getOrg();
			zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, havenodes);
		});
		
		<%-- var loginId = '<%=request.getParameter("loginId") %>'; --%>
		var loginId ='${loginId}';
		//alert(loginId);
		
		function getOrg(){
			var orgInfo = [];
			$.ajax({
				async:false,
				//url:"queryDeptList",
				url:"queryDeptListByLoginId?loginId="+loginId,
				type:"post",
				cache:false,
				dataType:"json",
				/* beforeSend:function(){					
					$("#treeDemo").append("<div id='loading'><img src='images/loading.gif' /><div>");
				}, */
				success:function(data){
						for(var i =0;i < data.length;i++){
							var node = {"id":"","name":"","isParent":"","pid":""};
							node.id = data[i].id;
							node.name = data[i].name;
							node.isParent = true;
							node.pId = data[i].pId;
							data[i] = node;						
							orgInfo[i] = node;
						}					
				},error:function(){
					alert("数据加载失败");
				}
			})
			return orgInfo;
		}
		
		function zTreeOnExpand(event, treeId, treeNode) {
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");			
			//var newNodes = [{id:"a1",name:"newNode1",isParent:"true"}, {id:"a2",name:"newNode2",isParent:"true"}, {id:"a3",name:"newNode3",isParent:"true"}];
			$.ajax({
				type: "POST",
				url: "queryChildDeptList?parentId="+treeNode.id,
				dataType:"json",
				success: function (data) {			    			
					treeObj.addNodes(treeNode, data);
	    		},      
	    		error: function (XMLHttpRequest, textStatus, errorThrown) {     
	    			alert(errorThrown);     
	    		}
			});
						
			
		};
		
		function zTreeOnCollapse(event, treeId, treeNode) {
		    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		    treeObj.removeChildNodes(treeNode);		    
		};
		
		var ids = [];
		var tr=null;
		function zTreeOnClick(event, treeId, treeNode) {
			
			
						
			var dId=treeNode.id;
			var dId2=treeNode.id;
			var name=treeNode.name;
			//向div方框中添加组织名称
			var sp1="<span class='"+dId+"'>"+name+"&nbsp;&nbsp;&nbsp;&nbsp;"+"</span>";	
			var index = ids.indexOf(dId);
			if(index == -1){
				ids.push(dId);
				$("#biaotidiv").append(sp1);
			}else{
				ids.splice(index,1);
				$('.'+dId).remove();
			}
			$("#ids").val(ids); 
			//“导出execl表格”显示与隐藏
			/* if($("#ids").val().length<=0 ){
				$('#exportExcel1').hide();
			}else{
				$('#exportExcel1').show();
			} */
			
        	var status=$("#status").val();
        	//alert(status);
		    var currentPage=1; //第几页 
		    var numPerPage=500; //每页显示条数     
		    //分页查询  
			var queryByPage=function(){
		    	//debugger;
				$(".tab #tr2").remove();
	    		var dId3=$("#ids").val();
	    		//alert("dId3:"+dId3);
				$.ajax({ 
			    		type: "post", 
			    		url: "queryEmpPage?dId="+dId3+"&currentPage="+currentPage+"&numPerPage="+numPerPage+"&status="+status, 
			    		dataType: "json", 
			    		success: function (data) {			    			
			    			var array=data.empList;  
			    			var totalPage=data.totalPage;  
			    			
			    			/* $('.dId'+dId2).remove(); */	
			    			$('#tr2').remove();
		    				$('.tdempty').show();
		    				
		    				//alert(array.length);
			    			if (array.length==0) {
								alert("查无数据！");
							}			    			
			    			
			    			if (array.length > 0) {
			    				//alert("have");
				    			$("#totalPage_input").val(totalPage);   
				    			$("#currentPage").html(currentPage);  
				    			$("#totalRows").html(data.totalRows);  
				    			$("#totalPage").html(totalPage);
				    			
				    			/* if(index == -1){	
				    				for(var i=0;i<array.length;i++){  
					    				var td3 =$("<td class='' id='uName'>"+array[i].uName+"</td>");
										var td4 =$("<td class='' id='uId1'><input type='text' style='background-color:transparent;border:none;font-size:12px;width:65px' class='uId' value='"+array[i].uId+"'/></td>"); 	
										var td5 =$("<td class='' id='position'>"+array[i].position+"</td>"); 	
					    				var td6 =$("<td class='' id='mobile'>"+array[i].mobile+"</td>"); 		    				 	    				    				
					    				var td8 =$("<td class=''>"+array[i].status+"</td>"); 
					    				tr=$("<tr class='"+dId2+"' id='tr2'></tr>"); 
										tr.append(td3).append(td4).append(td5).append(td6).append(td8);
										$(".tab").append(tr); 
					    			} 
				    				$('.tdempty').hide();
				    			}else{
				    				//ids.splice(index,1);
				    				$('.'+dId2).remove();	
				    				$('.tdempty').show();
				    			} */
				    							    			
			    				for(var i=0;i<array.length;i++){  
				    				var td3 =$("<td class='' id='uName'>"+array[i].uName+"</td>");
									var td4 =$("<td class='' id='uId1'><input type='text' style='background-color:transparent;border:none;font-size:12px;width:100px;text-align:center;' class='uId' value='"+array[i].uId+"'/></td>"); 	
									var td5 =$("<td class='' id='position'>"+array[i].position+"</td>"); 	
				    				var td6 =$("<td class='' id='mobile'>"+array[i].mobile+"</td>"); 		    				 	    				    				
				    				var td8 =$("<td class=''>"+array[i].status+"</td>"); 
				    				/* tr=$("<tr class='dId"+dId2+"' id='tr2'></tr>"); */
				    				tr=$("<tr class='userDetails' id='tr2'></tr>");
									tr.append(td3).append(td4).append(td5).append(td6).append(td8);
									$(".tab").append(tr); 
				    			} 
			    				$('.tdempty').hide();
				    			
			    			}				    				
			    		},      
			    		error: function (XMLHttpRequest, textStatus, errorThrown) {     
			    			alert(errorThrown);     
			    		}     
				 });    
			  }  
		      //初始化列表 
		      queryByPage(dId,currentPage,numPerPage);		    					
		}
		
		function validate(){ 
			if(ids!=''){ 				 
				document.forms["form1"].submit(); 
			}else{ 
				alert("请选择要导出的组织"); 
				return false; 
			} 				
		}
		
		//点击关注状态进行异步查询
		$("#status").change(function(){
			//debugger;
			//alert("init .status");
			var dId3=$("#ids").val();
	    	//alert("dId3: "+dId3);
	    	var status=$("#status").val();
        	//alert("status: "+status);
		    var currentPage=1; //第几页 
		    var numPerPage=500; //每页显示条数     
			$.ajax({
				type: "POST",
				url: "queryEmpPage?dId="+dId3+"&currentPage="+currentPage+"&numPerPage="+numPerPage+"&status="+status,
				dataType:"json",
				success: function (data) {			    			
	    			var array=data.empList;  
	    			var totalPage=data.totalPage;  
	    			
	    			$('.userDetails').remove();
    				$('.tdempty').show();
    				
    				//alert(array.length);
	    			if (array.length==0) {
						alert("查无数据！");
					}
	    			if (array.length > 0) {
	    				//alert("have");
		    			$("#totalPage_input").val(totalPage);   
		    			$("#currentPage").html(currentPage);  
		    			$("#totalRows").html(data.totalRows);  
		    			$("#totalPage").html(totalPage);		    			
		    			
	    				for(var i=0;i<array.length;i++){  
		    				var td3 =$("<td class='' id='uName'>"+array[i].uName+"</td>");
							var td4 =$("<td class='' id='uId1'><input type='text' style='background-color:transparent;border:none;font-size:12px;width:100px;text-align:center;' class='uId' value='"+array[i].uId+"'/></td>"); 	
							var td5 =$("<td class='' id='position'>"+array[i].position+"</td>"); 	
		    				var td6 =$("<td class='' id='mobile'>"+array[i].mobile+"</td>"); 		    				 	    				    				
		    				var td8 =$("<td class=''>"+array[i].status+"</td>"); 
		    				tr=$("<tr class='userDetails' id='tr2'></tr>"); 
							tr.append(td3).append(td4).append(td5).append(td6).append(td8);
							$(".tab").append(tr); 
		    			} 
	    				$('.tdempty').hide();
		    			
	    			}				    				
	    		},      
	    		error: function (XMLHttpRequest, textStatus, errorThrown) {     
	    			alert(errorThrown);     
	    		}
			});
		});
		
</script>			

</body>
</html>
