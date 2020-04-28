package com.geercode.elehall.core.mode.sm;

public interface StatusEvent<C> {
    void setContext(C c);
    C getContext();
}