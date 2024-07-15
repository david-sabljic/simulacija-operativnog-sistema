package boot;

import fajlSistem.Stablo;
import fajlSistem.SekundarnaMemorija;
import kernel.Kernel;
import memorija.Memory;

/*  Klasa za pokretanje svih elemenata operativnog sistema  */

public class Boot {

	public static void main(String[] args) {
		boot();
	}
	
/* funkcija koja pokrece operativni sistem
 * kreira sekundarnu memoriju od 4MB
 * kreira fajl sistem i kernel i RAM od 4 MB */
	
	public static void boot() {
		SekundarnaMemorija sm = new SekundarnaMemorija(4096);
		Stablo fs = new Stablo(sm);
		Kernel kernel = new Kernel(fs);
		//new Memory();
	}
}
