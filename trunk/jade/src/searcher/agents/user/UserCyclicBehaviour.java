package searcher.agents.user;

import sun.management.resources.agent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class UserCyclicBehaviour extends CyclicBehaviour {

	private UserAgent agent;

	public UserCyclicBehaviour(UserAgent userAgent) {
		this.agent = userAgent; 
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		if(msg != null){
			if(msg.getSender().equals(agent.getAID())){
				agent.send(msg);				
			}
			if(msg.getSender().equals(agent.getResultAgent1AID())||
					msg.getSender().equals(agent.getResultAgent2AID())){
				agent.showMSG(msg);
			}
		}else{
			//agent.blockingReceive();
			this.block();
			
			
		}
	}

}
