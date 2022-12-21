package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines


object Day21 : Day {

    override val input = readInputLines(21).associate { parse(it) }
    private val results = input.mapNotNull { (k, v) -> (v as? Operation.Result)?.let { k to v.result } }.toMap()
    private val root = input.getValue("root") as Operation.Nested

    override fun part1() = total("root", results.toMutableMap())

    override fun part2() = newtonRaphson(0)

    private tailrec fun newtonRaphson(value: Long): Long {
        val leftDiff = diff(value)
        val rightDiff = diff(value + 1)
        return when {
            leftDiff == 0L -> value
            rightDiff == 0L -> value + 1
            rightDiff - leftDiff == 0L -> newtonRaphson(value + 1)
            else -> newtonRaphson(value - (leftDiff / (rightDiff - leftDiff)))
        }
    }

    private fun diff(guess: Long): Long {
        return total(root.left, withHumanValue(guess)) - total(root.right, withHumanValue(guess))
    }

    private fun withHumanValue(value: Long) = results.toMutableMap().apply { compute("humn") { _, _ -> value } }

    private fun total(monkey: String, results: MutableMap<String, Long>): Long {
        return when (val operation = input.getValue(monkey)) {
            is Operation.Result -> operation.result
            is Operation.Nested -> {
                val left = results[operation.left] ?: total(operation.left, results)
                val right = results[operation.right] ?: total(operation.right, results)
                val result = when (operation.sign) {
                    Sign.ADD -> left + right
                    Sign.MINUS -> left - right
                    Sign.TIMES -> left * right
                    Sign.DIVIDE -> left / right
                }
                results[monkey] = result
                result
            }
        }
    }

    private fun parse(line: String): Pair<String, Operation> {
        val (key, ope) = line.split(':').map { it.trim() }
        return key to Operation.from(ope)
    }

    sealed interface Operation {

        companion object {

            fun from(input: String): Operation {
                input.toLongOrNull()?.let { return Result(it) }
                val (left, rawSign, right) = input.split(' ')
                val sign = when (rawSign.single()) {
                    '+' -> Sign.ADD
                    '-' -> Sign.MINUS
                    '*' -> Sign.TIMES
                    '/' -> Sign.DIVIDE
                    else -> error("Unknown sign $rawSign")
                }
                return Nested(left, right, sign)
            }
        }

        data class Result(val result: Long) : Operation

        data class Nested(val left: String, val right: String, val sign: Sign) : Operation
    }

    enum class Sign {
        ADD,
        MINUS,
        TIMES,
        DIVIDE
    }
}