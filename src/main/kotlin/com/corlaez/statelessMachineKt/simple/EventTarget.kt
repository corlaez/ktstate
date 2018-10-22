package com.corlaez.ktstate.simple

/**  Represents what the event target is, and allows to specify a tag to distinguish it from other transitions*/
data class EventTarget(
        val target: SimpleState?,// The target this event should point to.
        val displayName: String? = null,// You can use it to represent the transition in a gui
        val tag: String? = null// You can use it to distinguish between transitions. i.e. PRIMARY, SECONDARY, DISABLED
) {
    constructor (target: String, displayName: String? = null, tag: String? = null):
            this(target?.let{ SimpleState(target) }, displayName, tag)

    val hasTarget get() = target != null
}
