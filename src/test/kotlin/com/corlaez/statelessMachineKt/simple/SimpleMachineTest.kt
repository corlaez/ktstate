package com.corlaez.ktstate.simple

import com.corlaez.ktstate.InvalidInitialStateException
import com.corlaez.ktstate.InvalidTransitionException
import com.corlaez.ktstate.UnreachableStateException
import io.kotlintest.KTestJUnitRunner
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.StringSpec
import org.junit.runner.RunWith

@RunWith(KTestJUnitRunner::class)
class SimpleMachineTest : StringSpec() {

    init {
        "Should check initialState" {
            shouldThrow<InvalidInitialStateException> {
                SimpleMachine(
                        SimpleState("A"),
                        mapOf()
                )
            }
        }
        "Should check transitions" {
            shouldThrow<InvalidTransitionException> {
                SimpleMachine(
                        SimpleState("A"),
                        mapOf(
                                SimpleState("A") to SimpleStateDef(
                                        mapOf(Event("x") to EventTarget("B"))
                                ),
                                SimpleState("B") to SimpleStateDef(
                                        mapOf(Event("y") to EventTarget("C"))
                                )
                        )
                )
            }
        }
        "Should check transitions on InitialState" {
            shouldThrow<InvalidTransitionException> {
                SimpleMachine(
                        SimpleState("A"),
                        mapOf(
                                SimpleState("A") to SimpleStateDef(
                                        mapOf(Event("x") to EventTarget("C"))
                                )
                        )
                )
            }
        }
        "Should check for unreachable states" {
            shouldThrow<UnreachableStateException> {
                SimpleMachine(
                        SimpleState("A"),
                        mapOf(
                                SimpleState("A") to SimpleStateDef(
                                        mapOf(Event("x") to EventTarget("A"))
                                ),
                                SimpleState("C") to SimpleStateDef(
                                        mapOf(Event("y") to EventTarget("A"))
                                )
                        )
                )
            }
        }
        "Should check for unreachable states that target themselves" {
            shouldThrow<UnreachableStateException> {
                SimpleMachine(
                        SimpleState("A"),
                        mapOf(
                                SimpleState("A") to SimpleStateDef(
                                        mapOf(Event("x") to EventTarget("A"))
                                ),
                                SimpleState("C") to SimpleStateDef(
                                        mapOf(Event("y") to EventTarget("C"))
                                )
                        )
                )
            }
        }
        "Should allow empty eventTargets" {
            SimpleMachine(
                    SimpleState("A"),
                    mapOf(
                            SimpleState("A") to SimpleStateDef(
                                    mapOf(Event("x") to EventTarget(null))
                            )
                    )
            )
        }

        "Should allow progressing state" {
            val machine = SimpleMachine(
                    SimpleState("A"),
                    mapOf(
                            SimpleState("A") to SimpleStateDef(
                                    mapOf(Event("x") to EventTarget("B"))
                            ),
                            SimpleState("B") to SimpleStateDef()
                    )
            )
            val initial = machine.initialState
            machine.nextState(initial, Event("x")) shouldBe SimpleState("B")
        }
        "Should return the same state when ignoring events" {
            val machine = SimpleMachine(
                    SimpleState("A"),
                    mapOf(
                            SimpleState("A") to SimpleStateDef(
                                    mapOf(Event("x") to EventTarget("B"))
                            ),
                            SimpleState("B") to SimpleStateDef()
                    )
            )
            val initial = machine.initialState
            machine.nextState(initial, Event("y")) shouldBe SimpleState("A")
        }
    }
}
