// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package codeu.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import codeu.model.data.Conversation;
import codeu.model.data.ImageAttachment;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.ImageStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;

/** Servlet class responsible for the chat page. */
@MultipartConfig
public class ChatServlet extends HttpServlet {

	/** Store class that gives access to Conversations. */
	private ConversationStore conversationStore;

	/** Store class that gives access to Messages. */
	private MessageStore messageStore;

	/** Store class that gives access to Users. */
	private UserStore userStore;

	private ImageStore imageStore;

	/** Set up state for handling chat requests. */
	@Override
	public void init() throws ServletException {
		super.init();
		setConversationStore(ConversationStore.getInstance());
		setMessageStore(MessageStore.getInstance());
		setUserStore(UserStore.getInstance());
		setImageStore(ImageStore.getInstance());
	}

	/**
	 * Sets the ConversationStore used by this servlet. This function provides a
	 * common setup method for use by the test framework or the servlet's init()
	 * function.
	 */
	void setConversationStore(ConversationStore conversationStore) {
		this.conversationStore = conversationStore;
	}

	/**
	 * Sets the MessageStore used by this servlet. This function provides a common
	 * setup method for use by the test framework or the servlet's init() function.
	 */
	void setMessageStore(MessageStore messageStore) {
		this.messageStore = messageStore;
	}

	/**
	 * Sets the UserStore used by this servlet. This function provides a common
	 * setup method for use by the test framework or the servlet's init() function.
	 */
	void setUserStore(UserStore userStore) {
		this.userStore = userStore;
	}

	void setImageStore(ImageStore imageStore) {
		this.imageStore = imageStore;
	}

	/**
	 * This function fires when a user navigates to the chat page. It gets the
	 * conversation title from the URL, finds the corresponding Conversation, and
	 * fetches the messages in that Conversation. It then forwards to chat.jsp for
	 * rendering.
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String requestUrl = request.getRequestURI();
		String conversationTitle = requestUrl.substring("/chat/".length());
		
		/*String s1="javatpoint is a very good website";  
        String replaceString=s1.replace('a','e');//replaces all occurrences of 'a' to 'e'  
        System.out.println(replaceString); 
        System.out.println("🚗");*/

		Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
		if (conversation == null) {
			// couldn't find conversation, redirect to conversation list
			System.out.println("Conversation was null: " + conversationTitle);
			response.sendRedirect("/conversations");
			return;
		}

		UUID conversationId = conversation.getId();

		List<Message> messages = messageStore.getMessagesInConversation(conversationId);

		request.setAttribute("conversation", conversation);
		request.setAttribute("messages", messages);
		request.getRequestDispatcher("/WEB-INF/view/chat.jsp").forward(request, response);
	}

	/**
	 * This function fires when a user submits the form on the chat page. It gets
	 * the logged-in username from the session, the conversation title from the URL,
	 * and the chat message from the submitted form data. It creates a new Message
	 * from that data, adds it to the model, and then redirects back to the chat
	 * page.
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		String username = (String) request.getSession().getAttribute("user");
		if (username == null) {
			// user is not logged in, don't let them add a message
			response.sendRedirect("/login");
			return;
		}

		User user = userStore.getUser(username);
		if (user == null) {
			// user was not found, don't let them add a message
			response.sendRedirect("/login");
			return;
		}

		String requestUrl = request.getRequestURI();
		String conversationTitle = requestUrl.substring("/chat/".length());

		Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);

		if (conversation == null) {
			// couldn't find conversation, redirect to conversation list
			response.sendRedirect("/conversations");
			return;
		}

		
		String messageContent = request.getParameter("message");
		String replacementString = messageContent.replaceAll(":turtle", "🐢").replaceAll(":car", "🏎️").replaceAll(":laughing", "😂").replaceAll(":smile", "😊" ).replaceAll(":crying", "😢").replaceAll(":kiss", "😘");
		messageContent = replacementString;

		Part messagePart = request.getPart("message");
		if (messagePart != null) {
			Scanner s = new Scanner(messagePart.getInputStream());


			if (s.hasNextLine()) {
				messageContent = s.nextLine();

				// allows users to enter basic HTML tags that are not a threat to security
				String HTMLMessageContent = clean(messageContent);

				Message message = new Message(UUID.randomUUID(), conversation.getId(), user.getId(), HTMLMessageContent,
						Instant.now());

				messageStore.addMessage(message);

			}
			s.close();
		}
		// Add image message if there is one

		Part filePart = request.getPart("upload");
		if (filePart != null) {
			InputStream fileStream = filePart.getInputStream();

			// fileStream.available() returns the number of bytes that are ready to read,
			// so if it is 0, then no file was uploaded. We also cap this at 1MB because
			// that is the max data size that can be saved as a property in the datastore.
			int bytesAvailable = fileStream.available();
			if (bytesAvailable > 0 && bytesAvailable < 1000000) {

				// Convert bytestream into BufferedImage object
				BufferedImage image = ImageIO.read(fileStream);

				// Check that the file was actually an image
				if (image != null) {
					String contentType = filePart.getContentType();
					String imageType = contentType.substring(contentType.lastIndexOf("/")+1);

					// Create image attachment object from BufferedImage object
					ImageAttachment imageAttachment = new ImageAttachment(UUID.randomUUID(), image, imageType);

					// Set the content of the message to an img tag that calls the ImageServlet
					String src = "/image/" + imageAttachment.getId().toString();
					String content = "<a href=" + src + "><img src=" + src + " width=200></a>";
					Message message = new Message(UUID.randomUUID(), conversation.getId(),
							user.getId(), content, Instant.now());

					messageStore.addMessage(message);
					imageStore.addImage(imageAttachment);
				}
			}
		}
		// redirect to a GET request
		response.sendRedirect("/chat/" + conversationTitle);
	}

	private static String clean(String cleanedMessage) {
		Document dirty = Parser.parseBodyFragment(cleanedMessage, "");
		Cleaner cleaner = new Cleaner(Whitelist.basic());
		Document clean = cleaner.clean(dirty);
		clean.outputSettings().prettyPrint(false);
		return clean.body().html();
	}

	/*private static void main(String args[]){
        String s1="javatpoint is a very good website";  
        String replaceString=s1.replace('a','e');//replaces all occurrences of 'a' to 'e'  
        System.out.println(replaceString); 
        System.out.println('🚗');
	}*/
}