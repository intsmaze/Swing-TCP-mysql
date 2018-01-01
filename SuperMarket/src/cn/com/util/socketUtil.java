package cn.com.util;

import java.io.IOException;
import java.net.Socket;

public class socketUtil {


	public static void close(Socket socket) {
		// TODO Auto-generated method stub
		try
		{
			if(socket!=null)
				socket.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
