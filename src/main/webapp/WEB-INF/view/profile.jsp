<%@ page import="codeu.model.store.basic.UserProfileStore" %>
<%@ page import="codeu.model.data.UserProfile" %>

<%
String user = (String) request.getSession().getAttribute("user");
String profileAuthor = (String) request.getAttribute("profileAuthor");
%>

<!DOCTYPE html>

<html>

<head>
    <title>Profile</title>
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
      <a href="/activityfeed">Activity Feed</a>
      <% if (request.getSession().getAttribute("user") != null) { %>
        <a href="/profile/<%=request.getSession().getAttribute("user")%>">Profile</a>
      <% } else{ %>
        <a></a>
      <% } %>
    </nav>

    <div id="container"><h1><%=request.getSession().getAttribute("user")%>'s Profile</h1>
         <hr/>
         <% if(request.getSession().getAttribute("user") != null){ %>
          <p><%= request.getAttribute("userprofile") %><p>
         <% } else{ %>
            <p> </p>
         <% } %>
         <form action="/profile/<%=request.getSession().getAttribute("user")%>" method="POST">
           <input type="text" name="userprofile">
             <br/><br/>
           <button type="submit">Submit</button>
           <%--<p><%= UserProfileStore.getInstance().addUserProfile((String)request.getSession().getAttribute("user")).getUserProfileContent()%></p>--%>
         </form>
    </div>



</body>

</html>
