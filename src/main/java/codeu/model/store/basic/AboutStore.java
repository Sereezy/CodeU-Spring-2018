package codeu.model.store.basic;

import codeu.model.data.About;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AboutStore {

  private static AboutStore instance;

  public static AboutStore getInstance() {
    if (instance == null) {
      instance = new AboutStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * @param persistentStorageAgent
   */

   public static AboutStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
     return new AboutStore(persistentStorageAgent);
   }

   private PersistentStorageAgent persistentStorageAgent;

   private List<About> about;

   private AboutStore(PersistentStorageAgent persistentStorageAgent) {
     this.persistentStorageAgent = persistentStorageAgent;
     about = new ArrayList<>();
   }

   public void addAbout(About about) {
     about.add(about);
     persistentStorageAgent.writeThrough(about);
   }

   public void setAbout(List<About> about) {
     this.about = about;
   }

}
