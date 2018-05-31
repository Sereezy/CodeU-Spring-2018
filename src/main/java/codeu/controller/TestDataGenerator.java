package codeu.controller;

class TestDataGenerator {

  private TestDataGenerator instance;

  public static TestDataGenerator getInstance() {
    if (instance == null) {
      instance = new TestDataGenerator();
    }

    return instance;
  }

  private UserStore userStore;
  private ConversationStore conversationStore;
  private MessageStore messageStore;

  private TestDataGenerator() {
    setUserStore(UserStore.getTestInstance());
    setConversationStore(ConversationStore.getTestInstance());
    setMessageStore(MessageStore.getTestInstance());
  }

  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  void addTestData() {
    int numberOfUsers = ThreadLocalRandom.current().nextInt(10, 101);
    int numberOfConversations = ThreadLocalRandom.current().nextInt(5, 21);
    int numberOfMessages = ThreadLocalRandom.current().nextInt(100, 1001);

    User newUser;
    for (int i = 0; i < numberOfUsers; i++) {
      newUser = new User(UUID.randomUUID(), "user"+i, "password",
          Instant.ofEpochSecond(ThreadLocalRandom.current().nextInt(1, 10001)));

      userStore.addUser(newUser);
    }

    Conversation newConversation;
    User owner;
    for (int i = 0; i < numberOfConversations; i++) {
      owner = userStore.getAllUsers().get(ThreadLocalRandom.current().nextInt(0, userStore.getAllUsers().size()));
      newConversation = new Conversation(UUID.randomUUID(), owner.getId(), "conversation"+i,
          Instant.now());

      conversationStore.addConversation(newConversation);
    }

    Message newMessage;
    Conversation conversation;
    User author;
    int numberOfWords;
    String content;
    for (int i = 0; i < numberOfMessages; i++) {
      author = userStore.getAllUsers().get(ThreadLocalRandom.current().nextInt(0, userStore.getAllUsers().size()));
      conversation = conversationStore.getAllConversations().get(ThreadLocalRandom.current().
          nextInt(0, conversationStore.getAllConversations().size()));

      numberOfWords = ThreadLocalRandom.current().nextInt(0, 21);
      content = "" + (char) ThreadLocalRandom.current().nextInt('a', 'z'+1);
      for (int w = 1; w < numberOfWords; w++) {
        content += " " + (char) ThreadLocalRandom.current().nextInt('a', 'z'+1);
      }

      newMessage = new Message(UUID.randomUUID(), conversation.getId(), author.getId(),
          content, Instant.now());

      messageStore.addMessage(newMessage);
    }
  }

}
