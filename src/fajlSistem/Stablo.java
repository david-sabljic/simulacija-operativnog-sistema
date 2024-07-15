package fajlSistem;

/* Klasa koja predstavlja stablo */

public class Stablo {
	 	
		private Node root;					// korijen
	    private Node trenutniCvor;			// trenurni cvor
	    private SekundarnaMemorija sm;		//sekundarna memorija

/* Konstruktor koji prima sekundarnu memoriju i inicijalizuje fajl sistem  
 * Baca OutOfMemoryError ako nema dovoljno memorije za root (nikad) */
	    
	    public Stablo(SekundarnaMemorija sekmem) throws OutOfMemoryError {
	        this.sm = sekmem;
	        this.root = new Node("root", null, sm);
	        this.trenutniCvor = this.root;
	        System.out.println(root.toString());
	    }
	    
/* Funkcija za mijenjanje aktuelnog direktorijuma. Prelazi na poddirektorijum trenutnog direktorijuma ciji se naziv proslijedi kao dest,
 * ili u roditeljski direktorijum ako se proslijedi ".." */
	    
	    public void cd(String dest) {
	        if(dest.equals("..") && this.trenutniCvor.getParent() != null) {
	            this.trenutniCvor = this.trenutniCvor.getParent();
	        }
	        else if(!dest.equals("..")){
	            for(Node c: trenutniCvor.getChildren()) {
	                if(c.isDir() && c.getFileName().equals(dest)) {
	                    this.trenutniCvor = c;
	                }
	            }
	        }
	        System.out.println(this.trenutniCvor.toString());
	    }
	    
/* Funkcija za ispis svih fajlova i poddirektorijuma u trenutnom direktorijumu */
	    
	    public void dir() {
	        System.out.println(this.trenutniCvor.toString() + " :");
	        for(Node c: this.trenutniCvor.getChildren()) {
	            if(c.isDir()) {
	                System.out.print("Folder: ");
	            }
	            else {
	                System.out.print("File: ");
	            }
	            System.out.print(c.getFileName()+"\n");
	        }
	        System.out.println(this.trenutniCvor.toString());
	    }
	    
/* Funkcija za kreiranje novog poddirektorijuma u trenutnom direktorijumu:
 * 1. provjeri da li postoji folder/fajl sa istim imenom ako ima ispise "Unable to mkdir: Folder already exists"
 * 2. ako ne postoji napravi novi folder
 * 3. u slucaju nedostatka memorije ispise "Unable to mkdir: Insufficient memory" */
	    
	    public void mkdir(String naziv) {
	        for(Node c: this.trenutniCvor.getChildren())
	            if(c.getFileName().equals(naziv)) {
	                System.out.println("Unable to mkdir: Folder already exists");
	                System.out.println(this.trenutniCvor.toString());
	                return;
	            }
	        Node tempCvor;
	        try {
	            tempCvor = new Node(naziv, this.trenutniCvor, this.sm);
	            this.trenutniCvor.getChildren().add(tempCvor);
	        } catch (OutOfMemoryError e) {
	            System.out.println("Unable to mkdir: Insufficient memory");
	        }
	        System.out.println(this.trenutniCvor.toString());
	    }
	    
/* Funkcija za brisanje poddirektorijuma trenutnog direktorijuma
 * 1. prvo provjerava da li je folder ili fajl ako je fajl ispise "Unable to deldir: Is not a folder" 
 * 2. ako je folder onda poziva pomocnu funkcjiju za oslobadjanje memorije i uklanja folder iz liste potomaka roditeljskog foldera (brise folder) */
	    
	    public synchronized void deldir(String naziv) {
	    	Node cZaUkloniti = null;
	        for(Node c: this.trenutniCvor.getChildren()) {
	            if(c.getFileName().equals(naziv)) {
	                if(!c.isDir()) {
	                    System.out.println("Unable to deldir: Is not a folder");
	                    System.out.println(this.trenutniCvor.toString());
	                    return;
	                }else {
	                    cZaUkloniti = c;
	                    break;
	                }
	            }
	        }
	        if(cZaUkloniti != null) {
	            this.deldirOslobodiMemoriju(cZaUkloniti);
	            trenutniCvor.getChildren().remove(cZaUkloniti);
	        }
	        System.out.println(this.trenutniCvor.toString());
	    }
	    
/* Pomocna funkcija za deldir koja oslobadja memoriju:
 * 1. brise datote kao i sve poddirektorijume zadanog direktorijuma */
	    
