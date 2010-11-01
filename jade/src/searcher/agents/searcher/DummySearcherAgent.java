package searcher.agents.searcher;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.bind.v2.runtime.Name;

import searcher.agents.courier.CourierAgent;
import searcher.agents.user.UserAgent;

public abstract class DummySearcherAgent extends SearcherAgent {
	
	public DummySearcherAgent(){
		super();		
	}
	
	@Override
	protected void setup() {
		super.setup();
	}

	@Override
	public List<String> search(ACLMessage msg) {
		List<String> result = new ArrayList<String>();
		for (String pageName : getPages()) {
			result.add(pageName);
		}
		return result;
	}

	public abstract List<String> getPages();
	
	
	

}
