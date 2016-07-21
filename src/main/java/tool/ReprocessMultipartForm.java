package tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import extispyb.core.collection.Project;
import extispyb.core.collection.Run;
import extispyb.core.collection.Run.Status;
import extispyb.core.collection.User;

public class ReprocessMultipartForm extends ToolMultipartForm {

	@FormParam("dataCollectionId")
	protected String dataCollectionId;
	
	@FormParam("start")
	protected String start;
	
	public String getStart() {
		return start;
	}


	public void setStart(String start) {
		this.start = start;
	}


	public String getEnd() {
		return end;
	}


	public void setEnd(String end) {
		this.end = end;
	}


	public String getCutoff() {
		return cutoff;
	}


	public void setCutoff(String cutoff) {
		this.cutoff = cutoff;
	}


	@FormParam("end")
	protected String end;
	
	@FormParam("cutoff")
	protected String cutoff;
	
	
	public String getDataCollectionId() {
		return dataCollectionId;
	}


	public void setDataCollectionId(String dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}


	public void run(String token) throws IllegalStateException, IOException {
		Project project = this.getProject();
		Run run = new Run("reprocess", this.name, this.description);
		run.getInputs().put("dataCollectionId", this.getDataCollectionId());
		run.getInputs().put("cutoff", this.getCutoff());
		run.getInputs().put("start", this.getStart());
		run.getInputs().put("end", this.getEnd());
		run.getInputs().put("ispyb", this.getIspyb());
		
		run.setStatus(Status.CREATED);
		run.save();
		
		project.getRuns().add(run);
		project.save();
		
		User user = extispyb.core.collection.User.getByToken(token);
		
		List<NameValuePair> urlParameters = this.getGenericParameters(token, project, run);
		urlParameters.add(new BasicNameValuePair("ispyb", this.getIspyb()));
		urlParameters.add(new BasicNameValuePair("dataCollectionId", this.getDataCollectionId()));
		urlParameters.add(new BasicNameValuePair("start", this.getStart()));
		urlParameters.add(new BasicNameValuePair("end", this.getEnd()));
		urlParameters.add(new BasicNameValuePair("cutoff", this.getCutoff()));
		System.out.println("-------------");
		System.out.println(urlParameters);
		System.out.println("-------------");
		this.launch(urlParameters, "reprocess");
	}
	
	
}
