package codeu.controller;

import codeu.model.data.UserProfile;
import codeu.model.store.basic.UserProfileStore;
import codeu.model.data.User;
import codeu.model.data.basic.UserStore;
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

	private UserStore userStore;

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
					int sizeOfTheList = list.size();
					List<UserProfile> profiles = profileuserStore.getUserProfileContent();
					if (!profiles.isEmpty()){
					UserProfile oneprofile = profiles.get(sizeOfTheList-1);
					String aboutme = oneprofile.getContent();
					request.setAttribute("userprofile", aboutme);
				}
        	request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
					int sizeOfTheList = list.size();
					String username = (String) request.getSession().getAttribute("user");
					String userValue = request.getParameter("userprofile");
					UserProfile firstprofile =
							new UserProfile(
									UUID.randomUUID(),
									UUID.randomUUID(),
									userValue);

					profileuserStore.addUserProfile(firstprofile);
					UserProfile testProfile = profileuserStore.getUserProfileContent().get(sizeOfTheList-1);

					System.out.println(userValue);
					response.sendRedirect("/profile/" + request.getSession().getAttribute("user"));
    }
}
