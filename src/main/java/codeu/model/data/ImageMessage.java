package codeu.model.data;

import java.awt.Image;
import java.time.Instant;
import java.util.UUID;


public class ImageMessage extends Message {

  private Image image;

  public ImageMessage(UUID id, UUID conversation, UUID author, String content, Instant creation) {
    super(id, conversation, author, content, creation);
  }

  @Override
  public String getContent() {
    return "<img src=https://storage.googleapis.com/gd-wagtail-prod-assets/original_images/evolving_google_identity_videoposter_006.jpg width=500>";
  }
}
