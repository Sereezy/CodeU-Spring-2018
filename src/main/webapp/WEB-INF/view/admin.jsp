
<%@ page import="java.util.List" %>
<%@ page import="java.util.UUID" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>

<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.store.basic.ConversationStore" %>
<%@ page import="codeu.model.store.basic.MessageStore" %>

<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%
// Users
List<User> allUsers = UserStore.getInstance().getAllUsers();

String newestUser = "N/A";
String mostActiveUser = "N/A";

if (allUsers.size() > 0) {
    newestUser = allUsers.get(allUsers.size() - 1).getName();
}


// Conversations
List<Conversation> allConversations = ConversationStore.getInstance().getAllConversations();
List<Message> messagesInConversation;

HashMap<UUID, Integer> userMessageCounts = new HashMap<UUID, Integer>();
int totalMessageCount = 0;

for (Conversation c: allConversations) {
    messagesInConversation = MessageStore.getInstance().getMessagesInConversation(c.getId());
    totalMessageCount += messagesInConversation.size();
    for (Message m: messagesInConversation) {
        int tmp = userMessageCounts.getOrDefault(m.getAuthorId(), 0);
        userMessageCounts.put(m.getAuthorId(), tmp + 1);
    }
}

Map.Entry<UUID, Integer> maxMessageCountUser = null;
for (Map.Entry<UUID, Integer> entry: userMessageCounts.entrySet()) {
    if (maxMessageCountUser == null || entry.getValue() > maxMessageCountUser.getValue()) {
        maxMessageCountUser = entry;
    }
}
if (maxMessageCountUser != null) {
    mostActiveUser = UserStore.getInstance().getUser(maxMessageCountUser.getKey()).getName();
}


%>

<!DOCTYPE html>

<html>

<head>
    <title>Admin</title>
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
        <h1>Administration</h1>

        <h3>Users</h3>
            <p>Number of users: <%= allUsers.size() %></p>
            <p>Newest user: <%= newestUser %></p>
            <p>Most active user: <%= mostActiveUser %></p>
        <hr />

        <h3>Conversations</h3>
            <p>Number of conversations: <%= allConversations.size() %></p>
            <p>Number of messages: <%= totalMessageCount %></p>
            <p>Average messages per conversation: <%= (int) ((float) totalMessageCount / allConversations.size()) %></p>
        <hr />

        <h3>Import</h3>
            <p>~coming soon~</p>
            <button>Submit</button>
    </div>

</body>

</html>
