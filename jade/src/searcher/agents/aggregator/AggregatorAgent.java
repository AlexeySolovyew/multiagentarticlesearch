package searcher.agents.aggregator;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import searcher.Article;
import searcher.agents.aggregator.AggregatorCyclicBehaviour;
import searcher.agents.orchestrator.OrchestratorCyclicBehavior;
import searcher.agents.orchestrator.OrchestratorOneShotBehavior;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;

public class AggregatorAgent extends Agent {
	private AID orchestratorAgentAID;
	private Set<AID> searchersAID = new HashSet<AID>();
	private LinkedList<ACLMessage> queueOfSearchersMSGs = new LinkedList<ACLMessage>();
	private Set<String> pages = new HashSet<String>();
	private Set<String> searchersPropertyValues = new HashSet<String>();
	private Set<Article> derivedArticles = new HashSet<Article>();

	protected void setup() {
		super.setup();

		provideService();

		addBehaviour(new AggregatorCyclicBehaviour(this));
	}

	private void provideService() {
		String serviceName = "aggregating";

		// Register the service
		System.out.println("Agent " + getLocalName()
				+ " registering service \"" + serviceName
				+ "\" of type \"aggregate-articles\"");
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());

			ServiceDescription sd = new ServiceDescription();
			sd.setName(serviceName);
			sd.setType("aggregate-articles");

			dfd.addServices(sd);

			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	public void addMsgToQueueOfSearchersMSGs(ACLMessage msg) {
		ACLMessage newMSG = new ACLMessage(msg.getPerformative());
		newMSG.setSender(this.getAID());
		newMSG.setContent(msg.getContent());
		newMSG.setLanguage("Plain English");
		queueOfSearchersMSGs.addLast(newMSG);
	}

	public void sendMsgFromQueueToAgent(AID aid) {
		ACLMessage newMSG = queueOfSearchersMSGs.getFirst();
		newMSG.addReceiver(aid);
		this.send(newMSG);
	}

	public void sendArticle(Article page) {		
		for (Article curArticle : derivedArticles) {
			if (curArticle.equals(page)){
				page = curArticle.merge(page);
			}
		}
		
		ACLMessage responseMSG = new ACLMessage(ACLMessage.PROPOSE);
		responseMSG.setSender(this.getAID());
		//Random random = new Random();
		//page.setRank(page.getRank() + random.nextInt(20) % 20);
		responseMSG.setContent(page.toString());
		// responseMSG.addReceiver(this.getUserAgentAID());
		responseMSG.addReceiver(orchestratorAgentAID);
		this.send(responseMSG);
	}

	public Set<AID> getSearcherAgentsAID() {
		return searchersAID;
	}

	public void setOrchestratorAgentAID(AID sender) {
		orchestratorAgentAID = sender;
	}

	public AID getOrchestratorAgentAID() {
		return orchestratorAgentAID;
	}

	public void findAndLoadSearchers() {
		addBehaviour(new AggregatorOneShotBehavior(this));

	}

	public boolean hasSearcherWithThisPropertyValue(String value) {
		java.util.Iterator<String> it = searchersPropertyValues.iterator();
		while (it.hasNext()) {
			if (it.next().equals(value)) {
				return true;
			}
		}
		return false;
	}

	public void addSearcherPropertyValue(String value) {
		searchersPropertyValues.add(value);
	}

	public void addSearcherAgentAID(AID provider) {
		searchersAID.add(provider);
	}

	public void sendMsgFromQueueToSearchers() {
		ACLMessage newMSG = queueOfSearchersMSGs.removeFirst();
		java.util.Iterator<AID> it = searchersAID.iterator();
		while (it.hasNext()) {
			newMSG.addReceiver(it.next());
		}
		this.send(newMSG);
	}

	public void addArticle(Article cur) {
		derivedArticles.add(cur);
	}

}
