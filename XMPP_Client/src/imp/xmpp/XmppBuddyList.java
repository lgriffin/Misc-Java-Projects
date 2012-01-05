package imp.xmpp;

import imp.im.ImpBuddy;
import imp.im.ImpBuddyList;

import imp.im.ImpGroup;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.Roster;
import static org.jivesoftware.smack.Roster.SubscriptionMode.accept_all;

import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import java.util.Collection;
import java.util.List;


public class XmppBuddyList implements ImpBuddyList, RosterListener
{
  private XMPPConnection xmppConnection;
  private List<ImpGroup> groups;
  private Roster roster;
  private RosterGroup unclassified;
  private PropertyChangeSupport pcs;

  public XmppBuddyList()
  {
    groups = null;//new ArrayList<ImpGroup>();
    roster = null;
   
  }

  public PropertyChangeSupport getPCS()
  {
    return pcs;
  }

  public void setSession(XMPPConnection connection)
  {
    this.xmppConnection = connection;
    updateBuddyList();
    roster.addRosterListener(this);
  }

  public void updateBuddyList()
  {
    roster = xmppConnection.getRoster();
    roster.addRosterListener(this);

    groups = new ArrayList<ImpGroup>();
    for (RosterGroup entry : roster.getGroups())
    {
      groups.add(new XmppGroup(entry, roster));
    }
    if (!roster.getUnfiledEntries().isEmpty())
    {
      ImpGroup unfiledGroup = new XmppGroup(roster.getUnfiledEntries(), roster);
      groups.add(unfiledGroup);
    }

 //   pcs.firePropertyChange("buddylist", null, this);
  }

  public List<ImpGroup> getGroups()
  {
    return groups;
  }

  public void add(String buddyName, String ID) throws Exception
  {
    roster.createEntry(ID, ID, null);
  }

  public ImpBuddy get(String userID)
  {
    int atLoc = userID.indexOf("/");
    if (atLoc != -1)
    {
      userID = userID.substring(0, atLoc);
    }
    for (ImpGroup group : groups)
    {
      for (ImpBuddy buddy : group.getBuddies())
      {
        if (buddy.getUserID().equals(userID))
        {
          return buddy;
        }
      }
    }
    return null;
  }

  public void presenceChanged(Presence presence)
  {
    final XmppBuddy buddy = (XmppBuddy) get(presence.getFrom());
    if (buddy != null)
    {
      buddy.setPresence(presence);
    }
  //  pcs.firePropertyChange("presence", null, buddy);
  }

  public void entriesAdded(Collection<String> list)
  {
    for (String buddyID : list)
    {
      try
      {
        if (get(buddyID) == null)
        {
          roster.createEntry(buddyID, buddyID, null);
        }
      }
      catch (XMPPException ex)
      {
        Logger.getLogger(XmppBuddyList.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    updateBuddyList();
  }

  public void entriesDeleted(Collection<String> arg0)
  {
    updateBuddyList();
  }

  public void entriesUpdated(Collection<String> arg0)
  {
    updateBuddyList();
  }
}
