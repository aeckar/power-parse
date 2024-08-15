package io.github.aeckar.parsing

/**
 * An action performed whenever a successful match is made to a symbol.
 */
public sealed interface Listener

/**
 * A listener assigned to a symbol that emits the given node.
 */
public fun interface NullaryListener<MatchT : Symbol> : Listener {
    /**
     * Invokes the lambda that defines the listener of a specific named symbol.
     */
    public operator fun Node<MatchT>.invoke()
}

/**
 * A listener that takes an argument and includes the argument passed to the parser.
 */
public fun interface UnaryListener<MatchT : Symbol, ArgumentT> : Listener {
    /**
     * Invokes the lambda that defines the listener of a specific named symbol.
     *
     * Includes the argument passed to the parser.
     */
    public operator fun Node<MatchT>.invoke(argument: ArgumentT)
}