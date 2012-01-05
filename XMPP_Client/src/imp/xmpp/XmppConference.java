/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package imp.xmpp;

import imp.im.ImpConference;
import imp.im.ImpSession;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.beans.PropertyChangeSupport;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;

/**
 *
 * @author lgriffin
 */
public class XmppConference implements ImpConference, PacketListener {
    
    private MultiUserChat muc;
    private ImpSession session;
    private String roomName;
    private PropertyChangeSupport pcs;
    
    
    public XmppConference()
    {
      //   pcs = new SwingPropertyChangeSupport(this, true);
    }
    
    
    public XmppConference(ImpSession session, String roomName)
    {
        this.session = session;
        muc = new MultiUserChat(session.getXmppConnection(),roomName);
        muc.addMessageListener(this);
        this.roomName = roomName;
       
         
    
        
    }

  
    
    public void createRoomAndAddUser(String owner)
    {
        try
        {
           
            muc.create(owner);

         
          
 System.out.println("Get's in here and the owner is " + owner);
            // Get the the room's configuration form
                Form form = muc.getConfigurationForm();
                // Create a new form to submit based on the original form
                Form submitForm = form.createAnswerForm();
                // Add default answers to the form to submit
                for (Iterator fields = form.getFields(); fields.hasNext();)
                {
                    FormField field = (FormField) fields.next();
                    if (!FormField.TYPE_HIDDEN.equals(field.getType()) && field.getVariable() != null)
                    {
                        
                        submitForm.setDefaultAnswer(field.getVariable());
                    }
                }
                
                 List owners = new ArrayList();
                owners.add("b@leigh-test");
                 System.out.println("Form sent!!");
                  submitForm.setAnswer("muc#roomconfig_roomowners", owners);
                // Send the completed form (with default values) to the server to configure the room
                muc.sendConfigurationForm(submitForm);
            
            
            
         //   generateForm(owners); // This is the command form that needs to be run for each new room.
        } catch (XMPPException ex)
        {
            Logger.getLogger(XmppConference.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        
    }
    
    
    public void inviteUser(String uName, String message)
    {
        muc.invite(uName, message);
    }
    
       

    private void generateForm(List owners) throws XMPPException
    {
        System.out.println("Get's in here");
          // Get the the room's configuration form
                Form form = muc.getConfigurationForm();
                // Create a new form to submit based on the original form
                Form submitForm = form.createAnswerForm();
                // Add default answers to the form to submit
                for (Iterator fields = form.getFields(); fields.hasNext();)
                {
                    FormField field = (FormField) fields.next();
                    if (!FormField.TYPE_HIDDEN.equals(field.getType()) && field.getVariable() != null)
                    {
                        
                        submitForm.setDefaultAnswer(field.getVariable());
                        
                    } 
                  
                }
                 submitForm.setAnswer("muc#roomconfig_roomowners", owners);
                // Send the completed form (with default values) to the server to configure the room
                muc.sendConfigurationForm(submitForm);
                 System.out.println("Form sent!!");
    }

  

    public PropertyChangeSupport getPCS()
    {
       return pcs;
    }

  
    public String[] getParticipants()
    {
        try
        {
            Collection c = muc.getParticipants();
            final String[] data = new String[c.size() + 1];
            int val = 0;


            Iterator iter = c.iterator();

            while (iter.hasNext())
            {
                Occupant o = (Occupant) iter.next();

                data[val] = o.getNick();
                val++;
            }

            data[val] = session.getXmppConnection().getUser();

            return data;
            
            
        } catch (XMPPException ex)
        {
            Logger.getLogger(XmppConference.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            return null; // SORT THIS LATER!!!!!
       
    }

    public void sendMessage(String message)
    {
        try
        {
            muc.sendMessage(message);
            
        } catch (XMPPException ex)
        {
            Logger.getLogger(XmppConference.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void propertyChange(PropertyChangeEvent arg0)
    {
       System.out.println("propertyChange called");
       
    }

    public void processPacket(Packet arg0)
    {
        if(arg0.getPacketID() != null)
        {
            System.out.println("Oooooh something called processPacket with a messge of " + arg0.getFrom());
             String from = arg0.getFrom();
           int start = from.indexOf("/") +1;
         int end = from.length();
         
         String uName = from.substring(start, end);
          
           Message m = (Message) arg0;
           String message = m.getBody();
      pcs.firePropertyChange("message", uName, message);
        }
        
       
        
    }
    
    public MultiUserChat getMuc()
    {
        return muc;
    }

    
   
   
    

}
