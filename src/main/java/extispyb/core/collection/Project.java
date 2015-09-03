package extispyb.core.collection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.query.Query;

import extispyb.core.configuration.MongoDriver;

@Entity(value="projects", noClassnameStored=true)
public class Project extends EntityObject{
	@Id 
	private ObjectId id;
	
	@Embedded
	protected List<Integer> subtractions = new ArrayList<Integer>();
	@Embedded
	protected List<Integer> macromolecules = new ArrayList<Integer>();
	@Embedded
	protected List<Run> runs = new ArrayList<Run>();
	protected Date creationDate;
	protected String name;
	protected String description;
	
	
	public Project(){
	}
	
	public Project(String name, String description){
		super();
		this.name = name;
		this.description = description;
		this.creationDate = Calendar.getInstance().getTime();
	}
	
	
	public List<Integer> getSubtractions() {
		return subtractions;
	}

	public void setSubtractions(List<Integer> subtractions) {
		this.subtractions = subtractions;
	}

	public List<Integer> getMacromolecules() {
		return macromolecules;
	}

	public void setMacromolecules(List<Integer> macromolecules) {
		this.macromolecules = macromolecules;
	}

	public List<Run> getRuns() {
		return runs;
	}

	public void setRuns(List<Run> runs) {
		this.runs = runs;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static Project getByInternalId(String internalId) {
		Query<Project> query = MongoDriver.getInstance().getDatastore().createQuery(Project.class).field("internalId").equal(internalId);
		return query.get();
	}
	
	public Run getRun(String id){
		for (Run run : this.runs) {
			if (run.getInternalId().equals(id)){
				return run;
			}
		}
		return null;
	}
	
}
