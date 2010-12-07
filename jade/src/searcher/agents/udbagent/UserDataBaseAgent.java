package searcher.agents.udbagent;

import jade.core.AID;
import jade.core.Agent;

public class UserDataBaseAgent extends Agent {
	
	private AID userAgentAID;

	public static final String INIT_USER = "INIT_USER";
	
	public void setUserAID(AID aid) {
		userAgentAID = aid;
	}
	

}
