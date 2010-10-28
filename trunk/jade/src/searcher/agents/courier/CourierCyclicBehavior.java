package searcher.agents.courier;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class CourierCyclicBehavior extends CyclicBehaviour {

	private CourierAgent agent;

	public CourierCyclicBehavior(CourierAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		if(msg != null){
			if(msg.getSender().equals(agent.getUserAgentAID())){
				agent.distributeMSG(msg);
			}
		}else{
			this.block();
		}
	}

}
