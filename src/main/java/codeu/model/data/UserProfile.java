package codeu.model.data;

import java.util.UUID;

public class UserProfile {

  private final UUID id;
  private final UUID author;
  private final String content;

  /**
   * @param content the text content of this Message
   */
  public UserProfile(UUID id, UUID author, String content) {
    this.id = id;
    this.author = author;
    this.content = content;
  }
  public UUID getId() {
    return id;
  }

  public UUID getAuthorId() {
    return author;
  }

  public String getContent() {
    return content;
  }
}
