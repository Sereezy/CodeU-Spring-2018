package codeu.model.data;

import java.util.UUID;

public class UserProfile {

  private final UUID id;
  private final UUID author;
  private String content;

  /**
	 * Constructs a new Profile
	 *
	 * @param id the ID of the profile
	 * @param author the author of the profile
	 * @param content the About Me section text
	 */

  public UserProfile(UUID id, UUID author, String content) {
    this.id = id;
    this.author = author;
    this.content = content;
  }
  public UUID getId() {
    return this.id;
  }

  public String getContent() {
    return this.content;
  }
  /**
	 * Updates the About Me text for a user's profile
	 */
	public void setContentText(String content) {
		this.content = content;
	}

	/**
	 * @return the author of the profile
	 */
	public UUID getAuthorId() {
		return author;
	}
}
