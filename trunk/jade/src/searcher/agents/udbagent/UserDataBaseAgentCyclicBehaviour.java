package searcher.agents.udbagent;

import java.io.IOException;

import org.w3c.dom.DOMException;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import searcher.Article;
import searcher.agents.orchestrator.OrchestratorAgent;
import searcher.agents.user.UserAgent;
import searcher.exceptions.InitAgentException;

public class UserDataBaseAgentCyclicBehaviour extends CyclicBehaviour {

	UserDataBaseAgent agent;

	public UserDataBaseAgentCyclicBehaviour(UserDataBaseAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {

		ACLMessage msgINIT = agent.receive(MessageTemplate
				.MatchPerformative(UserAgent.INIT));
		try {

			if (msgINIT != null) {
				if (msgINIT.getContent().equals(OrchestratorAgent.INIT_USER)) {
					agent.setUserAID(msgINIT.getSender());
					System.out.println("UserDataBaseAgent receives INIT_USER");
				} else if (msgINIT.getSender().equals(agent.getUserAgentAID())) {
					agent.setOrchestratorAID(new AID(msgINIT.getContent(),
							AID.ISLOCALNAME));
					System.out
							.println("UserDataBaseAgent receives orchestrator AID from UserAgent");
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
		
		if (msgInform != null)
			if (msgInform.getSender().equals(agent.getUserAgentAID())){
				try {
					agent.addRatings(new Article(msgInform.getContent()));
				} catch (DOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		ACLMessage msgRequest = agent.receive(MessageTemplate
				.MatchPerformative(ACLMessage.REQUEST));
		if (msgRequest != null) {
			if (agent.getOrchestratorAID().equals(msgRequest.getSender())) {
				ACLMessage responseMSG = new ACLMessage(ACLMessage.INFORM);
				responseMSG.setSender(agent.getAID());
				responseMSG.setContent(msgRequest.getContent());
				responseMSG.addReceiver(agent.getOrchestratorAID());
				agent.send(responseMSG);
				System.out
						.println("UserDataBaseAgent receives request message"
								+ msgRequest.getContent());

			}
		}

	}

}
