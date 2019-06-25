package admin_login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class admindao {
	String sql="select * from admin where admin_id=? and password=?";
	String url="jdbc:mysql://localhost:3306/wildlife";
	String username="root";
	String pass="";
	public boolean check(String admin_id,String password)
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection(url,username,pass);
			PreparedStatement st=con.prepareStatement(sql);
			st.setString(1, admin_id);
			st.setString(2, password);
			ResultSet rs=st.executeQuery();
			if(rs.next())
			{
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