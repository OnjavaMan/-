--创建用户表
create table talkRoomUserInfo(
	usid number(5) primary key,
	uname varchar2(50) not null unique,
	pwd varchar2(50) not null,
	sex varchar2(3) check ( sex='男' or sex='女')
);
create sequence seq_TalkRoomuserInfo_usid start with 1000;

insert into talkRoomUserInfo values(seq_TalkRoomuserInfo_usid.nextval,'zwq','123','男');

drop table talkRoomUserInfo
select * from talkRoomUserInfo;

--创建后台表
create table BacktalkRoomUserInfo(
	usid number(5) primary key,
	uname varchar2(50) not null unique,
	pwd varchar2(50) not null
);

create sequence seq_BackTalkRoomuserInfo_usid start with 1000;
select * from BacktalkRoomUserInfo;
insert into BacktalkRoomUserInfo values(seq_BackTalkRoomuserInfo_usid.nextval,'zwq','123');
