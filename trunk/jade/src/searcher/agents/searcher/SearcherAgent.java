package searcher.agents.searcher;

import java.security.acl.Acl;

import searcher.agents.courier.CourierAgent;
import searcher.agents.user.UserAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public abstract class SearcherAgent extends Agent {

	private AID AID_courierAgent;
	private UserAgent userAgent;

	public SearcherAgent(AID AID_courierAgent, UserAgent userAgent) {
		this.AID_courierAgent = AID_courierAgent;
		this.userAgent = userAgent;
	}

	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new SearcherCyclicBehaviour(this));
	}

	public abstract void search(ACLMessage msg);

	public AID getCourierAgentAID() {
		return AID_courierAgent;
	}
	
	public AID getUserAgentAID(){
		return userAgent.getAID();
	}
}
