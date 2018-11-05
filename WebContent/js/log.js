function changeWindow(id){
	var d1="LoginWindow";
	var d2="RegWindow";
	
	if(id==d1){//切换到登陆窗口
		$("#"+d2).css("display","none");
		$("#"+d1).css("display","block");
	}else{//切换到注册窗口
		$("#"+d1).css("display","none");
		$("#"+d2).css("display","block");
	}
}

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
	$.post("loginServlet",{op:"login",uname:uname,pwd:pwd,logincode:logincode},function(data){
		if(data==1){//登陆成功
			//alert("成功");
			$.messager.show({
				title:'消息提示',
				msg:'登陆成功三秒后进入登录界面',
				timeout:3000,
				showType:'slide'
			});
			setTimeout(function(){location.href="talk.html"},3000);
		}else if(data==2){//验证码不正确
			$.messager.alert("消息提示","验证码错误");
		}else if(data==3){//登录失败
			$.messager.alert("消息提示","登录失败");
		}
	},"text");
}

function Reg(){
	var uname=$("#Reg_name").val();
	var pwd=$("#Reg_pwd").val();
	var sex=$("#Reg_sex").val();
	
	if(uname==""||pwd==""||sex==""||sex=="---请选择---"){
		$.messager.alert("消息提示","信息不输入不完整");
		return;
	}
	$.post("loginServlet",{op:"reg",uname:uname,pwd:pwd,sex:sex},function(data){
		data=parseInt($.trim(data));
		alert(data);
		if(data==1){//注册成功
			$.messager.show({
				title:'消息提示',
				msg:'注册成功三秒后进入聊天界面',
				timeout:3000,
				showType:'slide'
			});
		}else if(data==2){
			$.messager.alert("消息提示","该用户名已经存在");
		}else{
			$.messager.alert("消息提示","注册失败...");
		}
	},"text");
}