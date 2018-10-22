package com.corlaez.ktstate.parallel

import com.corlaez.ktstate.simple.*
import io.kotlintest.KTestJUnitRunner
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import org.junit.runner.RunWith

@RunWith(KTestJUnitRunner::class)
class ParallelMachineTest : StringSpec() {

    init {
        "Should allow progressing state" {
            val machine = ParallelMachine(mapOf(
                    "First" to SimpleMachine(
                            SimpleState("A"),
                            mapOf(
                                    SimpleState("A") to SimpleStateDef(
                                            mapOf(Event("x") to EventTarget("B"))
                                    ),
                                    SimpleState("B") to SimpleStateDef()
                            )
                    )
            ))
            val initial = machine.initialState
            val final = machine.nextState(initial, Event("x"))
            final shouldBe ParallelState(mapOf("First" to SimpleState("B")))
        }

        "Should allow progressing multiple states at the same time, ignoring if needed" {
            val machine = ParallelMachine(mapOf(
                    "1" to SimpleMachine(
                            SimpleState("A"),
                            mapOf(
                                    SimpleState("A") to SimpleStateDef(
                                            mapOf(Event("x") to EventTarget("B"))
                                    ),
                                    SimpleState("B") to SimpleStateDef()
                            )
                    ),
                    "2" to SimpleMachine(
                            SimpleState("C"),
                            mapOf(
                                    SimpleState("C") to SimpleStateDef(
                                            mapOf(Event("x") to EventTarget("D"))
                                    ),
                                    SimpleState("D") to SimpleStateDef()
                            )
                    ),
                    "3" to SimpleMachine(
                            SimpleState("E"),
                            mapOf(
                                    SimpleState("E") to SimpleStateDef(
                                            mapOf(Event("y") to EventTarget("F"))
                                    ),
                                    SimpleState("F") to SimpleStateDef()
                            )
                    )
            ))
            val initial = machine.initialState
            val final = machine.nextState(initial, Event("x"))
            final shouldBe ParallelState(
                    mapOf(
                            "1" to SimpleState("B"),
                            "2" to SimpleState("D"),
                            "3" to SimpleState("E")
                    )
            )
        }
        "Should return the same state when ignoring events" {
            val machine = ParallelMachine(mapOf(
                    "First" to SimpleMachine(
                            SimpleState("A"),
                            mapOf(
                                    SimpleState("A") to SimpleStateDef(
                                            mapOf(Event("x") to EventTarget("B"))
                                    ),
                                    SimpleState("B") to SimpleStateDef()
                            )
                    )
            ))
            val initial = machine.initialState
            val final = machine.nextState(initial, Event("y"))
            final shouldBe ParallelState(mapOf("First" to SimpleState("A")))
        }
    }
}
