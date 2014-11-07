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

import com.dicomcloudrouter.GeneralMessage;
import com.dicomcloudrouter.MessageI;
import com.pacscloud.bridgelib.msg.AETitle;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;


/**
 *
 * @author Luís A. Bastião Silva - <bastiao@ua.pt>
 */
public class DataStream {
    private final String USER_AGENT = "Mozilla/5.0";
    final static String MULTIPART_BOUNDARY = "------------------563i2ndDfv2rTHiSsdfsdbouNdArYfORhxcvxcvefj3q2f";

    public void sendData(Serializable message)
    {
        String url = GlobalSettings.getHTTPServerRootURL()+"receiveMessage";
        URL obj =null ;
        try {
            obj = new URL(url);
        
            //HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + MULTIPART_BOUNDARY);
            con.setRequestProperty("charset", "utf-8");
            
            String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
            con.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            
            byte[] data = SerializationUtils.serialize(message);

            wr.write(data);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());
        } catch (Exception ex) {
            Logger.getLogger(DataStream.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    public MessageI getData(String id)
    {
        
        
        String url = GlobalSettings.getHTTPServerRootURL()+"getMessage?id="+id;
        try
        {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            
            BufferedInputStream in = new BufferedInputStream(
                    con.getInputStream());
            
            
            
            System.out.println("connection lenght ");
            
            System.out.println(con.getContentLength());
            
            byte[] r = new byte[con.getContentLength()];
            in.read(r);
            
            in.close();
            System.out.println(r);
            System.out.println(r.length);
            GeneralMessage g = SerializationUtils.deserialize(r);
            return g;

            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null; 
    }
    
    
    public static void main(String [] args)
    {
        DataStream ds = new DataStream();
        AETitle aet = new AETitle();
        aet.setAETitle("asdasd");
        GeneralMessage m = new GeneralMessage(aet, "demo1", "demo2", "request");
        ds.sendData(m);
        GeneralMessage g = (GeneralMessage) ds.getData("demo2");
        System.out.println(g.getDestination());
        System.out.println(g.getOrigin());
        System.out.println(g.getMessageType());
        
    }
    

}
