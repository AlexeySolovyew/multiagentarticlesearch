package searcher.agents.searcher;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.List;

import com.sun.xml.internal.bind.v2.runtime.Name;

import searcher.agents.courier.CourierAgent;
import searcher.agents.user.UserAgent;

public class DummySearcherAgent extends SearcherAgent {

	private List<String> pages;

	public DummySearcherAgent(AID AID_courierAgent, UserAgent userAgent, List<String> pages) {
		super(AID_courierAgent, userAgent);
		this.pages = pages;
	}
	
	@Override
	protected void setup() {
		super.setup();
	}

	@Override
	public void search(ACLMessage msg) {
		for (String pageName : pages) {
			ACLMessage responseMSG = new ACLMessage(msg.getPerformative());
			responseMSG.setSender(this.getAID());
			responseMSG.setContent(pageName);
			responseMSG.addReceiver(this.getUserAgentAID());
			responseMSG.setLanguage("Plain English");
			this.send(responseMSG);
		}
	}
	
	
	

}