	    private void deldirOslobodiMemoriju(Node c) {
	        this.sm.brisanjeDatoteke(c.getDatoteka());
	        for(Node c1: c.getChildren()) {
	            this.deldirOslobodiMemoriju(c1);
	        }
	    }
	    
/* Funkcija za preimenovanje poddirektorijuma :
 * 1. provjerava da li je folder ako nije ispisuje "Unable to rnmdir: Is not a folder."
 * 2. u slucaju da ne pronadje folder ispise "Unable to rnmdir: File not found."
 * 3. ako pronadje folder obrise ga */
	    
	    public void rnmdir(String naziv, String noviNaziv) {
	        for(Node c: this.trenutniCvor.getChildren())
	            if(c.getFileName().equals(naziv)) {
	                if(!c.isDir()) {
	                    System.out.println("Unable to rnmdir: Is not a folder.");
	                    System.out.println(this.trenutniCvor.toString());
	                    return;
	                }
	                c.setFileName(noviNaziv);
	                System.out.println(this.trenutniCvor.toString());
	                return;
	            }
	        System.out.println("Unable to rnmdir: File not found.");
	        System.out.println(this.trenutniCvor.toString());
	    }
	    
/* Funkcija za kreiranje novog fajla u trenutnom direktorijumu sa odredjenim sadrzajem:
 * 1. ako postoji fajl sa istim nazivom ispise "Unable to mkfile: File already exists"
 * 2. ako ne postoji kreira novi fajl dodaje ga na listu potomaka direktorijuma...
 * 3. u slucaju nedostatka memorije ispisuje "Unable to mkfile: Insufficient memory"  */
	    
	    public void mkfile(String naziv, String sadrzaj) {
	        for(Node c: this.trenutniCvor.getChildren())
	            if(c.getFileName().equals(naziv)) {
	                System.out.println("Unable to mkfile: File already exists");
	                System.out.println(this.trenutniCvor.toString());
	                return;
	            }
	        Node tempCvor;
	        try {
	            tempCvor = new Node(naziv, sadrzaj, this.trenutniCvor, this.sm);
	            this.trenutniCvor.getChildren().add(tempCvor);
	        } catch (OutOfMemoryError e) {
	            System.out.println("Unable to mkfile: Insufficient memory");
	        }
	        System.out.println(this.trenutniCvor.toString());
	    }
	    
/* Funkcija koja vraca sadrzaj fajla u trenutnom direktorijumu, 
 * null ("Unable to rdfile: File not found") ako ne postoji ili ako je folder ("Unable to rdfile: Is not a file") */
	    
	    public String rdfile(String naziv) {
	        for(Node c: this.trenutniCvor.getChildren())
	            if(c.getFileName().equals(naziv))
	                if(c.isDir()) {
	                    System.out.println("Unable to rdfile: Is not a file");
	                    System.out.println(this.trenutniCvor.toString());
	                    return null;
	                }else {
	                    System.out.println(this.trenutniCvor.toString());
	                    return this.sm.sadrzajDatoteke(c.getDatoteka());
	                }
	        System.out.println("Unable to rdfile: File not found");
	        System.out.println(this.trenutniCvor.toString());
	        return null;
	    }
	    
/* Funkcija za brisanje fajla u trenutnom direktorijumu 
 * ("Unable to delfile: File not found") ako ne postoji ili ako je folder ("Unable to delfile: Is not a file") */
	    public synchronized void delfile(String naziv) {
	        Node cZaUkloniti = null;
	        for(Node c: this.trenutniCvor.getChildren())
	            if(c.getFileName().equals(naziv))
	                if(c.isDir()) {
	                    System.out.println("Unable to delfile: Is not a file");
	                    System.out.println(this.trenutniCvor.toString());
	                    return;
	                }else {
	                    cZaUkloniti = c;
	                    break;
	                }
	        if(cZaUkloniti != null) {
	            this.sm.brisanjeDatoteke(cZaUkloniti.getDatoteka());
	            this.trenutniCvor.getChildren().remove(cZaUkloniti);
	            System.out.println(this.trenutniCvor.toString());
	            return;
	        }
	        System.out.println("Unable to delfile: File not found");
	        System.out.println(this.trenutniCvor.toString());
	    }

}
