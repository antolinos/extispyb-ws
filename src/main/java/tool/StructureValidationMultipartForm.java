package tool;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import extispyb.core.collection.MongoIOUtils;
import extispyb.core.collection.Project;
import extispyb.core.collection.Run;
import extispyb.core.collection.Run.Status;

public class StructureValidationMultipartForm extends ToolMultipartForm {

	@FormParam("file")
	protected InputStream pdbInputStream;
	
	@FormParam("subtractionId")
	protected String subtractionId;

	@FormParam("pdbFileName")
	protected String pdbFileName;
	
	public InputStream getPdbInputStream() {
		return pdbInputStream;
	}

	public void setPdbInputStream(InputStream pdbInputStream) {
		this.pdbInputStream = pdbInputStream;
	}
	
	public String getPdbFileName() {
		return pdbFileName;
	}

	public void setPdbFileName(String pdbFileName) {
		this.pdbFileName = pdbFileName;
	}

	
	public void run(String token) throws IllegalStateException, IOException {
		Project project = this.getProject();
		Run run = new Run("Structure Validation", this.name, this.description);
		run.getInputs().put("subtractionId", this.getSubtractionId());
		String pdbFileId = MongoIOUtils.save(getPdbInputStream(), getPdbFileName()).toHexString();
		run.getInputs().put("pdbFileId", pdbFileId);
		run.setStatus(Status.CREATED);
		run.save();
		
		project.getRuns().add(run);
		project.save();
		
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("token", token));
		urlParameters.add(new BasicNameValuePair("subtractionId", this.getSubtractionId()));
		urlParameters.add(new BasicNameValuePair("runId", run.getInternalId()));
		urlParameters.add(new BasicNameValuePair("pdbFileId", pdbFileId));
		urlParameters.add(new BasicNameValuePair("projectId", project.getInternalId()));
		
		this.launch(urlParameters, "structurevalidation");
	}

	
	public String getSubtractionId() {
		return subtractionId;
	}

	public void setSubtractionId(String subtractionId) {
		this.subtractionId = subtractionId;
	}

}
