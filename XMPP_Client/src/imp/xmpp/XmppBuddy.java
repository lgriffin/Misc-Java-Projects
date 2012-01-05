package imp.xmpp;

import imp.im.ImpBuddy;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;

public class XmppBuddy implements ImpBuddy
{
  private RosterEntry rosterEntry;
  private Presence presence;

  public XmppBuddy(RosterEntry rosterEntry, Presence presence)
  {
    this.rosterEntry = rosterEntry;
    this.presence = presence;
  }

  public String getName()
  {
    if (rosterEntry.getName() == null)
    {
      return getUserID();
    }
    return rosterEntry.getName();
  }

  public String getUserID()
  {
    return rosterEntry.getUser();
  }
  
  public String getStatus()
  {
    String status = presence.getType().toString();
    if (presence.getStatus() != null)
    {
      status += "- " + presence.getStatus();
    }
    return status;
  }

  public void setPresence(Presence presence)
  {
    this.presence = presence;
  }

  public RosterEntry getEntry()
  {
    return rosterEntry;
  }

  public String toString()
  {
    return getUserID() + " (" +getStatus() + ")";
  }
  
  @Override
  public int hashCode()
  {
    return getUserID().hashCode();
  }
  
  public boolean equals(Object obj)
  {
    if (obj instanceof XmppBuddy)
    {
      return getUserID().equals(((XmppBuddy)obj).getUserID());
    }
    return false;
  }
}
