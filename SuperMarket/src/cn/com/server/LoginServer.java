package  cn.com.server; 
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class LoginServer {		
	private int port=1000;
	private ServerSocket serversocket;
	private ExecutorService executorservice;
	private final int POOL_SIZE=1;//一个CPU线程池中工作线程的数目
	public LoginServer() throws Exception
	{	
		serversocket=new ServerSocket();
		serversocket.bind(new InetSocketAddress("127.0.0.1",port));	
		executorservice=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*POOL_SIZE);	
		//当前CPU数*1就是总线程池数目
	}
	public void service()
	{
		while(true)
		{
			Socket socket=null;	
			try
			{
				socket=serversocket.accept();
				executorservice.execute(new Handler(socket));
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}	
	public static void main(String[] args) throws Exception {
		new LoginServer().service();					
	}
}
