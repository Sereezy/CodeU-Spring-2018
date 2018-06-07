

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
    <h1>Administration
    </h1>

    <button> Add Test Data </button>

    <h3>Users</h3>
      <p>Number of users: <%= request.getAttribute("numberOfUsers") %></p>
      <p>Newest user: <%= request.getAttribute("newestUser") %></p>
      <p>Most active user: <%= request.getAttribute("mostActiveUser") %></p>
    <hr />

    <h3>Conversations</h3>
      <p>Number of conversations: <%= request.getAttribute("numberOfConversations") %></p>
      <p>Number of messages: <%= request.getAttribute("numberOfMessages") %></p>
      <p>Average messages per conversation: <%= request.getAttribute("avgMessagesPerConvo") %></p>
      <p>Average words per message: <%= request.getAttribute("avgWordsPerMessage") %></p>
    <hr />

    <h3>Import</h3>
      <p>~coming soon~</p>
      <button>Submit</button>
  </div>

</body>

</html>
