package searcher.agents.user;

import jade.core.behaviours.CyclicBehaviour;

public class RefreshFrameBehaviour extends CyclicBehaviour {

	private long timeLastUpdate;
	private long timeCurr;
	private UserAgentFrame uf;
	
	
	public RefreshFrameBehaviour(UserAgentFrame userAgentFrame) {
		this.uf = userAgentFrame;
		timeLastUpdate=System.currentTimeMillis();
	}
	
	@Override
	public void action() {
		
		
		timeCurr=System.currentTimeMillis();
		if (timeCurr-timeLastUpdate>15000){
			try {
				uf.showPages();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// TODO Auto-generated method stub

	}

}
