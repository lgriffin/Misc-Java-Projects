/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package imp.im;

import java.beans.PropertyChangeSupport;
import java.util.Collection;
import org.jivesoftware.smackx.muc.MultiUserChat;


/**
 *
 * @author lgriffin
 */
public interface ImpConference {

    void sendMessage(String message);
    
    
    void createRoomAndAddUser(String owner);
    void inviteUser(String uName, String message);
    PropertyChangeSupport getPCS();
    String[] getParticipants();
    
}
