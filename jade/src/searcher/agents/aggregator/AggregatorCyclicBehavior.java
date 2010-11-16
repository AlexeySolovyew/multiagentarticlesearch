package searcher.agents.aggregator;

import searcher.agents.courier.CourierAgent;
import jade.core.behaviours.CyclicBehaviour;

public class AggregatorCyclicBehavior extends CyclicBehaviour {

	private AggregatorAgent agent;
		
	public AggregatorCyclicBehavior(AggregatorAgent aggregatorAgent) {
		this.agent=aggregatorAgent;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

	}

}
