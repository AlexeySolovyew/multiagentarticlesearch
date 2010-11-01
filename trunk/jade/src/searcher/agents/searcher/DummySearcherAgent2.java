package searcher.agents.searcher;

import java.util.ArrayList;
import java.util.List;

public class DummySearcherAgent2 extends DummySearcherAgent {
	protected static List<String> pages;
	
	static{
		 pages = new ArrayList<String>();
		 pages.add("page1"); 
		 pages.add("page2");
		 pages.add("page3");
	}

	public DummySearcherAgent2() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<String> getPages() {
		return pages;
	}

}
