package searcher.agents.user;

import jade.core.AID;
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

public class UserAgentFrame extends JFrame {

	private UserAgent agent;

	// private JTextArea jTextArea;
	// private JTextField jTextField;
	private JButton searchButton;
	private JTextArea outputField;
	private JTextField inputField;
	private List<String> resultPages;

	public UserAgentFrame(UserAgent agent) {
		super();
		this.resultPages = new ArrayList<String>();
		this.agent = agent;
		this.setVisible(true);
		jfInit();
		this.setSize(600, 600);
		this.setTitle("UserAgent - " + agent.getName());

	}

	public void addArticleToFrame(String a) {
		resultPages.add(a);
		try {
			showAll();
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
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setSender(agent.getAID());
		msg.addReceiver(agent.getCourierAID());
		msg.setContent(inputField.getText());
		msg.setLanguage("Plain English");
		agent.send(msg);
		outputField.setText("");
		resultPages.clear();
	}

	public void showAll() throws InterruptedException {
		//wait(2000);
		outputField.setText("");
		Collections.sort(resultPages, new Comparator<String>() {

			public int compare(String o1, String o2) {

				String r1 = o1.substring(o1.lastIndexOf(" $$ "));
				String r2 = o2.substring(o2.lastIndexOf(" $$ "));
				int rmin = Math.min(r1.length(), r2.length());

				for (int i = 0; i < rmin; i++)
					if (r1.charAt(i) < r2.charAt(i)) {
						return 1;
					} else {
						if (r1.charAt(i) > r2.charAt(i)) {
							return -1;
						}
					}
				if (rmin == r1.length()) {
					return -1;
				}
				return 1;

			}
		});
		for (int i=0;i<resultPages.size();i++) {
			outputField.append(resultPages.get(i).substring(0,resultPages.get(i).lastIndexOf(" $$ ") )+"\n");
		}

	}

}
