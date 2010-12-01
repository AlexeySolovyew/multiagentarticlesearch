package searcher.agents.user;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.tools.testagent.TestAgent;
import jade.util.leap.Collection;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import searcher.Article;

import examples.party.HostAgent;

public class UserAgentFrame extends JFrame {

	private UserAgent agent;

	private JButton searchButton;
	private JPanel p;
	private JTextField inputField;
	private JTextPane outputField;
	private List<Article> resultPages;

	public UserAgentFrame(UserAgent agent) {
		super();
		this.resultPages = new ArrayList<Article>();
		this.agent = agent;
		jfInit();
		this.setSize(600, 600);
		this.setTitle("UserAgent - " + agent.getName());
		this.setVisible(true);

	}

	public void addArticleToFrame(Article a) {
		resultPages.add(a);
		try {
			sortAndShow();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void jfInit() {
		// aclTreePanel = new ACLTracePanel(agent);
		// this.setFrameIcon("images/dummy.gif");
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());
		p = new JPanel();
		outputField = new JTextPane();
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
		// container.add(searchButton, BorderLayout.SOUTH);
		container.add(p, BorderLayout.SOUTH);
	}

	private void searchButton_actionPerformed() {
		outputField.setText("");
		resultPages.clear();
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

	public void sortAndShow() throws InterruptedException {
		// wait(2000);
		outputField.setText("");
		Collections.sort(resultPages, new Comparator<Article>() {

			public int compare(Article o1, Article o2) {
				return o1.getRank() - o2.getRank();
			}
		});

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
		outputField.setContentType("text/html");
		outputField.setEditable(false);
		outputField.setText(s);

	}

}
