package admin_login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import login_signup.logindao;
import login_signup.userbean;


@WebServlet("/adminsignin")
public class adminsignin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		
		String admin_id =request.getParameter("admin_id");
		String password=request.getParameter("password");
		admindao dao=new admindao();
		if(dao.check(admin_id, password))
		{
			HttpSession session=request.getSession();
			session.setAttribute("admin_id", admin_id);
			response.sendRedirect("admin.jsp");
		}
		else
		{
			HttpSession session=request.getSession();
			session.setAttribute("errormsg", "Invalid");
			response.sendRedirect("adminsign.jsp");
		}
	}
	

}
