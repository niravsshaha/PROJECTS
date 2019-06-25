package admin_login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class tp_add_dao {
	String sql="insert into package(pname,no_of_days,no_of_seats_available,a_rate,c_rate,details,admin_id) values(?,?,?,?,?,?,?)";
	String url="jdbc:mysql://localhost:3306/wildlife";
	String username="root";
	String pass="";
	public boolean check(tp_details tp,int admin_id)
	{
	System.out.println("In class tp_add_dao");
		try {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection(url,username,pass);
		PreparedStatement st=con.prepareStatement(sql);
		
		
		st.setString(1,tp.getPname());
		st.setInt(2, tp.getNo_of_days());
		st.setInt(3, tp.getNo_of_seats_available());
		st.setInt(4, tp.getA_rate());
		st.setInt(5, tp.getC_rate());
		st.setString(6,tp.getDetails());
		st.setInt(7,admin_id);
		
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
