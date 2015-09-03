package extispyb.test.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.enterprise.inject.spi.PassivationCapable;

import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.runners.MethodSorters;
import org.mongodb.morphia.query.Query;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.gridfs.GridFSDBFile;

import extispyb.core.collection.MongoIOUtils;
import extispyb.core.collection.Project;
import extispyb.core.collection.Run;
import extispyb.core.collection.User;
import extispyb.core.configuration.ExiSaxsProperties;
import extispyb.core.configuration.MongoDriver;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DBTest {

	String nameTest = "Morpheus";
	String email = nameTest + "@email.com";
	@Test
	public void readPropertiesConnection() {
		try {
			Assert.assertNotNull(ExiSaxsProperties.getInstance().getProperties().get("database.server"));
			Assert.assertNotNull(ExiSaxsProperties.getInstance().getProperties().get("database.server.port"));
			Assert.assertNotNull(ExiSaxsProperties.getInstance().getProperties().get("database.credential.user"));
			Assert.assertNotNull(ExiSaxsProperties.getInstance().getProperties().get("database.credential.password"));
			Assert.assertNotNull(ExiSaxsProperties.getInstance().getProperties().get("database.name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Test
	public void openConnection() {
			MongoDatabase database = MongoDriver.getInstance().getDatabase();
	}

	
	@Test
	public void A_createUser() {
		new User(nameTest).save();
	}
	
	@Test
	public void B_editUser() {
		/** Retrieve test user **/
		User user = User.getByName(nameTest);
		Assert.assertTrue(user != null);
		
		/** Edit test user **/
		user.setEmail(email);
		user.save();
		
		/** Check edition **/
		user = User.getByName(nameTest);
		Assert.assertTrue(user != null);
		Assert.assertTrue(user.getEmail().equals(email));
	}
	
	@Test
	public void C_removeUser() {
		/** Retrieve test user **/
		User user = User.getByName(nameTest);
		Assert.assertTrue(user != null);
		
		/** Edit test user **/
		MongoDriver.getInstance().getDatastore().delete(user);
		
		/** Check edition **/
		user = User.getByName(nameTest);
		Assert.assertTrue(user == null);
	}
	
	@Test
	public void D_getById() {
		User user = new User(nameTest);
		MongoDriver.getInstance().getDatastore().save(user);
					
		/** Retrieve test user **/
		user = User.getByName(nameTest);
		Assert.assertTrue(user != null);
		
		/** Querying by id **/
		user =  User.getById(user.getId());
		Assert.assertTrue(user != null);
		
		C_removeUser();
	}
	
	@Test
	public void E_createProject() {
		User user = new User(nameTest);
		MongoDriver.getInstance().getDatastore().save(user);
					
		Project project = new Project("Morpheus Project", "This is a test project executed from JUnit test");
		
		project.save();
		
		/** Retrieve test user **/
		user = User.getByName(nameTest);
		Assert.assertTrue(user != null);
		user.getProjects().add(project);
		
		/** Saving user **/
		user.save();
		
		/** Retrieve test user **/
		user = User.getByName(nameTest);
		Assert.assertTrue(user != null);
		Assert.assertTrue(user.getProjects().size() == 1);
		
		/** Removing user **/
		C_removeUser();
		
		/** Removing project **/
		MongoDriver.getInstance().getDatastore().delete(project);
	}
	
	@Test
	public void F_retrieveProject() {
		User user = new User(nameTest);
		MongoDriver.getInstance().getDatastore().save(user);
					
		Project project_1 = new Project("1 Morpheus Project", "This is a test project executed from JUnit test");
		project_1.save();
		
		Project project_2 = new Project("2 Morpheus Project", "This is a test project executed from JUnit test");
		project_2.save();
		
		/** Retrieve test user **/
		user = User.getByName(nameTest);
		Assert.assertTrue(user != null);
		user.getProjects().add(project_1);
		user.getProjects().add(project_2);
		
		/** Saving user **/
		user.save();
		
		/** Retrieve test user **/
		user = User.getByName(nameTest);
		Assert.assertTrue(user != null);
		Assert.assertTrue(user.getProjects().size() == 2);
		
		/** Removing user **/
		C_removeUser();
		
		System.out.println(new Gson().toJson(user.getProjects()));
		
		/** Removing project **/
		MongoDriver.getInstance().getDatastore().delete(project_1);
		MongoDriver.getInstance().getDatastore().delete(project_2);
	}
	
	
	@Test
	public void G_saveFile() throws IOException {
		
		ObjectId id = MongoIOUtils.save("/tmp/BSA_0010-1-1_dimer.pdb");
		System.out.println(id);
		
		
		ObjectId id2 = new ObjectId(id.toString());
		
		
		GridFSDBFile newFile = MongoIOUtils.getById(id2);
		System.out.println(newFile.getFilename());
		
		System.out.println(newFile.getLength());
		
		System.out.println(IOUtils.toString(newFile.getInputStream()));
		
		
	}
//	@Test
//	public void F_createRun() {
//		User user = new User(nameTest);
//		MongoDriver.getInstance().getDatastore().save(user);
//					
//		/** Retrieve test user **/
//		user = User.getByName(nameTest);
//		Assert.assertTrue(user != null);
//		
//		Project project = new Project("Morpheus Project", "This is a test project executed from JUnit test");
//		
//		project.getRuns().add(new Run("Morpheus Run", "this is a test RUN"));
//		user.getProjects().add(project);
//		
//		/** Saving user **/
//		user.save();
//		
//		/** Retrieve test user **/
//		user = User.getByName(nameTest);
//		Assert.assertTrue(user != null);
//		Assert.assertTrue(user.getProjects().size() == 1);
//		
//		/** Removing user **/
//		C_removeUser();
//	}
	
}
