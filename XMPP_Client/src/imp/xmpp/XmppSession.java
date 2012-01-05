package imp.xmpp;

import java.util.HashMap;
import java.util.Map;

import imp.im.ImpBuddy;
import imp.im.ImpBuddyList;
import imp.im.ImpConversation;
import imp.im.ImpSession;

import java.beans.PropertyChangeSupport;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
//import org.tssg.dbc.Dbc;
import static org.jivesoftware.smack.Roster.SubscriptionMode.accept_all;

public class XmppSession implements ImpSession, ConnectionCreationListener, ConnectionListener, ChatManagerListener
{
  private ConnectionStatus status;
  private XMPPConnection xmppConnection;
  private XmppBuddyList buddyList;
  private Map<String, ImpConversation> conversations;
  private PropertyChangeSupport pcs;

  public XmppSession()
  {
    buddyList = new XmppBuddyList();
    conversations = new HashMap<String, ImpConversation>();

   // pcs = new SwingPropertyChangeSupport(this, true);
 
    XMPPConnection.addConnectionCreationListener(this);
    Roster.setDefaultSubscriptionMode(accept_all);
    setStatus(ConnectionStatus.DISCONNECTED);
  }

  void setStatus(final ConnectionStatus status)
  {
    this.status = status;
   // pcs.firePropertyChange("status", null, status);
  }

  public PropertyChangeSupport getPCS()
  {
    return pcs;
  }

  public ConnectionStatus getStatus()
  {
    return status;
  }

  public XMPPConnection getConnection()
  {
    return xmppConnection;
  }

  public void connect(String server, int port) throws Exception
  {
    xmppConnection = new XMPPConnection(new ConnectionConfiguration(server, port));
    xmppConnection.connect();
    xmppConnection.addConnectionListener(this);
    xmppConnection.getChatManager().addChatListener(this);
  }

  public void disconnect() throws Exception
  {
   // Dbc.require(xmppConnection != null && xmppConnection.isConnected());
    xmppConnection.disconnect();
  }

  public void register(String username, String password) throws Exception
  {
   // Dbc.require(xmppConnection != null && xmppConnection.isConnected());
    AccountManager accountManager = xmppConnection.getAccountManager();
    accountManager.createAccount(username, password);
  }

  public void authenticate(String username, String password) throws Exception
  {
 //   Dbc.require(xmppConnection != null && xmppConnection.isConnected());
    xmppConnection.login(username, password);
    buddyList.setSession(xmppConnection);
    setStatus(ConnectionStatus.AUTHENTICATED);
  }

  public ImpConversation createConversation(ImpBuddy buddy)//throws Exception
  {
    XmppConversation conversation = new XmppConversation(buddy, xmppConnection.getChatManager().createChat(buddy.getUserID(), null));
    conversations.put(buddy.getUserID(), conversation);
    return conversation;
  }

  public void chatCreated(Chat chat, boolean createdLocally)
  {
    if (!createdLocally)
    {
      ImpBuddy buddy = buddyList.get(chat.getParticipant());
      final XmppConversation newConversation = new XmppConversation(buddy, chat);
      conversations.put(buddy.getUserID(), newConversation);
      pcs.firePropertyChange("Chat to Buddy", null, newConversation);
    }
  }

  public void setPresence(PresenceType presence, String presenceString) throws Exception
  {
  //  Dbc.require(xmppConnection != null && xmppConnection.isConnected());
    Presence.Type xmppPresenceType = Presence.Type.error;
    if (presence == PresenceType.AVAILABLE)
    {
      xmppPresenceType = Presence.Type.available;
    }
    else if (presence == PresenceType.UNAVAILABLE)
    {
      xmppPresenceType = Presence.Type.unavailable;
    }
    Presence xmppPresence = new Presence(xmppPresenceType);
    xmppPresence.setStatus(presenceString);
    xmppConnection.sendPacket(xmppPresence);
  }

  public ImpBuddyList getBuddyList()
  {
    return buddyList;
  }

  public void connectionCreated(XMPPConnection arg0)
  {
    setStatus(ConnectionStatus.CONNECTED);
  }

  public void connectionClosed()
  {
    setStatus(ConnectionStatus.DISCONNECTED);
  }

  public void connectionClosedOnError(Exception arg0)
  {
    setStatus(ConnectionStatus.DISCONNECTED);
  }

  public void reconnectingIn(int arg0)
  {
    setStatus(ConnectionStatus.CONNECTING);
  }

  public void reconnectionFailed(Exception arg0)
  {
    setStatus(ConnectionStatus.DISCONNECTED);
  }

  public void reconnectionSuccessful()
  {
    setStatus(ConnectionStatus.CONNECTED);
  }

    public XMPPConnection getXmppConnection()
    {
        return xmppConnection;
    }
  
  
}
