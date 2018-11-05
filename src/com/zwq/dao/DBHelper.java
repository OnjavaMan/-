package com.zwq.dao;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

//import com.yc.wowo.util.//LogUtil;

public class DBHelper {
	/**
	 * 获取数据库连接的方法---数据库连接池
	 * @return
	 */
	private Connection getConnection(){
		Connection con=null;
		try {
			Context ctx=new InitialContext();
			DataSource dataSource=(DataSource) ctx.lookup("java:comp/env/orcl");
			con=dataSource.getConnection();
		} catch (Exception e) {
			//LogUtil.log.error(e.toString());
			e.printStackTrace();
		}
		return con;
	}

	/**
	 * 给预编译语句中的占位符?赋值
	 * @param pstmt 要赋值的预编译块
	 * @param params 给占位符的值
	 * @throws SQLException 
	 */
	private void setParams(PreparedStatement pstmt,List<Object> params){
		if( params!=null && params.size()>0 ){
			for(int i=0,len=params.size();i<len;i++){
				try {
					pstmt.setObject(i+1,params.get(i));
				} catch (SQLException e) {
					//LogUtil.log.error( "第 "+(i+1)+" 个注值失败..."+ e.getMessage() +"\r\n"+e.toString());
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 给预编译语句中的占位符?赋值
	 * @param pstmt 要赋值的预编译块
	 * @param params 给占位符的值
	 * @throws SQLException 
	 */
	private void setParams(PreparedStatement pstmt,Object ... params){
		if( params!=null && params.length>0 ){
			for(int i=0,len=params.length;i<len;i++){
				try {
					pstmt.setObject(i+1,params[i]);
				} catch (SQLException e) {
					//LogUtil.log.error( "第 "+(i+1)+" 个注值失败..."+ e.getMessage() +"\r\n"+e.toString());
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 关闭资源的方法
	 * @param rs 要关闭的结果集
	 * @param pstmt 要关闭的预编译块
	 * @param con 要关闭的连接
	 */
	private void close(ResultSet rs,PreparedStatement pstmt,Connection con){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				//LogUtil.log.error(e.toString());
				e.printStackTrace();
			}
		}

		if(pstmt!=null){
			try {
				pstmt.close();
			} catch (SQLException e) {
				//LogUtil.log.error(e.toString());
				e.printStackTrace();
			}
		}

		if(con!=null){
			try {
				con.close();
			} catch (SQLException e) {
				//LogUtil.log.error(e.toString());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭资源的方法
	 * @param pstmt 要关闭的预编译块
	 * @param con 要关闭的连接
	 */
	private void close(PreparedStatement pstmt,Connection con){
		if(pstmt!=null){
			try {
				pstmt.close();
			} catch (SQLException e) {
				//LogUtil.log.error(e.toString());
				e.printStackTrace();
			}
		}

		if(con!=null){
			try {
				con.close();
			} catch (SQLException e) {
				//LogUtil.log.error(e.toString());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 更新数据的方法
	 * @param sql 要执行的sql语句
	 * @param params 要执行的sql语句中对应占位符?的值，在添加是必须按照sql语句中?的顺序给定
	 * @return 语句执行后，影响的数据的行数
	 */
	public int update(String sql,List<Object> params){
		int result=0;

		Connection con=null;
		PreparedStatement pstmt=null;

		try {
			con=this.getConnection(); //获取数据库的连接

			pstmt=con.prepareStatement(sql);//预编译执行语句

			//给预编译sql语句中的占位符赋值
			setParams(pstmt, params);

			//执行更新并获取结果
			result=pstmt.executeUpdate();

		} catch (SQLException e) {
			//LogUtil.log.error(e.toString());
			e.printStackTrace();
		} finally{
			close(pstmt,con);
		}
		return result;
	}

	/**
	 * 带事务更新数据的方法
	 * @param sqld 要执行的sql语句的集合
	 * @param paramss 要执行的sql语句中对应占位符?的值，在添加是必须按照sql语句中?的顺序给定
	 * @return 语句执行后，影响的数据的行数
	 */
	public int update(List<String> sqls,List<List<Object>> paramss){
		int result=0;
		Connection con=null;
		PreparedStatement pstmt=null;
		try {
			con=this.getConnection(); //获取数据库的连接
			con.setAutoCommit(false);//将自动提交事务改成false
			if(sqls!=null && sqls.size()>0){
				int index=0;
				for(String sql:sqls){ //循环执行每一条sql语句
					pstmt=con.prepareStatement(sql);//预编译执行语句
					//给预编译sql语句中的占位符赋值
					setParams(pstmt,paramss.get(index++));
					//执行更新并获取结果
					result=pstmt.executeUpdate();
				}
			}
			con.commit(); //如果所有语句都执行完成，且没有出现异常，则提交
		} catch (SQLException e) {
			try {
				con.rollback();//一旦出错，所有数据回滚。即当前所有的执行全部回退撤销。
			} catch (SQLException e1) {
				//LogUtil.log.error(e1.toString());
				e1.printStackTrace();
			} 
			e.printStackTrace();
		} finally{
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				//LogUtil.log.error(e.toString());
				e.printStackTrace();
			}
			close(pstmt,con);
		}
		return result;
	}


	/**
	 * 更新数据的方法
	 * @param sql 要执行的sql语句
	 * @param params 要执行的sql语句中对应占位符?的值，在添加是必须按照sql语句中?的顺序给定
	 * @return 语句执行后，影响的数据的行数
	 */
	public int update(String sql,Object ... params){
		int result=0;

		Connection con=null;
		PreparedStatement pstmt=null;

		try {
			con=this.getConnection(); //获取数据库的连接

			pstmt=con.prepareStatement(sql);//预编译执行语句

			//给预编译sql语句中的占位符赋值
			setParams(pstmt, params);

			//执行更新并获取结果
			result=pstmt.executeUpdate();

		} catch (SQLException e) {
			//LogUtil.log.error(e.toString());
			e.printStackTrace();
		} finally{
			close(pstmt,con);
		}
		return result;
	}

	/**
	 * 根据结果集对象获取这个结果集中每个列的列名，并以数组的方式返回
	 * @param rs 要获取每个列列名的结果集对象
	 * @return 存放了这个结果集中所有列的列名的一个字符串数组
	 * @throws SQLException 
	 */
	public String[] getColumnName(ResultSet rs) throws SQLException{
		ResultSetMetaData rsmd=rs.getMetaData();
		int len=rsmd.getColumnCount(); //获取给定结果集中列的数量
		String[] colNames=new String[len];

		//循环取出每个列的列名存放到colNames数组中
		for(int i=0;i<len;i++){
			colNames[i]=rsmd.getColumnName(i+1).toLowerCase(); //将每个列的列名全部转为小写字母
		}
		return colNames;
	}

	/**
	 * 查询
	 * @param sql 要执行的查询语句
	 * @param params 查询语句中对应?的值
	 * @return 所有数据存放在一个list中，每一行数据存放在一个map中
	 */
	public List<Map<String,Object>> finds(String sql,List<Object> params){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;

		try {
			con=getConnection(); //获取连接
			pstmt=con.prepareStatement(sql); //预编译查询语句
			setParams(pstmt, params); //给预编译语句中的占位符赋值
			rs=pstmt.executeQuery(); //执行查询语句并获取结果集
			Map<String,Object> map=null;
			String[] colNames=getColumnName(rs); //获取结果集中每个列的列名

			String type=null;
			while(rs.next()){  //处理结果
				//每循环一次就是一行数据，我们需要将这一行数据的每一个列中的值存放到一个map中，以列名为键，以这一列的值为值、
				map=new HashMap<String,Object>();
				//循环取出每一个列，通过列名
				for(String colName:colNames){
					Object obj=rs.getObject(colName);
					if(obj!=null){
						type=obj.getClass().getSimpleName();
						if("BLOB".equals(type)){
							Blob blob=rs.getBlob(colName);
							BufferedInputStream bis=null;
							byte[] bt=null;
							try {
								bis=new BufferedInputStream( blob.getBinaryStream());
								bt=new byte[(int) blob.length()];
								bis.read(bt);
							} catch (IOException e) {
								e.printStackTrace();
							} finally{
								if(bis!=null){
									try {
										bis.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
							map.put(colName,bt);
						}else{
							map.put(colName,obj);
						}
					}else{
						map.put(colName, obj );
					}
				}

				//当所有列已经循环取完后，说明这一条数据已经取完了，那么我们将这一条数据存放到list中
				list.add(map);
				map=null;
			}
		} catch (SQLException e) {
			//LogUtil.log.error(e.toString());
			e.printStackTrace();
		} finally{
			close(rs, pstmt, con);
		}
		return list;
	}

	/**
	 * 针对返回数据只有一行的情况
	 * @param sql
	 * @param params
	 * @return
	 */
	public Map<String,String> find(String sql,List<Object> params){
		Map<String,String> map=null;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			con=getConnection(); //获取连接
			pstmt=con.prepareStatement(sql); //预编译查询语句
			setParams(pstmt, params); //给预编译语句中的占位符赋值
			rs=pstmt.executeQuery(); //执行查询语句并获取结果集
			String[] colNames=getColumnName(rs); //获取结果集中每个列的列名
			if(rs.next()){  //处理结果
				//每循环一次就是一行数据，我们需要将这一行数据的每一个列中的值存放到一个map中，以列名为键，以这一列的值为值、
				map=new HashMap<String,String>();

				//循环取出每一个列，通过列名
				for(String colName:colNames){
					map.put(colName, rs.getString(colName));
				}
			}
		} catch (SQLException e) {
			//LogUtil.log.error(e.toString());
			e.printStackTrace();
		} finally{
			close(rs, pstmt, con);
		}
		return map;
	}

	/**
	 * 查询
	 * @param sql 要执行的查询语句
	 * @param params 查询语句中对应?的值
	 * @return 所有数据存放在一个list中，每一行数据存放在一个map中
	 */
	public List<Map<String,Object>> finds(String sql,Object ... params){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;

		try {
			con=getConnection(); //获取连接
			pstmt=con.prepareStatement(sql); //预编译查询语句
			setParams(pstmt, params); //给预编译语句中的占位符赋值
			rs=pstmt.executeQuery(); //执行查询语句并获取结果集
			Map<String,Object> map=null;
			String[] colNames=getColumnName(rs); //获取结果集中每个列的列名

			String type=null;
			while(rs.next()){  //处理结果
				//每循环一次就是一行数据，我们需要将这一行数据的每一个列中的值存放到一个map中，以列名为键，以这一列的值为值、
				map=new HashMap<String,Object>();
				//循环取出每一个列，通过列名
				for(String colName:colNames){
					Object obj=rs.getObject(colName);
					if(obj!=null){
						type=obj.getClass().getSimpleName();
						if("BLOB".equals(type)){
							Blob blob=rs.getBlob(colName);
							BufferedInputStream bis=null;
							byte[] bt=null;
							try {
								bis=new BufferedInputStream( blob.getBinaryStream());
								bt=new byte[(int) blob.length()];
								bis.read(bt);
							} catch (IOException e) {
								e.printStackTrace();
							} finally{
								if(bis!=null){
									try {
										bis.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
							map.put(colName,bt);
						}else{
							map.put(colName,obj);
						}
					}else{
						map.put(colName, obj );
					}
				}

				//当所有列已经循环取完后，说明这一条数据已经取完了，那么我们将这一条数据存放到list中
				list.add(map);
				map=null;
			}
		} catch (SQLException e) {
			//LogUtil.log.error(e.toString());
			e.printStackTrace();
		} finally{
			close(rs, pstmt, con);
		}
		return list;
	}

	/**
	 * 针对返回数据只有一行的情况
	 * @param sql
	 * @param params
	 * @return
	 */
	public Map<String,String> find(String sql,Object ... params){
		Map<String,String> map=null;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			con=getConnection(); //获取连接
			pstmt=con.prepareStatement(sql); //预编译查询语句
			setParams(pstmt, params); //给预编译语句中的占位符赋值
			rs=pstmt.executeQuery(); //执行查询语句并获取结果集
			String[] colNames=getColumnName(rs); //获取结果集中每个列的列名
			if(rs.next()){  //处理结果
				//每循环一次就是一行数据，我们需要将这一行数据的每一个列中的值存放到一个map中，以列名为键，以这一列的值为值、
				map=new HashMap<String,String>();

				//循环取出每一个列，通过列名
				for(String colName:colNames){
					map.put(colName, rs.getString(colName));
				}
			}
		} catch (SQLException e) {
			//LogUtil.log.error(e.toString());
			e.printStackTrace();
		} finally{
			close(rs, pstmt, con);
		}
		return map;
	}
	
	public List<Map<String,String>> findStr(String sql,List<Object> params)
	{
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		con=getConnection();//获取链接
		try {
			pstmt=con.prepareStatement(sql);//预编译查询语句
			setParams(pstmt, params);//给预编译中的占位符赋值
			rs=pstmt.executeQuery();//执行查询语句获取结果集
			Map<String,String> map=null;
			String[] colNames=getColumnName(rs);//获取结果集中每个列的列名
			while(rs.next())//处理结果
			{
				//每循环一次就是一行数据，把每一行数据中的每一列的数据存放到一个map中，以列为建，以列值为值
				map=new HashMap<String,String>();
				//循环取出每一列通过里列名
				for(String colName:colNames)
				{
					map.put(colName,rs.getString(colName));
				}
				//当所有的列循环完后将这一行数据存放到list中
				list.add(map);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 查询对象
	 * @param sql 要执行的sql语句
	 * @param c 查询的对象的类型
	 * @param params 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findObj(String sql,Class<?> c,Object ... params){
		List<T> list=new ArrayList<T>();

		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;

		try {
			con=this.getConnection();//获取连接

			pstmt=con.prepareStatement(sql); //预编译语句
			this.setParams(pstmt, params); //给预编译语句中的占位符赋值
			rs=pstmt.executeQuery();//执行语句获取结果集

			//获取结果集中每个列的列名
			String[] colNames=this.getColumnName(rs);

			List<Method> methods=this.getMethods(c);
			
			T t=null; //要注入属性的对象
			
			Object obj=null; //从数据库获取到的当前列的值
			String setName=null;
			String classType=null;
			
			while(rs.next()){//每循环一次就是一个对象
				t=(T) c.newInstance(); //调用这个类的无参构造方法，实例化一个对象
				for(String colName:colNames){ //根据列名找到对象中对应的setter方法，将值注入到对应的属性中->先必须获取对该类中的所有setter方法
					setName="set"+colName;
					
					//获取当前列的值的类型
					obj=rs.getObject(colName); //根据列名获取对应列的值
					for(Method method:methods){
						if(setName.equalsIgnoreCase(method.getName())){
							//反向激活这个方法     obj.setXXX(arg)
							//第一个参数是哪个对象的这个方法，第二个参数是一个不定参数，指定是这个方法需要的参数
							//因为java中有方法重载，如果同名的方法会很多，虚拟机需要根据参数类型来确定调用具体的方法
							//这里就要求我们在写实体类和数据库表的时候，要求实体类中属性的类型与对应表中列的类型对应
							
							if(obj!=null){ //如果不为空，取出该对象的类类型
								classType=obj.getClass().getSimpleName();
								
								if("BigDecimal".equals(classType)){ //说明这个值是一个数字类型
									//数字类型包括Integer Double Float
									try {
										method.invoke(t,rs.getDouble(colName));
									} catch (Exception e) {
										method.invoke(t,rs.getInt(colName));
									}
								}else if("String".equals(classType)){ //说明是一个varchar
									method.invoke(t,rs.getString(colName));
								}else{
									method.invoke(t,rs.getString(colName));
								}
							}
						}
					}
				}
				list.add(t);
			}
		} catch (Exception e) {
			//LogUtil.log.error(e.toString());
			e.printStackTrace();
		} finally{
			this.close(rs, pstmt, con);
		}

		return list;
	}

	/**
	 * 查询对象
	 * @param sql 要执行的sql语句
	 * @param c 查询的对象的类型
	 * @param params 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findObj(String sql,Class<?> c,List<Object> params){
		List<T> list=new ArrayList<T>();

		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;

		try {
			con=this.getConnection();//获取连接

			pstmt=con.prepareStatement(sql); //预编译语句
			this.setParams(pstmt, params); //给预编译语句中的占位符赋值
			rs=pstmt.executeQuery();//执行语句获取结果集

			//获取结果集中每个列的列名
			String[] colNames=this.getColumnName(rs);

			List<Method> methods=this.getMethods(c);
			
			T t=null; //要注入属性的对象
			
			Object obj=null; //从数据库获取到的当前列的值
			String setName=null;
			String classType=null;
			
			while(rs.next()){//每循环一次就是一个对象
				t=(T) c.newInstance(); //调用这个类的无参构造方法，实例化一个对象
				for(String colName:colNames){ //根据列名找到对象中对应的setter方法，将值注入到对应的属性中->先必须获取对该类中的所有setter方法
					setName="set"+colName;
					
					//获取当前列的值的类型
					obj=rs.getObject(colName); //根据列名获取对应列的值
					for(Method method:methods){
						if(setName.equalsIgnoreCase(method.getName())){
							//反向激活这个方法     obj.setXXX(arg)
							//第一个参数是哪个对象的这个方法，第二个参数是一个不定参数，指定是这个方法需要的参数
							//因为java中有方法重载，如果同名的方法会很多，虚拟机需要根据参数类型来确定调用具体的方法
							//这里就要求我们在写实体类和数据库表的时候，要求实体类中属性的类型与对应表中列的类型对应
							
							if(obj!=null){ //如果不为空，取出该对象的类类型
								classType=obj.getClass().getSimpleName();
								
								if("BigDecimal".equals(classType)){ //说明这个值是一个数字类型
									//数字类型包括Integer Double Float
									try {
										method.invoke(t,rs.getDouble(colName));
									} catch (Exception e) {
										method.invoke(t,rs.getInt(colName));
									}
								}else if("String".equals(classType)){ //说明是一个varchar
									method.invoke(t,rs.getString(colName));
								}else{
									method.invoke(t,rs.getString(colName));
								}
							}
						}
					}
				}
				list.add(t);
			}
		} catch (Exception e) {
			//LogUtil.log.error(e.toString());
			e.printStackTrace();
		} finally{
			this.close(rs, pstmt, con);
		}

		return list;
	}
	
	/**
	 * 获取给定类中的所有公有的setter方法
	 * @param c
	 * @return
	 */
	public List<Method> getMethods(Class<?> c){
		Method[] methods=c.getMethods();//获取该类中的所有公有方法

		//获取setter方法
		List<Method> setMethods=null;
		if(methods!=null){
			setMethods=new ArrayList<Method>();
			for(Method md:methods){
				if(md.getName().startsWith("set")){
					setMethods.add(md);
				}
			}
		}
		return setMethods;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findObjs(String sql,Class<?> c,List<Object> params){
		List<T> list=new ArrayList<T>();

		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;

		try {
			con=this.getConnection();//获取连接

			pstmt=con.prepareStatement(sql); //预编译语句
			this.setParams(pstmt, params); //给预编译语句中的占位符赋值
			rs=pstmt.executeQuery();//执行语句获取结果集

			//获取结果集中每个列的列名
			String[] colNames=this.getColumnName(rs);

			List<Method> methods=this.getMethods(c);
			
			T t=null; //要注入属性的对象
			
			Object obj=null; //从数据库获取到的当前列的值
			String setName=null;
			String classType=null;
			
			while(rs.next()){//每循环一次就是一个对象
				t=(T) c.newInstance(); //调用这个类的无参构造方法，实例化一个对象
				for(String colName:colNames){ //根据列名找到对象中对应的setter方法，将值注入到对应的属性中->先必须获取对该类中的所有setter方法
					setName="set"+colName;
					
					//获取当前列的值的类型
					obj=rs.getObject(colName); //根据列名获取对应列的值
					for(Method method:methods){
						if(setName.equalsIgnoreCase(method.getName())){
							//反向激活这个方法     obj.setXXX(arg)
							//第一个参数是哪个对象的这个方法，第二个参数是一个不定参数，指定是这个方法需要的参数
							//因为java中有方法重载，如果同名的方法会很多，虚拟机需要根据参数类型来确定调用具体的方法
							//这里就要求我们在写实体类和数据库表的时候，要求实体类中属性的类型与对应表中列的类型对应
							
							if(obj!=null){ //如果不为空，取出该对象的类类型
								classType=obj.getClass().getSimpleName();
								
								if("BigDecimal".equals(classType)){ //说明这个值是一个数字类型
									//数字类型包括Integer Double Float
									try {
										method.invoke(t,rs.getDouble(colName));
									} catch (Exception e) {
										method.invoke(t,rs.getInt(colName));
									}
								}else if("String".equals(classType)){ //说明是一个varchar
									method.invoke(t,rs.getString(colName));
								}else{
									method.invoke(t,rs.getString(colName));
								}
							}
						}
					}
				}
				list.add(t);
			}
		} catch (Exception e) {
			//LogUtil.log.error(e.toString());
			e.printStackTrace();
		} finally{
			this.close(rs, pstmt, con);
		}
		return list;
	}
}
