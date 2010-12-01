package searcher.agents.aggregator;

import searcher.Article;
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
		try {
			if (msgINIT != null) {
				if (msgINIT.getContent().equals(AggregatorAgent.INIT_USER)) {
					agent.setUserAID(msgINIT.getSender());
					System.out.println(agent.getName()
							+ " receive msg: INIT_USER");
				} else if (msgINIT.getSender().equals(agent.getUserAgentAID())) {
					/*
					 * agent.setSearchers(msgINIT.getContent());
					 * System.out.println("AggregatorAgent receives msg2");
					 */
				} else {
					throw new InitAgentException();
				}
			}
		} catch (InitAgentException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		ACLMessage msg = agent.receive(MessageTemplate
				.MatchPerformative(ACLMessage.INFORM));
		if (msg != null) {
			if (agent.getSearchersAID().contains(msg.getSender())) {
				agent.sendArticle(/* msg.getSender()+" - "+ */new Article(msg
						.getContent()));
				System.out.println("AggregatorAgent receives"
						+ msg.getContent());
			}

		} else {
			// agent.blockingReceive();
			this.block();
		}
	}

}
