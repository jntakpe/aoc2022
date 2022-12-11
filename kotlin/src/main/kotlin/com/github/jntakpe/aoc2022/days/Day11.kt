package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputSplitOnBlank

object Day11 : Day {

    override val input: List<Input> = readInputSplitOnBlank(11).map { parse(it) }

    override fun part1() = solve(20) { it / 3 }

    override fun part2() = solve(10000) { it % input.map { i -> i.denominator.toLong() }.reduce(Long::times) }

    private fun solve(rounds: Int, worryReducer: (Long) -> Long): Long {
        val monkeys = input.map { Monkey(it.worryLevels, it.rules) }
        repeat(rounds) { monkeys.forEach { m -> m.round(worryReducer).forEach { (k, v) -> monkeys[k].add(v) } } }
        val (a, b) = monkeys.map { it.inspected }.sortedDescending()
        return a * b
    }

    class Monkey(private var worryLevels: List<Long>, private val rules: Rules) {

        var inspected = 0L
            private set

        fun round(worryReducer: (Long) -> Long): Map<Int, List<Long>> {
            return worryLevels
                .map { rules.throwItem(it, worryReducer) }
                .onEach { inspected++ }
                .also { worryLevels = emptyList() }
                .groupBy({ it.first }, { it.second })
        }

        fun add(worryLevels: List<Long>) {
            this.worryLevels += worryLevels
        }
    }

    class Rules(
        private val operation: (Long) -> Long,
        private val predicate: (Long) -> Boolean,
        private val whenTrue: Int,
        private val whenFalse: Int
    ) {

        fun throwItem(
            initialLevel: Long,
            worryReducer: (Long) -> Long
        ): Pair<Int, Long> {
            val worryLevel = worryReducer(operation(initialLevel))
            return if (predicate(worryLevel)) {
                whenTrue
            } else {
                whenFalse
            } to worryLevel
        }
    }

    data class Input(val worryLevels: List<Long>, val rules: Rules, val denominator: Int)

    private fun parse(input: String): Input {
        val (rawItems, rawOperation, rawTest, rawTrueAction, rawFalseAction) = input.lines().drop(1)
        val (denominator, test) = parseTest(rawTest)
        return Input(
            parseItems(rawItems),
            Rules(
                parseOperation(rawOperation),
                test,
                parseAction(rawTrueAction),
                parseAction(rawFalseAction)
            ),
            denominator
        )
    }

    private fun parseItems(input: String): List<Long> {
        return input.substringAfter(':').split(',').map { it.trim().toLong() }
    }

    private fun parseOperation(input: String): (worryLevel: Long) -> Long {
        val (left, ope, right) = input.substringAfter('=')
            .split(' ')
            .map { it.trim() }
            .filter { it.isNotBlank() }
        return { worryLevel ->
            val a = if (left == "old") worryLevel else left.toLong()
            val b = if (right == "old") worryLevel else right.toLong()
            when (ope.single()) {
                '+' -> a + b
                '*' -> a * b
                else -> error("Unknown operation")
            }
        }
    }

    private fun parseTest(input: String): Pair<Int, (Long) -> Boolean> {
        val level = input.substringAfter("divisible by").trim()
        return level.toInt() to { it % level.toLong() == 0L }
    }

    private fun parseAction(input: String) = input.substringAfter("monkey ").trim().toInt()
}