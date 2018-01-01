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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class AddSolder extends JPanel implements ActionListener{
	
	private String url;
	private int port;
	private static final long serialVersionUID = 1L;
	Socket s=null;	
	BufferedWriter bufOut=null;
	BufferedReader bufIn=null;	
	public static double price=0;
	public static void main(String[] args) {
		AddSolder cm=new AddSolder("127.0.01",1000);
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(cm);
		jf.setVisible(true);
	}	
	private JLabel jl=new JLabel("姓名：");
	private JTextField jf=new JTextField();
	private JButton jb=new JButton("添加");
	private JLabel jl2=new JLabel();
	public AddSolder(String url,int port)
	{
		this.url=url;
		this.port=port;
		this.initialFrame();
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jb)
		{				
			String arr[]=null;
				try {
					String name=jf.getText().trim();	
					if(name.equals(""))
					{
						JOptionPane.showMessageDialog(this,"请输入有晓得姓名","错误",JOptionPane.ERROR_MESSAGE);
						return;
					}		
					
					s=new Socket(url,port);
					bufOut=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
					bufIn=new BufferedReader(new InputStreamReader(s.getInputStream()));						
					
					bufOut.write(7+"\n");//服务器端根据7来执行功能
					bufOut.flush();		
				
					bufOut.write(name+"\n");	//向服务器传递数据
					bufOut.flush();					
					if(bufIn.readLine().equalsIgnoreCase("ok"))
					{
						//获取服务器传来的售货员账号，密码，姓名,进行核对
						arr=bufIn.readLine().split("#");
						jl2.setText("账号： "+arr[0]+"\t密码： "+arr[1]+"\t姓名： "+arr[2]);	
					}			
					s.close();
				
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}					
		}
	}
	public void initialFrame()
	{   
		this.setLayout(null);

		jl.setBounds(60,10,130,30);
		this.add(jl);
		jf.setBounds(160,10,130,30);
		this.add(jf);	
		jl2.setBounds(60, 150, 300, 30);
		this.add(jl2);
		jb.setBounds(360,10,130,30);
		this.add(jb);			
		jb.addActionListener(this);
	}	

}
