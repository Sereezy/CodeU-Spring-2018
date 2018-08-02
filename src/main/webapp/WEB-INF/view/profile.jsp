<%@ page import="codeu.model.store.basic.UserProfileStore" %>
<%@ page import="codeu.model.data.UserProfile" %>
<%@ page import="codeu.helper.ProfileHelper"%>

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

  <%--  <div id="container"><h1><%=request.getSession().getAttribute("user")%>'s Profile</h1>
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
           <%--<p><%= UserProfileStore.getInstance().addUserProfile((String)request.getSession().getAttribute("user")).getUserProfileContent()%></p>
         </form>
    </div>--%>
    <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">
       <h1> <%=request.getSession().getAttribute("user")%>'s Profile</h1>
       <hr>
       <h2>About Me</h2>
       <p id="aboutMe"><%= request.getAttribute("userprofile") %></p>
       <%--><% if (ProfileHelper.isSameUser(user, profileAuthor)) { %><--%>
        <h3>Edit</h3>
        <hr>
        <form action="/profile/<%=request.getSession().getAttribute("user")%>" method="POST">
        	<textarea cols="50" rows="5" id="userprofile" name="userprofile"
        		placeholder="Edit your About Me section here."></textarea>
        	<br/>
        	<button type="submit">Submit</button>
        </form>
      <%--> <% } %> <--%>

      <%--<% if (!ProfileHelper.isSameUser(user, profileAuthor)) { %> --%>
        <form method="POST" action="${pageContext.request.contextPath}/profile/<%=request.getSession().getAttribute("user")%>">
           <input type="submit" name="messageUserButton" value="Message <%=request.getSession().getAttribute("user")%>" />
        </form>
      <%--> <% } %> <--%>



</body>

</html>
