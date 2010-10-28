package searcher.agents.searcher;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class SearcherCyclicBehaviour extends CyclicBehaviour {

	private SearcherAgent agent;

	public SearcherCyclicBehaviour(SearcherAgent a) {
		agent = a;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		if(msg != null){
			if(msg.getSender().equals(agent.getCourierAgentAID())){
				agent.search(msg);
			}
		}else{
			this.block();
		}
		
	}



}
