package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines
import kotlin.math.abs

object Day23 : Day {

    private val dirs = Direction.values().map { it.positions }
    override val input = parse(readInputLines(23))

    override fun part1(): Int {
        var elves = input
        repeat(10) { elves = next(elves, it) }
        val width = abs(elves.maxOf { it.x } - elves.minOf { it.x }) + 1
        val length = abs(elves.maxOf { it.y } - elves.minOf { it.y }) + 1
        return width * length - elves.size
    }

    override fun part2(): Int {
        var elves = input
        repeat(Int.MAX_VALUE) {
            val next = next(elves, it)
            if (next == elves) {
                return it + 1
            }
            elves = next
        }
        error("No matching result found")
    }

    private fun next(elves: Set<Position>, index: Int): Set<Position> {
        val next = elves
            .filter { elf -> elf.adjacent().any { it in elves } }
            .associateWith { nextPosition(index, it, elves) }
        return HashSet(elves).apply {
            next
                .filter { e -> next.none { e.value == it.value && e.key != it.key } }
                .forEach {
                    remove(it.key)
                    add(it.value)
                }
        }
    }

    private fun nextPosition(index: Int, elf: Position, elves: Set<Position>): Position {
        return dirs.indices
            .map { dirs[(index + it) % dirs.size] }
            .firstOrNull { dir -> dir.none { elf + it in elves } }
            ?.first()
            ?.plus(elf)
            ?: elf
    }

    private fun parse(input: List<String>): Set<Position> {
        return input
            .flatMapIndexed { y, l ->
                l.toCharArray()
                    .withIndex()
                    .filter { it.value == '#' }
                    .map { Position(it.index, -y) }
            }.toSet()
    }

    enum class Direction(val positions: Set<Position>) {
        NORTH(setOf(Position(0, 1), Position(-1, 1), Position(1, 1))),
        SOUTH(setOf(Position(0, -1), Position(-1, -1), Position(1, -1))),
        WEST(setOf(Position(-1, 0), Position(-1, -1), Position(-1, 1))),
        EAST(setOf(Position(1, 0), Position(1, -1), Position(1, 1))),
    }

    data class Position(val x: Int = 0, val y: Int = 0) {

        operator fun plus(other: Position) = Position(x + other.x, y + other.y)
        operator fun minus(other: Position) = Position(x - other.x, y - other.y)
        operator fun times(times: Int) = Position(x * times, y * times)
        operator fun unaryMinus() = Position(-x, -y)

        fun adjacent(): List<Position> {
            return (-1..1)
                .flatMap { y -> (-1..1).map { Position(it, y) } }
                .filter { it != Position() }
                .map { it + this }
        }
    }
}