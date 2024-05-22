package com.gadarts.parashoot.model.interfaces;

public interface Targeting<T> {

    boolean isPlayerCharacter();

    boolean isOnLeftSide();

    boolean hasTarget();

    boolean setTarget(T otherObject);

}
