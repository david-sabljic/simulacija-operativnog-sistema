package memorija;

import proces.Process;

/* Klasa koja predstavlja particiju u RAM memoriji */

public class Partition implements Comparable<Partition>{

	private int lowerLimit;  			// pocetni indeks
	private int upperLimit;	 			// krajnji indeks
	private int base;		 			// baza (mozda cak i nepotrbno)
	private int limit;		 			// velicina particije
    private Process current_process;  	// trenutni proces sacuvan u particiji
	
/* konstruktor koji prima pocetni i krajnji indeks (adresu) particije u RAM-u */
    
	public Partition(int lowerLimit, int upperLimit) {
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.base = lowerLimit;
		this.limit = upperLimit-lowerLimit;
	}

/* geteri i seteri */
	
	public int getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(int lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public int getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(int upperLimit) {
		this.upperLimit = upperLimit;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Process getCurrent_process() {
		return current_process;
	}

	public void setCurrent_process(Process current_process) {
		this.current_process = current_process;
	}

/* compareTo poredi particije po nizem indeksu (adresi) */
	
	@Override
	public int compareTo(Partition p) {
		return this.lowerLimit - p.lowerLimit;
	}
	

}
