package searcher.agents.strategy;

import searcher.agents.courier.CourierAgent;
import searcher.agents.searcher.SearcherAgent;
import searcher.agents.user.UserAgent;
import searcher.exceptions.InitAgentException;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class StrategyCyclicBehavior extends Behaviour {

	private StrategyAgent agent;

	public StrategyCyclicBehavior(StrategyAgent s) {
		this.agent = s;
	}

	@Override
	public void action() {
		ACLMessage msgINIT = agent.receive(MessageTemplate
				.MatchPerformative(UserAgent.INIT));
		
		try {

			if (msgINIT != null) {
				if (msgINIT.getContent().equals(StrategyAgent.INIT_USER)) {
					agent.setAgregatorAID(msgINIT.getSender());
					System.out.println("CouierAgent receive msg1");
				} else if (msgINIT.getSender().equals(agent.getUserAgentAID())) {
					agent.setSearchers(msgINIT.getContent());
					System.out.println("CouierAgent receive msg2");
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
				System.out.println("CouierAgent receive msgSearch = " + msgInform.getContent());

			}
		}
		if (msgINIT == null && msgInform == null) {
			this.block();
		}
	}

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
