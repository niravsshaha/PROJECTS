package login_signup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;


public class signupdao extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	

		public static boolean insert(userbean user)
		{
			String sql="insert into user(name,phone_no,email_id,city,password) values(?,?,?,?,?)";
			String url="jdbc:mysql://localhost:3306/wildlife";
			String username="root";
			String pass="";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con=DriverManager.getConnection(url,username,pass);
				PreparedStatement st=con.prepareStatement(sql);
				
				st.setString(1, user.getName());
				st.setString(2, user.getPhone_no());
				st.setString(3, user.getEmailid());
				st.setString(4, user.getCity());
				st.setString(5, user.getPassword());
				
				 int i=st.executeUpdate();
				if(i!=0)
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
