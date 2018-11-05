window.onload=function(){
	$.post("../../loginServlet",{op:"checkBacklog"},function(data){
		if(data==null){
			alert("请先登录...");
			location.href="backlogin.html";
		}else{
			$.post("../../loginServlet",{op:"getusers"},function(data){
				$(data).each(function(index,item){
					$("#usersInfo").append("<tr id='user_'"+item.usid+"><td>"+(index+1)+"</td><td>"+item.usid+"</td><td>"+item.uname+"</td><td>"+item.sex+"</td><td><a href='javascript:delUser("+item.usid+")'>删除</a></td></tr>");
				});
			},"json");
		}
	});
	
}

function delUser(usid){//删除用户
	$.post("../../loginServlet",{op:"delUsers",usid:usid},function(data){
		if(data==1){
			alert("删除成功");
			window.location.reload();
		}else{
			alert("删除失败");
		}
	});
}

function addManager(){//添加管理员
	var uname=$("#managerName").val();
	var pwd=$("#managerPwd").val();
	
	if(uname==""||pwd==""){
		alert("信息输入不完整");
		return;
	}
	$.post("../../loginServlet",{op:"addmanager",uname:uname,pwd:pwd},function(data){
		if(data==1){
			alert("添加成功");
			$("#managerName").val("");
			$("#managerPwd").val("");
		}else{
			alert("添加失败");
		}
	});
}