package com.corlaez.ktstate.simple

import com.corlaez.ktstate.*

data class SimpleMachine(
        val initialState: SimpleState,
        val states: Map<SimpleState, SimpleStateDef>
): Machine() {
    init {
        validate()
    }

    val stateNames: Set<SimpleState>
        get() = states.keys

    fun validate() {
        if(!stateNames.contains(initialState))
            throw InvalidInitialStateException(
                    "$name simpleMachine, initialState ($initialState) is not defined as a states map key"
            )
        states.forEach{ (state, stateInfo) ->
            validateTransitions(state, stateInfo)
            validateStateIsReachable(state)
        }
    }

    private fun validateTransitions(state: SimpleState, stateInfo: SimpleStateDef) {
            // If one of the transitions values is not a stateInfo key we throw
            stateInfo.transitions.forEach{ it ->
                if(it.value.hasTarget && !stateNames.contains(it.value.target))
                    throw InvalidTransitionException("$name simpleMachine, $state stateInfo, $it is not valid")
            }
    }

    private fun validateStateIsReachable(state: SimpleState) {
        // Since initial is reachable automatically we don't check it
        if(state != initialState) {
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
        return stateInfo.transitions[event]?.target ?: state
    }
}
