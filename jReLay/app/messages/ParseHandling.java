package messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

/**
 * Created by bastiao on 04/11/14.
 */
public class ParseHandling

{

    public static JsonNode getMessage(String message)
    {
        return Json.parse(message);
    }



    public static String calculateDestination(String destination){
        String _destinationFinal = destination;
        if (destination.contains(":"))
        {
            _destinationFinal = destination.split(":")[0];
        }
        return _destinationFinal;
    }


    public static String receiveMessage(String origin, String destination)
    {
        String message = "";
        ObjectNode jNode = JsonNodeFactory.instance.objectNode();
        jNode.put("origin", origin);
        jNode.put("destination", destination);
        jNode.put("message", "dataAvailable");

        return jNode.toString();
    }

}
