package searcher.agents.searcher;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

//import com.sun.xml.internal.bind.v2.runtime.Name;

import searcher.Article;
import searcher.agents.orchestrator.OrchestratorAgent;
import searcher.agents.user.UserAgent;

public abstract class DummySearcherAgent extends SearcherAgent {
	
	public DummySearcherAgent(){
		super();		
	}
	
	@Override
	public void searchAndSendResults(ACLMessage msg) {
		for (Article article : getPages()) {
			this.sendArticle(article);
		}
	}
	

	public abstract List<Article> getPages();
	
	
	

}
