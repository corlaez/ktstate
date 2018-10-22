package com.corlaez.ktstate

class InvalidInitialStateException(override val message: String) : Exception(message)
class InvalidTransitionException(override val message: String) : Exception(message)
class UnreachableStateException(override val message: String) : Exception(message)

class InvalidStateException(override val message: String) : Exception(message)