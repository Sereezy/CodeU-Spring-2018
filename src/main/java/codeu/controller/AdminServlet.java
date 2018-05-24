package codeu.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;

public class AdminServlet extends HttpServlet {

    private HashSet<String> admins;

    @Override
    public void init() throws ServletException {
        super.init();

        setAdminUsernames();
    }

    void setAdminUsernames() {
        admins = new HashSet<String>();
        admins.add("admin");
        admins.add("daniel");
        admins.add("leslie");
        admins.add("serena");
        admins.add("shana");
        admins.add("kyra");
    }

    void computeAdminStats(HttpServletRequest request) {
        // Calculate information to be displayed

        // Users
        List<User> allUsers = UserStore.getInstance().getAllUsers();

        String newestUser = "N/A";
        String mostActiveUser = "N/A";

        // get newest user
        if (allUsers.size() > 0) {
            newestUser = allUsers.get(allUsers.size() - 1).getName();
        }


        // Conversations
        List<Conversation> allConversations = ConversationStore.getInstance().getAllConversations();
        List<Message> messagesInConversation;

        // HashMap to hold user IDs and the number of messages sent by that user
        HashMap<UUID, Integer> userMessageCounts = new HashMap<UUID, Integer>();

        int totalMessageCount = 0; // total number of messages sent by all users
        int totalWordCount = 0; // total number of words sent by all users

        for (Conversation c: allConversations) {

            messagesInConversation = MessageStore.getInstance().getMessagesInConversation(c.getId());
            totalMessageCount += messagesInConversation.size();

            for (Message m: messagesInConversation) {

                int tmp = userMessageCounts.getOrDefault(m.getAuthorId(), 0);
                userMessageCounts.put(m.getAuthorId(), tmp + 1);

                totalWordCount += m.getContent().split("\\s+").length;
            }

        }

        // compute most active user
        Map.Entry<UUID, Integer> maxMessageCountUser = null;

        for (Map.Entry<UUID, Integer> entry: userMessageCounts.entrySet()) {

            if (maxMessageCountUser == null || entry.getValue() > maxMessageCountUser.getValue()) {
                maxMessageCountUser = entry;
            }

        }
        if (maxMessageCountUser != null) {
            mostActiveUser = UserStore.getInstance().getUser(maxMessageCountUser.getKey()).getName();
        }


        // Set attributes
        request.setAttribute("numberOfUsers", allUsers.size());
        request.setAttribute("newestUser", newestUser);
        request.setAttribute("mostActiveUser", mostActiveUser);

        request.setAttribute("numberOfConversations", allConversations.size());
        request.setAttribute("numberOfMessages", totalMessageCount);
        request.setAttribute("avgMessagesPerConvo", (int) ((double) totalMessageCount / allConversations.size()));
        request.setAttribute("avgWordsPerMessage", (int) ((double) totalWordCount / totalMessageCount));

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        String user = (String) request.getSession().getAttribute("user");

        if (admins.contains(user)) {
            computeAdminStats(request);
            request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

    }
}
