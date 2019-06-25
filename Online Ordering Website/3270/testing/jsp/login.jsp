<%@ page import ="java.sql.*" %>
<%
String username=request.getParameter("username");
session.putValue("username",username);
String password=request.getParameter("password");
Class.forName("com.mysql.jdbc.Driver");
java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://10.10.15.198:3306/t3270db","t3270","t3270");
Statement st= con.createStatement();
ResultSet rs=st.executeQuery("select * from user where username='"+username+"'");

if(rs.next())
{
if(rs.getString(3).equals(password))
{
out.println("welcome"+username);

}
else
{
out.println("Invalid password try again");
}
}
else
%>
