package cn.com.server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import cn.com.util.JdbcUtil;
import cn.com.util.socketUtil;
public class Handler implements Runnable{
	public static final int SOLDERCONNECTION=0;//售货员登录
	public static final int MANAGERCONNECTION=1;//管理员登录
	public static final int SEEKDAYCONDITION=2;//查看一天售货信息
	
	public static final int MODIFYMANAGERPASSWORD=4;//修改管理员密码
	public static final int MODIFYSOLDERPASSWORD=5;//修改售货员密码
	public static final int SOLDERWORK=6;//售货员出售商品
	
	public static final int MANAGERADDSOLDER=7;//管理员添加售货员
	public static final int MANAGERDELETESOLDER=8;//管理员删除售货员
	public static final int MANAGERMOIDFYSOLDER=9;//管理员修改售货员
	public static final int MANAGERADDGOODS=10;//管理员添加物品
	public static final int MANAGERDELETEGOODS=11;//管理员删除物品
	public static final int MANAGERMODIFYGOODS=12;//管理员修改物品
	private Socket socket;	
	private BufferedReader bufIn=null;
	private BufferedWriter bufOut=null;		
	private Connection connection=null;	
	private PreparedStatement preparedstatement=null;
	private ResultSet resultset=null;
	public Handler(Socket socket) throws IOException
	{
		this.socket=socket;
		bufIn=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bufOut=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));	
	}		
	
	public void run() 
	{
		try
		{																
				String line=bufIn.readLine();
				//读取第一个选着功能	
				int i=Integer.valueOf(line);				
				switch(i)			
				{
						case SOLDERCONNECTION:
							solderConnection();//售货员登录0
							break;
						case  MANAGERCONNECTION:
							managerConnection();//管理员登录1
							break;		
						case SEEKDAYCONDITION:
							seekDayCondition();//管理员查看超市某一天销售情况2
							break;	
						case  MODIFYMANAGERPASSWORD:
							modifyMangerPassword();//管理员修改密码	4
							break;						
						case MODIFYSOLDERPASSWORD:
							modifySolderPassword();//售货员修改密码	5
							break;						
						case SOLDERWORK:
							solderWork();//售货员录入商品	6	
							break;		
						case MANAGERADDSOLDER:
							managerAddSolder();//管理员添加售货员7
							break;
						case MANAGERDELETESOLDER:
							managerDeleteSolder();//管理员删除售货员8
							break;
						case  MANAGERMOIDFYSOLDER:
							managerModifySolder();//管理员修改售货员9
							break;
						case MANAGERADDGOODS:
							managerAddGoods();//管理员添加商品10
							break;
						case MANAGERDELETEGOODS:
							managerDeleteGoods();//管理员删除商品11
							break;
						case MANAGERMODIFYGOODS:
							managerModifyGoods();	//管理员修改商品12
							break;					
				}
		}catch(Exception e)
			{
			socketUtil.close(socket);
			}
		}			
	public void solderConnection() throws Exception
	{
		String sr=null;	
		//读取账号和密码
		String arr[] =bufIn.readLine().split("#");;			
		
		connection=JdbcUtil.getConnection();
		preparedstatement=connection.prepareStatement("SELECT * FROM solder WHERE solder_number=? and solder_password=?");
		preparedstatement.setString(1,arr[0]);
		preparedstatement.setString(2,arr[1]);		
		resultset=preparedstatement.executeQuery();			
		if(resultset.next()){	
			bufOut.write("ok\n");//回馈查询信心
			bufOut.flush();						
			sr=resultset.getInt(1)+"#"+resultset.getString(2)+"#"+resultset.getString(3)+"\n";
			bufOut.write(sr);
			bufOut.flush();			
			}
			else
			{
				bufOut.write("no");//回馈查询信心
				bufOut.flush();		
			}
			JdbcUtil.release(resultset, preparedstatement, connection);
		}	
		public void managerConnection() throws Exception
		{
			String sr=null;	
			String arr[] = null;		
			//读取账号和密码
			arr=bufIn.readLine().split("#");
			
			connection=JdbcUtil.getConnection();
			preparedstatement=connection.prepareStatement("SELECT * FROM manager WHERE  manager_number=? and manager_password=?");	
			preparedstatement.setString(1,arr[0]);
			preparedstatement.setString(2,arr[1]);			
			resultset=preparedstatement.executeQuery();	
			
			if(resultset.next()){	
				bufOut.write("ok\n");//回馈查询信心
				bufOut.flush();	
				sr=resultset.getString(1)+"#"+resultset.getString(2)+"#"+resultset.getString(3)+"\n";
				
				bufOut.write(sr);
				bufOut.flush();
			}
			else
			{
				bufOut.write("no");//回馈查询信心
				bufOut.flush();
			}
			JdbcUtil.release(resultset, preparedstatement, connection);
		}			
		public void solderWork() throws Exception
		{
			connection=JdbcUtil.getConnection();	
			double price=0;
			String name=null;
			int number=Integer.valueOf(bufIn.readLine());
	
			preparedstatement=connection.prepareStatement("select *from goods WHERE goods_number=? ");
			preparedstatement.setInt(1,number);	
			
			resultset=preparedstatement.executeQuery();	
			
			if(resultset.next())
			{
				if(Integer.valueOf(resultset.getString(3))>0)
				{
					bufOut.write("ok\n");//回馈查询信心
					bufOut.flush();
				
					price=resultset.getDouble(4);
					name=resultset.getString(2);
					String str=resultset.getInt(1)+"#"+resultset.getString(2)+"#"+resultset.getString(3)+"#"+resultset.getDouble(4);	
					bufOut.write(str+"\n");
					bufOut.flush();
				
					synchronized (Handler.class) 
					{
					
						preparedstatement=connection.prepareStatement("update goods set store_amount=store_amount-1 WHERE goods_number=? ");
						preparedstatement.setInt(1,number);	
						preparedstatement.executeUpdate();	
				
						java.sql.Date a=new java.sql.Date(new Date().getTime());
				
						preparedstatement=connection.prepareStatement("select *from goodssold WHERE goods_number=? and sold_time=?");
						preparedstatement.setInt(1,number);	
						preparedstatement.setDate(2,a);
						resultset=preparedstatement.executeQuery();	
				
						if(resultset.next())
						{
							preparedstatement=connection.prepareStatement("update goodssold set sold_amount=sold_amount+1 WHERE goods_number=?  ");
							preparedstatement.setInt(1,number);	
							preparedstatement.executeUpdate();
						}
						else
						{
							preparedstatement=connection.prepareStatement("insert into  goodssold values(?,?,?,?,?) ");
							preparedstatement.setInt(1,number);
							preparedstatement.setString(2,name);
							preparedstatement.setInt(3,1);
							preparedstatement.setDouble(4,price);
							preparedstatement.setDate(5,a);
				
							preparedstatement.executeUpdate();
						}		
					}
					 
				}
				else
				{
					bufOut.write("no\n");//回馈查询信心
					bufOut.flush();			
				}
			}
			else
			{
				bufOut.write("no\n");//回馈查询信心
				bufOut.flush();
			}
			
			JdbcUtil.release(resultset, preparedstatement, connection);
		}		
		public void managerAddSolder() throws Exception
		{
			connection=JdbcUtil.getConnection();				
			String name=bufIn.readLine();	
			System.out.println(name);
			preparedstatement=connection.prepareStatement("insert into solder(solder_name) values(?) ");
			preparedstatement.setString(1,name);	
			preparedstatement.executeUpdate();//执行更新							
			preparedstatement=connection.prepareStatement("select *from solder where solder_number=(select MAX(solder_number)from solder) ");
			resultset=preparedstatement.executeQuery();//执行更新		
			if(resultset.next())
			{
				bufOut.write("ok\n");//回馈查询信心
				bufOut.flush();
				String str=resultset.getString(1)+"#"+resultset.getString(2)+"#"+resultset.getString(3);
				bufOut.write(str+"\n");//回馈查询信心
				bufOut.flush();	
			}
			else
			{
				bufOut.write("no\n");//回馈查询信心
				bufOut.flush();
			}	
			
			JdbcUtil.release(resultset, preparedstatement, connection);
		}
		
		public void managerModifySolder() throws Exception
		{
			connection=JdbcUtil.getConnection();
			String arr[] = null;
			String line=null;
			
			
			String number=bufIn.readLine();
			preparedstatement=connection.prepareStatement("select *from solder  WHERE solder_number=? ");
			preparedstatement.setString(1,number);	
			resultset=preparedstatement.executeQuery();
			
			if(resultset.next())
			{
				bufOut.write("ok\n");//回馈查询信心
				bufOut.flush();	
				
				line=bufIn.readLine();
				arr=line.split("#");
				preparedstatement=connection.prepareStatement("update  solder set solder_password=?,solder_name=? WHERE solder_number=? ");
				preparedstatement.setString(1,arr[2]);	
				preparedstatement.setString(2,arr[1]);	
				preparedstatement.setString(3,arr[0]);	
				preparedstatement.executeUpdate();//执行更新		
			}
			else
			{
				bufOut.write("no\n");//回馈查询信心
				bufOut.flush();					
			}	
			socket.close();
			JdbcUtil.release(resultset, preparedstatement, connection);
		}
		public void managerSeekSolder() throws Exception
		{
			connection=JdbcUtil.getConnection();	
			
			String number=bufIn.readLine();
			preparedstatement=connection.prepareStatement("select *from solder  WHERE solder_number=? ");
			preparedstatement.setString(1,number);	
			resultset=preparedstatement.executeQuery();	
			
			if(resultset.next())
			{
				bufOut.write("ok\n");//回馈查询信心
				bufOut.flush();		
				
				String str=resultset.getString(1)+"#"+resultset.getString(2)+"#"+resultset.getString(3);
				
				bufOut.write(str+"\n");//回馈查询信心
				bufOut.flush();	
			}
			else
			{
				bufOut.write("no\n");//回馈查询信心
				bufOut.flush();					
			}	
			
			
			JdbcUtil.release(resultset, preparedstatement, connection);
		}
		
		
		public void managerModifyGoods() throws Exception
		{
			connection=JdbcUtil.getConnection();
			String arr[] = null;
			String line=null;
			
			String number=bufIn.readLine();
			preparedstatement=connection.prepareStatement("select *from goods  WHERE goods_number=? ");
			preparedstatement.setString(1,number);	
			resultset=preparedstatement.executeQuery();
			
			if(resultset.next())
			{
				bufOut.write("ok\n");//回馈查询信心
				bufOut.flush();	
				
				line=bufIn.readLine();
				arr=line.split("#");
				preparedstatement=connection.prepareStatement("update  goods set now_price=?,store_amount=? WHERE goods_number=? ");
				preparedstatement.setString(1,arr[2]);	
				preparedstatement.setString(2,arr[1]);	
				preparedstatement.setString(3,arr[0]);	
				preparedstatement.executeUpdate();//执行更新		
			}
			else
			{
				bufOut.write("no\n");//回馈查询信心
				bufOut.flush();					
			}	
			socket.close();
		
			JdbcUtil.release(resultset, preparedstatement, connection);
		}

		public void managerDeleteSolder() throws Exception
		{
			connection=JdbcUtil.getConnection();		
			String number=bufIn.readLine();
			preparedstatement=connection.prepareStatement("select *from solder WHERE solder_number=? ");
			preparedstatement.setString(1,number);	
			resultset=preparedstatement.executeQuery();
			
			if(resultset.next())
			{
				bufOut.write("ok\n");//回馈查询信心
				bufOut.flush();						
				preparedstatement=connection.prepareStatement("delete from solder  WHERE solder_number=? ");
				preparedstatement.setString(1,number);	
				preparedstatement.executeUpdate();//执行更新					
			}
			else
			{
				bufOut.write("no\n");//回馈查询信心
				bufOut.flush();	
			}	
			socket.close();
			JdbcUtil.release(resultset, preparedstatement, connection);
		}
		public void managerDeleteGoods() throws Exception
		{
			connection=JdbcUtil.getConnection();			
			String number=bufIn.readLine();
			preparedstatement=connection.prepareStatement("select *from goods  WHERE goods_number=? ");
			preparedstatement.setString(1,number);	
			resultset=preparedstatement.executeQuery();
			
			if(resultset.next())
			{
				bufOut.write("ok\n");//回馈查询信心
				bufOut.flush();		
				preparedstatement=connection.prepareStatement("delete from goods WHERE goods_number=? ");
				preparedstatement.setString(1,number);	
				preparedstatement.executeUpdate();//执行更新	
				socket.close();
			}
			else
			{
				bufOut.write("no\n");//回馈查询信心
				bufOut.flush();	
				socket.close();
			}	
			JdbcUtil.release(resultset, preparedstatement, connection);
		}
		
		public void managerAddGoods() throws Exception
		{
			connection=JdbcUtil.getConnection();
			String arr[] = null;		
			String number=bufIn.readLine();		
			preparedstatement=connection.prepareStatement("select *from goods  WHERE goods_number=? ");
			preparedstatement.setString(1,number);	
			resultset=preparedstatement.executeQuery();
			
			if(resultset.next())
			{
				bufOut.write("ok\n");//回馈查询信心
				bufOut.flush();			
			}
			else
			{
				bufOut.write("no\n");//回馈查询信心
				bufOut.flush();	
				
				arr=bufIn.readLine().split("#");
				preparedstatement=connection.prepareStatement("insert into goods values(?,?,?,?)  ");
				preparedstatement.setString(1,arr[0]);	
				preparedstatement.setString(2,arr[1]);	
				preparedstatement.setString(3,arr[2]);	
				preparedstatement.setString(4,arr[3]);	
				preparedstatement.executeUpdate();//执行更新			
			}	
			socket.close();
			JdbcUtil.release(resultset, preparedstatement, connection);
		}
			
		public void modifySolderPassword() throws Exception
		{
			connection=JdbcUtil.getConnection();
			String arr[] = null;
			arr=bufIn.readLine().split("#");	

			preparedstatement=connection.prepareStatement("select *from solder  WHERE solder_number=? and solder_password=?");
			preparedstatement.setString(1,arr[0]);
			preparedstatement.setString(2,arr[2]);
			resultset=preparedstatement.executeQuery();
			if(resultset.next())
			{
				bufOut.write("ok\n");//回馈查询信心
				bufOut.flush();	
				preparedstatement=connection.prepareStatement("update solder set solder_password=? WHERE solder_number=? and solder_password=?");
				preparedstatement.setString(1,arr[1]);	
				preparedstatement.setString(2,arr[0]);
				preparedstatement.setString(3,arr[2]);	
				preparedstatement.executeUpdate();//执行更新
			}
			else
			{
				bufOut.write("no\n");//回馈查询信心
				bufOut.flush();	
			}	
			JdbcUtil.release(resultset, preparedstatement, connection);
		}
		
		public void modifyMangerPassword() throws Exception
		{
			connection=JdbcUtil.getConnection();
			String arr[] = null;
			arr=bufIn.readLine().split("#");
			
			preparedstatement=connection.prepareStatement("select *from manager  WHERE manager_number=? and manager_password=?");
			preparedstatement.setString(1,arr[0]);
			preparedstatement.setString(2,arr[2]);
			resultset=preparedstatement.executeQuery();
			if(resultset.next())
			{
				bufOut.write("ok\n");//回馈查询信心
				bufOut.flush();	
				preparedstatement=connection.prepareStatement("update manager set manager_password=? WHERE manager_number=? and manager_password=?");
				preparedstatement.setString(1,arr[1]);	
				preparedstatement.setString(2,arr[0]);
				preparedstatement.setString(3,arr[2]);	
				preparedstatement.executeUpdate();//执行更新
			}
			else
			{
				bufOut.write("no\n");//回馈查询信心
				bufOut.flush();	
			}	
			JdbcUtil.release(resultset, preparedstatement, connection);
		}
			
		public void seekDayCondition() throws Exception//管理员查看超市某一天销售情况
		{
			
			connection=JdbcUtil.getConnection();
			
			String ss=bufIn.readLine();
			
			System.out.println(ss);
			
			java.sql.Date date = java.sql.Date.valueOf(ss);
			System.out.println(date);
			
			preparedstatement=connection.prepareStatement("select *from goodssold WHERE sold_time=? order by sold_amount desc");
			preparedstatement.setDate(1,date);			
			resultset=preparedstatement.executeQuery();//执行更新
			
			String str=null;
			while(resultset.next())
			{
				str=resultset.getString(1)+"#"+resultset.getString(2)+"#"+resultset.getString(3)+"#"+resultset.getString(4)+"#"+resultset.getString(5);	
				bufOut.write(str+"\n");//回馈查询信心
				bufOut.flush();	
			}	
			
			bufOut.write("end\n");//回馈查询信心
			bufOut.flush();
			socket.close();
			JdbcUtil.release(resultset, preparedstatement, connection);
		}		
		
}
