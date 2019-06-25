<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page
	import ="java.sql.Connection,
 java.sql.DriverManager,
 java.sql.PreparedStatement, java.sql.ResultSet"
 %>
 
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tadoba</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
 <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<style>.
.icon-bar {
  position: fixed;
  top: 50%;
  transform: translateY(-50%);
}

.icon-bar a {
  display: block;
  text-align: center;
  padding: 16px;
  transition: all 0.3s ease;
  color: white;
  font-size: 20px;
}

.icon-bar a:hover {
    background-color: #000;
}

.facebook {
  background: #3B5998;
  color: white;
}

.twitter {
  background: #55ACEE;
  color: white;
}

.google {
  background: #dd4b39;
  color: white;
}

.linkedin {
  background: #007bb5;
  color: white;
}

.youtube {
  background: #bb0000;
  color: white;
}
body, html {
  height: 100%;
  margin: 0;
  font: 400 15px/1.8 "Lato", sans-serif;
  color: black;
}

.mySlides {display: none;}
img {vertical-align: middle;}

/* Slideshow container */
.slideshow-container 
{
  max-width: 1000px;
  position: relative;
  margin: auto;
}



/* Number text (1/3 etc) */
.numbertext {
  color: #f2f2f2;
  font-size: 12px;
  padding: 8px 12px;
  position: absolute;
  top: 0;
}

/* The dots/bullets/indicators */
.dot {
  height: 15px;
  width: 15px;
  margin: 0 2px;
  background-color: #bbb;
  border-radius: 50%;
  display: inline-block;
  transition: background-color 0.6s ease;
}

.active {
  background-color: #717171;
}

/* Fading animation */
.fade {
  -webkit-animation-name: fade;
  -webkit-animation-duration: 1.5s;
  animation-name: fade;
  animation-duration: 1.5s;
}



/* On smaller screens, decrease text size */
@media only screen and (max-width: 300px) {
  .text {font-size: 11px}
}

/*navbar----*/
#navbar 
{
  overflow: hidden;
  background-color: black;
  
   padding: 50px 10px; /* Large padding which will shrink on scroll (using JS) */
  transition: 0.4s;
  top: 0;
  width: 100%;
   z-index: 50;
}
#navbar a 
{
  float: left;
  display: block;
  color: #f2f2f2;
  text-align: center;
 padding: 12px;
  text-decoration: none;
   font-size: 18px; 
  line-height: 25px;
  border-radius: 4px;
}

#navbar a:hover {
  background: #ddd;
  color: black;
}
#navbar #logo {
  font-size: 35px;
  font-weight: bold;
  transition: 0.4s;
}
#navbar a.active {
  background-color: dodgerblue;
  color: white;
}

#navbar-right {
  float: right;
}
@media screen and (max-width: 580px) 
{
  #navbar {
    padding: 20px 10px !important;
  }
  #navbar a {
    float: none;
    display: block;
    text-align: left;
  }
  #navbar-right 
  {
    float: none;
  }
}
  .footer 
  {
   
   left: 0;
   bottom: 0;
   height:30%; 
   width: 100%;
   background-color: black ;
   color: white;
   text-align: center;
   display: block;
}

</style>
</head>
<body>



<div id="navbar">
  <a href="#default" id="logo">CompanyLogo</a>
  <div id="navbar-right">
    <a class="active" href="#home"><i class="fa fa-fw fa-home"></i>Home </a>
  <a href="#Sanctuaries"><i class="fa fa-fw fa-search"></i>Sanctuaries</a>
  <% if(session.getAttribute("emailid")==null && session.getAttribute("admin_id")==null) 
  {
  	out.print("<a href='LoginPage.jsp'><i class='fa fa-fw fa-user'></i>Sign In</a>");
  	out.print("<a href='SignUp.jsp'><i class='fa fa-fw fa-user'></i>Sign Up</a>");
  	out.print("<a href='adminsign.jsp'><i class='fa fa-fw fa-user'></i>admin</a>");

  }
  else 
  {
	  out.print("<a href='LogoutServlet'><i class='fa fa-fw fa-user'></i>Log Out</a>");
  }
  %>
  <a href="#contact"><i class="fa fa-fw fa-envelope"></i>Contact</a>
