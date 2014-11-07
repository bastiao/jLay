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
import com.dicomcloudrouter.GeneralMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import messages.MongoManager;
import messages.ParseHandling;
import org.apache.commons.lang3.SerializationUtils;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;


/**
 * Created by bastiao on 05/11/14.
 */
public class DataController extends Controller {


    @BodyParser.Of(BodyParser.Json.class)
    public static Result getConnection() {
        JsonNode json = request().body().asJson();

        ObjectNode result = Json.newObject();
        result.put("status", "KO");
        return ok(result);

    }


    @BodyParser.Of(BodyParser.Raw.class)
    public static Result receiveMessage() {


        /* let's see what is happening here */

        System.out.println("Just to be sure that it receives the bytes properly");

        if (request().body().asRaw()!=null)
        {
            System.out.println(request().body().asRaw().size());

            GeneralMessage yourObject = (GeneralMessage) SerializationUtils.deserialize(request().body().asRaw().asBytes());
            System.out.println("Destination_: " + yourObject.getDestination());
            System.out.println("Origin: _" + yourObject.getOrigin());
            System.out.println("message type_ " + yourObject.getMessageType());



            MongoManager.store( request().body().asRaw().asBytes(), yourObject.getDestination());
            WebSocket.Out<String> wsOut = WebSocketsStore.connectionsMap.get(ParseHandling.calculateDestination(yourObject.getDestination()));
            System.out.println(WebSocketsStore.connectionsMap);
            System.out.println(ParseHandling.calculateDestination(yourObject.getDestination()));
            if (wsOut!=null)
            {
                System.out.println(ParseHandling.receiveMessage(yourObject.getOrigin(), yourObject.getDestination()));
                wsOut.write(ParseHandling.receiveMessage(yourObject.getOrigin(), yourObject.getDestination()));
            }

            else
                System.out.println("Does not receive sockets :(((");
        }
        else
        {
            System.out.println("Cannot read the raw data");
        }

        JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();
        result.put("status", "receiveMessage");
        return ok(result);

    }


    //@BodyParser.Of(BodyParser.Json.class)
    public static Result getMessage() {
        //JsonNode json = request().body().asJson();


        String id = request().getQueryString("id");
        ObjectNode result = Json.newObject();
        result.put("status", "KO");
        return ok(MongoManager.get(id));

        //return ok(result);

    }
}
