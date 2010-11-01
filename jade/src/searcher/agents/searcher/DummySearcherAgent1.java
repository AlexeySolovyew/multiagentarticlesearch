package searcher.agents.searcher;

import java.util.ArrayList;
import java.util.List;


public class DummySearcherAgent1 extends DummySearcherAgent {
	protected static List<String> pages;

	static{
		 pages = new ArrayList<String>();
		 pages.add("1 - http://www.google.ru");
		 pages.add("2 - http://www.nude-dreams.com");
		 pages.add("3 - http://www.vporyade.ru");
	}

	public DummySearcherAgent1() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<String> getPages() {
		return pages;
	}

}
