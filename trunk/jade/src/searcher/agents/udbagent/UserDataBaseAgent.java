package searcher.agents.udbagent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;

import javax.imageio.metadata.IIOMetadataNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import searcher.Article;
import searcher.agents.orchestrator.OrchestratorCyclicBehavior;
import searcher.agents.user.RefreshFrameBehaviour;
import searcher.agents.user.UserAgentFrame;
import searcher.agents.user.UserCyclicBehaviour;
import jade.core.AID;
import jade.core.Agent;

public class UserDataBaseAgent extends Agent {

	private AID userAgentAID;
	private AID orchestratorAgentAID;
	private File farticles;
	private File fauthors;

	public static final String INIT_USER = "INIT_USER";

	protected void setup() {
		super.setup();
		System.out.println("Вот щас бы файлики создать");
		farticles=initFile("articles.xml");
		fauthors=initFile("authors.xml");
		System.out.println("И где блять ваши файлики?!?");
		addBehaviour(new UserDataBaseAgentCyclicBehaviour(this));
	}

	public void setUserAID(AID aid) {
		userAgentAID = aid;
	}

	public AID getUserAgentAID() {
		return userAgentAID;
	}

	public AID getOrchestratorAID() {
		return orchestratorAgentAID;

	}

	public void setOrchestratorAID(AID aid) {
		orchestratorAgentAID = aid;

	}

	public File initFile(String s) {

		File f = new File(s);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("New file " + s
					+ " has been created to the current directory");

		}
		System.out.println("такой файл блять уже есть: "+f.getAbsolutePath());
		try {
			// Create filewriter
			FileWriter fstream = new FileWriter(f);
			BufferedWriter out = new BufferedWriter(fstream);

			out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			
			 out.newLine(); out.write("<ARTICLES>"); out.newLine();
			 out.write("</ARTICLES>");
			

			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return f;
	}

	public void addRatings(Article a) throws DOMException, IOException {

		/*
		// просто статьи по айдишникам
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(farticles);
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
		// Допустим это <ARTICLES>
		 Node articles = root.getChildNodes().item(0);

		NodeList nl = root.getElementsByTagName("ID");
		if (nl.getLength() == 0) {
			addArticle(a, document, root);

		} else{
			int i=0;
		for (; i < nl.getLength(); i++) {
			if (nl.item(i).getTextContent() == a.getURL()) {
				Node rankNode = nl.item(i).getParentNode().getChildNodes().item(1);
				//тут надо изменить ранг, это блин сложно, пока пусть будет 2
					rankNode.setTextContent("2");
			}
			if (i==nl.getLength()){
				addArticle(a, document, root);
			}
		}
		}

		Writer out = new OutputStreamWriter(new FileOutputStream(farticles));
		try {
			//тут надо бы записать нахимиченный DOM в файл (пишется вместо этого какая-то хуйня)
			out.write(root.getElementsByTagName("ID").item(0).getTextContent());
		} finally {
			out.close();
		}
*/
	}

	private void addArticle(Article a, Document document, Element root) {
		Element art = document.createElement("ARTICLE");
		Element elem;
		Text elem_value;

		elem = document.createElement("ID");
		elem_value = document.createTextNode(a.getURL());
		elem.appendChild(elem_value);
		art.appendChild(elem);

		elem = document.createElement("RATE");
		elem_value = document.createTextNode("1");
		elem.appendChild(elem_value);
		art.appendChild(elem);

		root.appendChild(art);
	}
}
