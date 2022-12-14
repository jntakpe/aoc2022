package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines

object Day14 : Day {

    private val INITIAL = Position(500, 0)
    override val input = parseRocks(readInputLines(14))

    override fun part1(): Int {
        val blocked = input.toMutableSet()
        val end = blocked.maxOf { it.y } + 1
        var count = 0
        while (true) {
            var current = INITIAL
            do {
                val next = current.next(blocked)
                if (next != null) {
                    if (next.y >= end) {
                        return count
                    }
                    current = next
                } else {
                    count++
                    blocked.add(current)
                }
            } while (next != null)
        }
    }

    override fun part2(): Int {
        val blocked = input.toMutableSet()
        val end = blocked.maxOf { it.y } + 1
        val hehe = (0..1001).map { Position(it, end + 1) }
        blocked += hehe
        var count = 0
        while (true) {
            var current = INITIAL
            do {
                val next = current.next(blocked)
                if (next != null) {
                    current = next
                } else {
                    count++
                    if (current == INITIAL) {
                        return count
                    }
                    blocked.add(current)
                }
            } while (next != null)
        }
    }

    data class Position(val x: Int, val y: Int) {
        companion object {

            fun of(input: String): Position {
                val (x, y) = input.split(",").map { it.toInt() }
                return Position(x, y)
            }
        }

        fun next(blocked: Set<Position>) = next().firstOrNull { it !in blocked }

        fun next() = listOf(copy(y = y + 1), copy(x = x - 1, y = y + 1), copy(x = x + 1, y = y + 1))

        fun rocks(other: Position): Set<Position> {
            return when {
                x > other.x || y > other.y -> other.rocks(this)
                x == other.x -> (y..other.y).map { Position(x, it) }.toSet()
                y == other.y -> (x..other.x).map { Position(it, y) }.toSet()
                else -> error("Invalid input")
            }
        }
    }

    private fun parseRocks(input: List<String>): Set<Position> {
        return input
            .flatMap { it.split(" -> ").windowed(2).flatMap { (a, b) -> Position.of(a).rocks(Position.of(b)) } }
            .toSet()
    }
}