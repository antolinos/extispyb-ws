package tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.ws.rs.FormParam;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import extispyb.core.collection.Project;
import extispyb.core.configuration.ExiSaxsProperties;

public class ToolMultipartForm {
	@FormParam("name")
	protected String name;
	
	@FormParam("description")
	protected String description;
	
	@FormParam("projectId")
	protected String projectId;
	
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
