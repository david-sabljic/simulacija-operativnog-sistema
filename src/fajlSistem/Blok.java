package fajlSistem;

/* Klasa koja predstavlja blok memorjie */

public class Blok {
	
	 	private final static int SIZE = 4;		// velicina bloka 
	    private final int adress;				// adresa bloka

/* konstruktor koji prima adresu */	    

	    public Blok(int adress) {
	        this.adress = adress;
	    }

/* geteri */
	    
	    public int getAdress() {
	        return adress;
	    }

	    public static int getSize() {
	        return SIZE;
	    }
}
