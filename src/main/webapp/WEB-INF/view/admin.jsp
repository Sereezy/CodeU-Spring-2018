

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
      <% if (request.getSession().getAttribute("user") != null) { %>
        <a href="/profile/<%=request.getSession().getAttribute("user")%>">Profile</a>
      <% } else{ %>
        <a></a>
      <% } %>
    
    </nav>


  <div id="container">
    <h1>Administration
    </h1>

    <button id="testDataButton" onclick="testDataButtonPressed()">Add Test Data</button>
    <form id="dataGenOptions" style="display:none" action="admin" method="POST">
      <input type="hidden" name="id" value="dataGenOptions">
      Number of test users: <input type="number" name="numUsers" value=0><br>
      Number of test conversations: <input type="number" name="numConvos" value=0><br>
      Number of test messages: <input type="number" name="numMessages" value=0><br>
      <button type="submit">Submit</button>
    </form>
    <form id="clearTestData" action="admin" method="POST">
      <input type="hidden" name="id" value="clearTestData">
      <button type="submit">Clear all test data</button>
    </form>

    <script>
      var dataGenOptionsShown = false;
      function testDataButtonPressed() {
        console.log(dataGenOptionsShown);
        let button = document.getElementById("testDataButton");
        let options = document.getElementById("dataGenOptions");

        if (dataGenOptionsShown) {
          options.style.display = "none";
          button.innerHTML = "Add Test Data"
        } else {
          options.style.display = "block";
          button.innerHTML = "Hide"
        }

        dataGenOptionsShown = !dataGenOptionsShown;
      }

    </script>

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
