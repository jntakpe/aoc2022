package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputSplitOnBlank
import java.util.function.Predicate

object Day11 : Day {

    override val input = readInputSplitOnBlank(11).map { Monkey.parse(it) }

    override fun part1(): Long {
        val input = readInputSplitOnBlank(11).map { Monkey.parse(it) }
        repeat(20) {
            input.forEach {
                val round = it.round()
                round.forEach { (k, v) -> input[k].items += v }
            }
        }
        val (first, second) = input.map { it.inspected }.sortedDescending().take(2)
        return first * second
    }

    override fun part2(): Long {
        val input = readInputSplitOnBlank(11).map { Monkey.parse(it) }
        val mod = input.map { it.divided }.reduce(Long::times)
        repeat(10000) {
            input.forEach {
                val round = it.round2(mod)
                round.forEach { (k, v) -> input[k].items += v }
            }
        }
        val map = input.map { it.inspected }
        val (first, second) = map.sortedDescending().take(2)
        return first * second
    }

    class Monkey(
        var items: MutableList<Long>,
        private val operation: (worryLevel: Long) -> Long,
        private val test: Predicate<Long>,
        private val throwIfTrue: Int,
        private val throwIfFalse: Int,
        val divided: Long
    ) {

        var inspected = 0L

        fun round(): Map<Int, MutableList<Long>> {
            return buildMap {
                items.forEach {
                    inspected++
                    val worryLevel = operation.invoke(it) / 3
                    if (test.test(worryLevel)) {
                        compute(throwIfTrue) { _, v -> v?.apply { add(worryLevel) } ?: mutableListOf(worryLevel) }
                    } else {
                        compute(throwIfFalse) { _, v -> v?.apply { add(worryLevel) } ?: mutableListOf(worryLevel) }
                    }
                }
                items = mutableListOf()
            }
        }

        fun round2(mod: Long): Map<Int, MutableList<Long>> {
            return buildMap {
                var listTrue = mutableListOf<Long>()
                var listFalse = mutableListOf<Long>()
                items.forEach {
                    inspected++
                    val worryLevel = operation.invoke(it) % mod
                    if (test.test(worryLevel)) {
                        listTrue.add(worryLevel)
                    } else {
                        listFalse.add(worryLevel)
                    }
                }
                put(throwIfTrue, listTrue)
                put(throwIfFalse, listFalse)
                items.clear()
            }
        }

        companion object {

            fun parse(input: String): Monkey {
                val (rawItems, rawOperation, rawTest, rawTrueAction, rawFalseAction) = input.lines().drop(1)
                val (divided, test) = parseTest(rawTest)
                return Monkey(
                    parseItems(rawItems).toMutableList(),
                    parseOperation(rawOperation),
                    test,
                    parseAction(rawTrueAction),
                    parseAction(rawFalseAction),
                    divided
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

            private fun parseTest(input: String): Pair<Long, Predicate<Long>> {
                val level = input.substringAfter("divisible by").trim().toLong()
                return level to Predicate { it % level == 0L }
            }

            private fun parseAction(input: String) = input.substringAfter("monkey ").trim().toInt()
        }

    }
}