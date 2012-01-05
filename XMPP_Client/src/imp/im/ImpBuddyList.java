package imp.im;

import java.beans.PropertyChangeSupport;
import java.util.List;

public interface ImpBuddyList
{
  void add(String buddyName, String ID) throws Exception;
  ImpBuddy get(String buddyName);
  List<ImpGroup> getGroups();

  PropertyChangeSupport getPCS();
}
