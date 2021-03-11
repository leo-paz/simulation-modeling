package com.simulationmodelling;

import java.util.ArrayList;
import java.util.Random;

public class Inspector {
   private int inspectorId;
   private Random r;
   private ArrayList<Buffer> buffers;

   public Inspector(){
       r = new Random();
       buffers = new ArrayList<>();

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
