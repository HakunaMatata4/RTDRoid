
package edu.buffalo.rtdroid.util;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class ObjectPool<T>
{
    private Queue<T> queue;
    private int capacity;
    private Object lock;
    protected PriorityQueue<T> inPorgressQueue;
    /**
     * Creates the pool.
     * 
     * @param minIdle minimum number of objects residing in the pool
     */
    public ObjectPool(final int capacity, Comparator<T> comparator) {
        this.queue = new LinkedList<T>();
        this.inPorgressQueue = new PriorityQueue<T>(capacity, comparator);
        this.capacity = capacity;
        lock = new Object();
        initialize(capacity);
    }
    
    public ObjectPool(final int capacity) {
        this.queue = new LinkedList<T>();
        this.inPorgressQueue = new PriorityQueue<T>(capacity);
        this.capacity = capacity;
        lock = new Object();
        initialize(capacity);
    }

    public T requestObject() {
        synchronized (lock) {
            T object = queue.poll();
            if (object == null) {
            	dropMessage();
            }else{
                setObjectContext(object);
                inPorgressQueue.add(object);
            }

            return object;
        }
    }
    
    /**
     * Returns object back to the pool.
     * 
     * @param object object to be returned
     */
    public void recycleObject(T object) {
        synchronized (lock) {
            if (object == null) {
                throw new NullPointerException();
            } else {
                if(!queue.offer(object)){
                    System.out.println("queue inserts failure!!!!!");
                }
               inPorgressQueue.remove(object);
            }
        }
    }

    public int getTotalCapacity() {
        return this.capacity;
    }

    public int getRemainingCapacity() {
        return queue.size();
    }

    protected abstract void setObjectContext(T a);

    /**
     * Creates a new object.
     * 
     * @return T new object
     */
    protected abstract T createObject();
    protected abstract T dropMessage();

    private void initialize(final int capacity) {
        for (int i = 0; i < capacity; i++) {
            queue.add(createObject());
        }
    }
}
