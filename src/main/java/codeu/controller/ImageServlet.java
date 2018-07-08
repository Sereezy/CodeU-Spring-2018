
package codeu.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageServlet extends HttpServlet {

  private ImageStore imageStore;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    response.setContentType("image/png");

    BufferedImage bi = ImageIO.read(f);
    OutputStream out = response.getOutputStream();
    ImageIO.write(bi, "png", out);
    out.close();
  }
}
