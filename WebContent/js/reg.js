window.onload=function(){
	$("#d1").dialog({
		title:"注册信息",
		modal:true,
		closable:true,
		shadow:true,
		buttons:[{
			text:'登陆',
			iconCls:'icon-edit',
			handler:function(){
				Log();
			}
		},{
			text:'注册',
			iconCls:'icon-help',
			handler:function(){
				Reg();
			}
		}]})
	};
function Reg(){
		 var username=$("#username").val();
		 var password=$("#password").val();
		 //alert(username+""+password);
		 $.post("regServlet",{op:"input",username:username,password:password},
			function(data){
			 data=$.trim(data);
			 if(data==1){
				 alert("注册成功");
				 }else if(data==2){
					 $.messager.show({
							title:"消息提示！",
							msg:'该账号已经注册，请直接登陆...',
							timeout:5000,
							showType:'slide',
							height:200
						});
				 }else{
					 $.messager.show({
							title:"消息提示！",
							msg:'注册失败',
							timeout:5000,
							showType:'slide',
							height:200
						});
				 }
		 },"text");
	 }
 function Log(){
	 var username=$("#username").val();
	 var password=$("#password").val();
	 $.post("regServlet",{op:"find",username:username,password:password},
				function(data){
				 data=$.trim(data);
				 if(data==1)
					 {
					 location.href="back/index.jsp";
					 }else if(data==2){
						 $.messager.alert('信息提示','密码错误');   
					 }else if(data==0){
						 $.messager.alert('信息提示','该账号不存在，请先注册....');   
					 }
			 },"text");
 }