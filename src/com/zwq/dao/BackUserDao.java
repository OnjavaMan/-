package com.zwq.dao;

import java.util.HashMap;
import java.util.Map;

import com.zwq.baen.BackUser;
import com.zwq.baen.talkUserInfo;

public class BackUserDao {
	/**
	 * 添加后台用户
	 * @param talkInfo
	 * @return
	 */
	public int AddUser(BackUser backUser) {
		DBHelper db=new DBHelper();
		String sql="insert into BacktalkRoomUserInfo values(seq_BackTalkRoomuserInfo_usid.nextval,?,?)";
		return db.update(sql,backUser.getUname(),backUser.getPwd());
	}
	
	/**
	 * 删除后台用户
	 * @param id
	 * @return
	 */
	public int dropUser(int id){
		DBHelper db=new DBHelper();
		String sql="delete from BacktalkRoomUserInfo where usid=?";
		return db.update(sql,id);
	}
	
	/**
	 * 查找后台密码
	 * @param uname
	 * @return
	 */
	public String findUserPwd(String uname){
		DBHelper db=new DBHelper();
		String sql="select pwd from BacktalkRoomUserInfo where uname=?";
		Map<String,String> map=new HashMap<>();
		map=db.find(sql,uname);
		String pwd="Nopwd";
		try {
			pwd=map.get("pwd");
		} catch (NullPointerException e) {
			System.out.println("管理员账号不存在");
		}
		return pwd;
	}
}
