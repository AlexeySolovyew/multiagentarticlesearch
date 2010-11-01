package searcher;

import searcher.agents.user.UserAgent;

public class SEBoot {

	public SEBoot() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String argsv[] = {"-agents", UserAgent.getAddressName()+":searcher.agents.user.UserAgent"};
	    jade.Boot.main(argsv);

	}

}
