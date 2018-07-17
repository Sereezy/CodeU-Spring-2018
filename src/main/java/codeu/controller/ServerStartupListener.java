package codeu.controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.mindrot.jbcrypt.BCrypt;

import codeu.model.data.Conversation;
import codeu.model.data.ImageAttachment;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.UserProfile;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.ImageStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserProfileStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.persistence.PersistentStorageAgent;

/**
 * Listener class that fires when the server first starts up, before any servlet classes are
 * instantiated.
 */
public class ServerStartupListener implements ServletContextListener {

  /** Loads data from Datastore. */
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    try {
      List<User> users = PersistentStorageAgent.getInstance().loadUsers();

      UserStore.getInstance().setUsers(users);
      if (UserStore.getInstance().getUser("admin") == null) {
        System.out.println("No admin exists, creating new admin account.");
        String adminPassword = "admin";
        String hashedPassword = BCrypt.hashpw(adminPassword, BCrypt.gensalt());
        User rootAdmin = new User(UUID.randomUUID(), "admin", hashedPassword, Instant.ofEpochSecond(0));
        rootAdmin.setAdminStatus(true);
        UserStore.getInstance().addUser(rootAdmin);
      }

      List<Conversation> conversations = PersistentStorageAgent.getInstance().loadConversations();
      ConversationStore.getInstance().setConversations(conversations);

      List<Message> messages = PersistentStorageAgent.getInstance().loadMessages();
      MessageStore.getInstance().setMessages(messages);

      List<UserProfile> userprofiles = PersistentStorageAgent.getInstance().loadUserProfiles();
      UserProfileStore.getInstance().setUserProfiles(userprofiles);

      List<ImageAttachment> images = PersistentStorageAgent.getInstance().loadImages();
      ImageStore.getInstance().setImages(images);

    } catch (PersistentDataStoreException e) {
      System.err.println("Server didn't start correctly. An error occurred during Datastore load!");
      System.err.println("This is usually caused by loading data that's in an invalid format.");
      System.err.println("Check the stack trace to see exactly what went wrong.");
      throw new RuntimeException(e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {}
}
