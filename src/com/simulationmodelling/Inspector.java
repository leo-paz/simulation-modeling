package com.simulationmodelling;

import java.util.Random;

public class Inspector {
   private int inspectorId;
   private Random r;
   public Inspector(){
       r = new Random();

   }

   /**
    * Puts a random component onto a buffer
    */
   public void putComponents(){

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
}
