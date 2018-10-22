package com.corlaez.ktstate.parallel

import com.corlaez.ktstate.State

data class ParallelState(
        val stateMap: Map<String, State>
) : State
