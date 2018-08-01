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

  private String imageType;

  public ImageAttachment(UUID id, BufferedImage image, String imageType) {
    this.image = image;
    this.id = id;
    this.imageType = imageType;
  }

  public ImageAttachment(UUID id, String base64String, String imageType) {
    try {
      this.image = getImageFromString(base64String);
    } catch (IOException e) {}

    this.id = id;
    this.imageType = imageType;
  }

  public BufferedImage getImage() {
    return image;
  }

  public UUID getId() {
    return id;
  }

  public String getImageType() {
    return imageType;
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
   * Converts the BufferedImage object of this object into a String
   */
  public String getBase64String() {
	  ByteArrayOutputStream os = new ByteArrayOutputStream();

	  try {
	    ImageIO.write(image, imageType, os);
	    return Base64.getEncoder().encodeToString(os.toByteArray());
	  }
	  catch (IOException ioe) {
		  ioe.printStackTrace();
	    return "";
	  }
  }

}
