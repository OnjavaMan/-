package com.zwq.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class checkLogfilter implements Filter{
	String errorPage="login.html";//跳转网站
	@Override
	public void destroy() {
		System.out.println("聊天登录过滤器销毁");
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest) arg0;
		HttpServletResponse resp=(HttpServletResponse) arg1;
		HttpSession session=req.getSession();
		
		if(session.getAttribute("currentLogin")==null){//说明当前没有用户登录
			String basePath=req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+req.getContextPath()+"/";
			resp.setContentType("text/html;charset=utf-8");

			PrintWriter out=resp.getWriter();
			out.print("<script>alert('请先登录....');location.href='"+basePath+errorPage+"';</script>");
			out.flush();
			out.close();
		}else{
			arg2.doFilter(req, resp);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("聊天过滤器开始初始化");
	}
}
