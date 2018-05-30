package codeu.model.data;

import java.util.UUID;

public class About {

  private final UUID author;
  private final String content;

  /**
   * Constructs a new Message.
   * @param content the text content of this Message
   */
  public About(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

}
