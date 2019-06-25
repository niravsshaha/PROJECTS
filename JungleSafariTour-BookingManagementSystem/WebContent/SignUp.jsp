<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sign Up</title>
<style>
body {font-family: Arial, Helvetica, sans-serif;}
* {box-sizing: border-box}

/* Full-width input fields */
input[type=text], input[type=password] ,input[type=email] {
    width: 100%;
    padding: 15px;
    margin: 5px 0 22px 0;
    display: inline-block;
    border: none;
    background: #f1f1f1;
}

input[type=text]:focus, input[type=password]:focus {
    background-color: #ddd;
    outline: none;
}

hr {
    border: 1px solid #f1f1f1;
    margin-bottom: 25px;
}

/* Set a style for all buttons */
button {
    background-color: #4CAF50;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    cursor: pointer;
    width: 100%;
    opacity: 0.9;
}

button:hover {
    opacity:0.5;
}

/* Extra styles for the cancel button */
.cancelbtn {
    padding: 14px 20px;
    background-color: #f44336;
}

/* Float cancel and signup buttons and add an equal width */
.cancelbtn, .signupbtn {
  float: left;
  width: 50%;
}

/* Add padding to container elements */
.container {
        width: 960px;
height:800px;
margin:50px auto;
font-family: 'Droid Serif', serif;
position:absolute;
left:25%;
right:25%;

width: 600px;

padding: 10px 55px 40px;

background-color: rgba(187, 255, 184, 0.65);
border: 10px solid white;   
box-shadow: 0 0 10px;
border-radius: 2px;
font-size: 20px;
    
}

/* Clear floats */
.clearfix::after {
    content: "";
    clear: both;
    display: table;
}

/* Change styles for cancel button and signup button on extra small screens */
@media screen and (max-width: 300px) {
    .cancelbtn, .signupbtn {
       width: 100%;
    }
}

body {
   background-image: url("login_bi.jpg ");
}


</style>

</head>



<body>

<form  style="border:1px solid #ccc ;width:45% ;margin-left:25%;margin-right: 25%"  method="post" action="SignUpServlet" >
  <div class="container">
    <h4>Sign Up</h4>
  
   <hr>
    <p id="name">Enter name <input type="text" name="name" value="" required></p>
     <p id="city">Enter city <input type="text" name="city" value="" required></p>
    
     <p id="phone">Enter Phone Number <input type="text" name="phone_no" value="" required></p>
   <p id="email">Enter Email Id <br><input type="email" name="emailid" value="" required></p>
   
<p id="password">Enter Password <input type="password" name="password" value="" required ></p>

<p id="repeat_password">Reenter Password <input type="password" name="repeat_password" value="" required></p>

     

    <div class="clearfix">
	<p style="text-align:center";><input  type="submit" name="SignUp" value="Sign Up" >
      <a href="index.jsp">Cancel</a></p>
    </div>
  </div>
</form>


</body>
</html>