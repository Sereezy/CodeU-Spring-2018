package codeu.helper;

/**
 * Helper class that includes a method for the ProfilePageServlet 
 * class.
 *
 */
public class ProfileHelper {
	/**
	 * 
	 * @param user 
	 * 			-- the name of the current user viewing the profile
	 * @param profileAuthor
	 * 			-- the author of the profile
	 * @return check if the two names match
	 */
	public static boolean isSameUser(String user, String profileAuthor) {
		if (user != null && profileAuthor.equals(user)) {
			return true;
		}
		
		return false;
	}
	
}
