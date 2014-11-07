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
package examples;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.java_websocket.drafts.Draft_10;
import pt.ieeta.jlay.jlay.agent.CommunicationManager;
import pt.ieeta.jlay.jlay.agent.GlobalSettings;
import pt.ieeta.jlay.jlay.agent.WSClient;

class Request {

    public String origin;
    public String destination;
    public String message;

}

/**
 *
 * @author Luís A. Bastião Silva - <bastiao@ua.pt>
 */
public class Agent1 {

    public static void main(String[] args) {
        try {

            GlobalSettings.agentName = "agent1";
            CommunicationManager com = new CommunicationManager(null, 0, "agent1", false, null);

            com.initiateComm();

            Thread.sleep(20000);
            com.stopComm();

        } catch (Exception ex) {
            Logger.getLogger(Agent1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
