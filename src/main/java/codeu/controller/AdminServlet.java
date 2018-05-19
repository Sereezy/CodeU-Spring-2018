package codeu.controller;

import java.io.IOException;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminServlet extends HttpServlet {

    private HashSet<String> admins;

    @Override
    public void init() throws ServletException {
        super.init();

        admins = new HashSet<String>();
        admins.add("daniel");
        admins.add("leslie");
        admins.add("serena");
        admins.add("shana");
        admins.add("kyra");
    }


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        String user = (String) request.getSession().getAttribute("user");

        if (admins.contains(user)) {
            request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

    }
}
