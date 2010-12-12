package searcher;

import jade.core.AID;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Article {
	private static final String TITLE = "title";
	private static final String RANK = "rank";
	private static final String URL = "url";
	private static final String AUTHOR = "author";
	private static final String URL_PDF = "urlPDF";
	private static final String SUMMARY = "summary";
	private static final String PUBLISHED_DATE = "publishedDate";
	private static final String UPDATED_DATE = "updatedDate";
	private static final String SEARCHER_NAME = "searcherName";
	private static final String SEARCHER_NAME_2_RANK = "SEARCHER_NAME_2_RANK";
	private static final String SEARCHER_RANK = "SEARCHER_RANK";

	private String title = "no title";
	private int rank = 0;
	private String url = "no url";
	private String author = "no author";
	private String urlPDF;
	private String summary;
	private String publishedDate;
	private String updatedDate;
	private Map<String, Integer> searchersNames2Rank = new HashMap<String, Integer>();
	private Set<AID> searchersSenders = new HashSet<AID>();

	public Article(String url, int rank) {
		this.rank = rank;
		this.url = url;
	}

	public Article(String url, int rank, String title, String author) {
		this.rank = rank;
		this.url = url;
		this.author = author;
		this.title = title;
	}

	/**
	 * 
	 * @param articleAsString
	 *            - article as string, which return method Article.toString()
	 */
	public Article(String articleAsString) {
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			InputSource inputSource = new InputSource(new StringReader(
					articleAsString));
			document = builder.parse(inputSource);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = document.getDocumentElement();

		this.title = root.getElementsByTagName(TITLE).item(0).getTextContent();
		this.rank = new Integer(root.getElementsByTagName(RANK).item(0)
				.getTextContent());
		this.url = root.getElementsByTagName(URL).item(0).getTextContent();
		this.author = root.getElementsByTagName(AUTHOR).item(0)
				.getTextContent();
		this.urlPDF = root.getElementsByTagName(URL_PDF).item(0)
				.getTextContent();
		this.summary = root.getElementsByTagName(SUMMARY).item(0)
				.getTextContent();
		this.publishedDate = root.getElementsByTagName(PUBLISHED_DATE).item(0)
				.getTextContent();
		this.updatedDate = root.getElementsByTagName(UPDATED_DATE).item(0)
				.getTextContent();
		extractSearchersNames(root);

	}

	private void extractSearchersNames(Element root) {
		NodeList nodeListSE2R = root.getElementsByTagName(SEARCHER_NAME_2_RANK);
		for (int i = 0; i < nodeListSE2R.getLength(); i++) {
			String searcherName = ((Element) nodeListSE2R.item(i))
					.getElementsByTagName(SEARCHER_NAME).item(0)
					.getTextContent();
			String rank = ((Element) nodeListSE2R.item(i))
					.getElementsByTagName(RANK).item(0).getTextContent();
			searchersNames2Rank.put(searcherName, new Integer(rank));
		}
	}

	public Article(String id, String idPDF, String title, String summary,
			String author, String publishedDate, String updatedDate) {
		this.url = id;
		this.urlPDF = idPDF;
		this.title = title;
		this.summary = summary;
		this.author = author;
		this.publishedDate = publishedDate;
		this.updatedDate = updatedDate;

	}

	public boolean equals(Article cur) {
		return this.getURL() == cur.getURL();
	}

	public void addRank(String searcherName, int rank) {
		searchersNames2Rank.put(searcherName, rank);
	}

	public String getTitle() {
		return title;
	}

	public String getURL() {
		return url;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getAuthor() {
		return author;
	}

	public String toString() {
		String searchersNamesInfo = getSearchersNamesInfo();
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<article>" + "<"
				+ TITLE + ">" + title + "</" + TITLE + ">" + "<" + RANK + ">"
				+ rank + "</" + RANK + ">" + "<" + URL + ">" + url + "</" + URL
				+ ">" + "<" + AUTHOR + ">" + author + "</" + AUTHOR + ">" + "<"
				+ URL_PDF + ">" + urlPDF + "</" + URL_PDF + ">" + "<" + SUMMARY
				+ ">" + summary + "</" + SUMMARY + ">" + "<" + PUBLISHED_DATE
				+ ">" + publishedDate + "</" + PUBLISHED_DATE + ">" + "<"
				+ UPDATED_DATE + ">" + updatedDate + "</" + UPDATED_DATE + ">"
				+ searchersNamesInfo + "</article>";
	}

	private String getSearchersNamesInfo() {
		String st = "";
		for (String searcherName : searchersNames2Rank.keySet()) {
			st += "<" + SEARCHER_NAME_2_RANK + ">" + "<" + SEARCHER_NAME + ">"
					+ searcherName + "</" + SEARCHER_NAME + ">" + "<"
					+ SEARCHER_RANK + ">"
					+ searchersNames2Rank.get(searcherName) + "</"
					+ SEARCHER_RANK + ">" + "</" + SEARCHER_NAME_2_RANK + ">";
		}
		return st;
	}

	public Article merge(Article page) {
		Iterator<AID> it = searchersSenders.iterator();
		while (it.hasNext()) {
			page.addSearcherSender(it.next());
		}
		return page;
	}

	public void addSearcherSender(AID cur) {
		searchersSenders.add(cur);
	}

}
