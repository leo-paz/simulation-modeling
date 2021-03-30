package com.simulationmodelling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Inspector implements Runnable {
    private HashMap<Component, List<Workstation>> workstations;
    private HashMap<Component, Float> componentsLambdas = new HashMap<Component, Float>();
    private boolean doStop = false;
    private Integer iterations;
    private Random r;
    private float blockedTime = 0;

    public Inspector(HashMap<Component, String> components, HashMap<Component, List<Workstation>> workstations, Integer iterations) {
        this.workstations = workstations;
        this.iterations = iterations;
        
        for(Component c: components.keySet()) {
            componentsLambdas.put(c, calculateLambda(components.get(c)));
        }
        r = new Random();
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
        var rand = new Random();
        var num = rand.nextFloat();
        return (float) (Math.log(1 - num) / (-componentsLambdas.get(component)));
    }

    private Component getRandomComponent(){
        Random rand = new Random();
        return (Component) componentsLambdas.keySet().toArray()[rand.nextInt(componentsLambdas.keySet().size())];
    }
    
    //     /**
    //  * Generates a random component
    //  * 
    //  * @param seed
    //  * @return
    //  */
    // public Component generateComponent(long seed) {
    //     r.setSeed(seed);
    //     int randomNumber = r.nextInt();
    //     randomNumber = randomNumber % 3;
    //     switch (randomNumber) {
    //     case 0:
    //         return Component.C1;
    //     case 1:
    //         return Component.C2;
    //     case 2:
    //         return Component.C3;
    //     default:
    //         return generateComponent(seed);
    //     }
    // }

    @Override
    public void run() {
        Integer currentIterations = 0;
        while(currentIterations < iterations && keepRunning()) {
            Component component = getRandomComponent();

            try {
                Thread.sleep((long) (getNextServiceTime(component).longValue() * 1000L)); //* 1000L));

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