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
		String argsv[] = {"-agents"
				, "bootStr" +":searcher.agents.bootstrapper.BootStrAgent"};
		jade.Boot.main(argsv);

	}

}
