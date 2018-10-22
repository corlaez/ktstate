package com.corlaez.ktstate.nested

import com.corlaez.ktstate.InvalidStateException
import com.corlaez.ktstate.Machine
import com.corlaez.ktstate.StateDef
import com.corlaez.ktstate.simple.Event
import com.corlaez.ktstate.simple.EventTarget

/** A state that also holds a machine */
data class NestedStateDef (
        val transitions: Map<Event, EventTarget> = emptyMap(),
        val machine: Machine
) : StateDef {

    init {
        if(machine is NestedMachine) {
            throw InvalidStateException("This nested state's machine can't be a NestedMachine")
        }
    }

    val isTerminal get() = transitions.values.all{ !it.hasTarget }
}