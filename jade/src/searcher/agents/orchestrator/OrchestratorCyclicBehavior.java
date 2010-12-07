package searcher.agents.orchestrator;

import java.io.Serializable;

import searcher.Article;
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
				}/*
				 * else if (msgINIT.getSender().equals(agent.getUserAgentAID()))
				 * { agent.setSearchers(msgINIT.getContent());
				 * System.out.println("OrchestratorAgent receives msg2"); }
				 */else {
					throw new InitAgentException();
				}
			}
		} catch (InitAgentException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		ACLMessage msgRequest = agent.receive(MessageTemplate
				.MatchPerformative(ACLMessage.REQUEST));
		if (msgRequest != null) {
			if (msgRequest.getSender().equals(agent.getUserAgentAID())) {
				agent.addMsgToQueueOfAggregatorsMSGs(msgRequest);
				if (agent.getAggregatorAgentAID() != null) {
					agent.sendMsgFromQueueToAggregator();
				}
				System.out.println("OrchestratorAgent receives msgSearch = "
						+ msgRequest.getContent());

			}
		}
		ACLMessage msgPropose = agent.receive(MessageTemplate
				.MatchPerformative(ACLMessage.PROPOSE));
		if (msgPropose != null) {
			if (agent.getAggregatorAgentAID().equals(msgPropose.getSender())) {
				agent.sendArticle(new Article(msgPropose.getContent()));
				System.out.println("OrchestratorAgent receives response message"
						+ msgPropose.getContent());

			}
		}
		if (msgINIT == null && msgRequest == null && msgPropose == null) {
			this.block();
		}
	}

}
