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
public class AddGood extends JPanel implements ActionListener
{
	int i=0;
	private String url;
	private int port;
	private static final long serialVersionUID = 1L;
	private Socket socket=null;	
	private BufferedWriter bufOut=null;
	private BufferedReader bufIn=null;	
	public static void main(String[] args) {
		AddGood cm=new AddGood("127.0.01",1000);
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(cm);
		jf.setVisible(true);
	}	
	private JLabel jl[]={new JLabel("商品编号："),new JLabel("商品名称")
	,new JLabel("商品数量"),new JLabel("价格")};
	private JTextField jf[]={new JTextField(),new JTextField(),new JTextField(),new JTextField()};
	private JButton jb1=new JButton("添加");
	private JLabel jlf=new JLabel("根据实际情况，只会是商品数量和价格输入错误，商品条形码是扫描记录！");
	public AddGood(String url,int port)
	{
		this.url=url;
		this.port=port;
		this.initialFrame();
	}
	public void actionPerformed(ActionEvent e) {
		String number=null;
			try {
				String patternStr="[0-9]{1,10}";			
				String amount=jf[2].getText().trim();
				if(amount.equals(""))
				{
					JOptionPane.showMessageDialog(this,"请输入数量","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}			
				String price=jf[3].getText().trim();
				if(price.equals(""))
				{
					JOptionPane.showMessageDialog(this,"请输入价格","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}		
				if(!amount.matches(patternStr))
				{
					JOptionPane.showMessageDialog(this,"数量输入有误","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(!price.matches(patternStr))
				{
					JOptionPane.showMessageDialog(this,"价格输入有误","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}		
				String name=jf[1].getText().trim();	
				if(name.equals(""))
				{
					JOptionPane.showMessageDialog(this,"请输入名称","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}		
				socket=new Socket(url,port);
				bufOut=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				bufIn=new BufferedReader(new InputStreamReader(socket.getInputStream()));				
				bufOut.write(10+"\n");//服务器端根据7来执行功能
				bufOut.flush();		
					
				number=jf[0].getText().trim();							
				bufOut.write(number+"\n");	//向服务器传递数据				
				bufOut.flush();						
				//如果商品存在，则不添加
				if(bufIn.readLine().equalsIgnoreCase("ok"))
				{
						JOptionPane.showMessageDialog(this,"商品存在bu可以添加！"," ",
		                           JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					String str=number+"#"+name+"#"+amount+"#"+price;				
					bufOut.write(str+"\n");
					bufOut.flush();	
					socket.close();
					JOptionPane.showMessageDialog(this,"商品添加ok！"," ",
		                          JOptionPane.ERROR_MESSAGE);
					socket.close();
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
		jlf.setBounds(10, 400, 480, 30);
		this.add(jlf);
		jb1.setBounds(300,500,130,30);
		this.add(jb1);
		jb1.addActionListener(this);
	}	
}