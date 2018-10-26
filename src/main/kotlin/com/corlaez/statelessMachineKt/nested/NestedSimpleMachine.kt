package com.corlaez.ktstate.nested

import com.corlaez.ktstate.*
import com.corlaez.ktstate.simple.Event
import com.corlaez.ktstate.simple.SimpleState

data class NestedSimpleMachine(
        val initialState: SimpleState,
        val states: Map<SimpleState, StateDef>
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

    private fun validateTransitions(state: SimpleState, stateInfo: StateDef) {
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
        return when (state) {
            is SimpleState -> {
                val stateInfo = states[state]!!
                val targetState = stateInfo.transitions[event]?.target ?: return state
                val targetStateInfo = states[targetState] as Neste!!
                when (targetStateInfo) {
                    is StateDef.Simple -> {
                        return stateInfo.transitions[event]?.target ?: state
                    }
                    is StateDef.Nested -> {
                        val nextState = targetStateInfo.machine.nextState(state, event)
                        return NestedState(
                                externalState = targetState,
                                internalState = nextState
                        )
                    }
                }
            }
            is NestedState -> {throw Exception()
                val stateInfoKey = state.externalState
                val stateInfo = states[stateInfoKey] as StateDef.Nested
                stateInfo.machine.nextState(state.internalState, event)
                return stateInfo.transitions[event]?.target ?: state
            }
            else -> throw Exception()
        }
    }
}
