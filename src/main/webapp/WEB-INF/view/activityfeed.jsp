<%@ page import="java.util.List" %>
<%@ page import="java.util.UUID" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>

<%
List<Object> allActivity = (List<Object>) request.getAttribute("activity");
Hashtable<Message, String> conversationTitles = (Hashtable<Message, String>) request.getAttribute("conversationTitles");
%>
<!DOCTYPE html>

<html>

<head>
    <title>Activity Feed</title>
    <link rel="stylesheet" href="/css/main.css">
    <style>
	    #activities_container {
	      background-color: white;
	      height: 500px;
	      overflow-y: scroll
	    }
  	</style>
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
        <p>This is the Activity Feed! </p>
        <div id="activities_container">
	        <ul>
			    <%!
			      public String setTime(Instant creationInstant){
			    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss").withZone(ZoneId.systemDefault());
		    		return formatter.format(creationInstant) + " " + formatter.getZone();
			      }
			    %>
			    <%
			      for (Object activity : allActivity) {
			    	  if (activity.getClass() == User.class){
			    		  User user = (User) activity;
			    		  String name = user.getName();
					      Instant creationInstant = user.getCreationTime();	
					      String creationTime = setTime(creationInstant);
					      %>
					      	<li><strong><%= creationTime %>:</strong> <%= name %> joined!</li>
					      <%
			    	  }
			    	  else if (activity.getClass() == Conversation.class){
			    		  Conversation conversation = (Conversation) activity;
			    		  String title = conversation.getTitle();
					      UUID ownerID = conversation.getOwnerId();
					      String ownerName =  UserStore.getInstance().getUser(ownerID).getName();
					      Instant creationInstant = conversation.getCreationTime();	
					      String creationTime = setTime(creationInstant);
					      %>
						    <li><strong><%= creationTime %>:</strong> <%= ownerName %> created <i><%= title %></i></li>
						  <%
			    	  }
			    	  else if (activity.getClass() == Message.class){
			    		  Message message = (Message) activity;
			    		  
					      String content = message.getContent();
					      
					      UUID authorId = message.getAuthorId();
					      UUID conversationId = message.getConversationId();
					      String authorName =  UserStore.getInstance().getUser(authorId).getName();
					      String conversationTitle = conversationTitles.get(message);
					      Instant creationInstant = message.getCreationTime();	
					      String creationTime = setTime(creationInstant);
					      %>
							<li><strong><%= creationTime %>:</strong> <%= authorName %> wrote "<%= content %>" in  <i><%= conversationTitle %></i> </li>
						  <%
					    }
					}
			    %>
	     	</ul>
        </div>
    </div>

</body>

</html>
