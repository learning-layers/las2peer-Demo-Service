package i5.las2peer.services.demoService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import i5.las2peer.p2p.LocalNode;
import i5.las2peer.p2p.ServiceNameVersion;
import i5.las2peer.security.ServiceAgent;
import i5.las2peer.security.UserAgent;
import i5.las2peer.testing.MockAgentFactory;
import i5.las2peer.webConnector.WebConnector;
import i5.las2peer.webConnector.client.ClientResponse;
import i5.las2peer.webConnector.client.MiniClient;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DemoServiceTest {

	private static final String HTTP_ADDRESS = "http://127.0.0.1";
	private static final int HTTP_PORT = WebConnector.DEFAULT_HTTP_PORT;

	private static LocalNode node;
	private static WebConnector connector;
	private static ByteArrayOutputStream logStream;

	private static UserAgent testAgent;
	private static final String testPass = "adamspass";

	private static final String mainPath = "demo/";

	@BeforeClass
	public static void startServer() throws Exception {

		// start node
		node = LocalNode.newNode();
		testAgent = MockAgentFactory.getAdam();
		testAgent.unlockPrivateKey(testPass); // agent must be unlocked in order to be stored
		node.storeAgent(testAgent);
		node.launch();

		// during testing, the specified service version does not matter
		ServiceAgent testService = ServiceAgent.createServiceAgent(
				ServiceNameVersion.fromString(DemoService.class.getName() + "@1.0"), "a pass");
		testService.unlockPrivateKey("a pass");
		node.registerReceiver(testService);

		// start remote service
		/*
		ServiceAgent testService2 = ServiceAgent.createServiceAgent(
				ServiceNameVersion.fromString("i5.las2peer.services.remoteService.RemoteService@1.0"), "a pass");
		testService2.unlockPrivateKey("a pass");
		node.registerReceiver(testService2);
		*/

		// start connector
		logStream = new ByteArrayOutputStream();

		connector = new WebConnector(true, HTTP_PORT, false, 1000);
		connector.setLogStream(new PrintStream(logStream));
		connector.start(node);
		Thread.sleep(1000); // wait a second for the connector to become ready
		testAgent = MockAgentFactory.getAdam(); // get a locked agent

	}

	@AfterClass
	public static void shutDownServer() throws Exception {

		connector.stop();
		node.shutDown();

		connector = null;
		node = null;

		LocalNode.reset();

		System.out.println("Connector-Log:");
		System.out.println("--------------");

		System.out.println(logStream.toString());

	}

	@Test
	public void testSayHello() {
		MiniClient c = new MiniClient();
		c.setAddressPort(HTTP_ADDRESS, HTTP_PORT);
		c.setLogin(Long.toString(testAgent.getId()), testPass);

		ClientResponse result = c.sendRequest("GET", mainPath + "local", "");
		assertEquals(200, result.getHttpCode());
		assertTrue(result.getResponse().trim().contains("locally"));
	}

	// @Test
	public void testCallRemote() {
		MiniClient c = new MiniClient();
		c.setAddressPort(HTTP_ADDRESS, HTTP_PORT);
		c.setLogin(Long.toString(testAgent.getId()), testPass);

		ClientResponse result = c.sendRequest("GET", mainPath + "remote", "");
		assertEquals(200, result.getHttpCode());
		assertTrue(result.getResponse().trim().contains("Aachen"));
	}

}
