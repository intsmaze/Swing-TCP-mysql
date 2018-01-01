package cn.com.util;
public class User {	
	private String number;
	private String password;
	private String name;
	private static User user;
	private User(){	}
	
	public static User getUser()
	{
		if(user==null)
		{
			user=new User();
		}
		return user;
	}	
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return number+" "+password+" "+name+" ";
	}
}
