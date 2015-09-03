package extispyb.core.collection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bson.types.ObjectId;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import extispyb.core.configuration.MongoDriver;

public class MongoIOUtils{
	
	public static ObjectId save(String filePath){
		try {
			File file = new File(filePath);
			GridFS gridfs = new GridFS(MongoDriver.getInstance().getDB(), "files");
			GridFSInputFile gfsFile = gridfs.createFile(file);
			gfsFile.setFilename(file.getName());
			gfsFile.save();
			return (ObjectId) gfsFile.getId();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static GridFSDBFile getById(ObjectId id){
		GridFS gridfs = new GridFS(MongoDriver.getInstance().getDB(), "files");
		return gridfs.findOne(id);
	}
	
	public static ObjectId save(InputStream inputStream, String fileName){
		GridFS gridfs = new GridFS(MongoDriver.getInstance().getDB(), "files");
		GridFSInputFile gfsFile =  gridfs.createFile(inputStream);
		gfsFile.setFilename(fileName);
		gfsFile.save();
		return (ObjectId) gfsFile.getId();
	}
}
