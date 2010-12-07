package searcher.agents.user;

import searcher.Article;
import searcher.agents.orchestrator.OrchestratorAgent;
import searcher.exceptions.InitAgentException;
//import sun.management.resources.agent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class UserCyclicBehaviour extends CyclicBehaviour {

	private UserAgent agent;
	

	public UserCyclicBehaviour(UserAgent userAgent) {
		this.agent = userAgent;
	}

	@Override
	public void action() {
		
		ACLMessage msg = agent.receive(MessageTemplate
				.MatchPerformative(ACLMessage.PROPOSE));
		if (msg != null) {

			if (agent.getOrchestratorAID().equals(msg.getSender())) {
				agent.addPageToList(/*msg.getSender()+" - "+*/new Article(msg.getContent()));
			}
			
		}

			else {
			// agent.blockingReceive();
			this.block();
		}

	}

}
