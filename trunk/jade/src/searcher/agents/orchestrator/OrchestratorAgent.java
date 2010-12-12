package searcher.agents.orchestrator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.StringTokenizer;

import com.sun.org.apache.bcel.internal.generic.NEW;

import examples.party.HostAgent;

import searcher.Article;
import searcher.agents.aggregator.AggregatorAgent;
import searcher.agents.aggregator.AggregatorCyclicBehaviour;
import searcher.agents.searcher.SearcherAgent;
import searcher.agents.user.UserAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

public class OrchestratorAgent extends Agent {

	public static final String INIT_USER = "INIT_USER";
	private AID userAgentAID;
	private AID userDataBaseAgentAID;
	private AID aggregatorAgentAID;
	private LinkedList<ACLMessage> queueOfAggregatorsMSGs = new LinkedList<ACLMessage>();

	public OrchestratorAgent() {

	}

	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new OrchestratorCyclicBehavior(this));
	}

	public AID getUserAgentAID() {
		return userAgentAID;
	}

	public AID getAggregatorAgentAID() {
		return aggregatorAgentAID;
	}

	public void addMsgToQueueOfAggregatorsMSGs(ACLMessage msg) {
		ACLMessage newMSG = new ACLMessage(msg.getPerformative());
		newMSG.setSender(this.getAID());
		newMSG.setContent(msg.getContent());
		newMSG.setLanguage("Plain English");
		queueOfAggregatorsMSGs.addLast(newMSG);
	}

	public void sendMsgFromQueueToAggregator() {
		ACLMessage newMSG = queueOfAggregatorsMSGs.removeFirst();
		newMSG.addReceiver(getAggregatorAgentAID());
		this.send(newMSG);
	}

	public void sendArticle(Article page,AID receiver,int perf) {
		ACLMessage responseMSG = new ACLMessage(perf);
		responseMSG.setSender(this.getAID());
		page.setRank(page.getRank());
		responseMSG.setContent(page.toString());
		responseMSG.addReceiver(receiver);
		this.send(responseMSG);
	}

	public void setUserAID(AID aid) {
		userAgentAID = aid;
	}

	public void findAndLoadAggregator() {
		addBehaviour(new OrchestratorOneShotBehavior(this));
	}

	public void setAggregatorAgentAID(AID aggregatorAgentAID) {
		this.aggregatorAgentAID = aggregatorAgentAID;
	}

	public void setUserDataBaseAgentAID(AID aid) {
		userDataBaseAgentAID = aid;
	}

	public AID getUserDataBaseAgentAID() {
		return userDataBaseAgentAID;
	}

}
