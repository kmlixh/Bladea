package com.janyee.bladea.POJO;

import java.lang.reflect.Method;

/**
 * Created by lier on kmlixh on 2015/11/3.
 */
public class ValueTuple<K, V> {
    public K front;
    public V last;

    public ValueTuple(K front, V last) {
        this.front = front;
        this.last = last;
    }

    public K getFront() {
        return front;
    }

    public void setFront(K front) {
        this.front = front;
    }

    public V getLast() {
        return last;
    }

    public void setLast(V last) {
        this.last = last;
    }
    public void PrintMethod(){
        Method[] methods=getClass().getDeclaredMethods();
        if(methods!=null&&methods.length>0){
            for(Method temp:methods){
                System.out.println(temp.getName());
            }
        }
    }
}
