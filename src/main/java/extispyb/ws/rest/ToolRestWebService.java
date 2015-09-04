package extispyb.ws.rest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import tool.DimpleMultipartForm;
import tool.StructureValidationMultipartForm;

import com.google.gson.reflect.TypeToken;

import extispyb.core.collection.Job;
import extispyb.core.collection.Param;
import extispyb.core.collection.Project;
import extispyb.core.collection.Run;
import extispyb.core.collection.User;

@Path("/")
public class ToolRestWebService extends RestWebService {
	@PermitAll
	@POST
	@Path("rest/{token}/tool/crysol/run")
	@Consumes("multipart/form-data")
	@Produces({ "application/json" })
	public Response runCrysol(
			@PathParam("token") String token,
			@MultipartForm StructureValidationMultipartForm form) throws IllegalStateException, IOException{
		form.run(token);
		
		User user = User.getByToken(token);
		if (user != null){
			return sendResponse(user.getProjects());
		}
			
		return this.sendResponse(new String("ok"));
	}
	
	@PermitAll
	@POST
	@Path("rest/{token}/tool/dimple/run")
	@Consumes("multipart/form-data")
	@Produces({ "application/json" })
	public Response runDimple(
			@PathParam("token") String token,
			@MultipartForm DimpleMultipartForm form) throws IllegalStateException, IOException{
		form.run(token);
		User user = User.getByToken(token);
		if (user != null){
			return sendResponse(user.getProjects());
		}
			
		return this.sendResponse(new String("ok"));
	}
	
	
	
	@PermitAll
	@POST
	@Path("rest/{token}/project/{projectId}/run/{runId}/job/add")
	@Produces("text/plain")
	public Response addJon(
			@PathParam("token") String token,
			@PathParam("projectId") String projectId,
			@PathParam("runId") String runId,
			@FormParam("name") String name,
			@FormParam("version") String version,
			@FormParam("status") String status,
			@FormParam("startDate") String startDate,
			@FormParam("endDate") String endDate,
			@FormParam("input") String input,
			@FormParam("output") String output) throws Exception{
				
		Project project = Project.getByInternalId(projectId);
		if (project != null){
			Run run = project.getRun(runId);
			if (run != null){
				Job job = new Job();
				job.setName(name);
				job.setVersion(version);
				job.setStatus(status);
				job.setStartDate(startDate);
				job.setEndDate(endDate);
				
				Type type = new TypeToken<ArrayList<Param>>(){}.getType();
				if (input != null){
					ArrayList<Param> inputHash = getGson().fromJson(input, type);
					job.setInput(inputHash);
				}
				
				if (output != null){
					ArrayList<Param>  outputHash = getGson().fromJson(output, type);
					job.setOutput(outputHash);
				}
				System.out.println(output);
				System.out.println(job.getOutput());
				run.getJobs().add(job);
				System.out.println("Saving project");
				project.save();
			}
			else{
				throw new Exception("Run does not exist");
			}
		}
		else{
			throw new Exception("Project does not exist");
		}
		return this.sendResponse(new String("ok"));
	}
	
	
	

}
