package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines

object Day18 : Day {

    override val input = readInputLines(18)
        .map { l -> l.split(',').map { it.toInt() }.let { (x, y, z) -> Position(x, y, z) } }

    override fun part1() = input.sumOf { c -> c.adjacent().count { it !in input } }

    override fun part2(): Int {
        val min = input.run { Position(minOf { it.x } - 1, minOf { it.y } - 1, minOf { it.z } - 1) }
        val max = input.run { Position(maxOf { it.x } + 1, maxOf { it.y } + 1, maxOf { it.z } + 1) }
        val visited = mutableSetOf<Position>()
        val queue = mutableListOf(min)
        while (queue.isNotEmpty()) {
            val p = queue.removeLast()
            if (p in input) continue
            if (p.x !in min.x..max.x || p.y !in min.y..max.y || p.z !in min.z..max.z) continue
            if (visited.add(p)) queue.addAll(p.adjacent())
        }
        return input.sumOf { p -> p.adjacent().count { it in visited } }
    }

    data class Position(val x: Int, val y: Int, val z: Int) {

        companion object {

            private val ADJACENT = listOf(
                Position(1, 0, 0),
                Position(-1, 0, 0),
                Position(0, 1, 0),
                Position(0, -1, 0),
                Position(0, 0, 1),
                Position(0, 0, -1)
            )
        }

        operator fun plus(other: Position) = copy(x = x + other.x, y = y + other.y, z = z + other.z)

        fun adjacent() = ADJACENT.map { this + it }
    }
}