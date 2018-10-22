package com.corlaez.ktstate.nested

import com.corlaez.ktstate.*
import com.corlaez.ktstate.simple.Event
import com.corlaez.ktstate.simple.SimpleState

data class NestedMachine(
        val initialState: String,
        val states: Map<SimpleState, NestedStateDef>,
        private var parent: Machine?
): Machine() {
    init {
        // override machine names
        states.forEach {
            it.value.machine?.name = it.key.name
        }
        validate()
    }

    val stateNames: Set<SimpleState>
        get() = states.keys

    fun validate() {
        if(!stateNames.contains(SimpleState(initialState)))
            throw InvalidInitialStateException(
                    "$name simpleMachine, initialState ($initialState) is not defined as a states map key"
            )
        states.forEach{ (state, stateInfo) ->
            validateTransitions(state, stateInfo)
            validateStateIsReachable(state)
        }
    }

    private fun validateTransitions(state: SimpleState, stateDef: NestedStateDef) {
            // If one of the transitions values is not a stateDef key we throw
            stateDef.transitions.forEach{ it ->
                if(it.value.hasTarget && !stateNames.contains(it.value.target))
                    throw InvalidTransitionException("$name simpleMachine, $state stateDef, $it is not valid")
            }
    }

    private fun validateStateIsReachable(state: SimpleState) {
        // Since initial is reachable automatically we don't check it
        if(state != SimpleState(initialState)) {
            // we don't want to consider a self referenced state reachable
            val allOtherStates = states.filter{it.key != state}
            // If none of the other states has this state key as a transition value we throw
            if(!allOtherStates.values.any{it.transitions.values.map{it.target}.contains(state)})
                throw UnreachableStateException("$name simpleMachine, $state state is not reachable")
        }
    }

    /** Provide current state and a event to return the next state. Null means no change. */
    override fun nextState(state: State, event: Event): State {
        val stateInfo = states[state] ?: throw InvalidStateException("$name simpleMachine, state $state does not exist.")
        return stateInfo.transitions[event]?.target ?: parent?.nextState(state, event) ?: state
    }// augment state if machine is present. TODO: xD
}
