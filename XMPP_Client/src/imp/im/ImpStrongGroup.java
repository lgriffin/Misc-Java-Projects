package imp.im;

import imp.services.ImpServices;

import java.util.List;

public interface ImpStrongGroup {
	
	  String getName();
	  String getGroupID();
	  List<ImpBuddy> getBuddies();
	  List<ImpServices> getServices();
	  void remove (ImpBuddy buddy) throws Exception;
	  void add (String buddyID) throws Exception;
	  

}
