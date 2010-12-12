package searcher.agents.orchestrator;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.util.leap.Iterator;

public class OrchestratorOneShotBehavior extends OneShotBehaviour {

	private OrchestratorAgent agent;

	public OrchestratorOneShotBehavior(OrchestratorAgent orchestratorAgent) {
		agent = orchestratorAgent;
	}

	@Override
	public void action() {
		// Search for services of type "aggregate-articles"
		System.out.println("Agent " + agent.getLocalName()
				+ " searching for services of type \"aggregate-articles\"");
		try {
			// Build the description used as template for the search
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription templateSd = new ServiceDescription();
			templateSd.setType("aggregate-articles");
			template.addServices(templateSd);

			SearchConstraints sc = new SearchConstraints();
			// We want to receive only 1 result
			sc.setMaxResults(new Long(1));

			DFAgentDescription[] results = DFService.search(agent, template, sc);
			if (results.length > 0) {
				System.out.println("Agent " + agent.getLocalName()
						+ " found the following \"aggregate-articles\" services:");
				for (int i = 0; i < results.length; ++i) {
					DFAgentDescription dfd = results[i];
					AID provider = dfd.getName();
					// The same agent may provide several services; we are only
					// interested
					// in the aggregate-articles one
					Iterator it = dfd.getAllServices();
					while (it.hasNext()) {
						ServiceDescription sd = (ServiceDescription) it.next();
						if (sd.getType().equals("aggregate-articles")) {
							System.out.println("- Service \"" + sd.getName()
									+ "\" provided by agent "
									+ provider.getName());
							agent.setAggregatorAgentAID(provider);
							agent.sendMsgFromQueueToAggregator();
						}
					}
				}
			} else {
				System.out.println("Agent " + agent.getLocalName()
						+ " did not find any \"aggregate-articles\" service");
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

}
