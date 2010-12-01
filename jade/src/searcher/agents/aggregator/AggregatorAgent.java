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
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import jade.util.leap.Iterator;

public class AggregatorAgent extends Agent {
	public static final String INIT_USER = "INIT_USER";
	private AID userAgentAID;
	private Set<AID> searchersAID = new HashSet<AID>();
	private Set<String> pages = new HashSet<String>();

	protected void setup() {
		super.setup();

		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription templateSd = new ServiceDescription();
		templateSd.setType("search-articles");

		// тут можно будет указывать источники, по которым нужно осуществлять
		// поиск
		// templateSd.addProperties(new Property("country", "Italy"));

		template.addServices(templateSd);

		SearchConstraints sc = new SearchConstraints();
		// We want to receive 10 results at most количество агентов,
		// предоставляющих данный сервис, пока оставил 10
		sc.setMaxResults(new Long(10));

		addBehaviour(new SubscriptionInitiator(this,
				DFService.createSubscriptionMessage(this, getDefaultDF(),
						template, sc)) {
			protected void handleInform(ACLMessage inform) {
				System.out.println("Agent " + getLocalName()
						+ ": Notification received from DF");
				try {
					DFAgentDescription[] results = DFService
							.decodeNotification(inform.getContent());
					if (results.length > 0) {
						for (int i = 0; i < results.length; ++i) {
							DFAgentDescription dfd = results[i];
							AID provider = dfd.getName();
							// The same agent may provide several services; we
							// are only interested
							// in the search-articles one
							// не правда, конечно, но опять же пока оставил
							Iterator it = dfd.getAllServices();
							while (it.hasNext()) {
								ServiceDescription sd = (ServiceDescription) it
										.next();
								if (sd.getType().equals("search-articles")) {
									System.out
											.println("Search-articles service found:");
									System.out.println("- Service \""
											+ sd.getName()
											+ "\" provided by agent "
											+ provider.getName());
									searchersAID.add(provider);
								}
							}
						}
					}
					System.out.println();
				} catch (FIPAException fe) {
					fe.printStackTrace();
				}
				if (searchersAID.size() == 3) {
					myAgent.addBehaviour(new AggregatorCyclicBehaviour(
							(AggregatorAgent) myAgent));
				}
			}
		});
	}

	public AID getUserAgentAID() {
		return userAgentAID;
	}

	public void setUserAID(AID aid) {
		userAgentAID = aid;
	}

	/*
	 * public void setSearchers(String content) { for (StringTokenizer tz = new
	 * StringTokenizer(content); tz .hasMoreTokens();) { String searcherName =
	 * tz.nextToken(); searchersAID.add(new AID(searcherName, AID.ISLOCALNAME));
	 * } }
	 */

	// public AID getNextSearcherAID() {
	// return searchersAID.iterator().next();
	// }

	public void sendArticle(Article page) {
		ACLMessage responseMSG = new ACLMessage(ACLMessage.INFORM);
		responseMSG.setSender(this.getAID());
		Random random = new Random();
		page.setRank(page.getRank() + random.nextInt(20) % 20);
		responseMSG.setContent(page.toString());
		// responseMSG.addReceiver(this.getUserAgentAID());
		responseMSG.addReceiver(userAgentAID);
		this.send(responseMSG);
	}

	public Set<AID> getSearchersAID() {
		return searchersAID;
	}
}
