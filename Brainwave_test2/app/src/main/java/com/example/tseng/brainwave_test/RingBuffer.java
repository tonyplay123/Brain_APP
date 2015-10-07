package com.example.tseng.brainwave_test;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Tseng on 2015/9/25.
 */
@SuppressWarnings("unchecked")
public class RingBuffer<Item> implements Iterable<Item>
{
    private Item[] buffer;          // queue elements
    private int count = 0;          // number of elements on queue
    private int indexOut = 0;       // index of first element of queue
    private int indexIn  = 0;       // index of next available slot

    // cast needed since no generic array creation in Java
    public RingBuffer(int capacity)
    {
        buffer = (Item[]) new Object[capacity];
    }

    public boolean isEmpty() { return count == 0; }
    public int size()        { return count;      }

    public void push(Item item)
    {
        if (count == buffer.length)
        { //throw new RuntimeException("Ring buffer overflow");
            pop();//滿了直接丟掉一個
         }
        buffer[indexIn] = item;
        indexIn = (indexIn + 1) % buffer.length;     // wrap-around
        count++;
    }

    public Item pop()
    {
        if (isEmpty()) {
            //throw new RuntimeException("Ring buffer underflow");
        }
        Item item = buffer[indexOut];
        buffer[indexOut] = null;                  // to help with garbage collection
        count--;
        indexOut = (indexOut + 1) % buffer.length; // wrap-around
        return item;
    }

    public Iterator<Item> iterator() { return new RingBufferIterator(); }

    // an iterator, doesn't implement remove() since it's optional
    private class RingBufferIterator implements Iterator<Item> {
        private int i = 0;
        public boolean hasNext()  { return i < count; }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return buffer[i++];
        }
    }




}