

package codeu.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;

public class AdminServletTest {

  private AdminServlet adminServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;

  private UserStore mockUserStore;
  private ConversationStore mockConversationStore;
  private MessageStore mockMessageStore;

  @Before
  public void setup() {
    adminServlet = new AdminServlet();
    adminServlet.setAdminUsernames();

    setUpStores();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);


    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/admin.jsp"))
      .thenReturn(mockRequestDispatcher);

  }

  void setUpStores() {
    mockUserStore = Mockito.mock(UserStore.class);
    adminServlet.setUserStore(mockUserStore);

    mockConversationStore = Mockito.mock(ConversationStore.class);
    adminServlet.setConversationStore(mockConversationStore);

    mockMessageStore = Mockito.mock(MessageStore.class);
    adminServlet.setMessageStore(mockMessageStore);

  }

  @Test
  public void testDoGet_NonAdmin() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("not an admin");

    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockResponse).sendError(HttpServletResponse.SC_NOT_FOUND);
  }

  @Test
  public void testDoGet_Admin() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("admin");

    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  // Number of users tests
  @Test
  public void testNumberOfUsers_NoUsers() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("admin");

    List<User> mockAllUsers = new ArrayList<User>();

    Mockito.when(mockUserStore.getAllUsers()).thenReturn(mockAllUsers);

    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("numberOfUsers", 0);

  }

  @Test
  public void testNumberOfUsers_MultipleUsers() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("admin");

    User u1 = new User(UUID.randomUUID(), "user1", "hashed_pw", Instant.now());
    User u2 = new User(UUID.randomUUID(), "user2", "hashed_pw", Instant.now());
    User u3 = new User(UUID.randomUUID(), "user3", "hashed_pw", Instant.now());

    List<User> mockAllUsers = new ArrayList<User>();
    mockAllUsers.add(u1);
    mockAllUsers.add(u2);
    mockAllUsers.add(u3);

    Mockito.when(mockUserStore.getAllUsers()).thenReturn(mockAllUsers);

    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("numberOfUsers", 3);
  }

  // Newest user tests
  @Test
  public void testNewestUser_NoUsers() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("admin");

    List<User> mockAllUsers = new ArrayList<User>();
    Mockito.when(mockUserStore.getAllUsers()).thenReturn(mockAllUsers);

    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("newestUser", "N/A");
  }

  @Test
  public void testNewestUser_MultipleUsers() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("admin");

    User u1 = new User(UUID.randomUUID(), "user1", "hashed_pw", Instant.now());
    User u2 = new User(UUID.randomUUID(), "user2", "hashed_pw", Instant.now());
    User u3 = new User(UUID.randomUUID(), "user3", "hashed_pw", Instant.now());

    List<User> mockAllUsers = new ArrayList<User>();
    mockAllUsers.add(u1);
    mockAllUsers.add(u2);
    mockAllUsers.add(u3);

    Mockito.when(mockUserStore.getAllUsers()).thenReturn(mockAllUsers);

    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("newestUser", "user3");
  }

  // Most active user tests
  @Test
  public void testMostActiveUser_NoUsers() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("admin");

    List<User> mockAllUsers = new ArrayList<User>();
    Mockito.when(mockUserStore.getAllUsers()).thenReturn(mockAllUsers);

    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("mostActiveUser", "N/A");
  }

  @Test
  public void testMostActiveUser_MultipleUsers() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn("admin");

    // Set up mock users
    User u1 = new User(UUID.randomUUID(), "user1", "hashed_pw", Instant.now());
    User u2 = new User(UUID.randomUUID(), "user2", "hashed_pw", Instant.now());
    User u3 = new User(UUID.randomUUID(), "user3", "hashed_pw", Instant.now());

    List<User> mockAllUsers = new ArrayList<User>();
    mockAllUsers.add(u1);
    mockAllUsers.add(u2);
    mockAllUsers.add(u3);

    Mockito.when(mockUserStore.getAllUsers()).thenReturn(mockAllUsers);


    // Set up mock conversations
    Conversation mockConversation = new Conversation(UUID.randomUUID(), u1.getId(), "mock conversation", Instant.now());

    List<Conversation> mockAllConversations = new ArrayList<Conversation>();
    mockAllConversations.add(mockConversation);

    Mockito.when(mockConversationStore.getAllConversations()).thenReturn(mockAllConversations);


    // Add mock messages
    List<Message> mockMessagesInConversation = new ArrayList<Message>();
    String[][] messages = {
      {"a"},
      {"a", "b"},
      {"a", "b", "c"}
    };
    User[] users = {u1, u2, u3};

    for (int u = 0; u < messages.length; u++) {
      for (String s : messages[u]) {
        mockMessagesInConversation.add(new Message(UUID.randomUUID(),
          mockConversation.getId(),
          users[u].getId(),
          s, Instant.now()));
      }
    }

    Mockito.when(mockMessageStore.getMessagesInConversation(mockConversation.getId())).thenReturn(mockMessagesInConversation);

    Mockito.when(mockUserStore.getUser(u3.getId())).thenReturn(u3);

    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("mostActiveUser", "user3");
  }


  // Number of conversations test
  @Test
  public void testMostActiveUser_NoUsers() throws IOException, ServletException {

  }

}
