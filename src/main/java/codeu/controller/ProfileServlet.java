package codeu.controller;

import codeu.model.data.About;
import codeu.model.store.basic.AboutStore;
import java.util.List;
import java.util.UUID;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class ProfileServlet extends HttpServlet {
	  @Override
    public void init() throws ServletException {
        super.init();
				setAboutStore(AboutStore.getinstance());

    }
		void setAboutStore(AboutStore aboutStore) {
			this.aboutStore = aboutStore;
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
