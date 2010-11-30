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

	public static final int INIT = ACLMessage.REQUEST;


	private static final String SEARCHER_AGENT_NAME = "searcherAgent";


	private static final String ORCHESTRATOR_AGENT_NAME = "orchestratorAgent";
	
	private static final String AGGREGATOR_AGENT_NAME = "aggregatorAgent";


	private static final int DummySearchAngen1 = 1;


	private static final int DummySearchAngen2 = 2;


	private static final int GOOGLE_SearchAgent = 3;
	
	
	private static String ADDRESS_NAME = "useragent";
	private UserAgentFrame itsFrame;
	private AID orchestratorAgentAID;
	private AID aggregatorAgentAID;
	private Set<AID> searchers;
	private AID AID_searcherAgent1;
	private AID AID_searcherAgent2;

//	public UserAgent() {
// }

	private void initRelatedAgents() {
		PlatformController container = getContainerController();
		try {
//			searchers = new HashSet<AID>();
			createOrchestratorAgent(container);
//			createAggregatorAgent(container);
//			createDummySA1(container);
//			createDummySA2(container);
//			createGoogleSA(container);
			sendInitMSGs();
		} catch (Exception e) {
			System.err.println("Exception while adding agents: " + e);
			e.printStackTrace();
		}
	}


/*	private void createGoogleSA(PlatformController container) {
		String searchAgent = this.SEARCHER_AGENT_NAME + GOOGLE_SearchAgent;
		AgentController search;
		try {
			search = container.createNewAgent(searchAgent,
					"searcher.agents.searcher.GoogleSearcherAgent", null);
			search.start();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AID AID_searcherAgent = new AID(searchAgent, AID.ISLOCALNAME);
		searchers.add(AID_searcherAgent);
		
	}
	*/

	private void sendInitMSGs() {
		sendInitMSG(OrchestratorAgent.INIT_USER, this.getOrchestratorAID());
//		sendInitMSG(this.getSearchAgentsName(), this.getOrchestratorAID());
//		sendInitMSG(AggregatorAgent.INIT_USER, this.getAggregatorAID());
//		sendInitMSG(this.getSearchAgentsName(), this.getAggregatorAID());
		
/*		for (AID searchAID : searchers) {
			sendInitMSG(CourierAgent.INIT_USER, searchAID);
			sendInitMSG(this.COURIER_AGENT_NAME, searchAID);
			sendFinishingInitMSG(this.AGGREGATOR_AGENT_NAME,searchAID);
		}
		*/


	}
	
	private void sendInitMSG(String content, AID recieverAID) {
		sendMSG(INIT, content, recieverAID);
	}
	
	private void sendFinishingInitMSG(String content, AID recieverAID) {
		sendMSG(ACLMessage.SUBSCRIBE, content, recieverAID);
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
		//CourierAgent test_courierAgent = new CourierAgent();
		// Object[] argsCour = { this, searchers };
		AgentController cour = container.createNewAgent(this.ORCHESTRATOR_AGENT_NAME,
				"searcher.agents.orchestrator.OrchestratorAgent", null);
		cour.start();
		orchestratorAgentAID = new AID(this.ORCHESTRATOR_AGENT_NAME, AID.ISLOCALNAME);
	}

/*	private void createAggregatorAgent(PlatformController container) throws ControllerException, StaleProxyException {
		AgentController cour = container.createNewAgent(this.AGGREGATOR_AGENT_NAME,
				"searcher.agents.aggregator.AggregatorAgent", null);
		cour.start();
		aggregatorAgentAID = new AID(this.AGGREGATOR_AGENT_NAME, AID.ISLOCALNAME);
	}
	*/
/*	private void createDummySA1(PlatformController container) {
		createDummySA(this.DummySearchAngen1, container);
		
	}
	private void createDummySA2(PlatformController container) {
		createDummySA(this.DummySearchAngen2, container);
		
	}
	private void createDummySA(int numberSA,PlatformController container) {
		//SearcherAgent test_searcherAgent = new DummySearcherAgent();
		// Object[] argsSearch = {AID_courierAgent,this, pages};
		String searchAgent = this.SEARCHER_AGENT_NAME + numberSA;
		AgentController search;
		try {
			search = container.createNewAgent(searchAgent,
					"searcher.agents.searcher.DummySearcherAgent"+numberSA, null);
			search.start();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AID AID_searcherAgent = new AID(searchAgent, AID.ISLOCALNAME);
		searchers.add(AID_searcherAgent);
	}
*/
	protected void setup() {
		super.setup();
		itsFrame = new UserAgentFrame(this);
		initRelatedAgents();
		addBehaviour(new UserCyclicBehaviour(this));
	}

	public AID getOrchestratorAID() {
		return orchestratorAgentAID;
	}

/*	public AID getAggregatorAID() {
		return aggregatorAgentAID;
	}
	
	public Object getResultAgent1AID() {
		return searchers.iterator().next();
	}

	public Object getResultAgent2AID() {
		Iterator<AID> iterator = searchers.iterator();
		iterator.next();
		return iterator.next();
	}
*/
	public void addPageToFrame(Article p) {
		itsFrame.addArticleToFrame(p);
	}

	/**
	 * 
	 * Get address name. This name must using for create UserAgent,because
	 * addressName used for sending ACLMessag this Agent
	 * 
	 * @return addressName
	 */
	public static String getAddressName() {
		return ADDRESS_NAME ;
	}

/*	public String getSearchAgentsName() {
		String searchNames = "";
		for (int numberAgent = 1; numberAgent<=searchers.size();numberAgent++) {
			searchNames += this.SEARCHER_AGENT_NAME+numberAgent +" ";
		}
		return searchNames;
	}
	*/

/*	public boolean thinkThatResultAgentIs(AID sender) {
		return searchers.contains(sender);
	}
	*/

	/*
	 * public void processIncomingMessage(ACLMessage msg) {
	 * itsFrame.addMessageNode("in", msg); if (pingBehaviour) { if
	 * (msg.getContent() == null) { return; }
	 * 
	 * if ((msg.getPerformative() == ACLMessage.QUERY_REF) &&
	 * (msg.getContent().equalsIgnoreCase("ping") ||
	 * (msg.getContent().equalsIgnoreCase("(ping)")) )) { ACLMessage alive =
	 * msg.createReply(); alive.setPerformative(ACLMessage.INFORM);
	 * alive.setSender(this.getAID()); alive.setContent("alive");
	 * this.send(alive); itsFrame.addMessageNode("out", alive); } } }
	 */

}
