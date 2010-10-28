package searcher.agents.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import searcher.agents.courier.CourierAgent;
import searcher.agents.searcher.SearcherAgent;
import searcher.agents.searcher.DummySearcherAgent;
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

	private UserAgentFrame itsFrame;
	private boolean pingBehaviour = true;
	private AID AID_courierAgent;
	private Set<AID> searchers;
	private AID AID_searcherAgent1;
	private AID AID_searcherAgent2;

	public UserAgent() {
	
	}

	private void initRelatedAgents() {
		PlatformController container = getContainerController();
		try {
			searchers = new HashSet<AID>();
			List<String> pages1 = new ArrayList<String>();
			pages1.add("http://www.google.ru");
			pages1.add("http://www.nude-dreams.com");
			pages1.add("http://www.vporyade.ru");

			List<String> pages2 = new ArrayList<String>();
			pages1.add("page1");
			pages1.add("page2");
			pages1.add("page3");

			Object[] argsCour = { this, searchers };
			AgentController cour = container.createNewAgent("courierAgent",
					"searcher.agents.courier.CourierAgent", argsCour);
			cour.start();
			
			createSA(container, pages1);
			createSA(container, pages2);			
			searchers.add(AID_searcherAgent1);
			searchers.add(AID_searcherAgent2);
			
		} catch (Exception e) {
			System.err.println("Exception while adding agents: " + e);
			e.printStackTrace();
		}
	}

	private void createSA(PlatformController container, List<String> pages)
			throws ControllerException, StaleProxyException {
		Object[] argsSearch1 = {AID_courierAgent,this, pages};
		AgentController search1 = container.createNewAgent("searcherAgent1",
				"searcher.agents.searcher.DummySearcherAgent", argsSearch1 );
		search1.start();
		AID_searcherAgent1 = new AID("searcherAgent1", AID.ISLOCALNAME);
	}

	protected void setup() {
		super.setup();
		itsFrame = new UserAgentFrame(this);
		initRelatedAgents();
		addBehaviour(new UserCyclicBehaviour(this));
	}

	public AID getCourierAID() {
		return AID_courierAgent;
	}

	public Object getResultAgent1AID() {
		return searchers.iterator().next();
	}

	public Object getResultAgent2AID() {
		Iterator<AID> iterator = searchers.iterator();
		iterator.next();
		return iterator.next();
	}

	public void showMSG(ACLMessage msg) {
		itsFrame.writeMSG(msg);

	}

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
