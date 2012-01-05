package imp.im;

import java.beans.PropertyChangeSupport;
import org.jivesoftware.smack.XMPPConnection;

public interface ImpSession
{
  enum ConnectionStatus {CONNECTING, DISCONNECTED, CONNECTED, AUTHENTICATED};
  enum PresenceType {AVAILABLE, UNAVAILABLE};
  
  ConnectionStatus getStatus();
  void connect(String server, int port)               throws Exception;
  void disconnect()                                   throws Exception;
  ImpConversation createConversation(ImpBuddy buddy);  //throws Exception;
  void authenticate(String username, String password) throws Exception;
  void register(String username, String password)     throws Exception;
  void setPresence(PresenceType presence, String message) throws Exception;
  ImpBuddyList getBuddyList();
  
  XMPPConnection getXmppConnection();
  
  PropertyChangeSupport getPCS();
}
