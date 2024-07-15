package memorija;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import proces.Process;

/* Klasa koja predstavlja RAM (primarnu memoriju) */

public class Memory {
	
	private static final int MEMORY_SIZE = 1024*1024*4; 						// velicina memorije 4MB
	private static List<Partition> holes = new ArrayList<Partition>();			// lista slobodnih particija
	private static List<Partition> allocations = new ArrayList<Partition>();	// lista zauzetih particija
	
/* konstruktor koji kreira RAM sacinjen od jedne prazne particije velicine 4MB */
	
	public Memory() {
		holes.add(new Partition(0,MEMORY_SIZE));
	}
	
/* funkicja za ucitavanje procesa u memoriju:
 * 1. prima proces koji treba dodati
 * 2. prolazi kroz sve particije i trazi particiju sa vecom velicinom od procesa
 * 3. pronadjenu particiju dijeli u nove particije (praticiju velicine procesa i particiju sa preostalom memorijom)
 * 4. upisuje proces u particiju i dodaje zauzetim particijama, particiju ostatka dodaje slobodnim particijama
 * 5. sortira slobodne particije	*/
	
	public static boolean loadProcess(Process process) {
		for(Partition p: holes) {
			if(p.getLimit() >= process.getSize()) {
				holes.remove(p);
				Partition p2 = new Partition(p.getLowerLimit(),p.getLowerLimit()+process.getSize());
				p2.setCurrent_process(process);
				allocations.add(p2);
				p.setLimit(p.getLimit()-process.getSize());
                p.setLowerLimit(p.getUpperLimit()-p.getLimit());
                p.setBase(p.getLowerLimit());
                if (p.getLimit()!=0) {
                    holes.add(p);
                    Collections.sort(holes);
                }
                return true;
			}
		}
		return false;
	}
	
/* funkcija za brisanje procesa iz memorije: 
 * 1. prima proces koji treba obrisati
 * 2. prolazi kroz listu zauzetih particija i provjerava da li je tu sacuvan proces koji se brise
 * 3. kada pronadje particiju postavi trenutni proces na null izbaci particiju iz liste zauzetih i ubaci u listu slobodnih particija
 * 4. sortira listu slobodnih particija
 * 5. kombinuje susjedne slobodne particije u jednu vecu particiju */	
	
	public static void removeProcess(Process process) {
		for(Partition p: allocations) {
			if(p.getCurrent_process() == process) {
				p.setCurrent_process(null);
				allocations.remove(p);
				holes.add(p);
				Collections.sort(holes);
                Combine(holes);
                break;
			}
		}
	}
	
/* funkcija za kombinovanje slobodnih particija:
 * 1. prolazi kroz sve slobodne particije
 * 2. provjerava da li su particije susjedne
 * 3. ako su susjedne izbacuje ih iz liste slobodnih particija
 * 4. kreira novu particiju kao kombinaciju izbacene dvije particije i dodaje u listu slobodnih particija
 * 5. sortira listu slobodnih particija */
	
	private static void Combine(List<Partition> holes) {
        for (int i = 0;i<holes.size();i++) {
            for (int j = 0;j<holes.size();j++) {
                if (holes.get(i).getUpperLimit() == holes.get(j).getLowerLimit()) {
                	Partition r1 = holes.get(i);
                	Partition r2 = holes.get(j);
                    holes.remove(r1);
                    holes.remove(r2);
                    Partition r = new Partition(r1.getLowerLimit(), r2.getUpperLimit());
                    holes.add(r);
                    Collections.sort(holes);
                    return;
                }
            }
        }
    }
	
/* funkcija za defragmentaciju memorije: 
 * 1. kreira novu particiju velicine 4MB
 * 2. isprazni listu slobodnih particija i doda novu particiju od 4MB
 * 3. pravi pomocnu listu u kojoj cuva sve particije iz liste zauzetih particija
 * 4. isprazni listu zauzetih particija
 * 5. za svaku particiju u pomocnoj listi ponovo ucita proces u memoriju */
	
	public void defragmentation() {
		Partition p = new Partition(0,MEMORY_SIZE);
		holes.removeAll(holes);
        holes.add(p);
        Partition[] partitions = new Partition[allocations.size()];
        int i = 0;
        for (Partition p2 : allocations) {
        	partitions[i++] = p2;
        }
        for(Partition p2: partitions) {
            	allocations.remove(p2);
            	loadProcess(p2.getCurrent_process());
           }
	}

/* seteri i geteri */
	
	public static List<Partition> getHoles() {
		return holes;
	}

	public static void setHoles(List<Partition> holes) {
		Memory.holes = holes;
	}

	public static List<Partition> getAllocations() {
		return allocations;
	}

	public static void setAllocations(List<Partition> allocations) {
		Memory.allocations = allocations;
	}

	public static int getMemorySize() {
		return MEMORY_SIZE;
	}
	
/* funkcija koja vraca kolicinu zauzete memorije */
	
	public static int getTakenSize() {
		int rez=0;
		for(Partition p : allocations) {
			rez+=p.getLimit();
		}
		return rez;
	}
	
/* pomocna funkcija stepen dvojke */	
	
	public static int powerOfTwo(int size) {
		int i=1;
		int counter=0;
		while(i<=size) {
			i*=2;
			counter++;
		}
		if (i/2 == size) {
			return --counter;
			}
		return -1;
	}
	
/*  pomocna funkcija iz decimalnog u binarni */	
	
	public static String decToBinary(int n){
		String binaryNumber="";
		int[] binaryNum = new int[1000];
		int i = 0;
		while (n > 0) {
			binaryNum[i] = n % 2;
			n = n / 2;
			i++;
		}
		for (int j = i - 1; j >= 0; j--) {
			binaryNumber+=String.valueOf(binaryNum[j]);
			}
		return binaryNumber;
	}
}
