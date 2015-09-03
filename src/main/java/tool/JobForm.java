package tool;

import java.util.Date;
import java.util.HashMap;

import javax.ws.rs.FormParam;

public class JobForm {

	@FormParam("name")
	private String name;
	
//	private String status;
	
	@FormParam("version")
	private String version;
	@FormParam("start")
	private Date start;
	@FormParam("end")
	private Date end;
	
	@FormParam("input")
	private HashMap<String, HashMap<String, String>> input = new HashMap<String, HashMap<String,String>>();
	
	@FormParam("output")
	private HashMap<String, HashMap<String, String>> output = new HashMap<String, HashMap<String,String>>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public HashMap<String, HashMap<String, String>> getInput() {
		return input;
	}

	public void setInput(HashMap<String, HashMap<String, String>> input) {
		this.input = input;
	}

	public HashMap<String, HashMap<String, String>> getOutput() {
		return output;
	}

	public void setOutput(HashMap<String, HashMap<String, String>> output) {
		this.output = output;
	}
}
