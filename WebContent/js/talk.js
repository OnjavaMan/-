var localuser;
window.onload=function(){
	$.post("dologinServlet",{op:"login"},function(data){
		var uname=data.currentLogin;
		localuser=uname;
		$("#right").html("&nbsp;欢迎您:"+uname+"<br /><hr />&nbsp;当前有 "+data.userlist.length+"个用户在线 <br /><hr />&nbsp;他们是:<hr/>");
		$(data.userlist).each(function(index,item){
			$("#right").append("&nbsp;"+item+"<hr />");
		});
	},"json");
	leftflash();
	rightflash();
}

function addContent(){//发送语句
	var word=$("#content").val();
	$.post("dologinServlet",{op:"talk",word:word},function(data){
		$("#left").html("");
		$(data).each(function(index,item){
			$("#left").append(item);
		});
	},"json");
}

function leftflash(){
	 setInterval(function(){
		 $.post("dologinServlet",{op:"reload"},function(data){
			 $("#left").html("");
			 //更新聊天区
				$(data.words).each(function(index,item){
					$("#left").append(item);
				});
		 });
		 
	 }, 1000);
}
function rightflash(){
	 setInterval(function(){
		 $.post("dologinServlet",{op:"reload"},function(data){
			$("#right").html("&nbsp;欢迎您:"+localuser+"<br /><hr />&nbsp;当前有 "+data.users.length+"个用户在线 <br /><hr />&nbsp;他们是:<hr/>");
			$(data.users).each(function(index,item){
				$("#right").append("&nbsp;"+item+"<hr />");
			});
		 });
	 },1000);
}