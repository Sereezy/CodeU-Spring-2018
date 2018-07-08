
package codeu.model.store.basic;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import codeu.model.data.ImageMessage;
import codeu.model.store.persistence.PersistentStorageAgent;

public class ImageStore {

  private static ImageStore instance;

  public static ImageStore getInstance() {
    if (instance == null) {
      instance = new ImageStore(PersistentStorageAgent.getInstance());
    }

    return instance;
  }

  private PersistentStorageAgent persistentStorageAgent;

  private List<ImageMessage> images;

  private ImageStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;

    images = new ArrayList<ImageMessage>();
  }

  public void addImage(ImageMessage im) {
    images.add(im);
    persistentStorageAgent.writeThrough(im);
  }

  public ImageMessage getImage(UUID id) {
    for (ImageMessage im : images) {
      if (im.getId().equals(id)) {
        return im;
      }
    }
    return null;
  }

  public void setImages(List<ImageMessage> images) {
    this.images = images;
  }
}
