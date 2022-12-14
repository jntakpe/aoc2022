package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines

object Day14 : Day {

    override val input = Terrain.of(readInputLines(14))

    override fun part1() = input.next { y > input.end }.tiles

    override fun part2() = input.next { this == Position.INITIAL }.tiles + 1

    data class Terrain(val rocks: Set<Position>, val end: Int, val tiles: Int = 0) {
        companion object {

            fun of(input: List<String>) = parseRocks(input).let { r -> Terrain(r, r.maxOf { it.y }) }

            private fun parseRocks(input: List<String>): Set<Position> {
                return input
                    .flatMap { it.split(" -> ").windowed(2).flatMap { (a, b) -> Position.of(a).rocks(Position.of(b)) } }
                    .toSet()
            }
        }

        fun next(endReached: Position.() -> Boolean): Terrain {
            return Position.INITIAL.landingPosition(rocks, end)
                ?.takeIf { !it.endReached() }
                ?.let { copy(rocks = rocks + it, tiles = tiles + 1).next(endReached) }
                ?: this
        }
    }


    data class Position(val x: Int, val y: Int) {
        companion object {

            val INITIAL = Position(500, 0)

            fun of(input: String): Position {
                val (x, y) = input.split(",").map { it.toInt() }
                return Position(x, y)
            }
        }

        fun landingPosition(blocked: Set<Position>, end: Int): Position? {
            val next = next(blocked)
            return when {
                next == null -> this
                next.y > end -> next
                else -> next.landingPosition(blocked, end)
            }
        }

        private fun next(blocked: Set<Position>) = attempts().firstOrNull { it !in blocked }

        private fun attempts() = listOf(copy(y = y + 1), copy(x = x - 1, y = y + 1), copy(x = x + 1, y = y + 1))

        fun rocks(other: Position): Set<Position> = when {
            x > other.x || y > other.y -> other.rocks(this)
            x == other.x -> (y..other.y).map { Position(x, it) }.toSet()
            y == other.y -> (x..other.x).map { Position(it, y) }.toSet()
            else -> error("Invalid input")
        }
    }
}