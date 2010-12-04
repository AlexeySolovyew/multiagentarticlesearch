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
		ACLMessage msgINIT = agent.receive(MessageTemplate
				.MatchPerformative(UserAgent.INIT));
		try {
			if (msgINIT != null) {
				if (msgINIT.getContent().equals(SearcherAgent.INIT_USER)) {
					agent.setUserAID(msgINIT.getSender());
					System.out.println(agent.getName()
							+ " receive msg: INIT_USER");
				} else {
					throw new InitAgentException();
				}
			}
		} catch (InitAgentException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	/*	ACLMessage msgFinishingINIT = agent.receive(MessageTemplate
				.MatchPerformative(ACLMessage.SUBSCRIBE));
		if (msgFinishingINIT != null) {
			if (msgFinishingINIT.getSender().equals(agent.getUserAgentAID())) {
				agent.setAggregatorAID(msgFinishingINIT.getContent());
				System.out.println(agent.getName() + " receives msg: "
						+ msgFinishingINIT.getContent());
			}
		}*/
		ACLMessage msg = agent.receive(MessageTemplate
				.MatchPerformative(ACLMessage.REQUEST));
		if (msg != null) {
		//	>>>>>>> .r29
			agent.setAggregatorAgentAID(msg.getSender());
			agent.searchAndSendResults(msg);
			System.out.println(agent.getName() + " receives search msg: "
					+ msg.getContent());
/*=======
			agent.setAggregatorAgentAID(msg.getSender());
			agent.sendSearchResult(agent.search(msg));
			System.out.println(agent.getName() + " receives search msg: "
					+ msg.getContent());

//<<<<<<< .mine
			if (msg.getSender().equals(agent.getOrchestratorAgentAID())) { 
				agent.searchAndSendResults(msg);
				System.out.println(agent.getName() + " receives search msg: "
						+ msg.getContent());
			}

 */
		} else {
			this.block();
		}
	}
	
}
