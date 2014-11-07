/*  
Copyright (C) 2014 - Luís A. Bastião Silva

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package pt.ieeta.jlay.jlay.agent;

import com.dicomcloudrouter.MessageI;
import com.dicomcloudrouter.MessageObservable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.java_websocket.drafts.Draft_10;


/**
 *
 * @author Luís A. Bastião Silva - <bastiao@ua.pt>
 */
public class CommunicationManager extends Thread{
    
    
    public static final Map<String, CommunicationManager> communicationMap = Collections.synchronizedMap(new HashMap<String, CommunicationManager> ()) ; 
    public static final String agentName = GlobalSettings.agentName;
    public static WSClient instanceWS () 
    {
        try {
            ws = new WSClient(new URI( "ws://localhost:9000/ws" ), new Draft_10(), agentName );
            ws.connect();
        } catch (URISyntaxException ex) {
            Logger.getLogger(CommunicationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ws;
    }
    
    public static WSClient ws = instanceWS();
    
    private boolean running = false;
    public boolean established = false;
    public String destinationChannel = null;
    private MessageObservable lastMessage;
    private String username = null;
    private boolean usePort = false;
    
    private String idNextMessage = "";
    
    public static final AtomicInteger c = new AtomicInteger(0);
    
    private String  destination ; 

    
    public CommunicationManager(String address, int port, String username, boolean usePort, String destination)
    {
        this.username = username ; 
        this.destination = destination ; 
        // this.address = address;
        this.usePort = usePort; 
        this.lastMessage = new MessageObservable();
        
        idNextMessage =  username + ":"+ String.valueOf(c.incrementAndGet());
    }
    public void initiateComm()
    {
        
        
        if (usePort)
        {
            
            communicationMap.put(idNextMessage, this);
            MessageJson r = new MessageJson();
            r.origin =idNextMessage;
            r.destination = destination;
            r.message = "requestChannel";
            GsonBuilder builder = new GsonBuilder();
            Gson s = builder.create();
            System.out.println(s.toJson(r));
            ws.send(s.toJson(r));
            
            
        }
        this.running = true;        
        this.start();
        if (usePort)
        {
            while(!established)
            {
                synchronized(this)
                {
                    try {
                        this.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CommunicationManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            
            }
            
        }
        
    }
    
    public String getOrigin()
    {
        String origin = username ; 
        if (usePort)
        {
            //origin += ":" + this.socket.getLocalPort();
        }
        return origin;

    }
    
    public void stopComm()
    {
        this.running = false;
        synchronized(this)
        {
            this.notifyAll();
        }

    }
    
    
    
    public void sendMessage(MessageI message) throws Exception
    {
        if (running)
        {
            DataStream ds = new DataStream();
            ds.sendData((Serializable) message);
        
        }    
        
    }
    
    public MessageI receiveMessage(String id)
    {
        DataStream ds = new DataStream();
        return ds.getData(id);
    }
    
    public MessageObservable getLastMessage()
    {
        return lastMessage;
    }
    
    /**
     * TODO: verification of the communications status, the relay may become offline
     */
    @Override
    public void run()
    {
        if (this.usePort)
        {
            System.out.println("The Channel is now waiting with " + idNextMessage);
            // Wait for the 
            synchronized(this)
            {
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(CommunicationManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("The Channel is now open with " +  idNextMessage);
        
        }
        
        
        DataStream ds = new DataStream();
        while (running)
        {
            synchronized(this)
            {
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(CommunicationManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // Received a message or died? 
            if (running)
            {
                
                // Try to get the message
                try
                {
                    MessageI m = ds.getData(getIdNextMessage());
                    this.lastMessage.SetMessage(m);
                    System.out.println("Receive new data");
                    System.out.println(m.getDestination());
                    System.out.println(m.getOrigin());
                    System.out.println(m.getMessageType());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                    
                
            }
            
        }
    }

    /**
     * @return the idNextMessage
     */
    public String getIdNextMessage() {
        return idNextMessage;
    }

    
}
