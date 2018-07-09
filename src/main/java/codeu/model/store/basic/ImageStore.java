
package codeu.model.store.basic;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import codeu.model.data.ImageAttachment;
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

  private List<ImageAttachment> images;

  private ImageStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;

    images = new ArrayList<ImageAttachment>();
  }

  public void addImage(ImageAttachment imageAttachment) {
    images.add(imageAttachment);
    persistentStorageAgent.writeThrough(imageAttachment);
  }

  public ImageAttachment getImage(UUID id) {
    for (ImageAttachment image : images) {
      if (image.getId().equals(id)) {
        return image;
      }
    }
    return null;
  }

  public void setImages(List<ImageAttachment> images) {
    this.images = images;
  }
}
