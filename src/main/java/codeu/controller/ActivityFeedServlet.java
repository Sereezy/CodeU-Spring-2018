package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Comparator;
import java.util.Hashtable;
import java.time.Instant;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActivityFeedServlet extends HttpServlet {
	
	/** Store class that gives access to Conversations. */
	private ConversationStore conversationStore;
	
	/** Store class that gives access to Messages. */
	private MessageStore messageStore;
	
	/** Store class that gives access to Users. */
	private UserStore userStore;

	@Override
    public void init() throws ServletException {
        super.init();
        setConversationStore(ConversationStore.getInstance());
        setMessageStore(MessageStore.getInstance());
        setUserStore(UserStore.getInstance());

    }
	
	/**
	 * Sets the ConversationStore used by this servlet. This function provides a common setup method
	 * for use by the test framework or the servlet's init() function.
	 */
	void setConversationStore(ConversationStore conversationStore) {
	  this.conversationStore = conversationStore;
	}

	/**
	* Sets the MessageStore used by this servlet. This function provides a common setup method for
	* use by the test framework or the servlet's init() function.
	*/
	void setMessageStore(MessageStore messageStore) {
	  this.messageStore = messageStore;
	}

	/**
	 * Sets the UserStore used by this servlet. This function provides a common setup method for use
	 * by the test framework or the servlet's init() function.
	 */
	void setUserStore(UserStore userStore) {
	  this.userStore = userStore;
	}
	
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    	
    	
    	List<User> allUsers = this.userStore.getAllUsers();
    	List<Conversation> allConversations = this.conversationStore.getAllConversations();
    	    			
    	List<Message> allMessages = new ArrayList<>();
    	Hashtable<Message, String> messageToConversationTitle = new Hashtable();
    	
		for (int i = 0; i < allConversations.size(); i++) {
			Conversation conversation = allConversations.get(i);
			UUID conversationId = conversation.getId();
			List<Message> messagesToAdd = messageStore.getMessagesInConversation(conversationId);
			allMessages.addAll(messagesToAdd);
			
			//stores conversation title corresponding to message in a hashtable
			String conversationTitle = conversation.getTitle();
			for (Message message : messagesToAdd) {
				messageToConversationTitle.put(message, conversationTitle);
			}
			
		}
		
		List<Object> allActivity = new ArrayList<>();
		allActivity.addAll(allUsers);
		allActivity.addAll(allConversations);
		allActivity.addAll(allMessages);
		
		Comparator<Object> byCreationDate = Comparator.comparing(o -> getCreationTime(o)).reversed();
		allActivity = allActivity.stream().sorted(byCreationDate).collect(Collectors.toList());
		
		//if (allActivity.size() > 25){ //truncate list if too long
		//	allActivity = new ArrayList<Object>(allActivity.subList(0, 25));
		//}
		
    	request.setAttribute("activity", allActivity);
    	request.setAttribute("conversationTitles", messageToConversationTitle);

        request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);

    }
    

    
    private Instant getCreationTime(Object object) {
        if (object instanceof Conversation) {
          return ((Conversation) object).getCreationTime();
        } 
        else if (object instanceof Message) {
          return ((Message) object).getCreationTime();
        }
        else if (object instanceof User) {
          return ((User) object).getCreationTime();
        }
        return null;
   }
   

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

    }
}
