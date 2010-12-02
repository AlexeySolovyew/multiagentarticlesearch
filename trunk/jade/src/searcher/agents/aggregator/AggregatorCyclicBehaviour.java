package searcher.agents.aggregator;

import java.util.Set;

import searcher.Article;
import searcher.agents.orchestrator.OrchestratorAgent;
import searcher.agents.searcher.SearcherAgent;
import searcher.agents.user.UserAgent;
import searcher.exceptions.InitAgentException;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AggregatorCyclicBehaviour extends CyclicBehaviour {

	private AggregatorAgent agent;

	public AggregatorCyclicBehaviour(AggregatorAgent a) {
		agent = a;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		ACLMessage msgINIT = agent.receive(MessageTemplate
				.MatchPerformative(UserAgent.INIT));
		if (msgINIT != null) {
			if (msgINIT.getContent().equals(AggregatorAgent.INIT_USER)) {
				agent.setUserAgentAID(msgINIT.getSender());
				System.out.println(agent.getName() + " receive msg: INIT_USER");
			} /*
			 * else if (msgINIT.getSender().equals(agent.getUserAgentAID())) {
			 * 
			 * agent.setSearchers(msgINIT.getContent());
			 * System.out.println("AggregatorAgent receives msg2");
			 * 
			 * }
			 */
			/*
			 * else if (msgINIT.getContent().equals(
			 * AggregatorAgent.INCOMING_NOTICE)) {
			 * agent.setOrchestratorAgentAID(msgINIT.getSender());
			 * System.out.println(agent.getName() + " receive msg: " +
			 * AggregatorAgent.INCOMING_NOTICE);
			 * 
			 * } }else { throw new InitAgentException(); }
			 */
		}
		ACLMessage msg = agent.receive(MessageTemplate
				.MatchPerformative(ACLMessage.PROPOSE));
		if (msg != null) {
			if (agent.getSearcherAgentsAID().contains(msg.getSender())) {
				agent.sendArticle(/* msg.getSender()+" - "+ */new Article(msg
						.getContent()));
				System.out.println("AggregatorAgent receives"
						+ msg.getContent());
			}

		}
		ACLMessage msgRequest = agent.receive(MessageTemplate
				.MatchPerformative(ACLMessage.REQUEST));
		if (msgRequest != null) {
			agent.setOrchestratorAgentAID(msgRequest.getSender());
			agent.addMsgToQueueOfSearchersMSGs(msgRequest);
			Set<AID> searcherAIDS = agent.getSearcherAgentsAID();
			if (!searcherAIDS.isEmpty()) {
				for (AID searchAID : searcherAIDS) {
					agent.sendMsgFromQueueToAgent(searchAID);
				}
			}
			System.out.println("AggregatorAgent receives msgSearch = "
					+ msgRequest.getContent());

		}
		if (msgINIT == null && msg == null && msgRequest == null) {
			this.block();
		}
	}
}