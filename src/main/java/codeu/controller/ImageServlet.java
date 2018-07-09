
package codeu.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import codeu.model.data.ImageMessage;
import codeu.model.store.basic.ImageStore;

public class ImageServlet extends HttpServlet {

  private ImageStore imageStore;

  @Override
  public void init() throws ServletException {
    super.init();

    setImageStore(ImageStore.getInstance());
  }

  void setImageStore(ImageStore imageStore) {
    this.imageStore = imageStore;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) {

    String requestUrl = request.getRequestURI();
    String imageId = requestUrl.substring("/image/".length());
    UUID imageUUID = UUID.fromString(imageId);

    ImageMessage imageMessage = imageStore.getImage(imageUUID);

    if (imageMessage == null) {
      System.out.println("No image found with id: " + imageId);
      return;
    }
    BufferedImage image = imageMessage.getImage();
    response.setContentType("image/png");

    try {
      OutputStream out = response.getOutputStream();
      ImageIO.write(image, "png", out);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
