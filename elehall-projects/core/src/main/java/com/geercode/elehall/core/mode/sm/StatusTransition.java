package com.geercode.elehall.core.mode.sm;

public interface StatusTransition<E extends StatusEvent<C>, C> {
    StatusTransition from(StatusState from);
    StatusTransition to(StatusState to);
    StatusTransition on(E event);
    StatusTransition invoke(StatusEventHandler<E, C> handler);
}
