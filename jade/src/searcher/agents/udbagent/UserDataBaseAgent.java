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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

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
		initFile(farticles,"articles.xml");
		initFile(fauthors,"authors.xml");
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
		orchestratorAgentAID=aid;
		
	}
	
	public void initFile(File f,String s){
		
	      f=new File(s);
	      if(!f.exists()){
	      try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      System.out.println("New file "+s+" has been created to the current directory");
		
			}
	      try{
	    	    // Create filewriter
	    	    FileWriter fstream = new FileWriter(f);
	    	        BufferedWriter out = new BufferedWriter(fstream);
	    	    
	    	    out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	    	    out.newLine();
	    	    out.write("<ARTICLES>");
	    	    out.newLine();
	    	    out.write("</ARTICLES>");
	    	    		    	    
	    	    //Close the output stream
	    	    out.close();
  	    }catch (Exception e){//Catch exception if any
	    	      System.err.println("Error: " + e.getMessage());
	    	    }
	}
	
	public void addRatings(Article a) throws DOMException, IOException{
		
		//просто статьи по айдишникам
		/*Document document = null;
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
		
		NodeList nl = root.getElementsByTagName("ID");
		if (nl==null){
			
		}
		
		for (int i=0;i<nl.getLength();i++){
			if (nl.item(i).getTextContent()==a.getURL()){
				out.write("<ARTICLE>");
	    	    out.newLine();
	    	    out.write("<ID>");
	    	    out.newLine();
	    	    out.write(a.getURL());
	    	    out.newLine();
	    	    out.write("</ID>");
	    	    out.newLine();
	    	    out.write("<RATE>");
	    	    out.newLine();
	    	    out.write("1");
	    	    out.newLine();
	    	    out.write("</RATE>");
	    	    out.newLine();
	    	    out.write("</ARTICLE>");
	    	    out.newLine();
			}
		}
			
		
		Writer out = new OutputStreamWriter(new FileOutputStream(farticles));
		    try {
		      out.write(document.getTextContent());
		    }
		    finally {
		      out.close();
		    }

		  
			
		
		
		*/
		
	}
	

}
