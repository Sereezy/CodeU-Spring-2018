

package codeu.controller;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    	            Instant.ofEpochSecond(105));
      
      List<User> fakeAllUsers = new ArrayList<>();
      fakeAllUsers.add(fakeUser);
      Mockito.when(mockUserStore.getAllUsers())
      .thenReturn(fakeAllUsers);
    	
      List<Conversation> fakeAllConversations = new ArrayList<>();
    	
      UUID fakeConversationId = UUID.randomUUID();
      Conversation fakeConversation =
          new Conversation(fakeConversationId, fakeUserId, "test_conversation", Instant.ofEpochSecond(100));
      
      fakeAllConversations.add(fakeConversation);
      Mockito.when(mockConversationStore.getAllConversations())
          .thenReturn(fakeAllConversations);

      
      List<Message> fakeAllMessages = new ArrayList<>();
      
      for (int i=30; i < 1; i++) {
	      fakeAllMessages.add(
	          new Message(
	              UUID.randomUUID(),
	              fakeConversationId,
	              fakeUserId,
	              "test message num:"+Integer.toString(i),
	              Instant.ofEpochSecond(i)));
      }
      Mockito.when(mockMessageStore.getMessagesInConversation(fakeConversationId))
          .thenReturn(fakeAllMessages);
      
      Hashtable<Message, String> fakeMessageToConversationTitle = new Hashtable();
      for (Message message : fakeAllMessages) {
    	  fakeMessageToConversationTitle.put(message, "test_conversation");
		}
      
      List<Object> fakeAllActivity = new ArrayList<>();
      fakeAllActivity.addAll(fakeAllUsers);
      fakeAllActivity.addAll(fakeAllConversations);
      fakeAllActivity.addAll(fakeAllMessages);
      //fakeAllActivity = new ArrayList<Object>(fakeAllActivity.subList(0, 25));
      
      
      
      activityFeedServlet.doGet(mockRequest, mockResponse);
      
      Mockito.verify(mockRequest).setAttribute("activity", fakeAllActivity);
      Mockito.verify(mockRequest).setAttribute("conversationTitles", fakeMessageToConversationTitle);
      

      Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }
    
    



}
