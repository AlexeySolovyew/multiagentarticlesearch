package searcher.agents.user;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.tools.testagent.TestAgent;
import jade.util.leap.Collection;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import searcher.Article;

import examples.party.HostAgent;

public class UserAgentFrame extends JFrame {

	private UserAgent agent;

	// private JTextArea jTextArea;
	// private JTextField jTextField;
	private JButton searchButton;
	private JTextArea outputField;
	private JTextField inputField;
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
		outputField = new JTextArea();
		inputField = new JTextField();
		searchButton = new JButton("search");
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				searchButton_actionPerformed();
			}

		});

		container.add(outputField, BorderLayout.CENTER);
		container.add(inputField, BorderLayout.SOUTH);
		container.add(searchButton, BorderLayout.WEST);
	}

	private void searchButton_actionPerformed() {
		outputField.setText("");
		resultPages.clear();
		agent.addBehaviour( new OneShotBehaviour() {
            public void action() {
            	ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        		msg.setSender(agent.getAID());
        		msg.addReceiver(agent.getOrchestratorAID());
        		msg.setContent(inputField.getText());
        		msg.setLanguage("Plain English");
        		agent.send(msg);
            }
        } );
	}

	public void sortAndShow() throws InterruptedException {
		//wait(2000);
		outputField.setText("");
		Collections.sort(resultPages, new Comparator<Article>() {

			public int compare(Article o1, Article o2) {
				return o1.getRank()-o2.getRank();
			}
		});
		
		for (int i=0;i<resultPages.size();i++) {
			outputField.append("Title="+resultPages.get(i).getTitle()+"\n"
					+"URL="+resultPages.get(i).getURL()+"\n"
					+"Author="+resultPages.get(i).getAuthor()+"\n"+"\n");
		}

	}

}
