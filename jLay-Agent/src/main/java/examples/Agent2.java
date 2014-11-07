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

import com.dicomcloudrouter.GeneralMessage;
import com.pacscloud.bridgelib.msg.AETitle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ieeta.jlay.jlay.agent.CommunicationManager;
import pt.ieeta.jlay.jlay.agent.GlobalSettings;

/**
 *
 * @author Luís A. Bastião Silva - <bastiao@ua.pt>
 */
public class Agent2 {

    public static void main(String[] args) {
        try {
            GlobalSettings.agentName = "agent2";
            CommunicationManager com = new CommunicationManager(null, 0, "agent2", false, null);

            com.initiateComm();
            Thread.sleep(2000);

            final CommunicationManager com2 = new CommunicationManager(null, 0, "agent2", true, "agent1");

            com2.initiateComm();
            AETitle aet = new AETitle();
            aet.setAETitle("DICOOGLE");
            final GeneralMessage m = new GeneralMessage(aet, com2.destinationChannel, com2.getIdNextMessage(), "requestChannel");

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        // task to run goes here
                        com2.sendMessage(m);
                    } catch (Exception ex) {
                        Logger.getLogger(Agent2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            Timer timer = new Timer();
            long delay = 0;
            long intevalPeriod = 1 * 1000;
          // schedules the task to be run in an interval 
            //timer.schedule(task, delay,
            //                            intevalPeriod);
            Thread.sleep(1000);
            com2.sendMessage(m);

            Thread.sleep(20000);
            com.stopComm();

        } catch (Exception ex) {
            Logger.getLogger(Agent1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
