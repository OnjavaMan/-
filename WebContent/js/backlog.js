function clearForm(id){
	$("#"+id).form("clear");
}

function Login(){
	var uname=$("#login_name").val();
	var pwd=$("#login_pwd").val();
	var logincode=$("#login_code").val();
	
	if(uname==""||pwd==""||logincode==""){
		$.messager.alert("消息提示","信息不输入不完整");
		return;
	}
	$.post("../../loginServlet",{op:"backlogin",uname:uname,pwd:pwd,logincode:logincode},function(data){
		if(data==1){//登陆成功
			//alert("成功");
			$.messager.show({
				title:'消息提示',
				msg:'登陆成功',
				timeout:1000,
				showType:'slide'
			});
			setTimeout(function(){location.href="manager.html"},1000);
		}else if(data==2){//验证码不正确
			$.messager.alert("消息提示","验证码错误");
		}else if(data==3){//登录失败
			$.messager.alert("消息提示","登录失败");
		}
	},"text");
}