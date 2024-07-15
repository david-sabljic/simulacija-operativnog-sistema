package fajlSistem;

import java.util.ArrayList;
import java.util.Collections;

/*	Klasa koja predstavlja cvor u stablu */

public class Node {
	
	private String fileName; 				// naziv fajla
    private boolean dir;					// boolean koji govori da li je fajl ili folder
    private Datoteka datoteka;				// datoteka
    private Node parent;					// roditeljski cvor
    private ArrayList<Node> children;		// lista potomaka 
    
/* Konstruktor za FAJL. Baca OutOfMemoryError ako se datoteka neuspjesno napravi */
    
    public Node(String fileName, String sadrzajDatoteke, Node parent, SekundarnaMemorija sm) throws OutOfMemoryError {
        this.setParent(parent);
        this.fileName = fileName;
        this.dir = false;
        this.children = new ArrayList<>();
        this.datoteka = sm.kreirajDatoteku(sadrzajDatoteke);
        if(this.datoteka == null) {
            throw new OutOfMemoryError("Insufficient memory to create file.");
        }
    }
    
/* Konstruktor za FOLDER. Baca OutOfMemoryError ako se datoteka neuspjesno napravi */
    
    public Node(String fileName, Node parent, SekundarnaMemorija sm) throws OutOfMemoryError {
        this.setParent(parent);
        this.fileName = fileName;
        this.dir = true;
        this.children = new ArrayList<>();
        this.datoteka = sm.kreirajDatoteku("dir");
        if(this.datoteka == null) {
            throw new OutOfMemoryError("Insufficient memory to create file.");
        }
    }
    
/* preklapanje metode toString tako da ispisuje direktoijum npr: "ccc\\fff\\ddd\\..." */
    
    public String toString() {
        String rez = "";
        ArrayList<String> tempStringList = new ArrayList<>();
        Node tempCvor = this;
        do {
            tempStringList.add(tempCvor.getFileName());
            tempCvor = tempCvor.getParent();
        }while (tempCvor != null);
        Collections.reverse(tempStringList);
        for(String str: tempStringList) {
        	rez = rez + str + "\\";
        }
        return rez;
    }
    
/* seteri i geteri */
    
    public void setParent(Node parent) {
        this.parent = parent;
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isDir() {
		return dir;
	}

	public void setDir(boolean dir) {
		this.dir = dir;
	}

	public Datoteka getDatoteka() {
		return datoteka;
	}

	public void setDatoteka(Datoteka datoteka) {
		this.datoteka = datoteka;
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Node> children) {
		this.children = children;
	}

	public Node getParent() {
		return parent;
	}
    
}
