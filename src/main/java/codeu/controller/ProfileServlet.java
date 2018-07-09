package codeu.controller;

import codeu.model.data.UserProfile;
import codeu.model.store.basic.UserProfileStore;
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

	private UserProfileStore profileuserStore;

	  @Override
    public void init() throws ServletException {
        super.init();
				setUserProfileStore(UserProfileStore.getInstance());

    }
		void setUserProfileStore(UserProfileStore userprofileStore) {
			this.profileuserStore = userprofileStore;
		}

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
					List<UserProfile> profiles = profileuserStore.getUserProfileContent();
					if (!profiles.isEmpty()){
					UserProfile oneprofile = profiles.get(.size-1);
					String aboutme = oneprofile.getContent();
					request.setAttribute("userprofile", aboutme);
				}
        	request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
					String username = (String) request.getSession().getAttribute("user");
					String userValue = request.getParameter("userprofile");
					UserProfile firstprofile =
							new UserProfile(
									UUID.randomUUID(),
									UUID.randomUUID(),
									userValue);

					profileuserStore.addUserProfile(firstprofile);
					UserProfile testProfile = profileuserStore.getUserProfileContent().get(.size-1);

					System.out.println(userValue);
					response.sendRedirect("/profile/" + request.getSession().getAttribute("user"));
    }
}
