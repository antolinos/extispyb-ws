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
import extispyb.core.collection.User;
import extispyb.core.configuration.*;

public class DimpleDCMultipartForm extends ToolMultipartForm {

	
	@FormParam("pdb")
	protected InputStream pdbInputStream;
	
	@FormParam("dataCollectionId")
	protected String dataCollectionId;

	@FormParam("pdbFileName")
	protected String pdbFileName;

	@FormParam("autoprocintegrationURL")
	protected String autoprocintegrationURL;
	
	@FormParam("autoProcAttachmentURL")
	protected String autoProcAttachmentURL;
	
	public String getAutoProcAttachmentURL() {
		return autoProcAttachmentURL;
	}


	public void setAutoProcAttachmentURL(String autoProcAttachmentURL) {
		this.autoProcAttachmentURL = autoProcAttachmentURL;
	}


	@FormParam("downloadAutoProcProgramAttachementURL")
	protected String downloadAutoProcProgramAttachementURL;
	
	public String getAutoprocintegrationURL() {
		return autoprocintegrationURL;
	}


	public void setAutoprocintegrationURL(String autoprocintegrationURL) {
		this.autoprocintegrationURL = autoprocintegrationURL;
	}


	


	public String getDownloadAutoProcProgramAttachementURL() {
		return downloadAutoProcProgramAttachementURL;
	}


	public void setDownloadAutoProcProgramAttachementURL(
			String downloadAutoProcProgramAttachementURL) {
		this.downloadAutoProcProgramAttachementURL = downloadAutoProcProgramAttachementURL;
	}



	
	
	
	public String getDataCollectionId() {
		return dataCollectionId;
	}


	public void setDataCollectionId(String dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}
	
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
		Run run = new Run("Dimple", this.name, this.description);
		String pdbFileId = MongoIOUtils.save(getPdbInputStream(), getPdbFileName()).toHexString();
		run.getInputs().put("pdbFileId", pdbFileId);
		
		
		run.setStatus(Status.CREATED);
		run.save();
		
		project.getRuns().add(run);
		project.save();
		
		
		
		List<NameValuePair> urlParameters = this.getGenericParameters(token, project, run);
		urlParameters.add(new BasicNameValuePair("pdbFile", String.format("%s/file/%s/download", this.getServerURL(), pdbFileId)));
		urlParameters.add(new BasicNameValuePair("dataCollectionId", this.getDataCollectionId()));
		urlParameters.add(new BasicNameValuePair("ispyb", this.getIspyb()));
		urlParameters.add(new BasicNameValuePair("autoProcAttachmentURL", this.getAutoProcAttachmentURL()));
		urlParameters.add(new BasicNameValuePair("autoprocintegrationURL", this.getAutoprocintegrationURL()));
		urlParameters.add(new BasicNameValuePair("downloadAutoProcProgramAttachementURL", this.getDownloadAutoProcProgramAttachementURL()));
		System.out.println("-------------");
		System.out.println(urlParameters);
		System.out.println("-------------");
		this.launch(urlParameters, "dimple");
	}
	
	
}
