package com.geercode.elehall.core.mode.sm.impl;

import com.geercode.elehall.core.mode.sm.*;

import java.util.HashSet;
import java.util.Set;

public class SimpleStateMachineBuilder<E extends StatusEvent<C>, C> implements StatusStateMachineBuilder<E, C> {
    private Set<StatusState> finalStates = new HashSet<>();
    private Set<StatusState> states = new HashSet<>();;
    private Set<StatusTransition<E, C>> transitions = new HashSet<>();
    private StatusDetectHandler detectHandler;

    @Override
    public StatusStateMachineBuilder<E, C> states(StatusState... states) {
        for (StatusState state : states) {
            this.states.add(state);
        }
        return this;
    }

    @Override
    public StatusStateMachineBuilder<E, C> finalState(StatusState state) {
        this.finalStates.add(state);
        return this;
    }

    @Override
    public StatusStateMachineBuilder<E, C> registerTransition(StatusTransition<E, C> transition) {
        this.transitions.add(transition);
        return this;
    }

    @Override
    public StatusStateMachineBuilder<E, C> registerDetectHandler(StatusDetectHandler<C> detectHandler) {
        this.detectHandler = detectHandler;
        return this;
    }

    @Override
    public StatusStateMachineBuilder<E, C> build() {
        return this;
    }

    @Override
    public StatusStateMachine<E, C> newInstance() {
        SimpleStateMachine<E, C> sm = new SimpleStateMachine<>();
        sm.setStates(this.states);
        sm.setFinalStates(this.finalStates);
        sm.setTransitions(this.transitions);
        sm.setDetectHandler(this.detectHandler);
        return sm;
    }
}
