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
	// public static final String NOTICE_AGGREGATOR = "I_NEED_YOUR_SERVICE";
	private AID userAgentAID;
	private AID aggregatorAgentAID;
	private LinkedList<ACLMessage> queueOfAggregatorsMSGs = new LinkedList<ACLMessage>();

	// private Set<AID> searchersAID = new HashSet<AID>();

	public OrchestratorAgent() {

	}

	/*
	 * public CourierAgent(UserAgent userAgent, Set<AID> AID_seachers) {
	 * this.userAgent = userAgent; this.AID_seachers = AID_seachers; }
	 */

	@Override
	protected void setup() {
		super.setup();
		subscribeOnService();
		addBehaviour(new OrchestratorCyclicBehavior(this));
	}

	private void subscribeOnService() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription templateSd = new ServiceDescription();
		templateSd.setType("aggregate-articles");
		template.addServices(templateSd);

		SearchConstraints sc = new SearchConstraints();
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

							// ������ ����� �������� �� ����� ��������, �����
							// ����� ���
							Iterator it = dfd.getAllServices();
							while (it.hasNext()) {
								ServiceDescription sd = (ServiceDescription) it
										.next();
								if (sd.getType().equals("aggregate-articles")) {
									System.out
											.println("Aggregate-articles service found:");
									System.out.println("- Service \""
											+ sd.getName()
											+ "\" provided by agent "
											+ provider.getName());
									aggregatorAgentAID = provider;
									// sendInitMSG(aggregatorAgentAID);
									if (!queueOfAggregatorsMSGs.isEmpty()) {
										sendMsgFromQueueToAggregator();
									}
								}
							}
						}
					}
					System.out.println();
				} catch (FIPAException fe) {
					fe.printStackTrace();
				}
			}
		});
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
		newMSG.addReceiver(aggregatorAgentAID);
		this.send(newMSG);
	}

	public void sendArticle(Article page) {
		ACLMessage responseMSG = new ACLMessage(ACLMessage.PROPOSE);
		responseMSG.setSender(this.getAID());
		page.setRank(page.getRank());
		responseMSG.setContent(page.toString());
		responseMSG.addReceiver(userAgentAID);
		this.send(responseMSG);
	}
	
	/*
	 * private void sendInitMSG(AID aid){ ACLMessage initMSG = new
	 * ACLMessage(UserAgent.INIT); initMSG.setSender(this.getAID());
	 * initMSG.setContent(NOTICE_AGGREGATOR); initMSG.addReceiver(aid);
	 * send(initMSG); }
	 */

	public void setUserAID(AID aid) {
		userAgentAID = aid;
	}

	/*
	 * public void setSearchers(String content) { for (StringTokenizer tz = new
	 * StringTokenizer(content); tz .hasMoreTokens();) { String searcherName =
	 * tz.nextToken(); searchersAID.add(new AID(searcherName, AID.ISLOCALNAME));
	 * }
	 * 
	 * }
	 */
}
