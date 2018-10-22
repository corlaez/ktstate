# ktstate

ktstate is a library that helps you write stateless finite state machine and statecharts* machine evaluators for kotlin.

Another goal of this library is to provide fully serializable states and machines.

*Only some of the statecharts features are implemented see Out of Scope.

# Usage
Create a machine with an initial state:

```kotlin
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
machine.nextState(initial, Event("x"))// SimpleState("B")
```

**Note:** You will get exceptions that will guide you if you misconfigured your machine. 

### Roadmap
* FSM support, DONE SimpleMachine
* Parallel/orthogonal machines DONE ParallelMachine
* Nested Machines/statechart WIP
* History state? I haven't decided yet about this one...

### Out of Scope

Functions in the machines will probably make them serialization unpractical (if not impossible).
I think these will remain out of scope:

* Broadcast communication
* onEntry, onExit
* guards


> "A point worth mentioning, though, is that the broadcast mechanism is not a crucial feature of the statechart formalism."
>
>Prof. David Harel, STATECHARTS: A VISUAL FORMALISM FOR COMPLEX SYSTEMS
