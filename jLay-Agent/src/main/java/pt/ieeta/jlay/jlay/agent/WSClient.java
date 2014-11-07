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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import static pt.ieeta.jlay.jlay.agent.CommunicationManager.communicationMap;

/**
 *
 * @author Luís A. Bastião Silva - <bastiao@ua.pt>
 */
class MessageJson {
          public String origin;
          public String destination;
          public String message;
          

      }
public class WSClient extends WebSocketClient{
    
    private String agentName = "";
    public WSClient( URI serverUri , Draft draft ) {
		super( serverUri, draft );
    }
    public WSClient(URI serverUri , Draft draft, String agentName)
    {
        super( serverUri, draft );
        this.agentName = agentName; 
        
    }
    
    public String getNewConnection() {
        
        return null; 
    
    }
    public static String calculateDestination(String destination){
        String _destinationFinal = destination;
        if (destination.contains(":"))
        {
            _destinationFinal = destination.split(":")[0];
        }
        return _destinationFinal;
    }
    public void disconnect()
    {
    }

    @Override
    public void onOpen(ServerHandshake sh) {
        System.out.println("Open Web Socket  ");
        
  
            MessageJson r = new MessageJson();
            r.origin =agentName;
            r.destination = "BRIDGE";
            r.message = "requestChannel";
            GsonBuilder builder = new GsonBuilder();
            Gson s = builder.create();
            //System.out.println(s.toJson(r));
            this.send(s.toJson(r));
    }

    @Override
    public void onMessage(String string) {
        System.out.println("Receive the message: " + string ); 
        
        // Now check where this messages belong 
        if (string==null || string.equals(""))
            return ;
        JsonObject object = new JsonParser().parse(string).getAsJsonObject();
        String origin = object.get("origin").getAsString(); //
        String destination = object.get("destination").getAsString(); //
        String message = object.get("message").getAsString(); //
        if (message.equals("requestChannel"))
        {
            
            if (CommunicationManager.communicationMap.get(destination)!=null)
            {
            
            }
            else
            {
                System.out.println("Received requestChannel from " + origin);
                CommunicationManager com = new CommunicationManager(null, 0, agentName, false, origin);
                communicationMap.put(com.getIdNextMessage(), com);
                com.initiateComm();
                //It should be added automatically to the mapping

                String response = "responseChannel";
                String autoId = com.getIdNextMessage();
                MessageJson jsonM = new MessageJson();
                jsonM.destination = origin;

                jsonM.origin = autoId;
                System.out.println("Origin: " + autoId);
                System.out.println("destination: " + origin);

                jsonM.message = response; 
                GsonBuilder builder = new GsonBuilder();
                Gson s = builder.create();
                System.out.println(s.toJson(jsonM));

                this.send(s.toJson(jsonM));
            }
            
            
            
        }
        else if (message.equals("responseChannel"))
        {
            
            CommunicationManager c = CommunicationManager.communicationMap.get(destination);
            c.established = true;
            c.destinationChannel = origin;
            System.out.println(destination + " is now established");
            synchronized(c)
            {
                c.notifyAll();
            }
            
        }
        else if (message.equals("dataAvailable"))
        {
            
            // Let's get the data.
            System.out.println(CommunicationManager.communicationMap);
            
            CommunicationManager c = CommunicationManager.communicationMap.get(destination);
            /*MessageI m = c.receiveMessage(origin);
            c.getLastMessage().SetMessage(m);*/
            /*try {
                c.sendMessage(m);
                
            } catch (Exception ex) {
                Logger.getLogger(WSClient.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            synchronized(c)
            {
                c.notifyAll();
            }
            
        }
        
        else if (message.equals("closeSocket"))
        {
            
            CommunicationManager c = CommunicationManager.communicationMap.get(origin);
            c.stopComm();
            // Send a request to remove the channel 
            
            CommunicationManager.communicationMap.remove(origin);
            
        }
        
    }

    @Override
    public void onClose(int i, String string, boolean bln) {
        System.out.println("Close the Web socket channel "); 
        
        // It's time to reset router! 
    }

    @Override
    public void onError(Exception excptn) {
        System.out.println("Error in the Web socket channel "); 
        // It's time to reset router! 
        excptn.printStackTrace();
        this.close();
        
    }
 
}
