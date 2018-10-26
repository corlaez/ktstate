package com.corlaez.ktstate

import com.corlaez.ktstate.simple.Event

abstract class Machine {
    // Parallel machines override the name with the corresponding keys
    var name: String = "Unnamed"

    abstract fun nextState(state: State, event: Event): State
}
