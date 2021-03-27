package com.simulationmodelling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Inspector {
   private int inspectorId;
   private float lambda;
   private ArrayList<Float> procTimes;
   private Random r;
   private ArrayList<Buffer> buffers;

   public Inspector(String pathToData) throws FileNotFoundException{
       r = new Random();
       buffers = new ArrayList<>();
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


   private float calculateMean() {
        float sum = 0.0f;
        for (Float num: procTimes) {
            sum += num;
        }
        return sum / 300;
    }
   public void addBuffer(Buffer buffer){
       this.buffers.add(buffer);
   }


   /**
    * Puts a random component onto a buffer
    */
   public void putComponents(){
       while(true){
        Component temp = generateComponent(1234);
        for(Buffer b : this.buffers){
            if(b.getSize()==0){
                b.addComponent(temp);
                return;
            }
        }
        for(Buffer b : this.buffers){
            if(b.getSize()==1){
                b.addComponent(temp);
                return;
            }
        }
        try {
            this.wait();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       }
       
        //IF IT GETS HERE THEN ITS BLOCKED FROM PUTTING COMPONENTS
   }

   /**
    * Generates a random component
    * @param seed
    * @return
    */
   public Component generateComponent(long seed){
       r.setSeed(seed);
       int randomNumber = r.nextInt();
        randomNumber = randomNumber % 3;
        switch(randomNumber){
            case 0:
                return Component.C1;
            case 1:
                return Component.C2;
            case 2:
                return Component.C3;
            default:
                return generateComponent(seed);
        }
   }

public int getInspectorId() {
    return inspectorId;
}

public void setInspectorId(int inspectorId) {
    this.inspectorId = inspectorId;
}
}
