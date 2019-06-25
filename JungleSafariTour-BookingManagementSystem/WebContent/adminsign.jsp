<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Log In</title>
		<style>
.container {
   
    width: 960px;
height:380px;
margin:50px auto;
font-family: 'Droid Serif', serif;
position:absolute;
left:25%;
right:25%;

width: 300px;

padding: 10px 55px 40px;

background-color: rgba(187, 255, 184, 0.65);
border: 10px solid white;   
box-shadow: 0 0 10px;
border-radius: 2px;
font-size: 20px;
}

body {
   background-image: url("login_bi.jpg ");
}

</style>
	</head>

	<body>


		
	<form method="post" action="adminsignin">
    <div class="container">
        <h1 style="text-align:center">Please<br> Log In</h1>
   <% if(session.getAttribute("errormsg")!=null) 
 	 {
 	 	out.print("<p style='color:red;'>Invalid Credentials");
 	 	session.removeAttribute("errormsg");
 	 }
  %>
        

<p >Admin Id: <input type="text" name="admin_id" value=""></p>
<p>Password: <input type="password" name="password" value=""></p>

<p><input  type="submit" name="LogIn" value="Log In" >
    <p><input type="submit" name="ForgotPassword" value="Forgot password">
<a href="index.jsp">Cancel</a></p>
</div>
</form>

	</body>	
	
</html>