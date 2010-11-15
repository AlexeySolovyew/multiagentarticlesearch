package searcher;

public class Article {

	private String name;
	private int rank;

	public String getName() {
		return name;
	}

	public int getRank() {
		return rank;
	}

	public Article(String s, int r) {
		rank = r;
		name = s;
	}
}
	
