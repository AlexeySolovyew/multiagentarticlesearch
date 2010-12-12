package searcher.agents.searcher;

import java.util.ArrayList;
import java.util.List;

import searcher.Article;


public class DummySearcherAgent1 extends DummySearcherAgent {
	protected static List<Article> pages;

	static{
		 pages = new ArrayList<Article>();
		 pages.add(new Article("http://www.google.ru", 1));
		 pages.add(new Article("http://www.nude-dreams.com", 2));
		 pages.add(new Article("http://www.vporyade.ru",5));
	}

	public DummySearcherAgent1() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Article> getPages() {
		return pages;
	}

	public void setSourceValue() {
		sourseValue = "Dummy1";
	}
	
}
