package com.zwq.baen;

public class BackUser {
	private String uname;
	private String pwd;
	
	public BackUser() {
		super();
	}
	public BackUser(String uname, String pwd) {
		super();
		this.uname = uname;
		this.pwd = pwd;
	}
	@Override
	public String toString() {
		return "BackUser [uname=" + uname + ", pwd=" + pwd + "]";
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	
}
