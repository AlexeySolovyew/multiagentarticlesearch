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
	private static final int AMOUNT_OF_RESULTS_ON_EACH_REQUEST = 5;
	
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

	public static void main(String[] args) {
		String query = "indexing";// msg.getContent();

		Document document = request(query,0);

	}
	@Override
	public void searchAndSendResults(ACLMessage msg) {
		String query = msg.getContent();
		for (int numberStart = 0; numberStart < MAX_AMOUNT_OF_RESULTS_ON_ONE_REQUEST; numberStart += AMOUNT_OF_RESULTS_ON_EACH_REQUEST) {
			System.out.println("NUMBER_START = " + numberStart);
			Document document = request(query,numberStart );
			Element root = document.getDocumentElement();
			NodeList nodeListEntry = root.getElementsByTagName("entry");
			System.out.println("кол-ыо результатов за раз = " + nodeListEntry.getLength());
			for (int i = 0; i < nodeListEntry.getLength(); i++) {
				assert i < AMOUNT_OF_RESULTS_ON_EACH_REQUEST;
				Node entryNode = nodeListEntry.item(i);
				
				String id = getTextByTagName(entryNode,"id");			
				String published = getTextByTagName(entryNode,"published");
				String updated = getTextByTagName(entryNode,"updated");
				String title = getTextByTagName(entryNode,"title");
				String summary = getTextByTagName(entryNode,"summary");
				String author = getAuthor(entryNode);
				String idPDF = getIdPDF(entryNode);
				
				Article	article = new Article(id,idPDF,title,summary,author,published,updated);
				article.addSearcherSenderAndRank(this.getName(), getCurRankArticle(numberStart + i));
				this.sendArticle(article);
			}
		}
		
		
	}

	private static Document request(String query, int numberStart) {
		Document doc = null;
		String arxivQuery = createArxivQueryURL(query, numberStart);
		
		try {
			URL url;
			url = new URL(arxivQuery);
					//"http://export.arxiv.org/api/query?search_query=" + arxivQuery + "&start="+numberStart+"&max_results=" + (numberStart+AMOUNT_OF_RESULTS_ON_EACH_REQUEST));
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
	private static String createArxivQueryURL(String query, int numberStart) {
		String arxivQuery = "http://export.arxiv.org/api/query?search_query=";
		for(StringTokenizer st = new StringTokenizer(query); st.hasMoreTokens();){
			arxivQuery += "all:" + st.nextToken();
			if(st.hasMoreTokens()){
				arxivQuery += "+AND+";
			}
		}
		arxivQuery+="&start="+numberStart+"&max_results=" + AMOUNT_OF_RESULTS_ON_EACH_REQUEST;
		return arxivQuery;
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
		sourseValue = "Arxiv";
	}

}
