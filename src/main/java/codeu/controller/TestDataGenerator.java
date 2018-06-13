package codeu.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.persistence.PersistentStorageAgent;

class TestDataGenerator {

  private static TestDataGenerator instance;

  public static TestDataGenerator getInstance() {
    if (instance == null) {
      instance = new TestDataGenerator(PersistentStorageAgent.getInstance());
      instance.init();
    }

    return instance;
  }

  public static TestDataGenerator getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new TestDataGenerator(persistentStorageAgent);
  }

  private PersistentStorageAgent persistentStorageAgent;

  private UserStore userStore;
  private ConversationStore conversationStore;
  private MessageStore messageStore;

  private List<User> testUsers;
  private List<Conversation> testConversations;
  private List<Message> testMessages;

  private TestDataGenerator(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;

    testUsers = new ArrayList<User>();
    testConversations = new ArrayList<Conversation>();
    testMessages = new ArrayList<Message>();
  }

  private void init() {
    setUserStore(UserStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
  }

  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }
  public UserStore getUserStore() {
    return userStore;
  }

  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }
  public ConversationStore getConversationStore() {
    return conversationStore;
  }

  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }
  public MessageStore getMessageStore() {
    return messageStore;
  }

  void addTestData() {
    int numberOfUsers = ThreadLocalRandom.current().nextInt(10, 101);
    int numberOfConversations = ThreadLocalRandom.current().nextInt(5, 21);
    int numberOfMessages = ThreadLocalRandom.current().nextInt(100, 1001);

    addTestUsers(numberOfUsers);

    addTestConversations(numberOfConversations);

    addTestMessages(numberOfMessages);
  }

  public void addTestUsers(int numberOfUsers) {
    User newUser;
    for (int i = 0; i < numberOfUsers; i++) {
      newUser = new User(UUID.randomUUID(), "user"+testUsers.size(), "password",
          Instant.ofEpochSecond(ThreadLocalRandom.current().nextInt(1, 10001)));

      userStore.addVolatileUser(newUser);
      testUsers.add(newUser);
    }
  }

  public void addTestConversations(int numberOfConversations) {
    if (testUsers.size() == 0) {
      System.out.println("Unable to create test conversations. Please create test users first.");
      return;
    }

    Conversation newConversation;
    User owner;
    for (int i = 0; i < numberOfConversations; i++) {
      owner = testUsers.get(ThreadLocalRandom.current().nextInt(0, testUsers.size()));
      newConversation = new Conversation(UUID.randomUUID(), owner.getId(), "conversation"+testConversations.size(),
          Instant.now());

      conversationStore.addVolatileConversation(newConversation);
      testConversations.add(newConversation);
    }
  }

  public void addTestMessages(int numberOfMessages) {
    if (testConversations.size() == 0) {
      System.out.println("Unable to create test messages. Please create test conversations first.");
      return;
    }

    Message newMessage;
    Conversation conversation;
    User author;
    int numberOfWords;
    String content;
    for (int i = 0; i < numberOfMessages; i++) {
      author = testUsers.get(ThreadLocalRandom.current().nextInt(0, userStore.getAllUsers().size()));
      conversation = conversationStore.getAllConversations().get(ThreadLocalRandom.current().
          nextInt(0, conversationStore.getAllConversations().size()));

      numberOfWords = ThreadLocalRandom.current().nextInt(0, 21);
      content = "" + (char) ThreadLocalRandom.current().nextInt('a', 'z'+1);
      for (int w = 1; w < numberOfWords; w++) {
        content += " " + (char) ThreadLocalRandom.current().nextInt('a', 'z'+1);
      }

      newMessage = new Message(UUID.randomUUID(), conversation.getId(), author.getId(),
          content, Instant.now());

      messageStore.addVolatileMessage(newMessage);
      testMessages.add(newMessage);
    }
  }

  public void clearTestData() {
    try {
      userStore.setUsers(persistentStorageAgent.loadUsers());
      conversationStore.setConversations(persistentStorageAgent.loadConversations());
      messageStore.setMessages(persistentStorageAgent.loadMessages());
    } catch(PersistentDataStoreException e) {
      e.printStackTrace();
    }
  }
}
