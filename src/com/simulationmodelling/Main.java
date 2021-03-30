package com.simulationmodelling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main implements Runnable {

    private final Workstation ws1, ws2, ws3;
    private final Thread ws1Thread, ws2Thread, ws3Thread, inspector1Thread, inspector2Thread;
    private final Inspector inspector1, inspector2;

    public Main() {
        ws1 = new Workstation(Product.P1, "C:/Users/Beshr/Desktop/School/2021 Winter/4005/project/simulation-modeling/simulation-data/ws1.dat", 10000);
        ws2 = new Workstation(Product.P2, "C:/Users/Beshr/Desktop/School/2021 Winter/4005/project/simulation-modeling/simulation-data/ws2.dat", 10000);
        ws3 = new Workstation(Product.P3, "C:/Users/Beshr/Desktop/School/2021 Winter/4005/project/simulation-modeling/simulation-data/ws3.dat", 10000);

        ws1Thread = new Thread(ws1);
        ws2Thread = new Thread(ws2);
        ws3Thread = new Thread(ws3);

        HashMap<Component, String> inspector1Data = new HashMap<>();
        inspector1Data.put(Component.C1, "C:/Users/Beshr/Desktop/School/2021 Winter/4005/project/simulation-modeling/simulation-data/servinsp1.dat");

        HashMap<Component, List<Workstation>> inspector1Workstation = new HashMap<>();
        inspector1Workstation.put(Component.C1, Arrays.asList(ws1, ws2, ws3));
        
        inspector1 = new Inspector(inspector1Data, inspector1Workstation, 10000);
        inspector1Thread = new Thread(inspector1);

        HashMap<Component, List<Workstation>> inspector2Workstation = new HashMap<>();
        inspector2Workstation.put(Component.C2, Collections.singletonList(ws2));
        inspector2Workstation.put(Component.C3, Collections.singletonList(ws3));

        HashMap<Component, String> inspector2Data = new HashMap<>();
        inspector2Data.put(Component.C2, "C:/Users/Beshr/Desktop/School/2021 Winter/4005/project/simulation-modeling/simulation-data/servinsp22.dat");
        inspector2Data.put(Component.C3, "C:/Users/Beshr/Desktop/School/2021 Winter/4005/project/simulation-modeling/simulation-data/servinsp23.dat");
        
        inspector2 = new Inspector(inspector2Data, inspector2Workstation, 10000);
        inspector2Thread = new Thread(inspector2);
    }

    public static void main(String[] args) {
        Thread mainThread = new Thread(new Main());
        mainThread.start();
    }

    private static List<Float> randomVariateGeneration(List<Float> input) {
        Random rand = new Random();
        List<Float> rng = new ArrayList<>();
        double average = input.stream().mapToDouble(a -> a).average().orElse(0.0);
        for (int i = 0; i < 300; i++) {
            rng.add((float) ((-1 * average) * (Math.log(1 - rand.nextFloat()))));
        }
        return rng;
    }

    private static List<Float> readData(File file) {
        Scanner scnr = null;
        List<Float> data = new ArrayList<>();

        try {
            scnr = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scnr.hasNextFloat()) {
            Float time = scnr.nextFloat();
            data.add(time);
        }
        return randomVariateGeneration(data);
    }

    @Override
    public void run() {
        ws1Thread.start();
        ws2Thread.start();
        ws3Thread.start();
        inspector1Thread.start();
        inspector2Thread.start();

        long startTime = System.currentTimeMillis();
        while (inspector1Thread.isAlive() && inspector2Thread.isAlive()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        inspector1.shouldStop();
        inspector2.shouldStop();
        ws1.shouldStop();
        ws2.shouldStop();
        ws3.shouldStop();

        long endTime = System.currentTimeMillis();

        int productsProduced = (ws1.getProductsProduced() + ws2.getProductsProduced() + ws3.getProductsProduced());
        double totalTime = (endTime - startTime) / 1000D;
        System.out.println("-------Simulation Complete----------");
        System.out.println("Simulation duration: " + totalTime + "s");
        System.out.println("Inspector 1 Blocked Time: " + inspector1.getBlockedTime() + "s");
        System.out.println("Inspector 2 Blocked Time: " + inspector2.getBlockedTime() + "s");

        System.out.println("Workstation 1 Production: " + ws1.getProductsProduced());
        System.out.println("Workstation 2 Production: " + ws2.getProductsProduced());
        System.out.println("Workstation 3 Production: " + ws3.getProductsProduced());

        System.out.println("Workstation 1 Throughput: " + ws1.getProductsProduced() / totalTime + " products/s");
        System.out.println("Workstation 2 Throughput: " + ws2.getProductsProduced() / totalTime + " products/s");
        System.out.println("Workstation 3 Throughput: " + ws3.getProductsProduced() / totalTime + " products/s");

        System.out.println("Total Production: " + productsProduced);
    }
}