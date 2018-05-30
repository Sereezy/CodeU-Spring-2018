package codeu.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProfileServlet extends HttpServlet {
	  @Override
    public void init() throws ServletException {
        super.init();

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
					String userValue = request.getParameter("about");
					System.out.println(userValue);
					response.sendRedirect("/profile/" + request.getSession().getAttribute("user"));
    }
}
