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
	}

	@Override
	public void action() {
		ACLMessage msgRequest = agent.receive(MessageTemplate
				.MatchPerformative(ACLMessage.REQUEST));
		if (msgRequest != null) {
			agent.setOrchestratorAgentAID(msgRequest.getSender());
			agent.cleanDerivedArticles();
			//agent.cleanSearchersAID();
			agent.addMsgToQueueOfSearchersMSGs(msgRequest);
			agent.findAndLoadSearchers();
			System.out.println("AggregatorAgent receives msgSearch = "
					+ msgRequest.getContent());

		}

		ACLMessage msgPropose = agent.receive(MessageTemplate
				.MatchPerformative(ACLMessage.PROPOSE));
		if (msgPropose != null) {
			if (agent.getSearcherAgentsAID().contains(msgPropose.getSender())) {
				Article cur = new Article(msgPropose.getContent());
				agent.sendArticle(cur);
			}
			System.out.println("AggregatorAgent receives response message"
					+ msgPropose.getContent());

		}
		if (msgRequest == null && msgPropose == null) {
			this.block();
		}
	}
}