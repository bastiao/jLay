package controllers;
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
import com.fasterxml.jackson.databind.JsonNode;
import messages.ParseHandling;
import models.ConnectionStore;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.WebSocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bastiao on 04/11/14.
 */
public class WebSocketsStore extends Controller {



    // collect all websockets here
    public static List<WebSocket.Out<String>> connections = new ArrayList<WebSocket.Out<String>>();

    public static Map<String,WebSocket.Out<String>> connectionsMap = new HashMap<String, WebSocket.Out<String>>();

    public static WebSocket<String> handlesWs() {
        return new WebSocket<String>() {

            // Called when the Websocket Handshake is done.
            public void onReady(In<String> in, final Out<String> out) {
                connections.add(out);

                // For each event received on the socket,
                in.onMessage(new F.Callback<String>() {
                    public void invoke(String event) {

                        System.out.println("This is the message: " + event);
                        JsonNode m = ParseHandling.getMessage(event);

                        String message = m.get("message").asText();
                        String origin = m.get("origin").asText();
                        String destination = m.get("destination").asText();
                        //destination = ParseHandling.calculateDestination(destination);

                        String _destinationFinal = ParseHandling.calculateDestination(destination);
                        if (message.equals("requestChannel"))
                        {
                            if (!origin.contains(":"))
                                connectionsMap.put(origin, out);
                            if (destination.equals("BRIDGE"))
                            {
                                /* in the future something should be done, not now */
                            }
                            else
                            {

                                System.out.println("Request a Channel ");
                                System.out.println("Origin: " + origin );
                                System.out.println("Destination: " + destination );


                                ConnectionStore cs = new ConnectionStore(destination,origin,message,null);
                                cs.name =origin ;
                                cs.origin = origin ;
                                cs.destination = destination;
                                cs.messageType = message;
                                cs.save();
                                //System.out.println(connectionsMap);

                                if (connectionsMap.get(_destinationFinal)!= null)
                                    connectionsMap.get(_destinationFinal).write(event);
                                else
                                    System.out.println("...");
                            /*

                            Query<ConnectionStore> res =ConnectionStore.find.fetch("byDestination", destination);
                            System.out.println("Size of Connection Store + "  + res.findRowCount());
                            if (res.findRowCount()==0)
                            {

                            }
                            */


                            }


                        }
                        else if (message.equals("dataAvailable"))
                        {

                            connectionsMap.get(_destinationFinal).write(event);
                        }

                        else if (message.equals("dataReceived"))
                        {
                            connectionsMap.get(_destinationFinal).write(event);
                        }
                        else if (message.equals("closeSocket"))
                        {
                            connectionsMap.get(_destinationFinal).write(event);
                        }
                        else {
                            System.err.println("I don't know how to handle that");
                            connectionsMap.get(_destinationFinal).write(event);
                        }
                        /*
                        if (out!=null)

                        {
                            Cache.set("staff", new String("test"), 30);
                            out.write("Hello!");
                        }
                        else
                            System.out.println("out is null");
                        */

                    }
                });

                // When the socket is closed.
                in.onClose(new F.Callback0() {
                    public void invoke() {

                        System.out.println("Disconnected");

                    }
                });



                // Send a single 'Hello!' message

                /*
                for ( WebSocket.Out ss : connections)
                {
                    //ss.write("Just to be sure, that everything is alive!");
                }*/


            }

        };
    }


}
