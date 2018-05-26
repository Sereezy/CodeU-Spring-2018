package codeu.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;

public class AdminServlet extends HttpServlet {

  private HashSet<String> admins;

  private UserStore userStore;
  private ConversationStore conversationStore;
  private MessageStore messageStore;

  @Override
  public void init() throws ServletException {
    super.init();

    setAdminUsernames();

    setUserStore(UserStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());

    // addTestData();
  }

  void setAdminUsernames() {
    admins = new HashSet<String>();
    admins.add("admin");
    admins.add("daniel");
    admins.add("leslie");
    admins.add("serena");
    admins.add("shana");
    admins.add("kyra");
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

  void computeAdminStats(HttpServletRequest request) {

    // Calculate information to be displayed

    // Users
    List<User> allUsers = userStore.getAllUsers();

    String newestUser = "N/A";
    String mostActiveUser = "N/A";

    // get newest user
    Instant newestInstant = null;
    for (User u : allUsers) {
      if (newestInstant == null || u.getCreationTime().compareTo(newestInstant) > 0) {
        newestInstant = u.getCreationTime();
        newestUser = u.getName();
      }
    }


    // Conversations
    List<Conversation> allConversations = conversationStore.getAllConversations();
    List<Message> messagesInConversation;

    // HashMap to hold user IDs and the number of messages sent by that user
    HashMap<UUID, Integer> userMessageCounts = new HashMap<UUID, Integer>();

    int totalMessageCount = 0; // total number of messages sent by all users
    int totalWordCount = 0; // total number of words sent by all users

    for (Conversation c: allConversations) {

      messagesInConversation = messageStore.getMessagesInConversation(c.getId());
      totalMessageCount += messagesInConversation.size();

      for (Message m: messagesInConversation) {

        int previousMessageCount = userMessageCounts.getOrDefault(m.getAuthorId(), 0);
        userMessageCounts.put(m.getAuthorId(), previousMessageCount + 1);

        totalWordCount += m.getContent().split("\\s+").length;
      }

    }

    // compute most active user
    Map.Entry<UUID, Integer> maxMessageCountUser = null;

    for (Map.Entry<UUID, Integer> entry: userMessageCounts.entrySet()) {

      if (maxMessageCountUser == null || entry.getValue() > maxMessageCountUser.getValue()) {
        maxMessageCountUser = entry;
      }

    }
    if (maxMessageCountUser != null && userStore.getUser(maxMessageCountUser.getKey()) != null) {
      mostActiveUser = userStore.getUser(maxMessageCountUser.getKey()).getName();
    }

    String avgMessagesPerConvo;
    if (allConversations.size() != 0) {
      avgMessagesPerConvo = String.format("%.3f", (double) totalMessageCount / allConversations.size());
    } else {
      avgMessagesPerConvo = "0.000";
    }

    String avgWordsPerMessage;
    if (totalMessageCount != 0) {
      avgWordsPerMessage = String.format("%.3f", (double) totalWordCount / totalMessageCount);
    } else {
      avgWordsPerMessage = "0.000";
    }

    // Set attributes
    request.setAttribute("numberOfUsers", allUsers.size());
    request.setAttribute("newestUser", newestUser);
    request.setAttribute("mostActiveUser", mostActiveUser);

    request.setAttribute("numberOfConversations", allConversations.size());
    request.setAttribute("numberOfMessages", totalMessageCount);
    request.setAttribute("avgMessagesPerConvo", avgMessagesPerConvo);
    request.setAttribute("avgWordsPerMessage", avgWordsPerMessage);

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

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    String user = (String) request.getSession().getAttribute("user");

    if (admins.contains(user)) {
      computeAdminStats(request);
      request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
    } else {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

  }


}
