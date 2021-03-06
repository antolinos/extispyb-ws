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

public class DimpleMultipartForm extends ToolMultipartForm {

	
	@FormParam("pdb")
	protected InputStream pdbInputStream;
	
	@FormParam("mtz")
	protected InputStream mtzInputStream;
	
	@FormParam("mtzFileName")
	protected String mtzFileName;

	@FormParam("pdbFileName")
	protected String pdbFileName;

	public InputStream getPdbInputStream() {
		return pdbInputStream;
	}

	public void setPdbInputStream(InputStream pdbInputStream) {
		this.pdbInputStream = pdbInputStream;
	}

	public InputStream getMtzInputStream() {
		return mtzInputStream;
	}

	public void setMtzInputStream(InputStream mtzInputStream) {
		this.mtzInputStream = mtzInputStream;
	}

	public String getMtzFileName() {
		return mtzFileName;
	}

	public void setMtzFileName(String mtzFileName) {
		this.mtzFileName = mtzFileName;
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
		
		String mtzFileId = MongoIOUtils.save(getMtzInputStream(), getMtzFileName()).toHexString();
		run.getInputs().put("mtzFileId", mtzFileId);
		
		run.setStatus(Status.CREATED);
		run.save();
		
		project.getRuns().add(run);
		project.save();
		
		
		List<NameValuePair> urlParameters = this.getGenericParameters(token, project, run);
		urlParameters.add(new BasicNameValuePair("pdbFile", String.format("%s/file/%s/download", this.getServerURL(), pdbFileId)));
		urlParameters.add(new BasicNameValuePair("mtzFile", String.format("%s/file/%s/download", this.getServerURL(), mtzFileId)));
		this.launch(urlParameters, "dimple");
	}

	
	
	
}
