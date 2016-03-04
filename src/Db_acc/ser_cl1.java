package Db_acc;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import com.fasterxml.jackson.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect.*;
import com.fasterxml.jackson.annotation.PropertyAccessor.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.DBObject;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import com.mongodb.util.JSON;
import java.util.ArrayList;
import java.util.List;

class ClientRecord {
	public String Client_Id = "client_id";
	public String Client = "client";
	public String DataInput = "data input";
}

@Path ("Input")
public class ser_cl1 {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response clPost(String msg) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objMap = new ObjectMapper();
		objMap.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
		ClientRecord clRecord = objMap.readValue(msg, ClientRecord.class); //deserialize JSON
		MongoClient db_cl;
		db_cl = new MongoClient("localhost", 27017);
		DB db = db_cl.getDB("database_name");
		DBCollection tab = db.getCollection("273_LAb1");
		DBObject query = (DBObject) JSON.parse(msg); 
		tab.insert(query);    //Insert current entry in the DB
		Integer i;
		i = Integer.parseInt(clRecord.Client_Id);
		i--;
		BasicDBObject nQuery = new BasicDBObject();
		List<BasicDBObject> ls_srch = new ArrayList<BasicDBObject>();
		ls_srch.add(new BasicDBObject("Client_Id", i.toString()));
		ls_srch.add(new BasicDBObject("Client", clRecord.Client));
		nQuery.put("$and", ls_srch);
		BasicDBObject fld = new BasicDBObject();
	    fld.put("DataInput", 1);
		DBObject cursor = tab.findOne(nQuery, fld);	//Read previous data
		JSON json = new JSON();
		String json_op = json.serialize(cursor);
		return Response.ok(json_op, MediaType.APPLICATION_JSON).build(); //Response Set
	 }
	
	@GET
	//@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response clOutput() throws IOException{ 
		//System.out.println(msg);
		return Response.status(Response.Status.CREATED).entity("HELLO!@@").build();
	}
}