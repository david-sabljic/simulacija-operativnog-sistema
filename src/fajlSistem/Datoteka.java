package fajlSistem;

import java.util.ArrayList;

/* Klasa koja predstavlja datoteku */

public class Datoteka {
	 	
		private int size; 										// velicina datoteke
	    private ArrayList<Blok> blokovi = new ArrayList<>();	// lista blokova

/* konstruktor koji prima velicinu datoteke */
	    
	    public Datoteka(int size) {
	        this.size = size;
	    }

/* geteri i seteri */	    
	    
	    public ArrayList<Blok> getBlokovi() {
	        return blokovi;
	    }

	    public void setBlokovi(ArrayList<Blok> blokovi) {
	        this.blokovi = blokovi;
	    }

	    public int getSize() {
	        return size;
	    }
}
