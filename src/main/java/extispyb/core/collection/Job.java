package extispyb.core.collection;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Job {

	protected String name;
	protected String status;
	protected String version;
	protected Date start;
	protected Date end;
	protected ArrayList<Param> input = new ArrayList<Param>();
	protected ArrayList<Param> output =  new ArrayList<Param>();
	protected String startDate;
	protected String endDate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
	public ArrayList<Param> getInput() {
		return input;
	}
	public void setInput(ArrayList<Param> input) {
		this.input = input;
	}
	public ArrayList<Param> getOutput() {
		return output;
	}
	public void setOutput(ArrayList<Param> output) {
		this.output = output;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
		
	}
	public String getEndDate() {
		return endDate;
	}
	public String getStartDate() {
		return startDate;
	}
	
}
