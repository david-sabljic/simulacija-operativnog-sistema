package CPU;

import proces.Process;

/* klasa za rad sa nitima */

public class ThreadHandler extends Thread {
    
	Process proces;		// proces

/* konstruktor */
	
    public ThreadHandler(Process proces) {
        this.proces = proces;
        this.start();

    }
    
/* funkcjia koja pokrece proces */
    
    public void run(){
            Cpu.execute(proces);
    }
}