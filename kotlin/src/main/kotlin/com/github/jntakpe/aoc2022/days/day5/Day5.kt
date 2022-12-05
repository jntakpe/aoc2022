package com.github.jntakpe.aoc2022.days.day5

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInput

object Day5 : Day {

    override val input = Supplies.create()

    override fun part1() = input.moveAll(false).print()

    override fun part2(): String = input.moveAll(true).print()

    data class Move(val items: Int, val from: Int, val to: Int)

    data class Supplies(private val stacks: Map<Int, List<Char>>, private val moves: List<Move>) {

        companion object {

            fun create(): Supplies {
                val (stacks, moves) = readInput(5)
                    .split("(?m)^\\s*$".toRegex())
                    .map { p -> p.trimEnd().lines().filter { it.isNotBlank() } }
                return Supplies(
                    parseStacks(stacks),
                    parseMoves(moves)
                )
            }

            private fun parseStacks(stackLines: List<String>): Map<Int, List<Char>> {
                return stackLines.last()
                    .mapIndexedNotNull { i, c -> i.takeIf { c.isDigit() } }
                    .mapIndexed { idx, col -> idx to col }
                    .associate { (idx, col) ->
                        idx + 1 to
                                stackLines.dropLast(1)
                                    .mapNotNull { l -> l.getOrNull(col) }
                                    .filter { it.isLetter() }
                                    .reversed()

                    }
            }

            private fun parseMoves(moveLines: List<String>): List<Move> {
                return moveLines
                    .map { l ->
                        l.split(' ')
                            .mapNotNull { s -> s.trim().filter { it.isDigit() }.takeIf { it.isNotBlank() } }
                            .map { it.toInt() }
                    }
                    .map { (items, from, to) -> Move(items, from, to) }
            }
        }

        fun moveAll(preserveOrder: Boolean) = copy(stacks = moves.fold(stacks) { a, c -> move(c, a, preserveOrder) })

        fun print() = stacks.map { it.value.last() }.joinToString("")

        private fun move(
            move: Move,
            stacks: Map<Int, List<Char>>,
            preserveOrder: Boolean
        ): Map<Int, List<Char>> {
            return stacks.mapValues { ArrayDeque(it.value) }.apply {
                val from = getValue(move.from)
                val stack = buildList {
                    repeat(move.items) { from.removeLast().also { if (preserveOrder) add(0, it) else add(it) } }
                }
                getValue(move.to).apply { addAll(stack) }
            }
        }
    }
}