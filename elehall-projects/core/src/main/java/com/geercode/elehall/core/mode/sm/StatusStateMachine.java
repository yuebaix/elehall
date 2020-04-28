package com.geercode.elehall.core.mode.sm;

public interface StatusStateMachine<E extends StatusEvent<C>, C> {
    boolean fire(E event);
}
