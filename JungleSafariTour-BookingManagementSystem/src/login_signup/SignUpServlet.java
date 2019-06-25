package login_signup;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		userbean user=new userbean();
		//System.out.println("hi in sign up servlet");
		user.setName(request.getParameter("name"));
		user.setCity(request.getParameter("city"));
		user.setPhone_no(request.getParameter("phone_no"));
		user.setEmailid(request.getParameter("emailid"));
		user.setPassword(request.getParameter("password"));
		
		if(signupdao.insert(user))
		{
			response.sendRedirect("index.jsp");
		}
		else
		{
			response.sendRedirect("SignUp.jsp");
		}
		
		
	}

}
