package imp.im;

import java.beans.PropertyChangeSupport;

public interface ImpConversation
{
  ImpBuddy getBuddy();
  void sendMessage(String message) throws Exception;
  PropertyChangeSupport getPCS();
}
