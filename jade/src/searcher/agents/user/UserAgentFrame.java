package searcher.agents.user;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;

import searcher.Article;

public class UserAgentFrame extends JFrame {

	private UserAgent agent;

	private JButton searchButton;
	private JPanel p;
	private JTextField inputField;
	private JTextPane outputField;
	
	public UserAgentFrame(UserAgent agent) {
		super();
		this.agent = agent;
		jfInit();
	}

	
		
			private void jfInit() {
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());
		p = new JPanel();
		outputField = new JTextPane();
		outputField.setContentType("text/html");
		outputField.setEditable(false);
		JScrollPane paneScrollPane = new JScrollPane(outputField);
	    paneScrollPane.setVerticalScrollBarPolicy(
	                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    paneScrollPane.setPreferredSize(new Dimension(250, 155));
	    paneScrollPane.setMinimumSize(new Dimension(10, 10));
	    
		inputField = new JTextField();
		searchButton = new JButton("search");
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				searchButton_actionPerformed();
			}

		});

		container.add(paneScrollPane, BorderLayout.CENTER);
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(inputField);
		p.add(searchButton);
		container.add(p, BorderLayout.NORTH);
		this.setSize(600, 600);
		this.setTitle("UserAgent - " + agent.getName());
		this.setVisible(true);
	}

	private void searchButton_actionPerformed() {
		outputField.setText("");
		agent.clearPages();
		agent.addBehaviour(new OneShotBehaviour() {
			public void action() {
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.setSender(agent.getAID());
				msg.addReceiver(agent.getOrchestratorAID());
				msg.setContent(inputField.getText());
				msg.setLanguage("Plain English");
				agent.send(msg);
			}
		});
	}
	
	public void hyperlinkUpdate(HyperlinkEvent event) {
	    if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	    	
	    	final URL url=event.getURL();
	      try {
	    	  
	        outputField.setPage(url);
	      } catch(IOException ioe) {
	        ioe.printStackTrace();
	      }
	      //���������� ������ ��� ������ � ���� ������ ����������
	      
	      agent.addBehaviour(new OneShotBehaviour() {
				public void action() {
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.setSender(agent.getAID());
					msg.addReceiver(agent.getUserDataBaseAID());
				//��� ���� ���-�� ��������� ������
					msg.setContent(agent.findByURL(url.toString()).toString());
					agent.send(msg);
				}
			});
	      
	    }
	  }

	public void showPages() throws InterruptedException {
		outputField.setText("");
		List<Article> resultPages=agent.getPages();
		String s = "";
		for (int i = 0; i < resultPages.size(); i++) {
			s += "<TABLE BORDER=3 WIDTH=100%>"
					+ "<TR><TD WIDTH=200><B>Title</B></TD><TD>"
					+ resultPages.get(i).getTitle() + "</TD></TR>"
					+ "<TR><TD WIDTH=200><B>Author</B></TD><TD>"
					+ resultPages.get(i).getAuthor() + "</TD></TR>"
					+ "<TR><TD WIDTH=200><B>URL</B></TD><TD><A HREF="
					+ resultPages.get(i).getURL() + ">"
					+ resultPages.get(i).getURL() + "</A></TD></TR>"
					+ "</TABLE><BR>";

		}
		outputField.setText(s);

	}

}
