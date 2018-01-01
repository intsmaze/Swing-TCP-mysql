package cn.com.client;
import java.net.Socket;
import java.sql.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.*;

import cn.com.util.User;
public class ChangePwd extends JPanel implements ActionListener
{
	private String url;
	private int port;
	private static final long serialVersionUID = 1L;
	Socket socket=null;	
	BufferedWriter bufOut=null;
	BufferedReader bufIn=null;
	

	private JLabel[] jlArray={new JLabel("原始密码"),new JLabel("新密码"),new JLabel("确认新密码"),	                         };
	private JPasswordField[] jpfArray={new JPasswordField(),new JPasswordField(),new JPasswordField()
	                             };
	private JButton[] jbArray={new JButton("确认"),new JButton("重置")
	                          };	
	private JLabel jl=new JLabel("对输出非法字符如空格进行了处理");
	public ChangePwd(String url,int port)
	{
		this.url=url;
		this.port=port;
		this.initialFrame();
		this.addListener();
	}	
	public void addListener()
	{
		jbArray[0].addActionListener(this);
		jbArray[1].addActionListener(this);
	}	
	public void initialFrame()
	{
		this.setLayout(null);		
		for(int i=0;i<jlArray.length;i++)
		{
			jlArray[i].setBounds(30,20+50*i,150,30);
			this.add(jlArray[i]);
			jpfArray[i].setBounds(130,20+50*i,150,30);
			this.add(jpfArray[i]);
		}	
		jl.setBounds(30, 300, 200, 30);
		this.add(jl);
		jbArray[0].setBounds(40,180,100,30);
		this.add(jbArray[0]);
		jbArray[1].setBounds(170,180,100,30);
		this.add(jbArray[1]);
	}	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==jbArray[1])
		{
			for(int i=0;i<jpfArray.length;i++)
			{
				jpfArray[i].setText("");
			}
		}
		else if(e.getSource()==jbArray[0])
		{	
			String patternStr="[0-9a-zA-Z]{4,6}";
			String oldPwd=jpfArray[0].getText();
			if(oldPwd.equals(""))
			{
				JOptionPane.showMessageDialog(this,"请输入原始密码","错误",JOptionPane.ERROR_MESSAGE);
				return;
			}			
			String newPwd=jpfArray[1].getText();
			if(newPwd.equals(""))
			{
				JOptionPane.showMessageDialog(this,"请输入新密码","错误",JOptionPane.ERROR_MESSAGE);
				return;
			}		
			if(!newPwd.matches(patternStr))
			{//新密码格式不正确
				JOptionPane.showMessageDialog(this,"密码只能是4到6位的字母或数字","错误",JOptionPane.ERROR_MESSAGE);
				return;
			}
			String newPwd1=jpfArray[2].getText();
			if(!newPwd.equals(newPwd1))
			{
				JOptionPane.showMessageDialog(this,"确认密码与新密码不符","错误",JOptionPane.ERROR_MESSAGE);
				return;
			}			
			try
			{   			
				socket=new Socket(url,port);	
				bufOut=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				bufIn=new BufferedReader(new InputStreamReader(socket.getInputStream()));							
				//把你修改的密码和你的账号发给服务器进行更新
				String str=User.getUser().getNumber()+"#"+newPwd+"#"+oldPwd;									
				bufOut.write(5+"\n");
				bufOut.flush();	
				bufOut.write(str+"\n");	//向服务器传递数据
				bufOut.flush();		
				if(bufIn.readLine().equalsIgnoreCase("ok"))
				{
					JOptionPane.showMessageDialog(this,"密码修改成功,退出额登录"," ",JOptionPane.ERROR_MESSAGE);
					socket.close();	
					System.exit(0);
				}
				else
				{
					JOptionPane.showMessageDialog(this,"原始密码错误","错误",JOptionPane.ERROR_MESSAGE);
					socket.close();	
				}
			}		
			catch(Exception ea)
			{
				ea.printStackTrace();
			}
		}
	}	
	public static void main(String[] args) {
		ChangePwd cm=new ChangePwd("127.0.01",1000);
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(cm);
		jf.setVisible(true);
	}
}