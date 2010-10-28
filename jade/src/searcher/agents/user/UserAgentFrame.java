package searcher.agents.user;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.tools.gui.ACLPanel;
import jade.tools.gui.ACLTextArea;
import jade.tools.gui.ACLTextField;
import jade.tools.gui.ACLTracePanel;
import jade.tools.testagent.TestAgent;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class UserAgentFrame extends JFrame {

	private UserAgent agent;

	//private JTextArea jTextArea;
	//private JTextField jTextField;
	private JButton searchButton;
	private JTextArea outputField;
	private JTextField inputField;


	public UserAgentFrame(UserAgent agent) {
		super();
		this.agent = agent;
		jfInit();
		this.setSize(600, 600);
		this.setTitle("UserAgent - " + agent.getName());
		this.setVisible(true);

	}

	private void jfInit() {
		// aclTreePanel = new ACLTracePanel(agent);
		// this.setFrameIcon("images/dummy.gif");
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());
		outputField = new JTextArea();
		inputField = new JTextField();
		searchButton = new JButton("search");
		searchButton.addActionListener(
				new ActionListener() {
					
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
	   // msg.setOntology("World Domination");
	    agent.send(msg);
	    //outputField.append(msg.getContent());
		
		
		/*ACLMessage aclMsg = new ACLMessage();
		aclMsg.setSender(agent.getAID());
		
		
		
		agent.postMessage(aclMsg);
		sendMsg(jTextField.getText());
		jTextField.setText("");
		inputField.ac*/
	}

	public void writeMSG(ACLMessage msg) {
	    outputField.append(msg.getContent());
		
	}
	
}
