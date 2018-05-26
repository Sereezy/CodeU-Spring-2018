
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
      <a href="/profile/<%=request.getSession().getAttribute("user")%>">Profile</a>
      <a href="/activityfeed">Activity Feed</a>
    </nav>

    <div id="container">
        <p>This is the admin page</p>
    </div>

</body>

</html>
