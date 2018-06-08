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
    	    			
    	List<List<Message>> allMessages = new ArrayList<>();
    	
		for (int i = 0; i < allConversations.size(); i++) {
			UUID conversationId = allConversations.get(i).getId();
			allMessages.add(messageStore.getMessagesInConversation(conversationId));
			
		}
        
        request.setAttribute("users", allUsers);
    	request.setAttribute("conversations", allConversations);
    	request.setAttribute("messages", allMessages);

        request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

    }
}
