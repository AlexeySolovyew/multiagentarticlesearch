package searcher.agents.user;

import searcher.agents.courier.CourierAgent;
import searcher.exceptions.InitAgentException;
//import sun.management.resources.agent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class UserCyclicBehaviour extends CyclicBehaviour {

	private UserAgent agent;

	public UserCyclicBehaviour(UserAgent userAgent) {
		this.agent = userAgent;
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive(MessageTemplate
				.MatchPerformative(ACLMessage.INFORM));
		if (msg != null) {

			if (msg.getSender().equals(agent.getAID())) {
				agent.send(msg);
			}
			if (msg.getSender().equals(agent.getResultAgent1AID())
					|| msg.getSender().equals(agent.getResultAgent2AID())) {
				agent.showMSG(msg);
			}

		} else {
			// agent.blockingReceive();
			this.block();
		}

	}

	private void sendMSG(String content, AID reciverAID) {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setSender(agent.getAID());
		msg.setContent(content);
		msg.addReceiver(reciverAID);
		agent.send(msg);
	}

}
