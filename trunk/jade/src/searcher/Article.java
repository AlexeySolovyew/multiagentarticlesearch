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
	private static final String URL = "url";
	private static final String AUTHOR = "author";
	private static final String URL_PDF = "urlPDF";
	private static final String SUMMARY = "summary";
	private static final String PUBLISHED_DATE = "publishedDate";
	private static final String UPDATED_DATE = "updatedDate";
	private static final String SEARCHER_NAME = "searcherName";
	private static final String SEARCHER_NAME_2_RANK = "SEARCHER_NAME_2_RANK";
	private static final String SEARCHER_RANK = "SEARCHER_RANK";
	private static final String USER_RANK = "user_rank";

	private String title = "no title";
	private int user_rank = 0;
	private String url = "no url";
	private String author = "no author";
	private String urlPDF = "no urlPDF";
	private String summary = "no summary";
	private String publishedDate = "no date Publ";
	private String updatedDate = "no date update";
	private Map<String, Integer> searchersSenders2Rank = new HashMap<String, Integer>();

	/*
	 * Constructor for DummySearchAgents and GoogleSearcherAgent
	 */
	public Article(String searcherName, String url, int rank) {
		this.addSearcherSenderAndRank(searcherName, rank);
		this.url = url;
	}

	/*
	 * public Article(String url, int rank, String title, String author) {
	 * this.rank = rank; this.url = url; this.author = author; this.title =
	 * title; }
	 */

	/**
	 * 
	 * @param articleAsString
	 *            - article as string which is opposite to method
	 *            Article.toString()
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
			System.out.println("______PALEVO_____IN_ARTICLE:");
			System.out.println(articleAsString);
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(document!=null){
			Element root = document.getDocumentElement();
	
			this.title = root.getElementsByTagName(TITLE).item(0).getTextContent();
			// this.rank = new Integer(root.getElementsByTagName(RANK).item(0)
			// .getTextContent());
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
			this.user_rank = new Integer(root.getElementsByTagName(USER_RANK)
					.item(0).getTextContent());
	
			extractSearchersNames(root);
		}else{
			System.out.println("___________PALEVO_________");
		}

	}

	private void extractSearchersNames(Element root) {
		NodeList nodeListSE2R = root.getElementsByTagName(SEARCHER_NAME_2_RANK);
		for (int i = 0; i < nodeListSE2R.getLength(); i++) {
			String searcherName = ((Element) nodeListSE2R.item(i))
					.getElementsByTagName(SEARCHER_NAME).item(0)
					.getTextContent();
			String rank = ((Element) nodeListSE2R.item(i))
					.getElementsByTagName(SEARCHER_RANK).item(0)
					.getTextContent();
			searchersSenders2Rank.put(searcherName, new Integer(rank));
		}
	}

	public Article(String id, String idPDF, String title, String summary,
			String author, String publishedDate, String updatedDate) {
		this.url = id;
		this.urlPDF = idPDF;
		this.title = deleteIllegalSymbolsForXML(title);
		this.summary = deleteIllegalSymbolsForXML(summary);
		this.author = deleteIllegalSymbolsForXML(author);
		this.publishedDate = deleteIllegalSymbolsForXML(publishedDate);
		this.updatedDate = deleteIllegalSymbolsForXML(updatedDate);

	}

	private String deleteIllegalSymbolsForXML(String st) {
		int startIndex = st.indexOf('<', 0);
		int curIndex = 0;
		while (startIndex != -1) {
			int finishIndex = st.indexOf('>', startIndex);
			if (finishIndex != -1) {
				String subst = st.substring(startIndex, finishIndex + 1);
				st = st.replaceAll(subst, "");
				curIndex = finishIndex - subst.length();
				startIndex = st.indexOf('<', curIndex);
			}else{
				st = st.replaceAll("<", " ");
				startIndex = -1;
			}
		}
		st = st.replace('&', ' ');
		return st;
	}

	public boolean equals(Article cur) {
		if (this.getAuthor().equals(cur.getAuthor())
				&& this.getTitle().equals(cur.getTitle())) {
			if (this.getAuthor().equals(author)
					&& this.getTitle().equals(title)) {
				return this.getURL().equals(cur.getURL());
			} else {
				return true;
			}
		}
		return false;
	}

	public void addRank(String searcherName, int rank) {
		searchersSenders2Rank.put(searcherName, rank);
	}

	/** Getters **/

	public String getTitle() {
		return title;
	}

	public String getDate() {
		return publishedDate;
	}

	public String getURL() {
		return url;
	}

	public String getAuthor() {
		return author;
	}

	public String getURLPDF() {
		return urlPDF;
	}

	public String getSummary() {
		return summary;
	}

	public int getRank() {
		int rank = 0;
		for (String searcherName : searchersSenders2Rank.keySet()) {
			rank += searchersSenders2Rank.get(searcherName);
		}
		rank += user_rank;
		return rank;
	}

	/** Here getters end **/

	public void addUserRank(int r) {
		user_rank += r;
	}

	public String toString() {
		String searchersNamesInfo = getSearchersNamesInfo();
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
		+ "<article>" 
		+ "<" + URL + ">"+ url + "</" + URL + ">" 
		+"<" + TITLE + ">" + title + "</" + TITLE + ">" 
		+ "<" + AUTHOR + ">" + author + "</"+ AUTHOR + ">" 
		+ "<" + URL_PDF + ">" + urlPDF + "</" + URL_PDF+ ">" 
		+ "<" + SUMMARY + ">" + summary + "</" + SUMMARY + ">"
		+ "<" + PUBLISHED_DATE + ">" + publishedDate + "</"+ PUBLISHED_DATE + ">" 
		+ "<" + UPDATED_DATE + ">" + updatedDate+ "</" + UPDATED_DATE + ">" 
		+ "<" + USER_RANK + ">" + user_rank+ "</" + USER_RANK + ">" 
		+ searchersNamesInfo 
		+ "</article>";
	}

	private String getSearchersNamesInfo() {
		String st = "";
		for (String searcherName : searchersSenders2Rank.keySet()) {
			st += "<" + SEARCHER_NAME_2_RANK + ">" + "<" + SEARCHER_NAME + ">"
					+ searcherName + "</" + SEARCHER_NAME + ">" + "<"
					+ SEARCHER_RANK + ">"
					+ searchersSenders2Rank.get(searcherName) + "</"
					+ SEARCHER_RANK + ">" + "</" + SEARCHER_NAME_2_RANK + ">";
		}
		return st;
	}

	/**
	 * @param page
	 * @return
	 */
	public Article merge(Article page) {
		Iterator<String> it = searchersSenders2Rank.keySet().iterator();
		while (it.hasNext()) {
			String searcherSender = it.next();
			page.addSearcherSenderAndRank(searcherSender,
					searchersSenders2Rank.get(searcherSender));
		}
		return page;
	}

	public void addSearcherSenderAndRank(String searcherName, int rank) {
		searchersSenders2Rank.put(searcherName, rank);
	}

}
