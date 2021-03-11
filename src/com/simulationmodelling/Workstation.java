package com.simulationmodelling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public abstract class Workstation implements Runnable{
    protected ArrayList buffers;
    protected ArrayList<Float> procTimes;
    public static int ITERATIONS;
    protected int procIndex;

    public Workstation(String pathToData, int iterations, ArrayList<Buffer> bufs) throws FileNotFoundException {
        buffers = bufs;
        procTimes = new ArrayList<>();
        ITERATIONS = iterations;
        procIndex = 0;
        File file = new File(pathToData);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            try {
                procTimes.add(Float.parseFloat(scanner.nextLine()));
            } catch (NumberFormatException e) {
                System.out.printf("Failed to read string, but continuing: %s\n", e.toString());
            }
        }

    }

    @Override
    public void run(){
        for(int i = 0; i<ITERATIONS; i++){

            // TO DO: Create build products function
            //buildProduct();
        }
    }

//    public Component getNextC1(){
//        Component1 c1 = (Component1) getc1Buffer().getNextComponent();
//        System.out.println("Worstation " + this.getNum() + " got component 1");
//        return c1;
//    }

    public Float getNextProcTime(){
        Float nextProcTime;
        if (procIndex < procTimes.size()) {
            nextProcTime = procTimes.get(procIndex);
            procIndex++;
        } else {
            nextProcTime = procTimes.get(0);
            procIndex = 1;
        }
        return nextProcTime;
    }

    public abstract int getNum();
}