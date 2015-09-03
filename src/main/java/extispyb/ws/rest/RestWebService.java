package extispyb.ws.rest;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.util.JSON;

import extispyb.core.collection.MongoIOUtils;
import extispyb.core.collection.Project;
import extispyb.core.collection.User;
import extispyb.core.configuration.MongoDriver;
import extispyb.logger.LoggerFormatter;

public class RestWebService {
	
	private final static Logger logger = Logger.getLogger(RestWebService.class);
	
	private long now;
	protected Morphia morphia = MongoDriver.getInstance().getMorphia();
	
	protected Gson getGson() {
		return new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE)
				.create();
	}
	
	protected Response sendResponse(String response) {
		return Response.ok(response).header("Access-Control-Allow-Origin", "*")
				.build();
	}
	
	protected Response sendResponse(Object response) {
		return Response.ok(getGson().toJson(response))
				.header("Access-Control-Allow-Origin", "*").build();
	}

	protected User getUserByToken(String token) {
		return User.getByToken(token);
	}
	
	public List<Integer> parseToInteger(String commaSeparated) {
		if (commaSeparated != null){
			List<String> macromoleculeIdsString = Arrays.asList(commaSeparated.split(","));
			ArrayList<Integer> macromolecules = new ArrayList<Integer>();
			if (!macromoleculeIdsString.equals("")) {
				for (String string : macromoleculeIdsString) {
					try {
						/** TODO: Reg expresion on this **/
						macromolecules.add(Integer.valueOf(string));
					} catch (Exception e) {
						/** No parseable value ***/
						System.out.println(e.getMessage());
					}
				}
			}
			return macromolecules;
		}
		return null;
	}
	
	
	
	
	
	protected long logInit(String methodName, String params) {
		logger.info("-----------------------");
		this.now = System.currentTimeMillis();
		logger.info(methodName.toUpperCase());
		LoggerFormatter.log(logger, LoggerFormatter.Package.EXTISPyB, methodName, System.currentTimeMillis(),
				System.currentTimeMillis(), params);
		return this.now;
	}
	
	protected void logFinish(String methodName, long id) {
		logger.debug("### [" + methodName.toUpperCase() + "] Execution time was " + (System.currentTimeMillis() - this.now) + " ms.");
		LoggerFormatter.log(logger, LoggerFormatter.Package.EXTISPyB, methodName, id, System.currentTimeMillis(),
				System.currentTimeMillis() - this.now);

	}
	

	protected void logError(String methodName, long start, String message, Exception e) {
		e.printStackTrace();
		LoggerFormatter.log(logger, LoggerFormatter.Package.EXTISPyB_ERROR, methodName, start, System.currentTimeMillis(),
				System.currentTimeMillis() - this.now);
		
	}
}
