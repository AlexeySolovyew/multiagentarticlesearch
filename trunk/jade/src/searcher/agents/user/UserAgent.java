package searcher.agents.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import searcher.Article;
import searcher.agents.aggregator.AggregatorAgent;
import searcher.agents.orchestrator.OrchestratorAgent;
import searcher.agents.searcher.SearcherAgent;
import searcher.agents.searcher.DummySearcherAgent;
import searcher.agents.udbagent.UserDataBaseAgent;
import searcher.exceptions.InitAgentException;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.FIPAAgentManagement.FIPAManagementVocabulary;
import jade.lang.acl.ACLMessage;
import jade.tools.testagent.ReceiveCyclicBehaviour;
import jade.tools.testagent.TestAgentFrame;
import jade.util.Logger;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;
import jade.wrapper.StaleProxyException;

public class UserAgent extends Agent {

	private List<Article> resultPages;

	public static final int INIT = ACLMessage.PROXY;

	private static final String ORCHESTRATOR_AGENT_NAME = "orchestratorAgent";

	private static final String User_DATABASE_AGENT_NAME = "userDataBaseAgent";
	private static String ADDRESS_NAME = "useragent";
	private AID orchestratorAgentAID;
	private AID userDataBaseAgentAID;

	private void initRelatedAgents() {
		PlatformController container = getContainerController();
		try {
			// searchers = new HashSet<AID>();
			createOrchestratorAgent(container);
			createUserDataBaseAgent(container);
			// createDummySA1(container);
			// createDummySA2(container);
			// createGoogleSA(container);
			sendInitMSGs();
		} catch (Exception e) {
			System.err.println("Exception while adding agents: " + e);
			e.printStackTrace();
		}
	}

	private void createUserDataBaseAgent(PlatformController container)
			throws ControllerException {
		AgentController cour = container.createNewAgent(
				this.User_DATABASE_AGENT_NAME,
				"searcher.agents.udbagent.UserDataBaseAgent", null);
		cour.start();
		userDataBaseAgentAID = new AID(this.User_DATABASE_AGENT_NAME,
				AID.ISLOCALNAME);

	}

	private void sendInitMSGs() {
		sendInitMSG(OrchestratorAgent.INIT_USER, this.getOrchestratorAID());
		sendInitMSG(UserDataBaseAgent.INIT_USER, this.getUserDataBaseAID());
		sendInitMSG(this.ORCHESTRATOR_AGENT_NAME, this.getUserDataBaseAID());
		sendInitMSG(this.User_DATABASE_AGENT_NAME, this.getOrchestratorAID());
	}

	private void sendInitMSG(String content, AID recieverAID) {
		sendMSG(INIT, content, recieverAID);
	}

	private void sendMSG(int performative, String content, AID recieverAID) {
		ACLMessage msg = new ACLMessage(performative);
		msg.setSender(getAID());
		msg.setContent(content);
		msg.addReceiver(recieverAID);
		send(msg);
	}

	private void createOrchestratorAgent(PlatformController container)
			throws ControllerException, StaleProxyException {

		AgentController cour = container.createNewAgent(
				this.ORCHESTRATOR_AGENT_NAME,
				"searcher.agents.orchestrator.OrchestratorAgent", null);
		cour.start();
		orchestratorAgentAID = new AID(this.ORCHESTRATOR_AGENT_NAME,
				AID.ISLOCALNAME);
	}

	protected void setup() {
		super.setup();
		resultPages = new ArrayList<Article>();
		initRelatedAgents();
		addBehaviour(new UserCyclicBehaviour(this));
		addBehaviour(new RefreshFrameBehaviour(new UserAgentFrame(this)));
	}

	public AID getOrchestratorAID() {
		return orchestratorAgentAID;
	}
	
	public AID getUserDataBaseAID() {
		return userDataBaseAgentAID;
	}

	/**
	 * 
	 * Get address name. This name must using for create UserAgent,because
	 * addressName used for sending ACLMessag this Agent
	 * 
	 * @return addressName
	 */
	public static String getAddressName() {
		return ADDRESS_NAME;
	}

	public void addPageToList(Article a) {
		
		if (resultPages != null) {
			int size=resultPages.size();
			if (size == 0){
				resultPages.add(a);
			}
			else {
				int place = a.getRank();
				int i = 0;
				for (; i<size && resultPages.get(i).getRank() <= place; i++)
					;
				resultPages.add(i, a);
			}
		}
	}

	public List<Article> getPages() {
		return resultPages;
	}

	public void clearPages() {
		resultPages.clear();
	}
	
	public Article findByURL(String url){
		for (Article a:resultPages){
			if (a.getURL().equals(url))
				return a;
		}
		return null;
		
	}

}
