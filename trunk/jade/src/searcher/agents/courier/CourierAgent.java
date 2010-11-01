package searcher.agents.courier;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import examples.party.HostAgent;

import searcher.agents.searcher.SearcherAgent;
import searcher.agents.user.UserAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class CourierAgent extends Agent {

	public static final String INIT_USER = "INIT_USER";
	private AID userAgentAID;
	private Set<AID> seachersAID = new HashSet<AID>();

	public CourierAgent() {

	}

	/*
	 * public CourierAgent(UserAgent userAgent, Set<AID> AID_seachers) {
	 * this.userAgent = userAgent; this.AID_seachers = AID_seachers; }
	 */

	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new CourierCyclicBehavior(this));
	}

	public AID getUserAgentAID() {
		return userAgentAID;
	}

	public void distributeMSG(ACLMessage msg) {
		ACLMessage newMSG = new ACLMessage(msg.getPerformative());
		newMSG.setSender(this.getAID());
		newMSG.setContent(msg.getContent());
		newMSG.setLanguage("Plain English");
		for (AID AID_searcherAgent : seachersAID) {
			newMSG.addReceiver(AID_searcherAgent);
		}
		this.send(newMSG);
	}

	public void setUserAID(AID aid) {
		userAgentAID = aid;
	}

	public void setSearchers(String content) {
		for (StringTokenizer tokenazer = new StringTokenizer(content); tokenazer.hasMoreTokens();) {
			String searcherName = tokenazer.nextToken();
			seachersAID.add(new AID(searcherName, AID.ISLOCALNAME));			
		}
		
	}
}
