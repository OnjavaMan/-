package com.zwq.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class doLoginServlet extends BaseServlet{
	protected void doPost(HttpServletRequest req,HttpServletResponse resp){
		String op=req.getParameter("op");
		
		
		if(op.equals("login")){//登录
			Map<String,Object> map=new HashMap<String,Object>();
			List<String> list=(List<String>) req.getServletContext().getAttribute("userlist");
			String currentLogin=(String) req.getSession().getAttribute("currentLogin");

			map.put("currentLogin", currentLogin);
			map.put("userlist",list);
			try {
				this.out(resp,map);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(op.equals("talk")){//说话
			String word=req.getParameter("word");
			String str="&nbsp;";
			ServletContext application=req.getServletContext();
			
			String uname=(String) req.getSession().getAttribute("currentLogin");
			SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:mm:ss");
			str+=uname+"说:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+sdf.format(new Date())+"<br/>&nbsp;"+word+"&nbsp;<hr/>";
			
			//将聊天信息存储起来
			List<String> words=new ArrayList<String>();
			if(application.getAttribute("words")!=null){
				words=(List<String>) application.getAttribute("words");
			}
			words.add(str);
			application.setAttribute("words",words);
			try {
				this.out(resp,words);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(op.equals("reload")){
			ServletContext application=req.getServletContext();
			List<String> words=new ArrayList<String>();
			List<String> users=new ArrayList<String>();
			Map<String,Object> map=new HashMap<String,Object>();
			
			if(application.getAttribute("words")!=null){//说明存在聊天语句
				words=(List<String>) application.getAttribute("words");
				map.put("words",words);
			}
			if(application.getAttribute("userlist")!=null){
				users=(List<String>) application.getAttribute("userlist");
				map.put("users",users);
			}
			try {
				this.out(resp,map);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}