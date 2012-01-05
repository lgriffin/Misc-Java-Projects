package imp.xmpp;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;

import imp.im.ImpBuddy;
import imp.im.ImpGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class XmppGroup implements ImpGroup
{
  private Roster roster;
  private RosterGroup    group;
  private List<ImpBuddy> buddies;
  private String name;

  public XmppGroup (RosterGroup group, Roster roster)
  {
    this.roster = roster;
    this.group = group;
    buddies = new ArrayList<ImpBuddy>();
    for (RosterEntry entry : group.getEntries())
    {
      ImpBuddy buddy = new XmppBuddy(entry, roster.getPresence(entry.getUser()));
      buddies.add(buddy);
    }
    name = group.getName();
  }

  public XmppGroup (Collection<RosterEntry> rosterEntries, Roster roster)
  {
    this.roster = roster;
    group = null;
    buddies = new ArrayList<ImpBuddy>();
    for (RosterEntry entry : rosterEntries)
    {
      ImpBuddy buddy = new XmppBuddy(entry, roster.getPresence(entry.getUser()));
      buddies.add(buddy);
    }
    name = "unfiled";
  }
  
  public String getName()
  {
    return name;
  }

  public String toString()
  {
    return name;
  }

  public List<ImpBuddy> getBuddies()
  {
    return buddies;
  }

  public void add(String ID) throws Exception
  {
    roster.createEntry(ID, ID, new String[]{name});
  }
  
  public void remove(ImpBuddy buddy) throws Exception
  {
    RosterEntry entry = ((XmppBuddy) buddy).getEntry();
    roster.removeEntry(entry);
  }
}
