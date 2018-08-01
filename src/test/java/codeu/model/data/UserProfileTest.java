package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class UserProfileTest {

  @Test
  public void testCreate() {
    UUID id = UUID.randomUUID();
    UUID author = UUID.randomUUID();
    String content = "test content";

    UserProfile userProfile = new UserProfile(id, author, content);

    Assert.assertEquals(id, userProfile.getId());
    Assert.assertEquals(author, userProfile.getAuthorId());
    Assert.assertEquals(content, userProfile.getContent());
  }
}
