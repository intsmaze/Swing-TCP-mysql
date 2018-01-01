package  cn.com.util; 
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
public class JdbcUtil {
	private static String className;
	private static String url;
	private static String user;
	private static String password;	
	static{
		try {		
			InputStream in = JdbcUtil.class.getClassLoader().getResourceAsStream("dbinfo.properties");			
			Properties props = new Properties();	
			props.load(in);		
			className = props.getProperty("className");
			url = props.getProperty("url");
			user = props.getProperty("user");
			password = props.getProperty("password");			
			Class.forName(className);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	public static Connection getConnection() throws Exception{		
		return DriverManager.getConnection(url,user,password);
	}	
	public static void release(ResultSet rs,Statement stmt,Connection conn){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
		}
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmt = null;
		}
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}
}
