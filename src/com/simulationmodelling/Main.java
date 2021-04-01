package com.simulationmodelling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main implements Runnable {

    private final Workstation ws1, ws2, ws3;
    private final Thread ws1Thread, ws2Thread, ws3Thread, insp1Thread, insp2Thread;
    private final Inspector insp1, insp2;
    private final int ITERATIONS = 100;

    public Main() {
        ws1 = new Workstation(Product.P1, "C:/Users/Beshr/Desktop/School/2021 Winter/4005/project/simulation-modeling/simulation-data/ws1.dat", ITERATIONS);
        ws2 = new Workstation(Product.P2, "C:/Users/Beshr/Desktop/School/2021 Winter/4005/project/simulation-modeling/simulation-data/ws2.dat", ITERATIONS);
        ws3 = new Workstation(Product.P3, "C:/Users/Beshr/Desktop/School/2021 Winter/4005/project/simulation-modeling/simulation-data/ws3.dat", ITERATIONS);

        ws1Thread = new Thread(ws1);
        ws2Thread = new Thread(ws2);
        ws3Thread = new Thread(ws3);

        HashMap<Component, String> insp1Data = new HashMap<>();
        insp1Data.put(Component.C1, "C:/Users/Beshr/Desktop/School/2021 Winter/4005/project/simulation-modeling/simulation-data/servinsp1.dat");

        HashMap<Component, List<Workstation>> insp1Workstation = new HashMap<>();
        insp1Workstation.put(Component.C1, Arrays.asList(ws1, ws2, ws3));
        
        insp1 = new Inspector(insp1Data, insp1Workstation, ITERATIONS);
        insp1Thread = new Thread(insp1);

        HashMap<Component, List<Workstation>> insp2Workstations = new HashMap<>();
        insp2Workstations.put(Component.C2, Collections.singletonList(ws2));
        insp2Workstations.put(Component.C3, Collections.singletonList(ws3));

        HashMap<Component, String> insp2Data = new HashMap<>();
        insp2Data.put(Component.C2, "C:/Users/Beshr/Desktop/School/2021 Winter/4005/project/simulation-modeling/simulation-data/servinsp22.dat");
        insp2Data.put(Component.C3, "C:/Users/Beshr/Desktop/School/2021 Winter/4005/project/simulation-modeling/simulation-data/servinsp23.dat");
        
        insp2 = new Inspector(insp2Data, insp2Workstations, ITERATIONS);
        insp2Thread = new Thread(insp2);
    }

    public static void main(String[] args) {
        Thread mainThread = new Thread(new Main());
        mainThread.start();
    }

    @Override
    public void run() {
        ws1Thread.start();
        ws2Thread.start();
        ws3Thread.start();
        insp1Thread.start();
        insp2Thread.start();

        long startTime = System.currentTimeMillis();
        while (insp1Thread.isAlive() && insp2Thread.isAlive()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        insp1.shouldStop();
        insp2.shouldStop();
        ws1.shouldStop();
        ws2.shouldStop();
        ws3.shouldStop();

        long endTime = System.currentTimeMillis();

        int productsProduced = (ws1.getProductsProduced() + ws2.getProductsProduced() + ws3.getProductsProduced());
        double totalTime = (endTime - startTime) / 1000D;
        System.out.println("\n\n\n<-_- Simulation Results -_->\n");

        System.out.println("Number of iterations: " + ITERATIONS+"\n");

        System.out.println("<-_- Workstation's production -_->");
        System.out.println("Workstation 1 Production: " + ws1.getProductsProduced());
        System.out.println("Workstation 2 Production: " + ws2.getProductsProduced());
        System.out.println("Workstation 3 Production: " + ws3.getProductsProduced()+"\n");

        System.out.println("<-_- Workstation's throughput -_->");
        System.out.println("Workstation 1 Throughput: " + ws1.getProductsProduced() / totalTime + " products/m");
        System.out.println("Workstation 2 Throughput: " + ws2.getProductsProduced() / totalTime + " products/m");
        System.out.println("Workstation 3 Throughput: " + ws3.getProductsProduced() / totalTime + " products/m\n");


        System.out.println("<-_- Total Components inspected -_->");
        System.out.println("Number of components inspected for Inspector 1: " + insp1.getComponentsInspected());
        System.out.println("Number of components inspected for Inspector 2: " + insp2.getComponentsInspected()+"/n");

        System.out.println("<-_- Inspectors busy time -_->");
        System.out.println("Busy time for Inspector 1: " + insp1.getBusyTime());
        System.out.println("Busy time for Inspector 2: " + insp2.getBusyTime() + "\n");

        
        System.out.println("<-_- Inspector's blocked times -_->");
        System.out.println("Inspector 1 Blocked Time: " + insp1.getBlockedTime() + "m");
        System.out.println("Inspector 2 Blocked Time: " + insp2.getBlockedTime() + "m\n");

        System.out.println("<-_- Simulation attributes -_->");
        System.out.println("Simulation duration: " + totalTime + "m");
        System.out.println("Total Production: " + productsProduced);
    }
}