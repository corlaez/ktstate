package com.corlaez.ktstate.parallel

import com.corlaez.ktstate.InvalidInitialStateException
import com.corlaez.ktstate.InvalidStateException
import com.corlaez.ktstate.*
import com.corlaez.ktstate.simple.*

data class ParallelMachine(
        val states: Map<String, Machine>
): Machine() {

    val initialState: ParallelState get() = ParallelState(states.mapValues { (machineName, machine) ->
        when (machine) {
            is SimpleMachine ->
                machine.initialState // exit
            is ParallelMachine ->
                machine.initialState // recursive
            else ->
                // error
                throw Exception("The $machineName machine is invalid, only SimpleMachine and ParallelMachine are supported")
        }
    })

    init {
        // Override machines names with the keys
        states.forEach { it.value.name = it.key }
    }

    /** Provide current state and a event to return the next state. Null state means no change. */
    override fun nextState(state: State, event: Event): State  {
        if(state is ParallelState) {
            val map = states.mapValues{(machineName, machine) ->
                val currentState = state.stateMap[machineName]
                        ?: InvalidInitialStateException("Input state $state is invalid. Machine name (key) $machineName was not found")
                when (machine) {
                    is SimpleMachine ->
                        when(currentState) {
                            is SimpleState -> machine.nextState(currentState, event)
                            else -> throw InvalidStateException("Input state $currentState should be a SimpleState for $machineName machine")
                        }
                    is ParallelMachine ->
                        when(currentState) {
                            is ParallelState -> machine.nextState(currentState, event)
                            else -> throw InvalidStateException("Input state $currentState should be a ParallelState for $machineName machine")
                        }
                    else -> throw Exception("The $machineName machine is invalid, only SimpleMachine and ParallelMachine are supported")
                }
            }
            return ParallelState(map)
        } else {
            throw InvalidStateException("Input state $state should be a ParallelState")
        }
    }
}