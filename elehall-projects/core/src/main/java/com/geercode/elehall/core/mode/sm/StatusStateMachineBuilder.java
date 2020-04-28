package com.geercode.elehall.core.mode.sm;

import java.util.EnumSet;
import java.util.Set;

public interface StatusStateMachineBuilder<E extends StatusEvent<C>, C> {
    StatusStateMachineBuilder<E, C> states(StatusState... states);
    StatusStateMachineBuilder<E, C> finalState(StatusState state);
    StatusStateMachineBuilder<E, C> registerTransition(StatusTransition<E, C> transition);
    StatusStateMachineBuilder<E, C> registerDetectHandler(StatusDetectHandler<C> detectHandler);
    StatusStateMachineBuilder<E, C> build();
    StatusStateMachine<E, C> newInstance();
}
