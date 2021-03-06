package com.simulationmodelling;

import java.util.concurrent.ArrayBlockingQueue;

public class Buffer {
    private Workstation bufferOwner;

    private Inspector bufferInspector;
    private ArrayBlockingQueue<Component> queueBuffer;
    private Component componentType;
    private final int MAX_CAPACITY = 2;

    public Buffer(Workstation bufferOwner, Inspector bufferInspector, Component type){
        this.bufferOwner = bufferOwner;
        this.bufferInspector = bufferInspector;
        this.componentType = type;
        queueBuffer = new ArrayBlockingQueue<>(MAX_CAPACITY);
    }

    public ArrayBlockingQueue<Component> getBuffer(){
        return this.queueBuffer;
    }

    public Workstation getBufferOwner(){
        return this.bufferOwner;
    }

    public boolean isFull(){
        return (this.queueBuffer.size() < this.MAX_CAPACITY);
    }

    public Component getType(){
        return this.componentType;
    }

    public int getSize(){
        return this.queueBuffer.size();
    }


    public void addComponent(Component c){
        try {
            this.queueBuffer.put(c);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void removeComponent(){
        try {
            this.queueBuffer.take();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setBufferOwner(Workstation bufferOwner) {
        this.bufferOwner = bufferOwner;
    }

    public Inspector getBufferInspector() {
        return bufferInspector;
    }

    public void setBufferInspector(Inspector bufferInspector) {
        this.bufferInspector = bufferInspector;
    }
}
