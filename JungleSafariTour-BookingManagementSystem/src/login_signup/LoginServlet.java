package login_signup;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		
		String emailid =request.getParameter("emailid");
		String password=request.getParameter("password");
		logindao dao=new logindao();
		userbean u1=new userbean();
		if(dao.check(emailid, password,u1))
		{
			HttpSession session=request.getSession();
			session.setAttribute("emailid", emailid);
			session.setAttribute("user_id", u1.getUser_id());
			response.sendRedirect("index.jsp");
		}
		else
		{
			HttpSession session=request.getSession();
			session.setAttribute("errormsg", "Invalid");
			response.sendRedirect("LoginPage.jsp");
		}
	}

}
