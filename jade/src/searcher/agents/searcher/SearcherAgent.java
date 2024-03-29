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

	public static final int MAX_RANK_ARTICLE = 1000;

	public static final int MAX_AMOUNT_OF_RESULTS_ON_ONE_REQUEST = 20;

	private AID aggregatorAgentAID;
	protected String sourseValue;

	public SearcherAgent() {

	}

	/*
	 * public SearcherAgent(AID AID_courierAgent, UserAgent userAgent) {
	 * this.AID_courierAgent = AID_courierAgent; this.userAgent = userAgent; }
	 */

	@Override
	protected void setup() {
		super.setup();

		setSourceValue();

		provideService();

		addBehaviour(new SearcherCyclicBehaviour(this));
	}

	private void provideService() {
		String serviceName = "search";

		// Register the service
		System.out.println("Agent " + getLocalName()
				+ " registering service \"" + serviceName
				+ "\" of type \"search-articles\"" + "\" with property value "
				+ sourseValue);
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setName(serviceName);
			sd.setType("search-articles");
			// Agents that want to use this service need to "know" the
			// search-articles-ontology
			// sd.addOntologies("search-articles-ontology");
			// Agents that want to use this service need to "speak" the FIPA-SL
			// language
			sd.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
			sd.addProperties(new Property("source", sourseValue));
			dfd.addServices(sd);

			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	public abstract void setSourceValue();

	/**
	 * Search results by request(msg.getContent()) and send msgs, which contain
	 * Articles. For each article need to stay rank
	 * (Article.addSearcherSenderAndRank(String searcherName, int rank))
	 * 
	 * @param msg
	 */
	public abstract void searchAndSendResults(ACLMessage msg);

	public void setAggregatorAgentAID(AID aid) {
		aggregatorAgentAID = aid;
	}
	
	/**
	 * 
	 * @param numberArticle - number from 0 to AMOUNT_OF_RESULTS-1
	 * @return
	 */
	public int getCurRankArticle(int numberArticle) {
		return MAX_RANK_ARTICLE - numberArticle*MAX_RANK_ARTICLE/MAX_AMOUNT_OF_RESULTS_ON_ONE_REQUEST;
	}

	public void sendArticle(Article article) {
		ACLMessage responseMSG = new ACLMessage(ACLMessage.PROPOSE);
		responseMSG.setSender(this.getAID());
		responseMSG.setContent(article.toString());
		// responseMSG.addReceiver(this.getUserAgentAID());
		responseMSG.addReceiver(aggregatorAgentAID);
		this.send(responseMSG);
		/*
		 * ======= public void sendSearchResult(List<Article> search) { for
		 * (Article page : search) { ACLMessage responseMSG = new
		 * ACLMessage(ACLMessage.PROPOSE); responseMSG.setSender(this.getAID());
		 * responseMSG.setContent(page .toString()); //
		 * responseMSG.addReceiver(this.getUserAgentAID());
		 * responseMSG.addReceiver(aggregatorAgentAID); this.send(responseMSG);
		 * } >>>>>>> .r29
		 */
	}
}
