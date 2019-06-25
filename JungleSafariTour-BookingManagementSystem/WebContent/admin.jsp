<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true" %>
    <%@ page
	import ="java.sql.Connection,
 java.sql.DriverManager,
 java.sql.PreparedStatement, java.sql.ResultSet"
 %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>


</head>
<body>

 <a href="LogoutServlet"><i class="fa fa-fw fa-user"></i>Logout</a>
 
 
 <%
String sql="select * from package where admin_id=?";
String url="jdbc:mysql://localhost:3306/wildlife";
String username="root";
String pass="579453";


//try {
	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection(url,username,pass);
	PreparedStatement st=con.prepareStatement(sql);
	
	st.setString(1, session.getAttribute("admin_id").toString());
	ResultSet rs=st.executeQuery();
	
//}
/*
catch(Exception e)
{
	System.out.println("Log In failed: An Exception has occurred! " + e);
}*/

while (rs.next ())
{
 
 out.println("<p><h2>"+rs.getString("pname")+"</h2></p>");
 out.println("<h5><p>Number of days::"+rs.getInt("no_of_days")+"Number of seats available::"+rs.getInt("no_of_seats_available")+"</p></h5>");
 out.println("<p><h5>Adult Rate::"+rs.getInt("a_rate")+ "Child Rate::"+rs.getInt("c_rate")  +"</h5></p>");
 out.println("<p><h4>Details::"+rs.getString("details")+"</h4></p>");
 
 out.println("<input type='button' value='delete' onclick=deletepackage("+ rs.getInt("package_id")+ ")>"  +  "</input>");
 
 out.println("<hr><hr>");
} 
out.println("<a href='admin_tp_add.jsp'><p><input type='submit' value='Add tours'</p></a>");
%>

<script>
function deletepackage(package_id)
{
	
	window.location.href="deletepackage.jsp?package_id=" + package_id;
	
}


</script>



</body>
</html>