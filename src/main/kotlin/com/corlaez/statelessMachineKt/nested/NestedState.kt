package com.corlaez.ktstate.nested

import com.corlaez.ktstate.State
import com.corlaez.ktstate.simple.SimpleState

/** ? A nested state is asociated with a machine? and has a state inside (parallel or simple) */
/** A nested state can have a nested state as a internal state */
data class NestedState(
        val state: SimpleState,
        val internalState: State
) : State