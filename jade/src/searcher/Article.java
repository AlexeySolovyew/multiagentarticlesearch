package searcher;

import java.io.IOException;
import java.io.StringReader;
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

	private static final String DELIM = "&";
	private static final String TITLE = "title";
	private static final String RANK = "rank";
	private static final String URL = "url";
	private static final String AUTHOR = "author";
	private String title = "no title";
	private int rank = 0;
	private String url = "no url";
	private String author = "no author";

	public Article(String url, int rank) {
		this.rank = rank;
		this.url = url;
	}

	public Article(String url, int rank, String title, String author) {
		this.rank = rank;
		this.url = url;
		this.author=author;
		this.title=title;
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
		
		NodeList nodeListTitle = root.getElementsByTagName(TITLE);
		String title = nodeListTitle.item(0).getTextContent();
		this.title = title;
		
		NodeList nodeListRank = root.getElementsByTagName(RANK);
		String rank = nodeListRank.item(0).getTextContent();
		this.rank = new Integer(rank);
		
		NodeList nodeListURL = root.getElementsByTagName(URL);
		String url = nodeListURL.item(0).getTextContent();
		this.url = url;
		
		NodeList nodeListAuthor = root.getElementsByTagName(AUTHOR);
		String author = nodeListAuthor.item(0).getTextContent();
		this.author = author;
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
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
				+ "<article>"
				+   "<" + TITLE + ">" + title + "</" + TITLE + ">"
				+   "<" + RANK  + ">" + rank  + "</" + RANK  + ">"
				+   "<" + URL   + ">" + url   + "</" + URL   + ">"
				+   "<" + AUTHOR + ">" + author + "</" + AUTHOR + ">"
				+ "</article>";
	}
}
