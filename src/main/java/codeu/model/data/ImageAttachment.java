package codeu.model.data;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import javax.imageio.ImageIO;


public class ImageAttachment {

  private BufferedImage image;

  private UUID id;

  public ImageAttachment(UUID id, BufferedImage image) {
    this.image = image;
    this.id = id;
  }

  public ImageAttachment(UUID id, String base64String) {
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

  /**
   * Converts a String containing the bytes of an image into a BufferedImage object
   */
  private static BufferedImage getImageFromString(String base64String) throws IOException {
    byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64String);

    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
    return image;
  }

  /**
   * Converts the BufferedImage object of this object into a String, given an image format
   */
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
