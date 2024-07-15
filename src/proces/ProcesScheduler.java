package proces;

import CPU.ThreadHandler;

import java.util.LinkedList;
import java.util.Queue;

/* Klasa koja predstavlja rasporedjivac procesa FCFS */

public class ProcesScheduler {

    private static Queue<Process> readyQueue = new LinkedList<>();		// queue spremnih ("READY") procesa
    private static Process aktivniProces = null;						// trenutno aktivan proces

    
/* funkcija koja rasporedjuje procese:
 * 1. provjerava da li postoji spreman proces u queue i da li je neki proces trenutno aktivan
 * 2. ako postoji spreman proces a ni jedan proces nije aktivan, izbaci proces iz queue spremmih, postavi ga kao aktivnog i postavi stanje procesa na "RUNNING"
 * 3. pravi ThreadHandler za proces */
    
    public static synchronized void schedule() {
        if(!readyQueue.isEmpty() && aktivniProces == null) {
                Process proces = readyQueue.remove();
                aktivniProces = proces;
                proces.setState("RUNNING");
                new ThreadHandler(proces);
        }
    }

/* geteri i seteri */    
    
    public static Queue<Process> getReadyQueue(){
        return readyQueue;
    }

    public static Process getAktivniProces(){
        return aktivniProces;
    }
    
    public static void removeRunningProcess() {
        aktivniProces=null;
    }
    
    public static void setAktivniProces(Process proces){
        aktivniProces = proces;
    }


}