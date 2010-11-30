package searcher.agents.bootstrapper;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jade.core.behaviours.OneShotBehaviour;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class BootStrAgentFrame extends JFrame {

	private BootStrAgent bootStrAgent;
	private JButton createButton;

	public BootStrAgentFrame(final BootStrAgent bootStrAgent) {
		super();
		jfInit();
		this.setSize(150, 150);
		this.setTitle("BootStrAgent - " + bootStrAgent.getName());
		this.setVisible(true);

		this.bootStrAgent = bootStrAgent;
	}

	private void jfInit() {
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());
		createButton = new JButton("create");
		createButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				createButton_actionPerformed();
			}

		});
		container.add(createButton, BorderLayout.CENTER);

	}

	protected void createButton_actionPerformed() {
		bootStrAgent.addBehaviour(new OneShotBehaviour(bootStrAgent) {
			@Override
			public void action() {
				((BootStrAgent) myAgent).createAllAgents();
			}
		});

	}

}
