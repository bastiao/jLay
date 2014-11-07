package messages;

/**
 * Created by bastiao on 05/11/14.
 */

import java.io.Serializable;

public class MessageToRelay
{
    private Serializable content;
    private String destination;
    private String origin;
    private String messageType;

    public MessageToRelay(Serializable content, String destination, String origin)
    {
        this.content = content;
        this.destination = destination;
        this.origin = origin;
    }

    public Serializable getContent()
    {
        return content;
    }

    public String getDestination()
    {
        return destination;
    }

    public String getOrigin()
    {
        return origin;
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
