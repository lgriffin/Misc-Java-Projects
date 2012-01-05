package imp.im;

import java.util.List;

public interface ImpGroup
{
  String getName();
  List<ImpBuddy> getBuddies();
  void remove (ImpBuddy buddy) throws Exception;
  void add (String buddyID) throws Exception;
}
