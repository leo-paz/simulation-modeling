package com.simulationmodelling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Random;

public abstract class Workstation implements Runnable{
    protected ArrayList<Buffer> buffers;
    protected ArrayList<Float> procTimes;
    public int iterations;
    protected int procIndex;
    private int ID;
    private float lambda;

    public Workstation(String pathToData, int iters, int id) throws FileNotFoundException {
        ID = id;
        buffers = new ArrayList<Buffer>();
        procTimes = new ArrayList<>();
        iterations = iters;
        procIndex = 0;
        File file = new File(pathToData);
        Scanner scanner = new Scanner(file);
        var max
        while (scanner.hasNextLine()) {
            try {
                procTimes.add(Float.parseFloat(scanner.nextLine()));
            } catch (NumberFormatException e) {
                System.out.printf("Failed to read string, but continuing: %s\n", e.toString());
            }
        }
        lambda = calculateMean();
    }

    @Override
    public void run(){
        for(int i = 0; i<iterations; i++){

            // TO DO: Create build products function
            //buildProduct();
        }
    }

    public void addBuffer(Buffer buffer) {
        buffers.add(buffer);
    }

    private float calculateMean() {
        float sum = 0.0f;
        for (Float num: procTimes) {
            sum += num;
        }
        return sum / 300;
    }

    public Float getNextServiceTime() {
        var rand = new Random();
        var num = rand.nextDouble();
        return (float) (Math.log(1 - num) / (-lambda));
    }

    public int getID() {
        return ID;
    }
}