<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<!DOCTYPE html>
<html>
<head>
  <title>CodeU Chat App</title>
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
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <p style="font-family:Phosphate"><font color="jade" size="45">About the CodeU Chat App</font></p>
      <p style="font-family:Chalkboard"><font color="jade" size="4">Hi fellow CodeUers!We are Team 32 AKA Team LSD! Here, we will improve on a chat app as a team! Here is a little bit about our team members:
      <ul style="font-family:Chalkboard">

        <li><strong>Leslie Coney:</strong> Rising sophomore at Howard University, majoring in computer science. Volunteering with a tech camp for HS students in DC. Is from Chicago just like Kanye West.</li>
        <li><strong>Shana Mathew:</strong>Awaiting on a bio. </li>
        <li><strong>Serena Burton:</strong> An upcoming sophomore at Queens College CUNY and majoring in computer science. Enjoys playing games and often only leaves her room for food and water, the essentials.</li>
        <li><strong>Daniel McCrystal:</strong>Upcoming Junior at The College of William & Mary, studying computer science. Can wrestle, so don't mess with him.</li>
        <li><strong>Kyra Busser PA:</strong> The team's lovely CodeU advisor.</li>
        <li><strong>Features/Changes added thus far: </strong>
        <ul>~Changed font size, color, style.</ul>
        <ul>~Changed Text</ul>
        </li>
     </ul>
     </font></p>


    </div>
  </div>
</body>
</html>
