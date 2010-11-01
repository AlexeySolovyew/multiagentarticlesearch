package searcher.agents.searcher;

import java.security.acl.Acl;
import java.util.List;

import searcher.agents.courier.CourierAgent;
import searcher.agents.user.UserAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public abstract class SearcherAgent extends Agent {

	public static final String INIT_USER = "INIT_USER";
	private AID courierAgentAID;
	private AID userAgentAID;
	
	public SearcherAgent(){
		
	}

	/*public SearcherAgent(AID AID_courierAgent, UserAgent userAgent) {
		this.AID_courierAgent = AID_courierAgent;
		this.userAgent = userAgent;
	}*/

	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new SearcherCyclicBehaviour(this));
	}

	public abstract List<String> search(ACLMessage msg);

	public AID getCourierAgentAID() {
		return courierAgentAID;
	}
	
	public AID getUserAgentAID(){
		return userAgentAID;
	}

	public void setUserAID(AID aid) {
		userAgentAID = aid;
	}

	public void setCourierAID(String nameAID) {
		courierAgentAID = new AID(nameAID, AID.ISLOCALNAME);
	}

	public void sendSearchResult(List<String> search) {
		for (String pageName : search) {
			ACLMessage responseMSG = new ACLMessage(ACLMessage.INFORM);
			responseMSG.setSender(this.getAID());
			responseMSG.setContent(pageName);
			responseMSG.addReceiver(this.getUserAgentAID());
			this.send(responseMSG);			
		}
		
	}
}
