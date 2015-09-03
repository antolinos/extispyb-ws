package extispyb.core.collection;

import java.lang.reflect.Modifier;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MongoObject {
//	protected String _id;
	
	private Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create();
	
	public String toJSON(){
		return gson.toJson(this);
	}
	
	public Document toDocument(){
		return Document.parse(gson.toJson(this));
	}
}
