package searcher.agents.bootstrapper;

import java.util.HashMap;
import java.util.Map;

import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

public class BootStrAgent extends Agent {
	private static final String AGGREGATOR_CLASS_NAME = "searcher.agents.aggregator.AggregatorAgent";
	private static final String User_CLASS_NAME = "searcher.agents.user.UserAgent";

	private static final String SEARCHER_AGENT_NAME = "searcherAgent";
	private static final String AGGREGATOR_AGENT_NAME = "aggregatorAgent";
	private static final String USER_AGENT_NAME = "userAgent";


	private BootStrAgentFrame itsFrame;
	private Map<Integer, String> dbsNumber2AgentClassName = new HashMap<Integer, String>();
	private int amountOfAggregators = 2;
	private int amountOfSearchersForEachDBS = 2; // DBS - DataBase of Searcher
	private int amountOfUsers = 1;

	public BootStrAgent() {
		/*
		dbsNumber2AgentClassName.put(1,
				"searcher.agents.searcher.DummySearcherAgent1");
		dbsNumber2AgentClassName.put(2,
				"searcher.agents.searcher.DummySearcherAgent2");
		dbsNumber2AgentClassName.put(1,
				"searcher.agents.searcher.GoogleSearcherAgent");
		 */
		dbsNumber2AgentClassName.put(1,
		"searcher.agents.searcher.ArxivSearcherAgent");
		dbsNumber2AgentClassName.put(2,
		"searcher.agents.searcher.GoogleScholarSearcherAgent");

	}

	@Override
	protected void setup() {
		super.setup();
		itsFrame = new BootStrAgentFrame(this);
	}

	protected void createAgent(String nickNameAgent, String classNameAgent) {
		PlatformController container = getContainerController();
		AgentController agentController;
		try {
			agentController = container.createNewAgent(nickNameAgent,
					classNameAgent, null);
			agentController.start();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void createAllAgents() {
		for (int i = 0; i < amountOfAggregators; i++) {
			createAgent(AGGREGATOR_AGENT_NAME + i, AGGREGATOR_CLASS_NAME);
		}
		for (int i = 0; i < amountOfUsers; i++) {
			createAgent(USER_AGENT_NAME + i, User_CLASS_NAME);
		}
		for (Integer dbsNumber : dbsNumber2AgentClassName.keySet()) {
			for (int searcherNumber = 0; searcherNumber < amountOfSearchersForEachDBS; searcherNumber++) {
				createAgent(SEARCHER_AGENT_NAME + dbsNumber + "."
						+ searcherNumber,
						dbsNumber2AgentClassName.get(dbsNumber));
			}
		}
	}

}
