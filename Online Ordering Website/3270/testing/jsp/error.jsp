 <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<%@page import="java.sql.*,java.util.*"%>

<%
String username=request.getParameter("username");
String email=request.getParameter("email");

String password=request.getParameter("password");

try
{
Class.forName("com.mysql.jdbc.Driver");
java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://10.10.15.198:3306/t3270db","t3270","t3270");
Statement st=con.createStatement();
int i=st.executeUpdate("insert into user(username,password,email)values('"+username+"','"+password+"','"+email+"')");
out.println("Data is successfully inserted!");
}
catch(Exception e)
{
out.println("Data is not inserted!");
System.out.print(e);
e.printStackTrace();
}
%> 
