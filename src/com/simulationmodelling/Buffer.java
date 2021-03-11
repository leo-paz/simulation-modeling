package com.simulationmodelling;

import java.util.concurrent.ArrayBlockingQueue;

public class Buffer {
    private ArrayBlockingQueue<Component> queueBuffer;
    private final int MAX_CAPACITY = 3;

    public Buffer(){
        queueBuffer = new ArrayBlockingQueue<>(MAX_CAPACITY);
    }

    public ArrayBlockingQueue<Component> getBuffer(){
        return this.queueBuffer;
    }

    public void addElement(Component c){

    }
}
