package searcher.agents.searcher;

import java.security.acl.Acl;
import java.util.List;

import searcher.Article;
import searcher.agents.orchestrator.OrchestratorAgent;
import searcher.agents.user.UserAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public abstract class SearcherAgent extends Agent {

	public static final String INIT_USER = "INIT_USER";
	private AID orchestratorAgentAID;
	private AID aggregatorAgentAID;
	private AID userAgentAID;

	public SearcherAgent() {

	}

	/*
	 * public SearcherAgent(AID AID_courierAgent, UserAgent userAgent) {
	 * this.AID_courierAgent = AID_courierAgent; this.userAgent = userAgent; }
	 */

	@Override
	protected void setup() {
		super.setup();

		// это поле должно отличаться у конкретных сёчеров
		String serviceName = "common search";

		// Register the service
		System.out.println("Agent " + getLocalName()
				+ " registering service \"" + serviceName
				+ "\" of type \"search-articles\"");
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setName(serviceName);
			sd.setType("search-articles");
			// Agents that want to use this service need to "know" the
			// search-articles-ontology
			sd.addOntologies("search-articles-ontology");
			// Agents that want to use this service need to "speak" the FIPA-SL
			// language
			sd.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
			// sd.addProperties(new Property("country", "Italy"));
			dfd.addServices(sd);

			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		addBehaviour(new SearcherCyclicBehaviour(this));
	}

	public abstract List<Article> search(ACLMessage msg);

	public AID getOrchestratorAgentAID() {
		return orchestratorAgentAID;
	}

	public AID getUserAgentAID() {
		return userAgentAID;
	}

	public void setUserAID(AID aid) {
		userAgentAID = aid;
	}

	public void setOrchestratorAID(String nameAID) {
		orchestratorAgentAID = new AID(nameAID, AID.ISLOCALNAME);
	}

	public void setAggregatorAgentAID(AID aid) {
		aggregatorAgentAID = aid;
	}

	public void sendSearchResult(List<Article> search) {
		for (Article page : search) {
			ACLMessage responseMSG = new ACLMessage(ACLMessage.PROPOSE);
			responseMSG.setSender(this.getAID());
			responseMSG.setContent(/* this.getName() + " - " + */page
					.toString());
			// responseMSG.addReceiver(this.getUserAgentAID());
			responseMSG.addReceiver(aggregatorAgentAID);
			this.send(responseMSG);
		}
	}
}
