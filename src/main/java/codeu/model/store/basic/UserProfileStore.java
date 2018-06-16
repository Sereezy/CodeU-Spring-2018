package codeu.model.store.basic;

import codeu.model.data.UserProfile;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserProfileStore {

  private static UserProfileStore instance;

  public static UserProfileStore getInstance() {
    if (instance == null) {
      instance = new UserProfileStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * @param persistentStorageAgent
   */

   public static UserProfileStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
     return new UserProfileStore(persistentStorageAgent);
   }

   private PersistentStorageAgent persistentStorageAgent;

   private List<UserProfile> userprofiles;

   private UserProfileStore(PersistentStorageAgent persistentStorageAgent) {
     this.persistentStorageAgent = persistentStorageAgent;
     userprofiles = new ArrayList<>();
   }

   public void addUserProfile(UserProfile userprofile) {
     userprofiles.add(userprofile);
     persistentStorageAgent.writeThrough(userprofile);
   }

   public void setUserProfiles(List<UserProfile> userprofiles) {
     this.userprofiles = userprofiles;
   }

   public List<UserProfile> getUserProfileContent() {
     return userprofiles;
   }

}
