package com.gadarts.parashoot.enemies;

import com.badlogic.gdx.utils.Timer;

public class Order {
    public final Timer.Task task;
    final Order nextOrder;
    public final float destinationX;
    public final float destinationY;
    final float taskDelay;
    final float destinationReach;

    public Order(float destinationX) {
        this(destinationX, 0, null, 0);
    }

    public Order(float destinationX, float destinationY) {
        this(destinationX, destinationY, null, 0);
    }

    public Order(float destinationX, float destinationY, float destinationReach) {
        this(destinationX, destinationY, null, null, 0, destinationReach);
    }

    @SuppressWarnings("unused")
    public Order(float destinationX, float destinationY, Timer.Task task) {
        this(destinationX, destinationY, task, 0);
    }

    public Order(float destinationX, Timer.Task task, float delay) {
        this(destinationX, 0, task, delay);
    }

    @SuppressWarnings("unused")
    public Order(float destinationX, float destinationY, Order order) {
        this(destinationX, destinationY, order, null, 0, -1);
    }

    public Order(float destinationX, float destinationY, Timer.Task task, float delay) {
        this(destinationX, destinationY, null, task, delay, -1);
    }

    public Order(float destinationX, float destinationY, Order order, Timer.Task task, float delay, float destinationReach) {
        this.destinationX = destinationX;
        this.destinationY = destinationY;
        this.nextOrder = order;
        this.task = task;
        this.taskDelay = delay;
        this.destinationReach = destinationReach;
    }

}