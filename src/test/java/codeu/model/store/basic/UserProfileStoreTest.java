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

public class UserProfileTest {

  private UserProfileStore userProfileStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final UserProfile PROFILE_ONE =
      new UserProfile(
          UUID.randomUUID(),
          UUID.randomUUID(),
          "bio one",
          Instant.ofEpochMilli(1000));
  private final UserProfile PROFILE_TWO =
      new UserProfile(
          UUID.randomUUID(),
          UUID.randomUUID(),
          "bio two",
          Instant.ofEpochMilli(1000));
  private final UserProfile PROFILE_THREE =
      new UserProfile(
          UUID.randomUUID(),
          UUID.randomUUID(),
          UUID.randomUUID(),
          "bio three",
          Instant.ofEpochMilli(1000));

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    userProfileStore = UserProfileStore.getTestInstance(mockPersistentStorageAgent);

    final List<UserProfile> userProfileList = new ArrayList<>();
    userProfileList.add(PROFILE_ONE);
    userProfileList.add(PROFILE_TWO);
    userProfileList.add(PROFILE_THREE);
    UserProfileStore.setUserProfiles(userProfileList);
  }

  @Test
  public void testGetUserProfileContent() {
    List<UserProfile> resultUserProfile = userProfileStore.getUserProfileContent(USER_ID_ONE);

    Assert.assertEquals(2, resultUserProfile.size());
    assertEquals(PROFILE_ONE, resultUserProfile.get(0));
    assertEquals(PROFILE_TWO, resultUserProfile.get(1));
  }

  @Test
  public void testAddUserProfile() {
    UserProfile inputUserProfile =
        new UserProfile(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "test message",
            Instant.now());

    userProfileStore.addUserProfile(inputUserProfile);
    UserProfile resultUserProfile = userProfileStore.getUserProfileContent(inputConversationId).get(0);

    assertEquals(inputMessage, resultMessage);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputMessage);
  }

  private void assertEquals(UserProfile expectedUserProfile, UserProfile actualUserProfile) {
    Assert.assertEquals(expectedUserProfile.getId(), actualUserProfile.getId());
    Assert.assertEquals(expectedUserProfile.getAuthorId(), actualUserProfile.getAuthorId());
    Assert.assertEquals(expectedUserProfile.getContent(), actualUserProfile.getContent());
  }
}
