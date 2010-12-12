package searcher.agents.aggregator;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.util.leap.Iterator;

public class AggregatorOneShotBehavior extends OneShotBehaviour {

	private AggregatorAgent agent;

	public AggregatorOneShotBehavior(AggregatorAgent orchestratorAgent) {
		agent = orchestratorAgent;
	}

	@Override
	public void action() {
		// Search for services of type "search-articles"
		System.out.println("Agent " + agent.getLocalName()
				+ " searching for services of type \"search-articles\"");
		try {
			// Build the description used as template for the search
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription templateSd = new ServiceDescription();
			templateSd.setType("search-articles");
			Property pr = new Property();
			pr.setName("source");
			templateSd.addProperties(pr);
			template.addServices(templateSd);
			SearchConstraints sc = new SearchConstraints();
			// We want to receive only 10 result
			sc.setMaxResults(new Long(10));

			DFAgentDescription[] results = DFService
					.search(agent, template, sc);
			if (results.length > 0) {
				System.out.println("Agent " + agent.getLocalName()
						+ " found the following \"search-articles\" services:");
				for (int i = 0; i < results.length; ++i) {
					DFAgentDescription dfd = results[i];
					AID provider = dfd.getName();
					// The same agent may provide several services; we are only
					// interested
					// in the aggregate-articles one
					Iterator serviceIt = dfd.getAllServices();
					while (serviceIt.hasNext()) {
						ServiceDescription sd = (ServiceDescription) serviceIt
								.next();
						if (sd.getType().equals("search-articles")) {
							System.out.println("- Service \"" + sd.getName()
									+ "\" provided by agent "
									+ provider.getName());
							Iterator propertyIt = sd.getAllProperties();
							while (propertyIt.hasNext()) {
								Property pt = (Property) propertyIt.next();
								if (pt.getName().equals("source")
										&& (pt.getValue().equals("Google") || pt
												.getValue().equals("Arxiv"))
										&& !agent
												.hasSearcherWithThisPropertyValue((String) pt
														.getValue())) {
									agent.addSearcherPropertyValue((String) pt
											.getValue());
									agent.addSearcherAgentAID(provider);
								}
							}
						}
					}
				}
			} else {
				System.out.println("Agent " + agent.getLocalName()
						+ " did not find any \"search-articles\" service");
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		agent.sendMsgFromQueueToSearchers();
	}

}
