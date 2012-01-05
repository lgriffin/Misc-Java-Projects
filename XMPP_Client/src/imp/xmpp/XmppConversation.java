package imp.xmpp;

import imp.im.ImpBuddy;
import imp.im.ImpConversation;
import java.beans.PropertyChangeSupport;


import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class XmppConversation implements ImpConversation, MessageListener
{
  private Chat chat;
  private ImpBuddy buddy;
  private PropertyChangeSupport pcs;

  public XmppConversation(ImpBuddy buddy, Chat chat)
  {
    this.chat = chat;
    this.buddy = buddy;
    chat.addMessageListener(this);
   
  }

  public ImpBuddy getBuddy()
  {
    return buddy;
  }

  public void sendMessage(String message) throws Exception
  {
    chat.sendMessage(message);
  }
  
  public PropertyChangeSupport getPCS()
  {
    return pcs;
  }

  public void processMessage(Chat chat, final Message message)
  {
    pcs.firePropertyChange("message", null, message.getBody());
  }
}
