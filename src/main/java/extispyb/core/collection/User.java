package extispyb.core.collection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.utils.IndexDirection;

import extispyb.core.configuration.MongoDriver;

@Entity(value="users", noClassnameStored=true)
public class User extends EntityObject {
	@Id 
	private ObjectId id;
	
	
	@Indexed(value=IndexDirection.ASC, unique=true, dropDups=true)
	protected String name;
	protected String email;
	protected String token;
	
	@Reference("projects")
	protected List<Project> projects = new ArrayList<Project>();
	
	protected Date creationDate;
	
	public User() {
	}
	
	public User(String name) {
		this(name, new String());
		this.name = name;
		this.creationDate = Calendar.getInstance().getTime();
	}
	

	public User(String name, String email) {
		super();
		this.name = name;
		this.email = email;
		this.creationDate = Calendar.getInstance().getTime();
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public ObjectId getId() {
		return this.id;
	}
	
	
	
	
	
	public static User getByName(String name){
		Query<User> query = MongoDriver.getInstance().getDatastore().createQuery(User.class)
				.field("name")
				.equal(name);
		return query.get();
	}
	
	public static User getById(ObjectId id){
		Query<User> query = MongoDriver.getInstance().getDatastore().createQuery(User.class).field("_id").equal(id);
		return query.get();
	}
	

	public static User getByToken(String token) {
		Query<User> query = MongoDriver.getInstance().getDatastore().createQuery(User.class).field("token").equal(token);
		return query.get();
	}

	public static User init(String userName, String token) {
		User user = new User(userName);
		user.setToken(token);
		Project project = new Project("DEFAULT", "Default empty project");
		project.save();
		user.getProjects().add(project);
		user.save();
		return user;
	}

	

}
