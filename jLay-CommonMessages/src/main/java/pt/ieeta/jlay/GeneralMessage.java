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
package pt.ieeta.jlay;

import java.io.Serializable;

/**
 *
 * @author Luís A. Bastião Silva - <bastiao@ua.pt>w
 */
public class GeneralMessage implements IMessage, Serializable
{
    private Serializable message;
    private String destination;
    private String origin;
    private String messageType;
    
    
    public GeneralMessage(Serializable object, 
            String destination, 
            String origin, 
            String messageType)
    {
        this.message = object;
        this.destination = destination;
        this.origin = origin;
        this.messageType = messageType;
    }
    
    @Override
    public Serializable getObject()
    {
        return this.message;
    }

    @Override
    public String getOrigin()
    {
        return this.origin;
    }

    @Override
    public String getDestination()
    {
        return this.destination;
    }
    
    
    /**
     * @return the messageType
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * @param messageType the messageType to set
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
   
    
    public String toString()
    {
    
        String result = "";
        result += "Origin: " + origin;
        result += "Destination: " + destination;
        result += "MsgType: " + messageType;
        return result;
        
    
    }
    
}
