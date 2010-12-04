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

public class ArxivSearcherAgent extends SearcherAgent {
	
	private static final int AMOUNT_OF_RESULTS = 20;

	private static String getIdPDF(Node entryNode) {
		NodeList nodeList = ((Element)entryNode).getElementsByTagName("link");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			Attr attrTitle = ((Element)node).getAttributeNode("title");
			if(attrTitle!=null && attrTitle.getValue().equals("pdf")){
				Attr attrHrefPDF = ((Element)node).getAttributeNode("href");
				return attrHrefPDF.getValue();				
			}
		}
		return null;
	}

	private static String getAuthor(Node entryNode) {
		NodeList nodeList = ((Element)entryNode).getElementsByTagName("name");
		return nodeList.item(0).getTextContent();
	}

	private static String getTextByTagName(Node entryNode, String tag) {
		NodeList nodeList = ((Element)entryNode).getElementsByTagName(tag);
		return nodeList.item(0).getTextContent();
	}

	@Override
	public void searchAndSendResults(ACLMessage msg) {
		String query = msg.getContent();
		
		Document document = request(query);
		Element root = document.getDocumentElement();
		NodeList nodeListEntry = root.getElementsByTagName("entry");
		List<Article> result = new ArrayList<Article>();
		for (int i = 0; i < nodeListEntry.getLength(); i++) {
			Node entryNode = nodeListEntry.item(i);
			
			String id = getTextByTagName(entryNode,"id");			
			String published = getTextByTagName(entryNode,"published");
			String updated = getTextByTagName(entryNode,"updated");
			String title = getTextByTagName(entryNode,"title");
			String summary = getTextByTagName(entryNode,"summary");
			String author = getAuthor(entryNode);
			String idPDF = getIdPDF(entryNode);
			
			this.sendArticle(new Article(id,idPDF,title,summary,author,published,updated));
		}
	}

	private static Document request(String query) {
		Document doc = null;
		String arxivQuery = "";
		for(StringTokenizer st = new StringTokenizer(query); st.hasMoreTokens();){
			arxivQuery += "all:" + st.nextToken();
			if(st.hasMoreTokens()){
				arxivQuery += "+AND+";
			}
		}
		try {
			URL url;
			url = new URL(
					"http://export.arxiv.org/api/query?search_query=" + arxivQuery + "&start=0&max_results=" + AMOUNT_OF_RESULTS);
			URLConnection connection = url.openConnection();

			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
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

}
