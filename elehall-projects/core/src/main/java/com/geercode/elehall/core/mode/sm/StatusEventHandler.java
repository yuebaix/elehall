package com.geercode.elehall.core.mode.sm;

public interface StatusEventHandler<E extends StatusEvent<C>, C> {
    void handle(E event);
}
