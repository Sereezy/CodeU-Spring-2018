<%@ page import="java.util.List" %>
<%@ page import="java.util.UUID" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>

<%
List<User> allUsers = (List<User>) request.getAttribute("users");
List<Conversation> allConversations = (List<Conversation>) request.getAttribute("conversations");
List<List<Message>> allMessages = (List<List<Message>>) request.getAttribute("messages");
List<Object> allActivity = (List<Object>) request.getAttribute("activity");
Hashtable<Message, String> conversationTitles = (Hashtable<Message, String>) request.getAttribute("conversationTitles");
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
      <% if (request.getSession().getAttribute("user") != null) { %>
        <a href="/profile/<%=request.getSession().getAttribute("user")%>">Profile</a>
      <% } else{ %>
        <a></a>
      <% } %>
    </nav>

    <div id="container">
        <p>This is the Activity Feed!</p>
        <div id="activities_container">
	        <ul>
			    <%
			      for (Object activity : allActivity) {
			    	  if (activity.getClass() == User.class){
			    		  User user = (User) activity;
			    		  String name = user.getName();
					      String creationTime = user.getCreationTime().toString();	
					      %>
					      	<li><strong><%= creationTime %>:</strong> <%= name %> joined!</li>
					      <%
			    	  }
			    	  else if (activity.getClass() == Conversation.class){
			    		  Conversation conversation = (Conversation) activity;
			    		  String title = conversation.getTitle();
					      String creationTime = conversation.getCreationTime().toString();
					      UUID ownerID = conversation.getOwnerId();
					      String ownerName =  UserStore.getInstance().getUser(ownerID).getName();
					      %>
						    <li><strong><%= creationTime %>:</strong> <%= ownerName %> created <%= title %></li>
						  <%
			    	  }
			    	  else if (activity.getClass() == Message.class){
			    		  Message message = (Message) activity;
			    		  
					      String content = message.getContent();
					      String creationTime = message.getCreationTime().toString();
					      UUID authorId = message.getAuthorId();
					      UUID conversationId = message.getConversationId();
					      String authorName =  UserStore.getInstance().getUser(authorId).getName();
					      String conversationTitle = conversationTitles.get(message);
					      %>
							<li><strong><%= creationTime %>:</strong> <%= authorName %> wrote <%= content %> in  <%= conversationTitle %> </li>
						  <%
					    }
					}
			    %>
	     	</ul>
        </div>
    </div>

</body>

</html>
