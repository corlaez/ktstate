package com.corlaez.ktstate

import com.corlaez.ktstate.nested.NestedMachine
import com.corlaez.ktstate.simple.Event
import com.corlaez.ktstate.simple.EventTarget

sealed class StateDef {
    abstract  val transitions: Map<Event, EventTarget>
    val isTerminal get() = transitions.values.all{ !it.hasTarget }

    data class Simple (
            override val transitions: Map<Event, EventTarget> = emptyMap()
    ) : StateDef()

    data class Nested (
            override val transitions: Map<Event, EventTarget> = emptyMap(),
            val machine: Machine
    ) : StateDef() {
        init {
            if(machine is NestedMachine) {
                throw InvalidStateException("This nested state's machine can't be a NestedMachine")
            }
        }
    }
}