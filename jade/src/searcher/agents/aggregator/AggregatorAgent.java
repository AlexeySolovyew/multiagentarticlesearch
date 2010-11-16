package searcher.agents.aggregator;

import searcher.agents.courier.CourierCyclicBehavior;
import jade.core.Agent;

public class AggregatorAgent extends Agent {

	protected void setup() {
		super.setup();
		addBehaviour(new AggregatorCyclicBehavior(this));
	}
	
	
}
