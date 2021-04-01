package com.simulationmodelling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Inspector implements Runnable {
    private HashMap<Component, List<Workstation>> workstations;
    private HashMap<Component, Float> componentsLambdas = new HashMap<Component, Float>();
    private boolean doStop = false;
    private Integer iterations;
    private float blockedTime = 0;
    private double busyTime = 0;
    private int componentsInspected=0;

    public Inspector(HashMap<Component, String> components, HashMap<Component, List<Workstation>> workstations, Integer iterations) {
        this.workstations = workstations;
        this.iterations = iterations;
        
        for(Component c: components.keySet()) {
            componentsLambdas.put(c, calculateLambda(components.get(c)));
        }
    }

    private float calculateLambda(String pathToData) {
        ArrayList<Float> procTimes = new ArrayList<Float>();
        File file = new File(pathToData);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        while (scanner.hasNextFloat()) {
            try {
                procTimes.add(scanner.nextFloat());
            } catch (NumberFormatException e) {
                System.out.printf("Failed to read string, but continuing: %s\n", e.toString());
            }
        }
        scanner.close();
        return calculateMean(procTimes);
    }

    private Float calculateMean(ArrayList<Float> procTimes) {
        float sum = 0.0f;
        for (Float num : procTimes) {
            sum += num;
        }
        return sum / 300;
    }

    private Float getNextServiceTime(Component component) {
        var seed = new Random().nextInt();
        var rand = new Random(seed);
        var num = rand.nextFloat();
        System.out.println("Next service time");
        System.out.println((Math.log(1 - num) / (-componentsLambdas.get(component))));
        return (float) (Math.log(1 - num) / (-componentsLambdas.get(component)));
    }

    private Component getRandomComponent(){
        var seed = new Random().nextInt();
        Random rand = new Random(seed);
        return (Component) componentsLambdas.keySet().toArray()[rand.nextInt(componentsLambdas.keySet().size())];
    }
    
    @Override
    public void run() {
        Integer currentIterations = 0;
        while(currentIterations < iterations && keepRunning()) {
            Component component = getRandomComponent();

            try {

                var serviceTime = (getNextServiceTime(component));
                System.out.println("service time: " + serviceTime + " in minutes");
                Thread.sleep((long) (serviceTime * 1000));
                this.busyTime = busyTime + (serviceTime*1000);
                this.componentsInspected++;

                Workstation workstation = findBuffer(component);
                if(workstation == null) {
                    long blockedStartTime = System.currentTimeMillis();
                    while (workstation == null) {
                        //IF IT GETS HERE THEN ITS BLOCKED
                        workstation = findBuffer(component);
                    }
                    long blockedEndTime = System.currentTimeMillis();
                    blockedTime += (blockedEndTime - blockedStartTime);
                }

                workstation.put(component);
                currentIterations++;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public double getBusyTime() {
        return busyTime / 1000;
    }

    public int getComponentsInspected(){
        return this.componentsInspected;
    }

    private Workstation findBuffer(Component component){
        Workstation workstationBuffer = null;
        List<Workstation> workstationList = this.workstations.get(component);

        for (Workstation workstation: workstationList) {
            if((workstationBuffer == null && workstation.getBuffers().get(component) <2) || (workstationBuffer != null &&  workstation.getBuffers().get(component) < workstationBuffer.getBuffers().get(component))){
                workstationBuffer = workstation;
            }
        }
        return workstationBuffer;
    }

    public float getBlockedTime() {
        return blockedTime /1000;
    }

    public synchronized void shouldStop() {
        this.doStop = true;
    }

    private synchronized boolean keepRunning() {
        return !this.doStop;
    }

}