package tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import extispyb.core.collection.MongoIOUtils;
import extispyb.core.collection.Project;
import extispyb.core.collection.Run;
import extispyb.core.collection.Run.Status;
import extispyb.core.configuration.ExiSaxsProperties;

public class StructureValidationMultipartForm {

	@FormParam("file")
	private InputStream pdbInputStream;

	@FormParam("projectId")
	private String projectId;
	
	@FormParam("subtractionId")
	private String subtractionId;

	@FormParam("pdbFileName")
	private String pdbFileName;
	
	public InputStream getPdbInputStream() {
		return pdbInputStream;
	}

	public void setPdbInputStream(InputStream pdbInputStream) {
		this.pdbInputStream = pdbInputStream;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	
	public String getPdbFileName() {
		return pdbFileName;
	}

	public void setPdbFileName(String pdbFileName) {
		this.pdbFileName = pdbFileName;
	}

	public Project getProject(){
		return Project.getByInternalId(this.projectId);
	}
	public void run(String token) throws IllegalStateException, IOException {
		Project project = this.getProject();
		Run run = new Run("Structure Validation", "");
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

	private void launch(List<NameValuePair> urlParameters, String name) throws IllegalStateException, IOException {
		String url = ExiSaxsProperties.getInstance().getProperties().get("workflow.server").toString();
		url = url + name + "/RUN";
		
		HttpClient client = new DefaultHttpClient();
		
		String paramString = URLEncodedUtils.format(urlParameters, "utf-8");
		url = url + "?" + paramString;
		HttpPost post = new HttpPost(url);
		
		HttpParams params = new BasicHttpParams();
		post.setParams(params);
		post.setEntity(new UrlEncodedFormEntity(urlParameters));
//		System.out.println(new UrlEncodedFormEntity(urlParameters).toString());
		HttpResponse response = client.execute(post);
//		System.out.println("\nSending 'POST' request to URL : " + url);
//		System.out.println("Post parameters : " + post.getEntity());
//		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
 
		BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
 
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
 
		System.out.println(result.toString());
		
		
		
	}

	public String getSubtractionId() {
		return subtractionId;
	}

	public void setSubtractionId(String subtractionId) {
		this.subtractionId = subtractionId;
	}
	
	



}
