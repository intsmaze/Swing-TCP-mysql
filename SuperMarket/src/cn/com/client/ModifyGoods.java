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
public class  ModifyGoods extends JPanel implements ActionListener
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
		ModifyGoods cm=new  ModifyGoods("127.0.01",1000);
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(cm);
		jf.setVisible(true);
	}	
	private JLabel jl[]={new JLabel("商品编号："),new JLabel("商品名称")
	,new JLabel("商品数量"),new JLabel("商品价格")};
	private JTextField jf[]={new JTextField(),new JTextField(),new JTextField(),new JTextField()};
	private JButton jb1=new JButton("修改");
	public  ModifyGoods(String url,int port)
	{
		this.url=url;
		this.port=port;
		this.initialFrame();
	}
	public void actionPerformed(ActionEvent e) {
			try {
				String patternStr="[0-9]{1,10}";
				String number=jf[0].getText().trim();	
				if(number.equals(""))
				{
					JOptionPane.showMessageDialog(this,"请输入编号","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}	
				if(!number.matches(patternStr))
				{
					JOptionPane.showMessageDialog(this,"编号输入有误","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}
				String name=jf[1].getText().trim();	
				if(name.equals(""))
				{
					JOptionPane.showMessageDialog(this,"请输入名称","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}		
				String amount=jf[2].getText().trim();
				if(amount.equals(""))
				{
					JOptionPane.showMessageDialog(this,"请输入数量","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(!amount.matches(patternStr))
				{
					JOptionPane.showMessageDialog(this,"数量输入有误","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}
				String price=jf[3].getText().trim();
				if(price.equals(""))
				{
					JOptionPane.showMessageDialog(this,"请输入价格","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}					
				if(!price.matches(patternStr))
				{
					JOptionPane.showMessageDialog(this,"价格输入有误","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}		
						
				s=new Socket(url,port);
				bufOut=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				bufIn=new BufferedReader(new InputStreamReader(s.getInputStream()));	
				
				bufOut.write(12+"\n");//服务器端根据7来执行功能
				bufOut.flush();			
					
				bufOut.write(number+"\n");	//向服务器传递数据
				bufOut.flush();	
						
				if(bufIn.readLine().equalsIgnoreCase("ok"))
				{					
					String str=number+"#"+amount+"#"+price;						
					bufOut.write(str+"\n");
					bufOut.flush();	
					s.close();

					JOptionPane.showMessageDialog(this,"商品修改成！"," ",
		                         JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(this,"商品不存在！"," ",
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