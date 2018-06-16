package codeu.model.store.basic;

import codeu.model.data.UserProfile;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UserProfileStoreTest {

  private UserProfileStore userProfileStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final UUID ABOUT_PROFILE = UUID.randomUUID();
  private final UserProfile PROFILE_ONE =
      new UserProfile(
          UUID.randomUUID(),
          ABOUT_PROFILE,
          "bio one");

  private final UserProfile PROFILE_TWO =
      new UserProfile(
          UUID.randomUUID(),
          ABOUT_PROFILE,
          "bio two");

  private final UserProfile PROFILE_THREE =
      new UserProfile(
          UUID.randomUUID(),
          UUID.randomUUID(),
          "bio three");


  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    userProfileStore = UserProfileStore.getTestInstance(mockPersistentStorageAgent);

    final List<UserProfile> userProfileList = new ArrayList<>();
    userProfileList.add(PROFILE_ONE);
    userProfileList.add(PROFILE_TWO);
    userProfileList.add(PROFILE_THREE);
    userProfileStore.setUserProfiles(userProfileList);
  }

  @Test
  public void testGetUserProfileContent() {
    List<UserProfile> resultUserProfiles = userProfileStore.getUserProfileContent();

    Assert.assertEquals(3, resultUserProfiles.size());
    assertEquals(PROFILE_ONE, resultUserProfiles.get(0));
    assertEquals(PROFILE_TWO, resultUserProfiles.get(1));
    assertEquals(PROFILE_THREE, resultUserProfiles.get(2));
  }

  @Test
  public void testAddUserProfile() {
    UUID inputUserProfile = UUID.randomUUID();
    UserProfile inputBio =
        new UserProfile(
            UUID.randomUUID(),
            inputUserProfile,
            "test bio");

    userProfileStore.addUserProfile(inputBio);
    UserProfile resultUserProfile = userProfileStore.getUserProfileContent().get(3);

    assertEquals(inputBio, resultUserProfile);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputBio);
  }

  private void assertEquals(UserProfile expectedUserProfile, UserProfile actualUserProfile) {
    Assert.assertEquals(expectedUserProfile.getId(), actualUserProfile.getId());
    Assert.assertEquals(expectedUserProfile.getAuthorId(), actualUserProfile.getAuthorId());
    Assert.assertEquals(expectedUserProfile.getContent(), actualUserProfile.getContent());
  }
}
