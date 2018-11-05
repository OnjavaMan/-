package com.zwq.baen;

public class talkUserInfo {
	private String Uname;
	private String sex;
	private String pwd;
	
	
	public talkUserInfo() {
		super();
	}
	public talkUserInfo(String uname, String sex, String pwd) {
		super();
		Uname = uname;
		this.sex = sex;
		this.pwd = pwd;
	}
	@Override
	public String toString() {
		return "talkUserInfo [Uname=" + Uname + ", sex=" + sex + ", pwd=" + pwd + "]";
	}
	public String getUname() {
		return Uname;
	}
	public void setUname(String uname) {
		Uname = uname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
