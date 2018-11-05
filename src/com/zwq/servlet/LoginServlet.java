package com.zwq.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zwq.baen.BackUser;
import com.zwq.baen.talkUserInfo;
import com.zwq.dao.BackUserDao;
import com.zwq.dao.UserInfoDao;

public class LoginServlet extends BaseServlet{
	protected void doPost(HttpServletRequest req,HttpServletResponse resp){
		String op=req.getParameter("op");

		if(op.equals("login")){//登录操作
			String uname=req.getParameter("uname");
			String pwd=req.getParameter("pwd");
			String logincode=req.getParameter("logincode");
			String code=(String) req.getSession().getAttribute("BackCode");
			UserInfoDao userDao=new UserInfoDao();
			String getpwd=null;

			if(code.equals(logincode)){//说明验证码正确 判断账号密码是否正确
				getpwd=userDao.findUserPwd(uname);
				if(getpwd.equals(pwd)){//账号密码匹配
					try {//登陆成功
						HttpSession session=req.getSession();
						List<String>  list=new ArrayList<String>();
						ServletContext application=req.getServletContext();

						session.setAttribute("currentLogin",uname);//设置当前用户
						if(application.getAttribute("userlist")!=null){//说明当前用户不是第一个登录
							list=(List<String>) application.getAttribute("userlist");
						}
						list.add(uname);
						application.setAttribute("userlist",list);
						this.out(resp,1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					try {
						this.out(resp,3);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else{
				try {
					this.out(resp,2);
				} catch (IOException e) {
					e.printStackTrace();
				}//说明验证码不正确
			}
		}else if(op.equals("reg")){//进行注册操作
			String uname=req.getParameter("uname");
			String pwd=req.getParameter("pwd");
			String sex=req.getParameter("sex");
			talkUserInfo userInfo=new talkUserInfo(uname,sex,pwd);
			UserInfoDao userDao=new UserInfoDao();

			try {
				this.out(resp,userDao.AddUser(userInfo));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(op.equals("backlogin")){//进行后台登录
			String uname=req.getParameter("uname");
			String pwd=req.getParameter("pwd");
			String logincode=req.getParameter("logincode");
			String code=(String) req.getSession().getAttribute("BacklogCode");
			BackUserDao backuserDao=new BackUserDao();
			String getpwd=null;

			if(code.equals(logincode)){//说明验证码正确 判断账号密码是否正确
				getpwd=backuserDao.findUserPwd(uname);
				if(getpwd.equals(pwd)){//账号密码匹配
					try {//登陆成功
						HttpSession session=req.getSession();
						List<String>  list=new ArrayList<String>();

						session.setAttribute("backcurrentLogin",uname);//设置当前用户

						this.out(resp,1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					try {
						this.out(resp,3);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else{//说明验证码不正确
				try {
					this.out(resp,2);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else if(op.equals("getusers")){
			UserInfoDao userDao=new UserInfoDao();
			try {
				this.out(resp,userDao.findAllUsers());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(op.equals("delUsers")){
			UserInfoDao userDao=new UserInfoDao();
			int id=Integer.valueOf(req.getParameter("usid"));
			try {
				this.out(resp,userDao.dropUser(id));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(op.equals("addmanager")){
			String uname=req.getParameter("uname");
			String pwd=req.getParameter("pwd");
			
			BackUserDao backDao=new BackUserDao();
			BackUser backUser=new BackUser(uname, pwd);
			try {
				this.out(resp,backDao.AddUser(backUser));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(op.equals("checkBacklog")){
			String backManager=(String) req.getSession().getAttribute("backcurrentLogin");
			try {
				this.out(resp,backManager);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
