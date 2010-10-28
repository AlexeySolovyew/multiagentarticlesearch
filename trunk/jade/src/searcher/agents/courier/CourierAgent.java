package searcher.agents.courier;

import java.util.Set;

import searcher.agents.searcher.SearcherAgent;
import searcher.agents.user.UserAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class CourierAgent extends Agent {

	private UserAgent userAgent;
	private Set<AID> AID_seachers;

	public CourierAgent(UserAgent userAgent, Set<AID> AID_seachers) {
		this.userAgent = userAgent;
		this.AID_seachers = AID_seachers;
	}
		
	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new CourierCyclicBehavior(this));
	}

	public AID getUserAgentAID() {
		return userAgent.getAID();
	}

	public void distributeMSG(ACLMessage msg) {
		ACLMessage newMSG = new ACLMessage(msg.getPerformative());
		newMSG.setSender(this.getAID());
		newMSG.setContent(msg.getContent());
		newMSG.setLanguage("Plain English");
		for (AID AID_searcherAgent : AID_seachers) {
			newMSG.addReceiver(AID_searcherAgent);
		}
		this.send(newMSG);
	}
}
