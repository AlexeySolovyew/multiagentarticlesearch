package searcher.agents.aggregator;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import searcher.Article;
import searcher.agents.aggregator.AggregatorCyclicBehaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class AggregatorAgent extends Agent {
	public static final String INIT_USER = "INIT_USER";
	private AID userAgentAID;
	private Set<AID> searchersAID = new HashSet<AID>();
	private Set<String> pages = new HashSet<String>();

	protected void setup() {
		super.setup();
		addBehaviour(new AggregatorCyclicBehaviour(this));
	}
	
	public AID getUserAgentAID() {
		return userAgentAID;
	}

	public void setUserAID(AID aid) {
		userAgentAID = aid;
	}

	public void setSearchers(String content) {
		for (StringTokenizer tz = new StringTokenizer(content); tz
				.hasMoreTokens();) {
			String searcherName = tz.nextToken();
			searchersAID.add(new AID(searcherName, AID.ISLOCALNAME));
		}
	}

	public AID getNextSearcherAID() {
		return searchersAID.iterator().next();
	}

	public void sendArticle(Article page) {
			ACLMessage responseMSG = new ACLMessage(ACLMessage.INFORM);
			responseMSG.setSender(this.getAID());
			Random random = new Random();
			page.setRank(page.getRank()+random.nextInt(20)%20);
			responseMSG.setContent(page.toString());
			// responseMSG.addReceiver(this.getUserAgentAID());
			responseMSG.addReceiver(userAgentAID);
			this.send(responseMSG);
	}
}
