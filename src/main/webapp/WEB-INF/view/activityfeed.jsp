<%@ page import="java.util.List" %>
<%@ page import="java.util.UUID" %>
<%@ page import="java.time.Instant" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>

<%
List<User> allUsers = (List<User>) request.getAttribute("users");
List<Conversation> allConversations = (List<Conversation>) request.getAttribute("conversations");
List<List<Message>> allMessages = (List<List<Message>>) request.getAttribute("messages");
%>
<!DOCTYPE html>

<html>

<head>
    <title>Activity Feed</title>
    <link rel="stylesheet" href="/css/main.css">
</head>

<body>
    <nav>
      <a id="navTitle" href="/">CodeU Chat App</a>
      <a href="/conversations">Conversations</a>
      <% if(request.getSession().getAttribute("user") != null){ %>
        <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
      <% } else{ %>
        <a href="/login">Login</a>
      <% } %>
      <a href="/about.jsp">About</a>
      <a href="/activityfeed">Activity Feed</a>
    </nav>

    <div id="container">
        <p>This is the Activity Feed!</p>
        <div id="users_container">
	        <ul>
			    <%
			      for (User user : allUsers) {
			        String name = user.getName();
			        String creationTime = user.getCreationTime().toString();
			    %>
			      <li><strong><%= creationTime %>:</strong> <%= name %> joined!</li>
			    <%
			      }
			    %>
	     	</ul>
        </div>
        <div id="conversations_container">
	        <ul>
			    <%
			      for (Conversation conversation : allConversations) {
			        String title = conversation.getTitle();
			        String creationTime = conversation.getCreationTime().toString();
			        UUID ownerID = conversation.getOwnerId();
			        String ownerName =  UserStore.getInstance().getUser(ownerID).getName();
			        
			    %>
			      <li><strong><%= creationTime %>:</strong> <%= ownerName %> created <%= title %></li>
			    <%
			      }
			    %>
	     	</ul>
        </div>
        <div id="messages_container">
	        <ul>
			    <%
			      int i = 0;
			      for (List<Message> messagesInConvo : allMessages) {
			    	  for (Message message : messagesInConvo) {
				        String content = message.getContent();
				        String creationTime = message.getCreationTime().toString();
				        UUID authorId = message.getAuthorId();
				        UUID conversationId = message.getConversationId();
				        String authorName =  UserStore.getInstance().getUser(authorId).getName();
				        String conversationTitle = allConversations.get(i).getTitle();
				        			        
					    %>
					      <li><strong><%= creationTime %>:</strong> <%= authorName %> wrote <%= content %> in  <%= conversationTitle %> </li>
					    <%
			    	  }
			    	  i++;
			      }
			    %>
	     	</ul>
        </div>
    </div>

</body>

</html>
