package CPU;

import memorija.Memory;

/* Klasa koja predstavlja registar */

public class Register {
	
	private String value;		// vrijednost upisana u registr
    private String address;		// adresa regista
    private String name;		// naziv registra
    
/* konstruktor za registar koji prima naziv i adresu */
    public Register(String name, String address) {
        this.address = address;
        this.name = name;
    }
    
/* default konstruktor */    
    public Register() {}

/* geteri i seteri */
    
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
/* funkcija za inkrementovanje vrijednosti registra */
	
	 public void increment() {
	        int number = Integer.parseInt(value,2);
	        number++;
	        String binaryNumber = Register.decToBinary(number+16);
	        String newBinary = "";
	        for(int i = 0; i < Register.powerOfTwo(Memory.getMemorySize()) - binaryNumber.length(); i++)
	            newBinary += "0";
	        newBinary += binaryNumber;
	        value = newBinary;
	    }

/* pomocna funkcija za stepen dvojke */	 
	 
	 public static int powerOfTwo(int size) {
			int i=1;
			int counter=0;
			while(i<=size) {
				i*=2;
				counter++;
			}
			if (i/2 == size)
				return --counter;
			return -1;
		}
	 
/* pomocna funkcija za prevod decimalni u binarni  */
	 
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
