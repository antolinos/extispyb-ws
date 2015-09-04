package extispyb.ws.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.bson.types.ObjectId;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import tool.FileUploaderMultipartForm;

import com.mongodb.gridfs.GridFSDBFile;

import extispyb.core.collection.MongoIOUtils;

@Path("/")
public class FileRestWebService extends RestWebService {

	@PermitAll
	@GET
	@Path("rest/file/{fileId}/download")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response download(@PathParam("fileId") String fileId) {
		GridFSDBFile file = MongoIOUtils.getById(new ObjectId(fileId));
		if (file != null) {
			ResponseBuilder response = Response.ok((Object) file
					.getInputStream());
			response.header("Content-Disposition", "attachment; filename="
					+ file.getFilename());
			return response.build();
		} else {
			System.out.println("Error file is null: " + fileId);
			return Response.serverError().build();
		}
	}

	@PermitAll
	@GET
	@Path("rest/file/{fileId}/image")
	@Produces("image/png")
	public Response image(@PathParam("fileId") String fileId)
			throws IOException {
		GridFSDBFile file = MongoIOUtils.getById(new ObjectId(fileId));
		if (file != null) {
			return Response.ok(file.getInputStream()).build();
		} else {
			System.out.println("Error file is null: " + fileId);
			return Response.serverError().build();
		}
	}

	@PermitAll
	@GET
	@Path("rest/file/{fileId}/content")
	@Produces({ MediaType.TEXT_PLAIN })
	public Response text(@PathParam("fileId") String fileId) throws IOException {
		GridFSDBFile file = MongoIOUtils.getById(new ObjectId(fileId));
		if (file != null) {
			return Response.ok(this.getStringFromInputStream(file.getInputStream())).header("Access-Control-Allow-Origin", "*").build();
		} else {
			System.out.println("Error file is null: " + fileId);
			return Response.serverError().build();
		}
	}

	
	private  String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	@PermitAll
	@POST
	@Path("rest/file/filename/{filename}/upload")
	@Produces({ "application/json" })
	public Response upload(@PathParam("filename") String filename,
			@MultipartForm FileUploaderMultipartForm form) throws Exception {
		System.out.println("UPLOAD FILE");
		System.out.println("------------");
		System.out.println("Filename: " + filename);
		System.out.println("Size: " + form.getInputStream());
		ObjectId id = MongoIOUtils.save(form.getInputStream(), filename);

		System.out.println("Finished");
		return sendResponse(id.toHexString());
	}

}