package searcher.agents.udbagent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import searcher.agents.orchestrator.OrchestratorAgent;
import searcher.agents.user.UserAgent;
import searcher.exceptions.InitAgentException;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class UserDataBaseAgentCyclicBehaviour extends CyclicBehaviour {

	UserDataBaseAgent agent; 
	
	@Override
	public void action() {
				
		ACLMessage msgINIT = agent.receive(MessageTemplate
				.MatchPerformative(UserAgent.INIT));
		try {

			if (msgINIT != null) {
				if (msgINIT.getContent().equals(OrchestratorAgent.INIT_USER)) {
					agent.setUserAID(msgINIT.getSender());
					System.out.println("UserDataBaseAgent receives msg1");
				}/*
				 * else if (msgINIT.getSender().equals(agent.getUserAgentAID()))
				 * { agent.setSearchers(msgINIT.getContent());
				 * System.out.println("OrchestratorAgent receives msg2"); }
				 */else {
					throw new InitAgentException();
				}
			}
		} catch (InitAgentException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		/**
		 * 
		 * Now this is just a working example how JAVA can communicate with MySQL Server Databases.
		 * 
		 * TODO: We must create a public DataBase for our User in the Internet
		 */
		
		
		/*
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(
			        "jdbc:mysql://localhost:3306/test_db",
			        "root", "Phoenix");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	        if (conn==null)
	        {
	            System.out.println("Нет соединения с БД!");
	            System.exit(0);
	        }
	 
	        Statement stmt = null;
			try {
				stmt = conn.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//stmt.executeUpdate("INSERT INTO user_data VALUES ('lolshto',3)");
	        ResultSet rs = stmt.executeQuery("SELECT * FROM user_data");
	 
	        while(rs.next())
	        {
	            System.out.println(rs.getRow() + ". " + rs.getString("article_name") + "\t" + rs.getString("use_quantity"));
	        }
	 
	        // if(rs!=null)rs.close();
	 
	        /**
	         * stmt.close();
	         * При закрытии Statement автоматически закрываются
	         * все связанные с ним открытые объекты ResultSet
	         */
	 
	        // if(stmt!=null)stmt.close();
	      //stmt.close(); 
	        
	}

	

}
