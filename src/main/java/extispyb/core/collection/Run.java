package extispyb.core.collection;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



@Entity(value="runs", noClassnameStored=true)
public class Run extends EntityObject{
	
	public enum Status {
		CREATED,
		SENT,
		STARTED,
		FINISHED,
		ERROR
	}

	
	@Id 
	private ObjectId id;
	protected String name;
	protected String description;
	protected Status status;
	protected Date start;
	protected Date end;
	

	@Embedded
	protected HashMap<String, String> inputs = new HashMap<String, String>();
	
	@Embedded
	protected List<Job> jobs = new ArrayList<Job>();
	protected Date creationDate;
	
	public Run(){
	}
	
	public Run(String name, String description){
		this.name = name;
		this.setDescription(description);
		this.creationDate = Calendar.getInstance().getTime();
		this.status = Status.CREATED;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public HashMap<String, String> getInputs() {
		return inputs;
	}
	public void setInputs(HashMap<String, String> inputs) {
		this.inputs = inputs;
	}
	public List<Job> getJobs() {
		return jobs;
	}
	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public void setInputs(String input) {
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		HashMap<String,String> inputHash = new Gson().fromJson(input, type);
		this.setInputs(inputHash);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
