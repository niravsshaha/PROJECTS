package admin_login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/tp_add")
public class tp_add extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		tp_details tp =new tp_details();
		tp.setPname(request.getParameter("pname"));
		
		tp.setNo_of_days(Integer.parseInt(request.getParameter("no_of_days")));
		
		tp.setNo_of_seats_available(Integer.parseInt(request.getParameter("no_of_seats_available")));
		
		tp.setA_rate(Integer.parseInt(request.getParameter("a_rate")));
		
		tp.setC_rate(Integer.parseInt(request.getParameter("c_rate")));
		
		tp.setDetails(request.getParameter("details"));
		
		tp_add_dao dao=new tp_add_dao();
		
		
		HttpSession session=request.getSession();
		String admin_id1=(String)session.getAttribute("admin_id");
		int admin_id=Integer.parseInt(admin_id1);
		if(dao.check(tp,admin_id))
		{	
			response.sendRedirect("admin.jsp");
		}
		else
		{	
			response.sendRedirect("admin_tp_add.jsp");
		}
		
	}

}
