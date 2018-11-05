package com.zwq.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zwq.baen.talkUserInfo;

public class UserInfoDao {
	/**
	 * 添加用户
	 * @param talkInfo
	 * @return
	 */
	public int AddUser(talkUserInfo talkInfo) {
		DBHelper db=new DBHelper();
		String pwd=findUserPwd(talkInfo.getUname());
		if(!"Nopwd".equals(pwd)){//说明账号存在
			return 2;
		}else{
			String sql="insert into talkRoomUserInfo values(seq_TalkRoomuserInfo_usid.nextval,?,?,?)";
			return db.update(sql,talkInfo.getUname(),talkInfo.getPwd(),talkInfo.getSex());
		}
	}

	/**
	 * 删除用户
	 * @param id
	 * @return
	 */
	public int dropUser(int id){
		DBHelper db=new DBHelper();
		String sql="delete from talkRoomUserInfo where usid=?";
		return db.update(sql,id);
	}
	
	/**
	 * 验证账号密码
	 * @param uname
	 * @return
	 */
	public String findUserPwd(String uname){
		DBHelper db=new DBHelper();
		String sql="select pwd from talkRoomUserInfo where uname=?";
		Map<String,String> map=new HashMap<>();
		map=db.find(sql,uname);
		String pwd="Nopwd";
		try {
			pwd=map.get("pwd");
		} catch (NullPointerException e) {
			System.out.println("账号不存在");
		}
		return pwd;
	}
	
	public List<Map<String,Object>> findAllUsers(){
		DBHelper db=new DBHelper();
		String sql="select * from talkRoomUserInfo";
		return db.finds(sql);
	}
}
