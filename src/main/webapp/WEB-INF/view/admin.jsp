
<%@ page import="codeu.model.store.basic.UserStore" %>


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

        <h2>Users</h2><hr />
            <p>Number of users: <%= UserStore.getInstance().getUsers().size() %></p>

        <h2>Conversations</h2><hr />
        <h2>Import<h2><hr />
    </div>

</body>

</html>
