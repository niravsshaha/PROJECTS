package login_signup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class logindao {
	String sql="select * from user where email_id=? and password=?";
	String url="jdbc:mysql://localhost:3306/wildlife";
	String username="root";
	String pass="";
	public boolean check(String email_id,String password,userbean u1)
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection(url,username,pass);
			PreparedStatement st=con.prepareStatement(sql);
			st.setString(1, email_id);
			st.setString(2, password);
			ResultSet rs=st.executeQuery();
			if(rs.next())
			{
				u1.setUser_id(rs.getInt("user_id"));
				return true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Log In failed: An Exception has occurred! " + e);
		}
		return false;
		
	}
	
	
}
