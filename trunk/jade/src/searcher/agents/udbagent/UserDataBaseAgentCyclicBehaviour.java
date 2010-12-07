package searcher.agents.udbagent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jade.core.behaviours.CyclicBehaviour;

public class UserDataBaseAgentCyclicBehaviour extends CyclicBehaviour {

	@Override
	public void action() {
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
