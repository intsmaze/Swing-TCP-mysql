package cn.com.client;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
public class SolderClient extends JFrame
{
	private static final long serialVersionUID = 1L;
	public static void main(String[] args) throws Exception {
		new  SolderClient("127.0.01",1000);					
	}

	private String url;
	private int port;
	private DefaultMutableTreeNode dmtnRoot=new DefaultMutableTreeNode(new MyNode("操作选项","0"));
	private DefaultMutableTreeNode dmtn1=new DefaultMutableTreeNode(new MyNode("修改密码","1"));
	private DefaultMutableTreeNode dmtn2=new DefaultMutableTreeNode(new MyNode("商品录入","2"));	
	private DefaultMutableTreeNode dmtn3=new DefaultMutableTreeNode(new MyNode("退出","3"));
	//创建根节点
	private DefaultTreeModel dtm=new DefaultTreeModel(dmtnRoot);	
	private JTree jtree=new JTree(dtm);
	private JScrollPane jspz=new JScrollPane(jtree);
	
	//存放个功能模块面板
	private JPanel jpy=new JPanel();
	private JSplitPane jsp1=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jspz,jpy);
	
	private CardLayout cl;
	private SolderWork solderwork;
	private ChangePwd changepwd;
	public SolderClient(String url,int port)
	{
		this.url=url;
		this.port=port;
		this.initialTree();
		this.initialPane();
		this.initialJpy();
		this.addListener();
		this.initialFrame();
	}
	public void initialJpy()
	{
		jpy.setLayout(new CardLayout());
		cl=(CardLayout)jpy.getLayout();
		jpy.add(solderwork,"solderwork");
		jpy.add(changepwd,"changepwd");
	}
	
	public void initialPane()
	{
		solderwork=new SolderWork(url,port);
		changepwd=new ChangePwd(url,port);
	}
	
	public void initialTree()
	{
		dmtnRoot.add(dmtn1);
		dmtnRoot.add(dmtn2);
		dmtnRoot.add(dmtn3);
		jtree.setToggleClickCount(1);
	}
	public void addListener()
	{
		jtree.addMouseListener(
               new MouseAdapter()
               {
               	  public void mouseClicked(MouseEvent e)
               	  { 
               	      DefaultMutableTreeNode dmtntemp=(DefaultMutableTreeNode)jtree.getLastSelectedPathComponent();
					  MyNode mynode=(MyNode)dmtntemp.getUserObject();
					  String id=mynode.getId();
					 
               	      if(id.equals("3"))
               	      {
               	      	    int i=JOptionPane.showConfirmDialog(jpy,"您确认要退出出系统吗？","询问",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
               	      		if(i==0)
               	      		{
               	      			System.exit(0);
               	      		}             	      		
               	      }
               	      else if(id.equals("1"))
               	      {
               	      		cl.show(jpy,"changepwd");
               	      }
               	      else if(id.equals("2"))
               	      {
               	      		cl.show(jpy,"solderwork");
               	      }
	              }
	           }
		                       );
	}	
	public void initialFrame()
	{
		this.add(jsp1);
		jsp1.setDividerLocation(200);
		jsp1.setDividerSize(4);
		this.setTitle("售货员客户端");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX=screenSize.width/2;
		int centerY=screenSize.height/2;
		int w=900;
		int h=650;
		this.setBounds(centerX-w/2,centerY-h/2-30,w,h);//设置窗体出现在屏幕中央
		this.setVisible(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	class MyNode
	{
		private String values;
		private String id;
		public MyNode(String values,String id)
		{
			this.values=values;
			this.id=id;
		}
		public String toString()
		{
			return this.values;
		}
		public String getId()
		{
			return this.id;
		}
	}		
}