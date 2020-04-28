package com.geercode.elehall.core.mode.sm.impl;

import com.geercode.elehall.core.mode.sm.*;
import lombok.Data;

import java.util.Set;

@Data
public class SimpleStateMachine<E extends StatusEvent<C>, C> implements StatusStateMachine<E, C> {
    private StatusState currentState;
    private StatusState initialState;
    private Set<StatusState> finalStates;
    private Set<StatusState> states;
    private Set<StatusTransition<E, C>> transitions;
    private StatusDetectHandler detectHandler;

    @Override
    public boolean fire(E event) {
        return false;
    }
}
