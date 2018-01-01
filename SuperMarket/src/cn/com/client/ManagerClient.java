package cn.com.client;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
public class ManagerClient extends JFrame
{	

	private String url;
	private int port;
	private static final long serialVersionUID = 1L;
	private DefaultMutableTreeNode dmtnRoot=new DefaultMutableTreeNode(new MyNode("操作选项","0"));
	private DefaultMutableTreeNode dmtn1=new DefaultMutableTreeNode(new MyNode("修改密码","1"));
	private DefaultMutableTreeNode dmtn2=new DefaultMutableTreeNode(new MyNode("售货员管理","2"));
	private DefaultMutableTreeNode dmtn3=new DefaultMutableTreeNode(new MyNode("商品管理","3"));
	private DefaultMutableTreeNode dmtn4=new DefaultMutableTreeNode(new MyNode("售货情况","4"));
	private DefaultMutableTreeNode dmtn5=new DefaultMutableTreeNode(new MyNode("退出","5"));
	private DefaultMutableTreeNode dmtn21=new DefaultMutableTreeNode(new MyNode("添加售货员","21"));
	private DefaultMutableTreeNode dmtn22=new DefaultMutableTreeNode(new MyNode("删除售货员","22"));
	private DefaultMutableTreeNode dmtn23=new DefaultMutableTreeNode(new MyNode("修改售货员","23"));
	private DefaultMutableTreeNode dmtn31=new DefaultMutableTreeNode(new MyNode("添加商品","31"));
	private DefaultMutableTreeNode dmtn32=new DefaultMutableTreeNode(new MyNode("删除商品","32"));
	private DefaultMutableTreeNode dmtn33=new DefaultMutableTreeNode(new MyNode("修改商品","33"));
	private DefaultMutableTreeNode dmtn41=new DefaultMutableTreeNode(new MyNode("一天售货情况","41"));
	
	private DefaultTreeModel dtm=new DefaultTreeModel(dmtnRoot);	
	private JTree jtree=new JTree(dtm);
	private JScrollPane jspz=new JScrollPane(jtree);	
	private JPanel jpy=new JPanel();
	private JSplitPane jsp1=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jspz,jpy);
	
	private CardLayout cl;
	//生命欢迎页面
	private AddSolder  addsolder;
	private AddGood  addgoods;
	private ModifyGoods  modifygoods;
	private ModifySolder  modifysolder;
	private DeleteGoods  deletegoods;
	private DeleteSolder  deletesolder;
	private ChangePwdManager changepwd;
	private OneCondition onecondition;
	public ManagerClient(String url,int port)
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
		jpy.add(addgoods,"addgoods");
		jpy.add(addsolder,"addsolder");
		jpy.add(modifygoods,"modifygoods");
		jpy.add(modifysolder,"modifysolder");
		jpy.add(deletegoods,"deletegoods");
		jpy.add(deletesolder,"deletesolder");
		jpy.add(onecondition,"onecondition");
		jpy.add(changepwd,"changepwd");

	}
	
	public void initialPane()
	{
		addgoods=new AddGood(url,port);
		 addsolder=new AddSolder(url,port);
		 modifygoods=new ModifyGoods(url,port);;
		 modifysolder=new ModifySolder(url,port);
		 deletegoods=new DeleteGoods(url,port);
		 deletesolder=new DeleteSolder(url,port);
		changepwd=new ChangePwdManager(url,port);
		onecondition=new OneCondition(url,port);
	}
	//初始化树状列表控件的方法
	public void initialTree()
	{
		dmtnRoot.add(dmtn1);
		dmtnRoot.add(dmtn2);
		dmtnRoot.add(dmtn3);
		dmtnRoot.add(dmtn4);
		dmtnRoot.add(dmtn5);
		dmtn2.add(dmtn21);
		dmtn2.add(dmtn22);
		dmtn2.add(dmtn23);
		dmtn3.add(dmtn31);
		dmtn3.add(dmtn32);
		dmtn3.add(dmtn33);
		dmtn4.add(dmtn41);
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
					  //根据id值显示不同的卡片
               	      if(id.equals("5"))
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
               	      else if(id.equals("21"))
               	      {
               	      		cl.show(jpy,"addsolder");
               	      }
               	      else if(id.equals("22"))
            	      {
            	      		cl.show(jpy,"deletesolder");
            	      }
               	      else if(id.equals("23"))
               	      {
               	    	  cl.show(jpy,"modifysolder");
               	      }
               	      else if(id.equals("31"))
               	      {
               	    	  cl.show(jpy,"addgoods");
               	      }
               	      else if(id.equals("32"))
               	      {
               	      		cl.show(jpy,"deletegoods");
               	      }
               	      else if(id.equals("33"))
               	      {
               	      		cl.show(jpy,"modifygoods");
               	      }            	      
               	      else if(id.equals("41"))
               	      {
               	      		cl.show(jpy,"onecondition");
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
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX=screenSize.width/2;
		int centerY=screenSize.height/2;
		int w=900;//本窗体宽度
		int h=650;//本窗体高度
		this.setBounds(centerX-w/2,centerY-h/2-30,w,h);//设置窗体出现在屏幕中央
		this.setVisible(true);
		//窗体全屏
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	
	public static void main(String[] args) {
		ManagerClient cm=new ManagerClient("127.0.01",1000);
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(cm);
		jf.setVisible(true);
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