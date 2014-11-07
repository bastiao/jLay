package messages;

/**
 * Created by bastiao on 05/11/14.
 */

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class MongoManager {
    public static Mongo mongo = null;

    public static void store(byte [] arr, String id)
    {

        //Load our image

        //Connect to database
        try
        {
            if (mongo==null)
                mongo = new Mongo( "127.0.0.1" );
            String dbName = "router";
            DB db = mongo.getDB( dbName );
            //Create GridFS object
            GridFS fs = new GridFS( db, "router" );
            //Save image into database
            ByteArrayInputStream is=new ByteArrayInputStream(arr);
            System.out.println("size: ");
            System.out.println(arr.length);
            GridFSInputFile in = fs.createFile( is, true );
            in.setFilename(id);
            in.setId(id);
            in.setContentType("router-message");
            in.save();
            mongo.close();
            mongo =null;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public static byte [] get(String id)
    {
        try
        {

            if (mongo==null)
                mongo = new Mongo( "127.0.0.1" );
            String dbName = "router";
            DB db = mongo.getDB( dbName );

            GridFS fs = new GridFS( db, "router"  );
            //Find saved image
            GridFSDBFile out = fs.findOne( id );

            ByteArrayOutputStream b = new ByteArrayOutputStream();

            System.out.println("Reading file");
            if (out!=null)
                out.writeTo( b );

            System.out.println("Reading file with bytes b  " +b.toByteArray().length);
            return b.toByteArray();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;

    }

}
