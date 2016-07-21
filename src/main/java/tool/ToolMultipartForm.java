package tool;

import java.io.BufferedReader;
import java.io.IOException;
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

import extispyb.core.collection.Project;
import extispyb.core.collection.Run;
import extispyb.core.collection.User;
import extispyb.core.configuration.ExiSaxsProperties;

public class ToolMultipartForm {
	@FormParam("name")
	protected String name;
	
	@FormParam("description")
	protected String description;
	
	@FormParam("projectId")
	protected String projectId;
	
	@FormParam("ispyb")
	protected String ispyb;
	
	public String getIspyb() {
		return ispyb;
	}

	public void setIspyb(String ispyb) {
		this.ispyb = ispyb;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
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
	
	public Project getProject(){
		return Project.getByInternalId(this.projectId);
	}
	
	protected String getServerURL() {
		return ExiSaxsProperties.getInstance().getProperties().get("extispyb.server").toString();
	}
	
	protected List<NameValuePair> getGenericParameters(String token, Project project, Run run) {
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("token", token));
		urlParameters.add(new BasicNameValuePair("projectId", project.getInternalId()));
		urlParameters.add(new BasicNameValuePair("runId", run.getInternalId()));
		
		User user = extispyb.core.collection.User.getByToken(token);
		urlParameters.add(new BasicNameValuePair("initiator", "EXI"));
		urlParameters.add(new BasicNameValuePair("externalRef", user.getName()));
		urlParameters.add(new BasicNameValuePair("reuseCase", "true"));
		urlParameters.add(new BasicNameValuePair("extispyb", this.getServerURL()));
		
		return urlParameters;
	}
	
	
	protected void launch(List<NameValuePair> urlParameters, String name) throws IllegalStateException, IOException {
		String url = ExiSaxsProperties.getInstance().getProperties().get("workflow.server").toString();
		url = url + name + "/RUN";
		
		HttpClient client = new DefaultHttpClient();
		
		String paramString = URLEncodedUtils.format(urlParameters, "utf-8");
		url = url + "?" + paramString;
		HttpPost post = new HttpPost(url);
		
		HttpParams params = new BasicHttpParams();
		post.setParams(params);
		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		HttpResponse response = client.execute(post);
 
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
 
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		System.out.println(result.toString());
	}

}
