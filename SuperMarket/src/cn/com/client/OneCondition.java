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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
public class OneCondition extends JPanel implements ActionListener{
	private String url;
	private int port;
	private static final long serialVersionUID = 1L;
	Socket s=null;	
	BufferedWriter bufOut=null;
	BufferedReader bufIn=null;	
	public static double price=0;
	public static void main(String[] args) {
		OneCondition cm=new OneCondition("127.0.01",1000);
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(cm);
		jf.setVisible(true);
	}	
	private JLabel jl[]={new JLabel("年"),new JLabel("月"),new JLabel("日")};
	private JTextField jf[]={new JTextField(),new JTextField(),new JTextField()};
	private JButton jb=new JButton("确定");
	private JTextArea jta=new JTextArea();
	private JScrollPane jsp=new JScrollPane(jta);	
	public OneCondition(String url,int port)
	{
		this.url=url;
		this.port=port;
		this.initialFrame();
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jb)
		{
			String patternday="[0-2][0-9]";
			String patternmon="[0-1][0-2]";//month
			String patternyear="[1-9][0-9]{3,4}";	//year		
			String year=jf[0].getText().trim();
			if(year.equals(""))
			{
				JOptionPane.showMessageDialog(this,"请输入年","错误",JOptionPane.ERROR_MESSAGE);
				return;
			}	
			if(!year.matches(patternyear))
			{//新密码格式不正确
				JOptionPane.showMessageDialog(this,"年份错误","错误",JOptionPane.ERROR_MESSAGE);
				return;
			}			
			String month=jf[1].getText().trim();
			if(month.equals(""))
			{
				JOptionPane.showMessageDialog(this,"请输入月份","错误",JOptionPane.ERROR_MESSAGE);
				return;
			}	
			if(!month.matches(patternmon))
			{//新密码格式不正确
				JOptionPane.showMessageDialog(this,"月份错误","错误",JOptionPane.ERROR_MESSAGE);
				return;
			}				
			String day=jf[2].getText().trim();
			if(day.equals(""))
			{
				JOptionPane.showMessageDialog(this,"天错","错误",JOptionPane.ERROR_MESSAGE);
				return;
			}	
			if(!day.matches(patternday))
			{//新密码格式不正确
				JOptionPane.showMessageDialog(this,"天错","错误",JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				s=new Socket(url,port);
				bufOut=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				bufIn=new BufferedReader(new InputStreamReader(s.getInputStream()));			
				String arr[]=null;
					
				String date=year+"-"+month+"-"+day;		
				bufOut.write(2+"\n");//服务器端根据7来执行功能
				bufOut.flush();				
				bufOut.write(date+"\n");	//向服务器传递数据
				bufOut.flush();	
				
				String line=null;
				jta.setText("编号\t名称\t销量\t价格\t时间\n");
				while((line=bufIn.readLine())!=null)
				{
					if(line.equalsIgnoreCase("end"))
					{
						break;
					}
					arr=line.split("#");
					jta.append(arr[0]+"\t"+arr[1]+"\t"+arr[2]+"\t"+arr[3]+"\t"+arr[4]+"\n");	
					}					
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
		jta.setEditable(false);
		this.setLayout(null);		
		for(int i=0;i<jf.length;i++)
		{
			jl[i].setBounds(60,10+i*40,130,30);
			this.add(jl[i]);
			jf[i].setBounds(160,10+i*40,130,30);
			this.add(jf[i]);
		}
		jsp.setBounds(60,170,500,400);
		this.add(jsp);		
		jb.setBounds(360,10,130,30);
		this.add(jb);		
		jb.addActionListener(this);
	}	
}

