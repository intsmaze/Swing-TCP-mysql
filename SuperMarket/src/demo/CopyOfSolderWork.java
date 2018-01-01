package demo;
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

import cn.com.util.socketUtil;
public class CopyOfSolderWork extends JPanel implements ActionListener
{

	private String url;
	private int port;
	private static final long serialVersionUID = 1L;
	
	private Socket socket=null;	
	private BufferedWriter bufOut=null;
	private BufferedReader bufIn=null;	
	private static double price=0;
	public static void main(String[] args) {
		CopyOfSolderWork cm=new CopyOfSolderWork("127.0.01",1000);
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(cm);
		jf.setVisible(true);
	}	
	private JLabel jl1[]={new JLabel("商品扫描："),new JLabel("商品编号")
	,new JLabel("商品名称"),new JLabel("价格")};
	private JTextField jf=new JTextField();
	private JButton jb[]={new JButton("确定"),new JButton("完成"),new JButton("清空")};
	private JLabel jl2=new JLabel("总价：");
	private JLabel jl3=new JLabel("根据实际情况，因为扫描的是条码，不存在输入字母情况！");
	private JTextArea jta=new JTextArea();
	private JScrollPane jsp=new JScrollPane(jta);	
	public CopyOfSolderWork(String url,int port)
	{
		this.url=url;
		this.port=port;
		this.initialFrame();
	}
	public void actionPerformed(ActionEvent e) {
	
		if(e.getSource()==jb[0])
			
		{
			String number=jf.getText().trim();
			try {			
				for(int i=0;i<20;i++)
				{
					socket=new Socket(url,port);
					bufOut=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					bufIn=new BufferedReader(new InputStreamReader(socket.getInputStream()));			
					bufOut.write(6+"\n");//服务器端根据6来执行功能
					bufOut.flush();		
				
					String arr[]=null;	
				
					bufOut.write(number+"\n");	//向服务器传递数据
					bufOut.flush();			
				
					if(bufIn.readLine().equalsIgnoreCase("ok"))
					{
						
					//获取服务器传来的商品编号，名称，价格。
						arr=bufIn.readLine().split("#");
						jta.append(arr[0]+"\t"+arr[1]+"\t"+arr[2]+"\t"+arr[3]+"\n");			
						price+=Double.valueOf(arr[2]);
						jf.setText(" ");
						socketUtil.close(socket);
					}
					else
					{
						socketUtil.close(socket);
						jta.append("商品不存在或库存不足！\n");	
					}							
					try {
			            Thread.sleep(100);
			        } catch (InterruptedException e1) {
			            e1.printStackTrace(); 
			        }
				}
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
			
		}else if(e.getSource()==jb[1])
		{
			jl2.setText("总价："+price);
		}	
		else if(e.getSource()==jb[2])
		{
			jta.setText("");
			price=0;
		}	
	}
	public void initialFrame()
	{   
		jta.setEditable(false);
		this.setLayout(null);
		
		jsp.setBounds(60,80,500,400);
		this.add(jsp);
		jl1[0].setBounds(60,10,130,30);
		this.add(jl1[0]);

		jf.setBounds(160,10,130,30);
		this.add(jf);

		jb[0].setBounds(360,10,130,30);
		this.add(jb[0]);		
		jb[0].addActionListener(this);
		jb[1].addActionListener(this);
		jb[2].addActionListener(this);
		jl1[1].setBounds(60,40,130,30);
		this.add(jl1[1]);
		jl1[2].setBounds(160,40,130,30);
		this.add(jl1[2]);
		jl1[3].setBounds(260,40,130,30);
		this.add(jl1[3]);
		jl2.setBounds(200,500,130,30);
		this.add(jl2);
		jb[1].setBounds(300,500,130,30);
		this.add(jb[1]);
		jb[2].setBounds(500,500,130,30);
		this.add(jb[2]);
		jl3.setBounds(200,550,350,30);
		this.add(jl3);
	}	
}