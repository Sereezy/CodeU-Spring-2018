package codeu.model.data;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import javax.imageio.ImageIO;


public class ImageMessage {

  private BufferedImage image;

  private UUID id;

  public ImageMessage(UUID id, BufferedImage image) {
    this.image = image;
    this.id = id;
  }

  public ImageMessage(UUID id, String base64String) {
    try {
      this.image = getImageFromString(base64String);
    } catch (IOException e) {}

    this.id = id;
  }

  public BufferedImage getImage() {
    return image;
  }

  public UUID getId() {
    return id;
  }

  private BufferedImage getImageFromString(String base64String) throws IOException {
    byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64String);

    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
    return image;
  }

  public String getBase64String(String format) {
	  ByteArrayOutputStream os = new ByteArrayOutputStream();

	  try {
	    ImageIO.write(image, format, os);
	    return Base64.getEncoder().encodeToString(os.toByteArray());
	  }
	  catch (IOException ioe) {
		  ioe.printStackTrace();
	    return "";
	  }
  }

}
