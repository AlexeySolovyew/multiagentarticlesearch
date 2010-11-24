package searcher.agents.strategy;

import searcher.agents.courier.CourierCyclicBehavior;
import jade.core.AID;
import jade.core.Agent;

public class StrategyAgent extends Agent {
	public static final String INIT_USER = "INIT_USER";
	private AID agregatorAgentAID;

	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new StrategyCyclicBehavior(this));
	}
	
	public void setAgregatorAID(AID aid) {
		agregatorAgentAID = aid;
	}

}
