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
		NodeList nodeListName = root.getElementsByTagName("name");
		String name = nodeListName.item(0).getTextContent();
		this.name = name;
		NodeList nodeListRank = root.getElementsByTagName("rank");
		String rank = nodeListRank.item(0).getTextContent();
		this.rank = new Integer(rank);
	}

	public String toString() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
				+ "<article>"
				+   "<name>PAGE1111</name>"
				+   "<rank>rank10</rank>"
				+ "</article>";
	}
}
