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

    int numUsersBefore = mockUserStore.getAllUsers().size();

    mockTestDataGenerator.addTestUsers(3);

    int numUsersAfter = mockUserStore.getAllUsers().size();

    assertEquals(numUsersBefore + 3, numUsersAfter);

  }

  @Test
  public void testAddTestConversations_NoTestUsers() {

    int numConversationsBefore = mockConversationStore.getAllConversations().size();

    mockTestDataGenerator.addTestConversations(3);

    int numConversationsAfter = mockConversationStore.getAllConversations().size();

    assertEquals(numConversationsBefore, numConversationsAfter);

  }

  @Test
  public void testAddTestConversations() {

    int numConversationsBefore = mockConversationStore.getAllConversations().size();

    mockTestDataGenerator.addTestUsers(3);
    mockTestDataGenerator.addTestConversations(3);

    int numConversationsAfter = mockConversationStore.getAllConversations().size();

    assertEquals(numConversationsBefore + 3, numConversationsAfter);

  }

  @Test
  public void testAddTestMessages_NoTestUsers() {

    int numMessagesBefore = mockMessageStore.getAllMessages().size();

    mockTestDataGenerator.addTestMessages(3);

    int numMessagesAfter = mockMessageStore.getAllMessages().size();

    assertEquals(numMessagesBefore, numMessagesAfter);

  }

  @Test
  public void testAddTestMessages_NoTestConversations() {

    int numMessagesBefore = mockMessageStore.getAllMessages().size();

    mockTestDataGenerator.addTestUsers(3);
    mockTestDataGenerator.addTestMessages(3);

    int numMessagesAfter = mockMessageStore.getAllMessages().size();

    assertEquals(numMessagesBefore, numMessagesAfter);

  }

  @Test
  public void testAddTestMessages() {

    int numMessagesBefore = mockMessageStore.getAllMessages().size();

    mockTestDataGenerator.addTestUsers(3);
    mockTestDataGenerator.addTestConversations(3);
    mockTestDataGenerator.addTestMessages(3);

    int numMessagesAfter = mockMessageStore.getAllMessages().size();

    assertEquals(numMessagesBefore + 3, numMessagesAfter);

  }

  @Test
  public void testClearTestData() {

    int numUsersBefore = mockUserStore.getAllUsers().size();
    int numConversationsBefore = mockConversationStore.getAllConversations().size();
    int numMessagesBefore = mockMessageStore.getAllMessages().size();

    mockTestDataGenerator.addTestUsers(3);
    mockTestDataGenerator.addTestConversations(3);
    mockTestDataGenerator.addTestMessages(3);

    int numUsersAfter = mockUserStore.getAllUsers().size();
    int numConversationsAfter = mockConversationStore.getAllConversations().size();
    int numMessagesAfter = mockMessageStore.getAllMessages().size();

    assertEquals(numUsersBefore + 3, numUsersAfter);
    assertEquals(numConversationsBefore + 3, numConversationsAfter);
    assertEquals(numMessagesBefore + 3, numMessagesAfter);

    mockTestDataGenerator.clearTestData();

    int numUsersAfterClear = mockUserStore.getAllUsers().size();
    int numConversationsAfterClear = mockConversationStore.getAllConversations().size();
    int numMessagesAfterClear = mockMessageStore.getAllMessages().size();

    assertEquals(0, numUsersAfterClear);
    assertEquals(0, numConversationsAfterClear);
    assertEquals(0, numMessagesAfterClear);

  }

}
