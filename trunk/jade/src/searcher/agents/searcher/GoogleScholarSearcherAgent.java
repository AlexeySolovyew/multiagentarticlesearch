package searcher.agents.searcher;

import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import searcher.Article;

public class GoogleScholarSearcherAgent extends SearcherAgent {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String query = "indexing";// msg.getContent();

		Document document = request(query);
		Element root = document.getDocumentElement();
		// List<Article> result = new ArrayList<Article>();
		NodeList nodeListArticls = root.getElementsByTagName("div");
		for (int i = 0; i < args.length; i++) {
			Node articleNode = nodeListArticls.item(i);
			Attr attrClass = ((Element) articleNode).getAttributeNode("class");
			if (attrClass != null && attrClass.getValue().equals("gs_r")) {
				String id = "";
				String title = "";
				// Begin <div class = "gs_rt">
				NodeList nodeListDivClassGS_RT = root
						.getElementsByTagName("div");
				assert nodeListDivClassGS_RT.getLength() == 1;
				Node divClassGS_RTNode = nodeListDivClassGS_RT.item(0);
				Attr attrClassGS_RT = ((Element) divClassGS_RTNode)
						.getAttributeNode("class");
				if (attrClassGS_RT != null
						&& attrClassGS_RT.getValue().equals("gs_rt")) {
					NodeList nodeListH3 = ((Element) nodeListDivClassGS_RT)
							.getElementsByTagName("h3");
					assert nodeListH3.getLength() == 1;
					NodeList nodeListA = ((Element) nodeListH3.item(0))
							.getElementsByTagName("a");
					assert nodeListA.getLength() == 1;
					Attr attrHref = ((Element) nodeListA.item(0))
							.getAttributeNode("href");
					if (attrHref != null) {
						id = attrHref.getValue();
					}
					title = ((Element) nodeListA.item(0)).getTextContent();

					System.out.println("id = " + id);
					System.out.println("title = " + title);
					System.out.println();
				}

				/*
				 * Node divClassGS_RTNode = nodeListDivClassGS_RT.item(i); Attr
				 * attrClass = ((Element) divClassGS_RTNode)
				 * .getAttributeNode("class"); if (attrClass != null &&
				 * attrClass.getValue().equals("gs_rt")) {
				 * 
				 * String published = getTextByTagName(entryNode, "published");
				 * String updated = getTextByTagName(entryNode, "updated");
				 * String summary = getTextByTagName(entryNode, "summary");
				 * String author = getAuthor(entryNode); String idPDF =
				 * getIdPDF(entryNode);
				 * 
				 * // this.sendArticle(new Article(id, idPDF, title, summary, //
				 * author, // published, updated)); }
				 */
				// END <div class = "gs_rt">
			}
		}
		// for (int i = 0; i < nodeListDivClassGS_RT.getLength(); i++) {
		// }

	}

	// ----------------------------------
	@Override
	public void searchAndSendResults(ACLMessage msg) {

	}

	private static Document request(String query) {
		Document doc = null;
		String googleScholarQuery = "http://scholar.google.com";//createQueryForGoogleScholar(query);
		try {
			URL url;
			url = new URL(googleScholarQuery);
			URLConnection connection = url.openConnection();

			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			System.out.println(builder.toString());
			doc = parseXML(builder.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;

	}

	private static String createQueryForGoogleScholar(String query) {
		String googleScholarQuery = "http://scholar.google.com/scholar?q=";
		for (StringTokenizer st = new StringTokenizer(query); st
				.hasMoreTokens();) {
			googleScholarQuery += st.nextToken();
			if (st.hasMoreTokens()) {
				googleScholarQuery += "+";
			}
		}
		return googleScholarQuery;
	}

	private static Document parseXML(String content) {
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			// document = builder.parse(uri);
			InputSource inputSource = new InputSource(new StringReader(content));
			document = builder.parse(inputSource);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

	@Override
	public void setSourceValue() {
		sourseValue = "GoogleScholar";
		
	}

}
