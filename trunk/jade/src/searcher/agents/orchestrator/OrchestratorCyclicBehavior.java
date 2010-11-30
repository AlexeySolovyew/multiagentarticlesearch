package searcher.agents.orchestrator;

import java.io.Serializable;

import searcher.agents.user.UserAgent;
import searcher.exceptions.InitAgentException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class OrchestratorCyclicBehavior extends CyclicBehaviour {

	private OrchestratorAgent agent;

	public OrchestratorCyclicBehavior(OrchestratorAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {
		ACLMessage msgINIT = agent.receive(MessageTemplate
				.MatchPerformative(UserAgent.INIT));
		try {

			if (msgINIT != null) {
				if (msgINIT.getContent().equals(OrchestratorAgent.INIT_USER)) {
					agent.setUserAID(msgINIT.getSender());
					System.out.println("OrchestratorAgent receives msg1");
				} else if (msgINIT.getSender().equals(agent.getUserAgentAID())) {
					agent.setSearchers(msgINIT.getContent());
					System.out.println("OrchestratorAgent receives msg2");
				} else {
					throw new InitAgentException();
				}
			}
		} catch (InitAgentException e) {
			// TODO: handle exception
			e.printStackTrace();
		} 
		ACLMessage msgInform = agent.receive(MessageTemplate
				.MatchPerformative(ACLMessage.INFORM));
		if (msgInform != null) {
			if (msgInform.getSender().equals(agent.getUserAgentAID())) {
				agent.distributeMSG(msgInform);
				System.out.println("OrchestratorAgent receives msgSearch = " + msgInform.getContent());

			}
		}
		if (msgINIT == null && msgInform == null) {
			this.block();
		}
	}

}
