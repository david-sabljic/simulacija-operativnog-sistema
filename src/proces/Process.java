package proces;

import java.util.ArrayList;
import java.util.Queue;

import memorija.Memory;

/* Klasa koja predstavlja proces */

public class Process {
	
	private static ArrayList<Process> processes = new ArrayList<>();	// lista svih procesa
	private static int counter = 0;										// ukupan broj procesa
	public ArrayList<String> codeAndData;  								// podaci 
	private int processId;												// id procesa
	private String state;												// stanje procesa
	private int size;													// velicina procesa
	private String name;												// naziv procesa

	private String outputFile;											// naziv fajla za ispis rezultata
	public String currentPC;											// Program Counter
	public String currentR1;											// Registar 1
	public String currentR2;											// Registar 2
	public String currentR3;											// Registar 3

	
/* konstruktor koji prima podatke procesa, naziv procesa i naziv izlaznog fajla */
	
	public Process(ArrayList<String> codeAndData, String name, String file) {
		counter++;
		this.processId = counter;
		this.state = "NEW";
		this.codeAndData = codeAndData;
		this.name = name;
		this.outputFile = file;
		size = codeAndData.size()*16;
		int length = Memory.powerOfTwo(Memory.getMemorySize());
		String firstInstruction="";
		for(int i=0; i<length; i++) {
			firstInstruction+="0";
		}
		this.currentPC = firstInstruction;
		this.currentR1 = "";
		this.currentR2 = "";
		this.currentR3 = "";
		processes.add(this);
		this.init();
	}
	
/* funkcija za ucitavanje procesa:
 * 1. ucita proces u memoriju 
 * 2. postavi stanje procesa na "READY"
 * 3. dodaje proces u ReadyQueue rasporedjivaca procesa
 * 4. ako nema aktivnog procesa poziva se metoda rasporedjivaca procesa schedule() */
	
		public void init() {
			Memory.loadProcess(this);
			this.setState("READY");
			ProcesScheduler.getReadyQueue().add(this);
			if(ProcesScheduler.getAktivniProces() == null) {
				ProcesScheduler.schedule();
			}
		}
		
/* funkcija za prekidanje procesa: 
 * 1. postavlja stanje procesa na "TERMINATED"
 * 2. brise proces iz memorije
 * 3. na rasporedjivacu procesa izbacuje trenutni proces i rasporedjuje novi proces */
		
		public void exit() {
			this.state = "TERMINATED";
			Memory.removeProcess(this);
			ProcesScheduler.removeRunningProcess();
			ProcesScheduler.schedule();
		}
		
/* funkcjia koja ispisuje sve procese koji su u stanjima "READY" i "RUNNING" u slucaju da nema procesa ispise odgovarajucu poruku */		
		
		public static void redayRunningList() {
			Queue<Process> readyProcesses = ProcesScheduler.getReadyQueue();
			Process runningProcess = ProcesScheduler.getAktivniProces();
			if(runningProcess == null && readyProcesses.isEmpty())
				System.out.println("There are no processes that are currently in ready or running state.");
			else {
				System.out.println("List of processes:");
				if(runningProcess != null) {
					System.out.println("\tPID: "+runningProcess.processId);
					System.out.println("\tName: "+runningProcess.name);
					System.out.println("\tState: "+runningProcess.state);
					System.out.println("\tSize: "+runningProcess.size);
				}
				if(!readyProcesses.isEmpty()) {
					for(Process proces : readyProcesses) {
						System.out.println("\tPID: "+ proces.processId);
						System.out.println("\tName: "+ proces.name);
						System.out.println("\tState: "+ proces.state);
						System.out.println("\tSize: "+ proces.size);
					}
				}
				System.out.println();
			}
		}
		
/* funkcija koja ispisuje sve procese */
		
		public static void allList() {
			System.out.println("List of processes:");
			for(Process proces : processes) {
					System.out.println("\tPID: " + proces.processId);
					System.out.println("\tName: " + proces.name);
					System.out.println("\tState: " + proces.state);
					System.out.println("\tSize: " + proces.size);
			}
			System.out.println();
		}
	
/* geteri i seteri */
		
		public static ArrayList<Process> getProcesses() {
			return processes;
		}

		public static void setProcesses(ArrayList<Process> processes) {
			Process.processes = processes;
		}

		public ArrayList<String> getCodeAndData() {
			return codeAndData;
		}

		public void setCodeAndData(ArrayList<String> codeAndData) {
			this.codeAndData = codeAndData;
		}

		public static int getCounter() {
			return counter;
		}

		public static void setCounter(int counter) {
			Process.counter = counter;
		}

		public int getProcessId() {
			return processId;
		}

		public void setProcessId(int processId) {
			this.processId = processId;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getOutputFile() {
			return outputFile;
		}

		public void setOutputFile(String outputFile) {
			this.outputFile = outputFile;
		}

		public String getCurrentPC() {
			return currentPC;
		}

		public void setCurrentPC(String currentPC) {
			this.currentPC = currentPC;
		}

		public String getCurrentR1() {
			return currentR1;
		}

		public void setCurrentR1(String currentR1) {
			this.currentR1 = currentR1;
		}

		public String getCurrentR2() {
			return currentR2;
		}

		public void setCurrentR2(String currentR2) {
			this.currentR2 = currentR2;
		}

		public String getCurrentR3() {
			return currentR3;
		}

		public void setCurrentR3(String currentR3) {
			this.currentR3 = currentR3;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public int getSize() {
			return this.size;
		}
}
