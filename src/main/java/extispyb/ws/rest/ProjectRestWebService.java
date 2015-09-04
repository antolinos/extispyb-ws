package extispyb.ws.rest;

import java.util.HashMap;

import javax.annotation.security.PermitAll;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import extispyb.core.collection.Project;
import extispyb.core.collection.Run;
import extispyb.core.collection.Run.Status;
import extispyb.core.collection.User;

@Path("/")
public class ProjectRestWebService extends RestWebService {

	@PermitAll
	@GET
	@Path("rest/{token}/project/list")
	@Produces({ "application/json" })
	public Response getProjectList(@PathParam("token") String token) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", String.valueOf(token));
		long start = this.logInit("getProjectList", getGson().toJson(params));
		try {
			User user = User.getByToken(token);
			if (user != null){
				return sendResponse(user.getProjects());
			}
		} catch (Exception e) {
			this.logError("getProjectList", start, e.getMessage(), e);
		}
		return Response.serverError().build();
	}

	
	@PermitAll
	@GET
	@Path("rest/{token}/project/{internalId}/subtraction/{subtractionIds}/save")
	@Produces({ "application/json" })
	public Response saveSubtraction(@PathParam("token") String token,
			@PathParam("internalId") String internalId,
			@PathParam("subtractionIds") String subtractionIds)
			throws Exception {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", String.valueOf(token));
		params.put("internalId", String.valueOf(internalId));
		params.put("subtractionIds", String.valueOf(subtractionIds));
		long start = this.logInit("saveSubtraction", getGson().toJson(params));
		try {
			Project project = Project.getByInternalId(internalId);
			project.setSubtractions(this.parseToInteger(subtractionIds));
			project.save();
			return sendResponse(project);
			
		} catch (Exception e) {
			this.logError("saveSubtraction", start, e.getMessage(), e);
		}
		return Response.serverError().build();
	}


	@PermitAll
	@GET
	@Path("rest/{token}/project/{internalId}/run/list")
	@Produces({ "application/json" })
	public Response getRuns(
			@PathParam("token") String token,
			@PathParam("internalId") String internalId)
			throws Exception {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", String.valueOf(token));
		params.put("internalId", String.valueOf(internalId));
		long start = this.logInit("getRuns", getGson().toJson(params));
		try {
			Project project = Project.getByInternalId(internalId);
			return sendResponse(project.getRuns());
			
		} catch (Exception e) {
			this.logError("getRuns", start, e.getMessage(), e);
		}
		return Response.serverError().build();
	}
	
	
	@PermitAll
	@POST
	@Path("rest/{token}/project/{internalId}/run/{toolName}/add")
	@Produces({ "application/json" })
	public Response addRunToProject(@PathParam("token") String token,
			@PathParam("internalId") String internalId,
			@PathParam("toolName") String toolName,
			@FormParam("input") String input) throws Exception {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", String.valueOf(token));
		params.put("internalId", String.valueOf(internalId));
		params.put("input", String.valueOf(input));
		long start = this.logInit("addRunToProject", getGson().toJson(params));
		try {
			Project project = Project.getByInternalId(internalId);
			if (project != null) {
				Run run = new Run(toolName, "", "");
				run.setInputs(input);
				project.getRuns().add(run);
				project.save();
				this.logFinish("addRunToProject", start);
			}
			return sendResponse(project);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.serverError().build();
	}

	
	
	@PermitAll
	@POST
	@Path("rest/{token}/project/{projectId}/run/{runId}/status/{statusEnum}/save")
	@Produces({ "application/json" })
	public Response saveStatusProject(
			@PathParam("token") String token,
			@PathParam("projectId") String projectId,
			@PathParam("runId") String runId,
			@PathParam("statusEnum") String status) throws Exception {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", String.valueOf(token));
		params.put("projectId", String.valueOf(projectId));
		params.put("status", String.valueOf(status));
		long start = this.logInit("saveStatusProject", getGson().toJson(params));
		try {
			Project project = Project.getByInternalId(projectId);
			if (project != null) {
				project.getRun(runId).setStatus(Status.valueOf(status));
				project.save();
				this.logFinish("saveStatusProject", start);
			}
			return sendResponse(project);
		} catch (Exception e) {
			this.logError("saveStatusProject", start, e.getMessage(), e);
			e.printStackTrace();
		}
		return Response.serverError().build();
	}

	// @PermitAll
	// @POST
	// @Path("rest/{token}/project/create")
	// @Produces({ "application/json" })
	// public Response add(@PathParam("token") String token) throws Exception {
	// return sendResponse("create new project");
	// }
	//
	// @PermitAll
	// @DELETE
	// @Path("rest/{token}/project/{projectId}/delete")
	// @Produces({ "application/json" })
	// public Response remove(@PathParam("token") String token,
	// @PathParam("projectId") String projectId) throws Exception {
	// return sendResponse("Remove project: " + projectId + " by " + token);
	// }
	//
	// @PermitAll
	// @PUT
	// @Path("rest/{token}/project/{projectId}/edit")
	// @Produces({ "application/json" })
	// public Response edit(@PathParam("token") String token,
	// @PathParam("project") String project) throws Exception {
	// return sendResponse("Edit project: " + project + " by " + token);
	// }

}
