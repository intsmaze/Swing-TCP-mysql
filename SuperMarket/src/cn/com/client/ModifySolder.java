package cn.com.client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;
public class   ModifySolder extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private String url;
	private int port;
	private static final long serialVersionUID = 1L;
	Socket s=null;	
	BufferedWriter bufOut=null;
	BufferedReader bufIn=null;	
	public static double price=0;
	public static void main(String[] args) {
		 ModifySolder cm=new  ModifySolder("127.0.01",1000);
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(cm);
		jf.setVisible(true);
	}	
	private JLabel jl[]={new JLabel("账号："),new JLabel("姓名")
	,new JLabel("密码"),new JLabel("确认密码")};
	private JTextField jf[]={new JTextField(),new JTextField(),new JTextField(),new JTextField()};
	private JButton jb1=new JButton("修改");
	public  ModifySolder(String url,int port)
	{
		this.url=url;
		this.port=port;
		this.initialFrame();
	}
	public void actionPerformed(ActionEvent e) {		
		 try {
			String patternStr="[0-9]{4,6}";
			String number=jf[0].getText();
				if(number.equals(""))
				{
					JOptionPane.showMessageDialog(this,"请输入账号","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}	
				if(!number.matches(patternStr))
				{
					JOptionPane.showMessageDialog(this,"账号有误","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}
		
				String name=jf[1].getText();
				if(name.equals(""))
				{
					JOptionPane.showMessageDialog(this,"请输入姓名","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}	
				
				String pass=jf[2].getText();
				if(pass.equals(""))
				{
					JOptionPane.showMessageDialog(this,"密码有误","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}				
				if(!pass.matches(patternStr))
				{
					JOptionPane.showMessageDialog(this,"密码只能是4到6位的字母或数字","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}				
				String newPwd1=jf[3].getText();
				if(!pass.equals(newPwd1))
				{
					JOptionPane.showMessageDialog(this,"确认密码与新密码不符","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}			
				
			s=new Socket(url,port);
			bufOut=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			bufIn=new BufferedReader(new InputStreamReader(s.getInputStream()));	
	
			bufOut.write(9+"\n");//服务器端根据7来执行功能
			bufOut.flush();		
			
			bufOut.write(number+"\n");	//向服务器传递数据
			bufOut.flush();	
				
			if(bufIn.readLine().equalsIgnoreCase("no"))
			{
				JOptionPane.showMessageDialog(this,"说账号不存在无法操作！"," ",
                        JOptionPane.ERROR_MESSAGE);
				s.close();
			}
			else
			{				
				String str=number+"#"+name+"#"+pass;			
				bufOut.write(str+"\n");
				bufOut.flush();	
				JOptionPane.showMessageDialog(this,"修改成功！！"," ",
                        JOptionPane.ERROR_MESSAGE);
				s.close();	
			}
		} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
				// TODO Auto-generated catch block
			e1.printStackTrace();
		}			
	}	
	

	public void initialFrame()
	{   
		this.setLayout(null);
		for(int i=0;i<jl.length;i++)
		{
			jl[i].setBounds(60,10+i*80,130,30);
			this.add(jl[i]);
			jf[i].setBounds(160,10+i*80,130,30);
			this.add(jf[i]);
		}				
		jb1.setBounds(300,500,130,30);
		this.add(jb1);
		jb1.addActionListener(this);
	}	
}