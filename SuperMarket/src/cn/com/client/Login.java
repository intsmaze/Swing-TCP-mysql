package cn.com.client;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.*;

import cn.com.util.User;
import cn.com.util.socketUtil;
public class Login extends JFrame implements ActionListener
{	
	private static final long serialVersionUID = 1L;
	private String url="127.0.0.1";
	private int port=1000;
	private Socket socket=null;	
	private BufferedWriter bufOut=null;
	private BufferedReader bufIn=null;
	public Login()
	{ 
	    this.addListener();    
		initialFrame();
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==this.jb1)
		{
			this.jl3.setText("正 在 验 证 ， 请 稍 候. . . . .");	
		
			String number=this.jtf.getText().trim();		
			if(number.equals("")){
				JOptionPane.showMessageDialog(this,"请输入用户名","错误",
				                               JOptionPane.ERROR_MESSAGE);
				jpwf.setText("");
				return;
			}			
			String pwd=this.jpwf.getText().trim();		
			if(pwd.equals("")){
				JOptionPane.showMessageDialog(this,"请输入密码","错误",
				                           JOptionPane.ERROR_MESSAGE);
				jtf.setText("");
				return;
			}						
			int type=this.jrbArray[0].isSelected()?0:1;//获取登陆类型			
			try{   	
				socket=new Socket(url,port);	
				bufOut=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				bufIn=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				if(type==0){									
					bufOut.write(type+"\n");	
					bufOut.flush();						
					//发送账号密码
					bufOut.write(number+"#"+pwd+"\n");	
					bufOut.flush();										
					//查询是否符合，符合返回Ok，否则说明输入信息不正确
					if(bufIn.readLine().equalsIgnoreCase("ok"))
					{
						String arr[] = null;
						//获取传送来的账号密码，一辈以后使用，减少查询数据库次数
						arr=bufIn.readLine().split("#");
						User.getUser().setNumber(arr[0]);
						User.getUser().setPassword(arr[1]);
						User.getUser().setName(arr[2]);
						
						socketUtil.close(socket);
						
						new SolderClient(url,port);
						this.dispose();//关闭登陆窗口并释放资源
					}
					else{//弹出错误提示窗口
						JOptionPane.showMessageDialog(this,"用户名或密码错误","错误",
						                           JOptionPane.ERROR_MESSAGE);
						jl3.setText("");
					}
				}
				else{					
					bufOut.write(type+"\n");	
					bufOut.flush();							
					//发送账号密码
					bufOut.write(number+"#"+pwd+"\n");	
					bufOut.flush();									
					//查询是否符合，符合返回Ok，否则说明输入信息不正确
					if(bufIn.readLine().equalsIgnoreCase("ok"))
					{
						String arr[] = null;
						//获取传送来的账号密码，一辈以后使用，减少查询数据库次数
						arr=bufIn.readLine().split("#");
						User.getUser().setNumber(arr[0]);
						User.getUser().setPassword(arr[1]);
						User.getUser().setName(arr[2]);
						System.out.println(User.getUser().toString());
						
						socketUtil.close(socket);
						
						new ManagerClient(url,port);
						this.dispose();//关闭登陆窗口并释放资源
					}
					else{//弹出错误提示窗口
						JOptionPane.showMessageDialog(this,"用户名或密码错误","错误",
						                           JOptionPane.ERROR_MESSAGE);
						jl3.setText("");
					}
				}
			}
			catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource()==this.jb2){//按下重置按钮,清空输入信息
			this.jtf.setText("");
			this.jpwf.setText("");
		}
	}	
	private JPanel jp=new JPanel();
	private JLabel jname=new JLabel("用    户    名");
	private JLabel jpassword=new JLabel("密            码");
	private JLabel jl3=new JLabel("");
	

	private JTextField jtf=new JTextField();
	private JPasswordField jpwf=new JPasswordField();		
	private JRadioButton[] jrbArray=//创建单选按钮数组
	        {
	        	new JRadioButton("售货员",true),
	        	new JRadioButton("管理人员")
	        };
	private ButtonGroup bg=new ButtonGroup();
	private JButton jb1=new JButton("登    陆");
	private JButton jb2=new JButton("重    置");

	public void addListener(){
		this.jb1.addActionListener(this);
		this.jb2.addActionListener(this);
	}
	
	public void initialFrame()
	{
		jp.setLayout(null);
		this.jname.setBounds(30,20,110,25);
		this.add(jname);
		this.jtf.setBounds(120,20,130,25);
		this.add(jtf);		
		this.jpassword.setBounds(30,60,110,25);
		this.add(jpassword);
		this.jpwf.setBounds(120,60,130,25);
		this.jpwf.setEchoChar('*');
		this.jp.add(jpwf);
		this.bg.add(jrbArray[0]);
		this.bg.add(jrbArray[1]);
		this.jrbArray[0].setBounds(40,100,100,25);
		this.jp.add(jrbArray[0]);
		this.jrbArray[1].setBounds(145,100,100,25);
		this.jp.add(jrbArray[1]);
		this.jb1.setBounds(35,140,100,30);
		this.jp.add(jb1);
		this.jb2.setBounds(150,140,100,30);
		this.jp.add(jb2);
		this.jl3.setBounds(40,175,150,25);
		this.jp.add(jl3);
		this.add(jp);

		this.setTitle("登陆");
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();	
		int centerX=screenSize.width/2;
		int centerY=screenSize.height/2;	
		int w=300;//本窗体宽度
		int h=240;//本窗体高度	
		this.setBounds(centerX-w/2,centerY-h/2-100,w,h);//设置窗体出现在屏幕中央
		
		this.setVisible(true);
		
		//将填写账号的文本框设为默认焦点
		this.jtf.requestFocus(true);
	}
	
	public static void main(String args[])
	{
		    Login login=new Login();
	}
}