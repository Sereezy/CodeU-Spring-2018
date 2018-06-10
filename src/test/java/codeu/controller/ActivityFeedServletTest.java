

package codeu.controller;

import static org.junit.Assert.fail;

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

public class ActivityFeedServletTest {

    private ActivityFeedServlet activityFeedServlet;
    private HttpServletRequest mockRequest;
    private HttpSession mockSession;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockRequestDispatcher;

    private UserStore mockUserStore;
    private ConversationStore mockConversationStore;
    private MessageStore mockMessageStore;


    @Before
    public void setup() {
    	activityFeedServlet = new ActivityFeedServlet();

    	setUpStores();
    	
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);


        mockResponse = Mockito.mock(HttpServletResponse.class);
        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp"))
            .thenReturn(mockRequestDispatcher);

    }
    
    void setUpStores() {
        mockUserStore = Mockito.mock(UserStore.class);
        activityFeedServlet.setUserStore(mockUserStore);

        mockConversationStore = Mockito.mock(ConversationStore.class);
        activityFeedServlet.setConversationStore(mockConversationStore);

        mockMessageStore = Mockito.mock(MessageStore.class);
        activityFeedServlet.setMessageStore(mockMessageStore);

      }


    @Test
    public void testDoGet() throws IOException, ServletException {
      UUID fakeUserId = UUID.randomUUID();
      User fakeUser =
    	        new User(
    	        	fakeUserId,
    	            "test username",
    	            "$2a$10$.e.4EEfngEXmxAO085XnYOmDntkqod0C384jOR9oagwxMnPNHaGLa",
    	            Instant.now());
      
      List<User> fakeAllUsers = new ArrayList<>();
      fakeAllUsers.add(fakeUser);
      Mockito.when(mockUserStore.getAllUsers())
      .thenReturn(fakeAllUsers);
    	
      List<Conversation> fakeAllConversations = new ArrayList<>();
    	
      UUID fakeConversationId = UUID.randomUUID();
      Conversation fakeConversation =
          new Conversation(fakeConversationId, fakeUserId, "test_conversation", Instant.now());
      
      fakeAllConversations.add(fakeConversation);
      Mockito.when(mockConversationStore.getAllConversations())
          .thenReturn(fakeAllConversations);

      
      List<List<Message>> fakeAllMessages = new ArrayList<>();

      List<Message> fakeMessageList = new ArrayList<>();
      fakeMessageList.add(
          new Message(
              UUID.randomUUID(),
              fakeConversationId,
              fakeUserId,
              "test message",
              Instant.now()));
      
      fakeAllMessages.add(fakeMessageList);
      
      Mockito.when(mockMessageStore.getMessagesInConversation(fakeConversationId))
          .thenReturn(fakeMessageList);

      activityFeedServlet.doGet(mockRequest, mockResponse);
      
      Mockito.verify(mockRequest).setAttribute("users", fakeAllUsers);
      Mockito.verify(mockRequest).setAttribute("conversations", fakeAllConversations);
      Mockito.verify(mockRequest).setAttribute("messages", fakeAllMessages);

      Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }



}
