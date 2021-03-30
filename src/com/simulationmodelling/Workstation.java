package com.simulationmodelling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Workstation implements Runnable {
    
    private float lambda;
    private boolean shouldStop = false;
    public int iterations;
    private int productsProduced = 0;
    private HashMap<Component, Integer> buffers = new HashMap<>();

    public Workstation(Product product, String pathToData, Integer iterations) {
        this.iterations = iterations;
        lambda = calculateLambda(pathToData);
        setBuffers(product);
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

    private Float getNextServiceTime() {
        var rand = new Random();
        var num = rand.nextFloat();
        return (float) (Math.log(1 - num) / (-lambda));
    }

    public HashMap<Component, Integer> getBuffers() {
        return buffers;
    }

    private void setBuffers(Product Product){
        switch (Product) {
            case P1:
                buffers.put(Component.C1, 0);
                break;
            case P2:
                buffers.put(Component.C1, 0);
                buffers.put(Component.C2, 0);
                break;
            case P3:
                buffers.put(Component.C1, 0);
                buffers.put(Component.C3, 0);
                break;
        }
    }

    public synchronized void put(Component Component){
        while (buffers.get(Component) == 2) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        buffers.put(Component,buffers.get(Component) +1);
        notifyAll();
    }

    @Override
    public void run() {
        Integer currentIterations = 0;
        while(currentIterations < iterations && keepRunning()){
            if(canMakeProduct()){
              makeProduct();
              currentIterations++;
            }
        }
    }

    private boolean canMakeProduct(){
        boolean makeProduct = true;
        for (Component component: buffers.keySet()) {
            if(buffers.get(component) ==0){
                makeProduct = false;
            }
        }
        return makeProduct;
    }

    private void makeProduct(){
        try {
            Thread.sleep((long) (getNextServiceTime().longValue() * 1000L));
            //  System.out.println("Produced: " + Product.name());
            productsProduced++;
            for (Component component: buffers.keySet()) {
                buffers.put(component,buffers.get(component)-1) ;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void shouldStop() {
        shouldStop = true;
    }

    private synchronized boolean keepRunning() {
        return !shouldStop;
    }

    public int getProductsProduced() {
        return productsProduced;
    }
}