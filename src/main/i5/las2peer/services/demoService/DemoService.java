package i5.las2peer.services.demoService;

import i5.las2peer.api.Context;
import i5.las2peer.api.exceptions.RemoteServiceException;
import i5.las2peer.api.exceptions.ServiceNotAvailableException;
import i5.las2peer.api.exceptions.ServiceNotFoundException;
import i5.las2peer.restMapper.RESTService;
import i5.las2peer.restMapper.annotations.ServicePath;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ServicePath("demo")
public class DemoService extends RESTService {

	@Override
	protected void initResources() {
		getResourceConfig().register(RootResource.class);
	}

	public DemoService() {
	}

	@Path("/")
	public static class RootResource {

		@GET
		@Path("remote")
		@Produces(MediaType.TEXT_PLAIN)
		public Response callRemote() {

			try {
				String response;

				response = (String) Context.getCurrent().invoke("i5.las2peer.services.remoteService.RemoteService",
						"sayHello");

				String returnString = "Response from remote service: " + response;
				return Response.ok().entity(returnString).build();
			} catch (ServiceNotFoundException | ServiceNotAvailableException | RemoteServiceException e) {
				String returnString = "Could not locate service in federation!";
				return Response.ok().entity(returnString).build();
			}

		}

	}

}