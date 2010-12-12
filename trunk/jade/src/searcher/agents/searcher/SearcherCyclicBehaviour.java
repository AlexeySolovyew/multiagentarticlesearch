package searcher.agents.searcher;

import searcher.agents.orchestrator.OrchestratorAgent;
import searcher.agents.user.UserAgent;
import searcher.exceptions.InitAgentException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class SearcherCyclicBehaviour extends CyclicBehaviour {

	private SearcherAgent agent;

	public SearcherCyclicBehaviour(SearcherAgent a) {
		agent = a;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive(MessageTemplate
				.MatchPerformative(ACLMessage.REQUEST));
		if (msg != null) {
			agent.setAggregatorAgentAID(msg.getSender());
			agent.searchAndSendResults(msg);
			System.out.println(agent.getName() + " receives search msg: "
					+ msg.getContent());
		} else {
			this.block();
		}
	}

}
