package kernel;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import CPU.Cpu;
import asembler.Assembler;
import fajlSistem.SekundarnaMemorija;
import fajlSistem.Stablo;
import memorija.Memory;
import proces.Process;

/* Klasa koja predstavlja kernel */

public class Kernel {
	
		private final static String[] komande = {"cd", "dir", "mkdir", "ps", "run","mem", "exit", "rm"};	// lista komandi
		private static Stablo fs;																			// fajl sistem
		private Scanner input;																				// ulaz
		
/* konstruktor koji prima stablo (fajl sistem) */
		
		public Kernel(Stablo fs) {
			this.input = new Scanner(System.in);
			Kernel.fs = fs;
			this.napraviListuProcesa();
			this.start();
		}
		
/* funkcija main:
 * 1. kreira sekundarnu memoriju od 4MB
 * 2. kreira fajl sistem za tu memoriju
 * 3. kreira cmd */
		
		public static void main(String[] args) {
			SekundarnaMemorija sm = new SekundarnaMemorija(4096);
			Stablo fs = new Stablo(sm);
			Kernel cmd = new Kernel(fs);
		}
		
/* Funkcija koja pokrece loop za upis komandi:
 * 1. dok go postoji uzima input (komandu)
 * 2. provjerava da li je komanda validna
 * 3. ako je komanda validna izvrsi komandu */
		
		public void start() {
			while(input.hasNextLine()) {
				String komanda = input.nextLine();
				if(this.validacijaKomande(komanda)) {
					this.izvrsiKomandu(komanda);
				}
			}
		}
		
/* Funkcija koja prepoznaje da li se komanda nalazi u listi komandi: 
 * 1. uzima komadu i pravi token (samo prva rijec komande se provjerava) 
 * 2. provjerava da li se token nalazi u listi komandi ako da vrati true ako ne ispise "Invalid command" */
		
		private boolean validacijaKomande(String komanda) {
			String token = komanda.split(" ")[0];
			for(int i = 0; i< Kernel.komande.length; i++) {
				if(token.equals(Kernel.komande[i])) {
					return true;
				}
			}
			System.out.println("Invalid command");
			return false;
		}
		
/* Funkcija koja definise ponasanje komandi:
 * skraceno: provjerava koja je komanda i na osnovu toga poziva odgovarajuce funkcije iz drugih klasa */
		
		private void izvrsiKomandu(String komanda) {
			String[] tokeni = komanda.split(" ");
			//cd
			if(tokeni[0].equals(komande[0]) && tokeni.length == 2) {
				fs.cd(tokeni[1]);
			}
			//dir
			else if(tokeni[0].equals(komande[1]) && tokeni.length == 1) {
				fs.dir();
			}
			//mkdir
			else if(tokeni[0].equals(komande[2])&& tokeni.length == 2) {
				fs.mkdir(tokeni[1]);
			}
			//ps
			else if(tokeni[0].equals(komande[3]) && tokeni.length == 1) {
				Cpu.ps();
			}
			//run
			else if(tokeni[0].equals(komande[4]) && (tokeni.length == 2 || tokeni.length == 3)) {
				File file=new File(tokeni[1]);
				if(file.exists()) {
					ArrayList<String> codeAndData = Assembler.convert(tokeni[1]);
					int index=tokeni[1].indexOf('.');
					String name=tokeni[1].substring(0,index)+".asm";
					if(tokeni.length == 3)
						new Process(codeAndData,name,tokeni[2]);
					else
						new Process(codeAndData,name,null);
				}else {
					System.out.println("Error! File '"+tokeni[1]+"' does not exist!");
				}
			}
			//exit
			else if(tokeni[0].equals(komande[6]) && tokeni.length == 1) {
				exit();
			}
			//mem
			else if(tokeni[0].equals(komande[5]) && tokeni.length == 1) {
				System.out.println(Memory.getTakenSize());
			}
			//rm
			else if(tokeni[0].equals(komande[7]) && tokeni.length == 2) {
				fs.deldir(tokeni[1]);
			}
			else {
				System.out.println("Error! Invalid parameters!");
			}	
		}

/* funkcija koja gasi OS */
		
		public static void exit() {
			System.out.println("Goodbye!");
			System.exit(0);
		}
		
/* funkcija koja napravi listu procesa i pokrene neke da se izvrsavaju */
		
		public void napraviListuProcesa() {
			String[] list= {"run pr1.txt","run pr2.txt res.txt","run pr3.txt res.txt","run pr4.txt res.txt","run pr5.txt res.txt","mem"};
			izvrsiKomandu(list[2]);
			izvrsiKomandu(list[4]);
			izvrsiKomandu(list[1]);
			izvrsiKomandu(list[0]);
			izvrsiKomandu(list[2]);
			izvrsiKomandu(list[4]);
			izvrsiKomandu(list[1]);
			izvrsiKomandu(list[1]);
			izvrsiKomandu(list[1]);
			izvrsiKomandu(list[5]);
		}
}
