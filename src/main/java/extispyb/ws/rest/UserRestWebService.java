package extispyb.ws.rest;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import extispyb.core.collection.User;

@Path("/")
public class UserRestWebService extends RestWebService {

	@PermitAll
	@GET
	@Path("rest/{token}/user/{login}/authenticate")
	@Produces({ "application/json" })
	public Response authenticate(
			@PathParam("login") String login,
			@PathParam("token") String token) throws Exception {
		
		System.out.println("Authenticate User !!");
		User user = User.getByName(login);
		
		if (user == null){
    		/** Never logged before then creating initial setup**/
    		user = User.init(login, token);
    	}
		
		user.setToken(token);
		user.save();
		return sendResponse(user);
	}
	
	@PermitAll
	@GET
	@Path("rest")
	public void test() throws Exception {
		
		System.out.println("Test User !!");
	}
}
