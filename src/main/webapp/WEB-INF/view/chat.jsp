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
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%
Conversation conversation = (Conversation) request.getAttribute("conversation");
List<Message> messages = (List<Message>) request.getAttribute("messages");
List<String> allGIFs = (List<String>) request.getAttribute("allGIFs");
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= conversation.getTitle() %></title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">
  <!-- Bootstrap CSS -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
  <style>
    #chat {
      background-color: white;
      height: 500px;
      overflow-y: scroll
    }
  </style>
  <script
  src="https://code.jquery.com/jquery-3.3.1.min.js"
  integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
  crossorigin="anonymous"></script>
  <script>
    // scroll the chat div to the bottom
    function scrollChat() {
      var chatDiv = document.getElementById('chat');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    }
    
    function sendGIF(el) {
    	document.getElementById("hiddenField").value = el.src;
    	document.forms["chat"].submit();
    }
    
    $(document).ready(function(){
    	
	    $(document).on("submit", "#gifSearchForm", function(event) {  
	        var myForm = $(this);
		    $.ajax({  
		    	type: "GET",  
		    	url: $(this).attr('action'),  // read the action attribute of the form
		    	data: $(this).serialize(),  
		    	success: function(data) {  
		    	 $("#modal-container").html(data);
		    	}  
		    });  
		    event.preventDefault();
		        
	    });
    });
  </script>
</head>
<body onload="scrollChat()">
  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
      <% if (request.getSession().getAttribute("user") != null) { %>
    <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else { %>
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

    <h1><%= conversation.getTitle() %>
      <a href="" style="float: right">&#8635;</a></h1>

    <hr/>

    <div id="chat">
      <ul>
    <%
      for (Message message : messages) {
        String author = UserStore.getInstance()
          .getUser(message.getAuthorId()).getName();
    %>
    <%--  <li><strong><%= author %>:</strong> <%= message.getContent() %></li> --%>
      <li><strong><a href="/profile/<%=request.getSession().getAttribute("author")%>"><%= author %>:</a></strong> <%= message.getContent() %></li>
    <%
      }
    %>
      </ul>
    </div>

    <hr/>

    <% if (request.getSession().getAttribute("user") != null) { %>
    <form name="chat" action="/chat/<%= conversation.getTitle() %>" method="POST" enctype="multipart/form-data">
        <input type="text" name="message">
        <input type="hidden" name="GIFSrc" id="hiddenField" value=""/>
        <input type="file" name="upload">
        <button type="submit">Send</button>
    </form>

        <!-- Button trigger modal -->
		    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modal">
		    Send a GIF
		    </button>
    	<form id="gifSearchForm" name="gifSearch" action="/search/" method="GET"> 
    		<!-- Modal -->
    		<div class="modal fade" id="modal" tabindex="-1" role="dialog" aria-labelledby="modalTitle" aria-hidden="true">
    		  <div class="modal-dialog" role="document">
    		    <div class="modal-content">
    		      <div class="modal-header">
    		        <input type="search" name="search" class="form-control" id="GIFsearch" placeholder="Search GIFs on GIPHY...">
    		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
    		          <span aria-hidden="true">&times;</span>
    		        </button>
    		      </div>
    		      <div class="modal-body">
    		        <div id="modal-container" class="container">
    				  <div class="row">
    				    <div class="col">
    				    	<div data-dismiss="modal">
         						<img onclick="sendGIF(this)" src="https://i.giphy.com/media/feqkVgjJpYtjy/200.gif" class="img-fluid" alt="Responsive image">
         					</div>
    				    </div>
    				    <div class="col">
    				    	<div data-dismiss="modal">
    				      		<img onclick="sendGIF(this)" src="https://media0.giphy.com/media/HkEAY1Yu8UWLS/giphy.gif" class="img-fluid" alt="Responsive image">
    				    	</div>
    				    </div>
    				  </div>
    				  <div class="row">
    				    <div class="col">
    				    	<div data-dismiss="modal">
    				      		<img onclick="sendGIF(this)" src="https://media1.giphy.com/media/tG6ZDOfW5Xeo/giphy.gif" class="img-fluid" alt="Responsive image">
    				      	</div>
    				    </div>
    				    <div class="col">
    				    	<div data-dismiss="modal">
    				      		<img onclick="sendGIF(this)" src="https://media3.giphy.com/media/1tS2pqIiNwJDa/giphy.gif" class="img-fluid" alt="Responsive image">
    				      	</div>
    				    </div>
    				  </div>


    				</div>
    		      </div>
    		      <div class="modal-footer">
    		        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
    		      </div>
    		    </div>
    		  </div>
    		</div>
        <br/>
        

    </form> 
    <% } else { %>
      <p><a href="/login">Login</a> to send a message.</p>
    <% } %>

    <hr/>

  </div>

  <!-- Popper.js, Bootstrap JS -->
  
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

</body>
</html>
