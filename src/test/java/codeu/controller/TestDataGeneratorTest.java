package codeu.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.persistence.PersistentStorageAgent;

public class TestDataGeneratorTest {

  private PersistentStorageAgent mockPersistentStorageAgent;
  private TestDataGenerator mockTestDataGenerator;

  private UserStore mockUserStore;
  private ConversationStore mockConversationStore;
  private MessageStore mockMessageStore;

  @Before
  public void setup() throws PersistentDataStoreException {

    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    Mockito.when(mockPersistentStorageAgent.loadUsers()).thenReturn(new ArrayList<User>());
    Mockito.when(mockPersistentStorageAgent.loadConversations()).thenReturn(new ArrayList<Conversation>());
    Mockito.when(mockPersistentStorageAgent.loadMessages()).thenReturn(new ArrayList<Message>());

    mockTestDataGenerator = TestDataGenerator.getTestInstance(mockPersistentStorageAgent);

    setUpStores();
  }

  void setUpStores() {

    mockUserStore = UserStore.getTestInstance(mockPersistentStorageAgent);
    mockTestDataGenerator.setUserStore(mockUserStore);

    mockConversationStore = ConversationStore.getTestInstance(mockPersistentStorageAgent);
    mockTestDataGenerator.setConversationStore(mockConversationStore);

    mockMessageStore = MessageStore.getTestInstance(mockPersistentStorageAgent);
    mockTestDataGenerator.setMessageStore(mockMessageStore);

  }

  @Test
  public void testAddTestUsers() {

    mockTestDataGenerator.addTestUsers(3);

    int numUsersAfter = mockUserStore.getAllUsers().size();

    assertEquals(3, numUsersAfter);

  }

  @Test
  public void testAddTestConversations_NoTestUsers() {

    mockTestDataGenerator.addTestConversations(3);

    int numConversationsAfter = mockConversationStore.getAllConversations().size();

    assertEquals(0, numConversationsAfter);

  }

  @Test
  public void testAddTestConversations() {

    mockTestDataGenerator.addTestUsers(3);
    mockTestDataGenerator.addTestConversations(3);

    int numConversationsAfter = mockConversationStore.getAllConversations().size();

    assertEquals(3, numConversationsAfter);

  }

  @Test
  public void testAddTestMessages_NoTestUsers() {

    mockTestDataGenerator.addTestMessages(3);

    int numMessagesAfter = mockMessageStore.getAllMessages().size();

    assertEquals(0, numMessagesAfter);

  }

  @Test
  public void testAddTestMessages_NoTestConversations() {

    mockTestDataGenerator.addTestUsers(3);
    mockTestDataGenerator.addTestMessages(3);

    int numMessagesAfter = mockMessageStore.getAllMessages().size();

    assertEquals(0, numMessagesAfter);

  }

  @Test
  public void testAddTestMessages() {

    mockTestDataGenerator.addTestUsers(3);
    mockTestDataGenerator.addTestConversations(3);
    mockTestDataGenerator.addTestMessages(3);

    int numMessagesAfter = mockMessageStore.getAllMessages().size();

    assertEquals(3, numMessagesAfter);

  }

  @Test
  public void testClearTestData() {

    mockTestDataGenerator.addTestUsers(3);
    mockTestDataGenerator.addTestConversations(3);
    mockTestDataGenerator.addTestMessages(3);

    int numUsersAfter = mockUserStore.getAllUsers().size();
    int numConversationsAfter = mockConversationStore.getAllConversations().size();
    int numMessagesAfter = mockMessageStore.getAllMessages().size();

    assertEquals(3, numUsersAfter);
    assertEquals(3, numConversationsAfter);
    assertEquals(3, numMessagesAfter);

    mockTestDataGenerator.clearTestData();

    int numUsersAfterClear = mockUserStore.getAllUsers().size();
    int numConversationsAfterClear = mockConversationStore.getAllConversations().size();
    int numMessagesAfterClear = mockMessageStore.getAllMessages().size();

    assertEquals(0, numUsersAfterClear);
    assertEquals(0, numConversationsAfterClear);
    assertEquals(0, numMessagesAfterClear);

  }

}
