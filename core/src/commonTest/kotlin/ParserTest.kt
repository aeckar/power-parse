@file:Suppress("LocalVariableName")

import io.github.aeckar.parsing.*
import io.github.aeckar.parsing.containers.DoubleList
import kotlin.test.Test

class ParserTest {
    @Test
    fun `basic arithmetic`() {
        val math by parser {
            val term by junction()
            val expression by junction()

            val digit by of("0-9")
            val number by multiple(digit)
            val factor by '(' + expression + ')' or
                    number

            term.actual = factor + '*' + term or
                    factor + '/' + term or
                    factor
            expression.actual = term + '+' + expression or
                    term + '-' + expression or
                    term

            start = expression
            skip = multiple(" ")  // Prefer multiple to any for skip symbols
        }
        println("\n" + math.parse("(1 + 2) * 3")?.treeString())
        // from source...
    }

    @Test
    fun `arithmetic with evaluation`() {
        val math by parserOperator {
            // ---- fragments ----
            val DIGIT = of("0-9")
            val WHOLE_PART = multiple(DIGIT)
            val DECIMAL_PART: Symbol = '.' + multiple(DIGIT)

            // ---- symbols ----
            val expression by junction()

            fun operation(symbol: Char) = expression + symbol + expression

            val addition by operation('+')
            val subtraction by operation('-')
            val multiplication by operation('*')
            val division by operation('/')

            //val number by any(DIGIT) + maybe('.') + any(DIGIT) not '.'
            val number by WHOLE_PART + maybe(DECIMAL_PART) or
                    DECIMAL_PART

            // ---- configuration ----
            expression.actual = '(' + expression + ')' or
                    multiplication or
                    division or
                    addition or
                    subtraction or
                    number

            start = expression
            skip = multiple(" ")

            // ---- mutable state ----
            val operands = DoubleList()

            // ---- listeners ----
            addition listener { operands += operands.removeLast() + operands.removeLast() }
            subtraction listener { operands += operands.removeLast() - operands.removeLast() }
            multiplication listener { operands += operands.removeLast() * operands.removeLast() }
            division listener { operands += operands.removeLast() / operands.removeLast() }
            number listener { operands += substring.toDouble() }

            returns { operands.last }
        }
        println("\n" + math.parse("(1 + 2) * 3")?.treeString())
    }

    @Test
    fun ebnf() {
        val ebnf = parser {

        }
        println("\n" + ebnf.parse("(1 + 2) * 3")?.treeString())
        // from source...
    }
}