package com.geercode.elehall.core;

import com.geercode.elehall.core.mode.sm.StatusState;
import com.geercode.elehall.core.mode.sm.StatusStateMachineBuilder;
import com.geercode.elehall.core.mode.sm.impl.SimpleStateMachineBuilder;
import org.junit.Test;

import java.util.EnumSet;

public class EntryRunner {
    @Test
    public void test() {
        StatusStateMachineBuilder<SampleEvents, LockDomain> smBuilder = new SimpleStateMachineBuilder();
        smBuilder.states((StatusState[]) EnumSet.allOf(SampleStates.class).toArray());
    }
}