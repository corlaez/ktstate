package com.corlaez.ktstate.simple

import com.corlaez.ktstate.StateDef

data class SimpleStateDef (
        val transitions: Map<Event, EventTarget> = emptyMap()
) : StateDef {
    val isTerminal get() = transitions.values.all{ !it.hasTarget }
}