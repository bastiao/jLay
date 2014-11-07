jLay - Communication Framework - with a Relay
---------------------------------------

Components
-------


- jLay-Agent: library that can be used in Java applications to work as agent
- jReLay - Relay server (development over Playframework 2.3.6)
- jLay-CommonMessages (Java projects with messages - optional dependency)


External dependencies
--------------

- MongoDB


How to start? 
--------------------


Initiate the communication (sample java code ):  

```

GlobalSettings.agentName = "agent2";
CommunicationManager com = new CommunicationManager(null, 0, "agent2", false, null);
           

GeneralMessage m = new GeneralMessage(aet, com2.destinationChannel, 
                      com2.getIdNextMessage(), "requestChannel");
com.initiateComm();
CommunicationManager com2 = new CommunicationManager(null, 0, "agent2", true, "agent1");
           
com2.initiateComm();
Thread.sleep(1000); 
com2.sendMessage(m); 
           
Thread.sleep(20000); 

```


(under development)