</div>
</div>

<br>
<br>
<br>


      <br>
      <h1 style=" font-size:50px; text-align:center;">| Tadoba National Park</h1>
      <br>
      <br>
      <div class="container" style="width:60%;">
  <div id="myCarousel" class="carousel slide" data-ride="carousel">
    <!-- Indicators -->
    <ol class="carousel-indicators">
      <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
      <li data-target="#myCarousel" data-slide-to="1"></li>
      <li data-target="#myCarousel" data-slide-to="2"></li>
    </ol>

    <!-- Wrapper for slides -->
    <div class="carousel-inner">
      <div class="item active">
        <img src="t2.jpg" alt="Los Angeles" style="width:100%;height: 50%;">
      </div>

      <div class="item">
        <img src="t3.jpg" alt="Chicago" style="width:100%;height: 50%;">
      </div>
    
      <div class="item">
        <img src="t1.jpg" alt="New york" style="width:100%;height: 50%;">
      </div>
    </div>

    <!-- Left and right controls -->
    <a class="left carousel-control" href="#myCarousel" data-slide="prev">
      <span class="glyphicon glyphicon-chevron-left"></span>
      <span class="sr-only">Previous</span>
    </a>
    <a class="right carousel-control" href="#myCarousel" data-slide="next">
      <span class="glyphicon glyphicon-chevron-right"></span>
      <span class="sr-only">Next</span>
    </a>
  </div>
</div>

<br><br>
     
  <div role="main" >
 
    <p><h2> @ Nagpur, Maharashtra. Established since 1955. Famous for – Tigers, Leopards, Sloth bears, Wild dogs, Migratory Birds, Eagles & Owls, Boat Safari, Jungle Safari</h2></p>
    <br>
    <p style="font-size: 20px;">Situated in the core of a reserved forest and spread in the area of 1727 sq. km, Tadoba National Park lies in the Chandrapur district of Maharashtra (India). One of the largest and oldest national park,  which has endless treasure trove of large number of species of trees & plants and wildlife like panthers, tigers, hyenas, sloth bears, wild dogs, jackals, barking deer, bison, sambar, herd of deer to name a few. </p>
 
      <table  class="table table-striped table table-bordered" >
    
      <tr>
      <td style="width: 50%;">
        <h3>| Flora in Tadoba </h3>
        <p>The most popular species of trees is Teak and bamboo in this forest. Other common trees include:</p>
        <ul >
            <li >Ain (Crocodile Bark)</li>
            <li>Bija</li>
            <li>Dhaudab</li>
            <li>Hald</li>
            <li>Salai</li>
            <li>Semal</li>
            <li>Shisham</li>
        </ul>
        </td>
        <td>
        </td>
        <td>
            <h3 style="text-align: center;"> | Fauna in Tadoba </h3>
            <p> Tadoba National Park is a popular tiger reserve that’s affably known as ‘The Land of Tigers’ as large number of tigers (approx.43) are found here.</p>
             <br>
          <ul>
              <li>Hyenas</li>
              <li>Spotted Deer</li>
              <li>Wild Boars</li>
              <li>Barking deer</li>
              <li>Gaurs</li>
              <li>Four horned Antelopes</li>
          </ul>
          </td>
      </tr>
      </table>
    </div>




<%
String sql="select * from package where admin_id=1";
String url="jdbc:mysql://localhost:3306/wildlife";
String username="root";
String pass="579453";


//try {
	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection(url,username,pass);
	PreparedStatement st=con.prepareStatement(sql);
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
 
 
 out.println("<hr><hr>");
 

} 

%>
<div class="footer">

</div>
<script >
function scrollFunction() {
    if (document.body.scrollTop > 80 || document.documentElement.scrollTop > 80) {
        document.getElementById("navbar").style.padding = "30px 10px";
        document.getElementById("logo").style.fontSize = "25px";
    } else {
        document.getElementById("navbar").style.padding = "80px 10px";
        document.getElementById("logo").style.fontSize = "35px";
    }
}
}
</script>


</body>
</html>