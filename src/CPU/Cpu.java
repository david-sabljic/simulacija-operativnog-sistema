package CPU;

import java.io.FileWriter;
import memorija.Memory;
import proces.Process;

/* Klasa koja predstavlja procesor */

public class Cpu {
	
	private static Register IR = new Register();				// instruction registar
    public static Register PC = new Register();             	// program counter
    public static Register R1 = new Register("R1", "10");		// registar R1
    public static Register R2 = new Register("R2", "11");		// registar R2
    public static Register R3 = new Register("R3", "01");		// regisrar R3
    private static Process currentProcess;						// trenutni proces

/* Funkcija za izvrsavanje procesa */
    
    public static void execute(Process process) {
        System.out.println("Process "+ process.getProcessId() +" started its execution.");
        Cpu.currentProcess = process;
        PC.setValue(process.currentPC);
        R1.setValue(process.currentR1);
        R2.setValue(process.currentR2);
        R3.setValue(process.currentR3);
        for(String instrukcija : currentProcess.getCodeAndData()) {
        	IR.setValue(instrukcija);
            PC.increment();
        	executeInstruction(IR.getValue());
            //ako smo dosli do kraja koda gdje su naredbe, iza je halt i onda data, pa nam to ne treba
            if(IR.getValue().substring(0,4).equals("0000"))
            	break;
          	}
    }
    
/* pomocna funckija za izvrsavanje instrukcije procesa */
    
    public static void executeInstruction(String instruction) {
       String opCode = instruction.substring(0,4);
       /* proces gotov */     
       if (opCode.equals("0000")) {
                try {
                     Thread.sleep(4000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("The result of process " + currentProcess.getProcessId() + ": " + Integer.parseInt(R1.getValue(), 2));
                if (currentProcess.getName() != null) {
                    writeToFile();
                }
                System.out.println("Process " + currentProcess.getProcessId() + " has completed its execution.");
                System.out.println();
                currentProcess.exit();
            } 
       /* LOAD */  
       else if (opCode.equals("0010")) {
                String register = instruction.substring(4, 6);
                String memoryLocation = instruction.substring(6);
                int length = Memory.powerOfTwo(Memory.getMemorySize());
                String dataLocation = "";
                for (int i = 0; i < length - memoryLocation.length(); i++) {
                    dataLocation += "0";
                }
                dataLocation += memoryLocation;
                String offsetForData = dataLocation.substring(4, 8);
                String bitoviOffset[] = offsetForData.split("");
                int offset = 0;
                if (Integer.parseInt(bitoviOffset[0]) == 1) {
                	offset += 8;
                }
                if (Integer.parseInt(bitoviOffset[1]) == 1) {
                	offset += 4;
                }
                if (Integer.parseInt(bitoviOffset[2]) == 1) {
                	offset += 2;
                }
                if (Integer.parseInt(bitoviOffset[3]) == 1) {
                	offset += 1;
                }
                String data = currentProcess.codeAndData.get(offset);
                if (register.equals(R1.getAddress())) {
                	R1.setValue(data);
                }
                else if (register.equals(R2.getAddress())) {
                	R2.setValue(data);
                }
                else {
                	R3.setValue(data);
                }
            }
       /* ADD  SUB  MUL  DIV */
       else if (opCode.equals("0100") || opCode.equals("0101") || opCode.equals("0110") || opCode.equals("0111")) {
                String register1 = instruction.substring(8, 10);
                String register2 = instruction.substring(14);
                String data1 = "";
                String data2 = "";
                if (register1.equals(R1.getAddress())) {
                	data1 = R1.getValue();
                }
                else if (register1.equals(R2.getAddress())) {
                	data1 = R2.getValue();
                }
                else {
                	data1 = R3.getValue();
                }
                if (register2.equals(R1.getAddress())){
                	data2 = R1.getValue();
                }
                else if (register2.equals(R2.getAddress())){
                	data2 = R2.getValue();
                }
                else{
                	data2 = R3.getValue();
                }
                int dataNumber1 = Integer.parseInt(data1, 2);
                int dataNumber2 = Integer.parseInt(data2, 2);
                int result = 0;
                if (opCode.equals("0100")){
                	result = dataNumber1 + dataNumber2;
                }
                else if (opCode.equals("0101")){
                	result = dataNumber1 - dataNumber2;
                }
                else if (opCode.equals("0110")) {
                	result = dataNumber1 * dataNumber2;
                }
                else{
                	result = dataNumber1 / dataNumber2;
                }
                String binaryNumber = "";
                if (result == 0) {
                	binaryNumber = "0";
                }
                else{
                	binaryNumber = Memory.decToBinary(result);
                }
                if (register1.equals(R1.getAddress())){
                	R1.setValue(binaryNumber);
                }
                else{
                	R2.setValue(binaryNumber);
                }
       }
    }
    
/* funkcija koja isprazni sve registre */
    
    public static void setToZero() {
        R1.setValue("");
        R2.setValue("");
        R3.setValue("");
        PC.setValue("0");
        IR.setValue("");
    }
    
/* funkcija koja upisuje vrijednost R1 registra u fajl */
    
    public static void writeToFile(){
        String result=R1.getValue();
        String file=currentProcess.getName();
        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(String.valueOf(Integer.parseInt(result,2)));
            myWriter.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

/* funkcija za ispis stanja na registrima */
    
    public static void print() {
        System.out.println("State of memory and registers:");
        System.out.println();
        System.out.println("IR: "+IR.getValue());
        System.out.println("PC: "+PC.getValue());
        System.out.println("R1: "+R1.getValue());
        System.out.println("R2: "+R2.getValue());
        System.out.println("R3: "+R3.getValue());
    }
    
/* funkcija za ispis stanja trenutnog procesa i registara */
    
    public static void ps() {
        System.out.println("State of process:");
        System.out.println();
        System.out.println("Process: "+currentProcess.getName());
        System.out.println("Size: " + currentProcess.getSize());
        System.out.println("IR: "+IR.getValue());
        System.out.println("PC: "+PC.getValue());
        System.out.println("R1: "+R1.getValue());
        System.out.println("R2: "+R2.getValue());
        System.out.println("R3: "+R3.getValue());
        System.out.println();
    }
}
