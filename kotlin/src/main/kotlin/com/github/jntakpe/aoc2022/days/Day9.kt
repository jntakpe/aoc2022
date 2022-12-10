package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines
import kotlin.math.abs

object Day9 : Day {

    override val input = readInputLines(9)
        .map { line -> line.split(" ").map { it.trim() } }
        .map { (dir, steps) -> dir.first() to steps.toInt() }

    override fun part1(): Int {
        return buildSet {
            val headPosition = Position()
            val tailPosition = Position()
            for ((dir, steps) in input) {
                repeat(steps) {
                    headPosition.move(dir)
                    tailPosition.follow(headPosition)
                    add(tailPosition.copy())
                }
            }
        }.size
    }


    override fun part2(): Int {
        return buildSet {
            val positions = buildMap { (0..9).forEach { put(it, Position()) } }
            for ((dir, steps) in input) {
                repeat(steps) {
                    positions.forEach { (k, v) ->
                        if (k == 0) v.move(dir) else v.follow(positions.getValue(k - 1))
                        if (k == 9) add(v.copy())
                    }
                }
            }
        }.size
    }

    data class Position(var x: Int = 0, var y: Int = 0) {

        fun move(direction: Char) {
            when (direction) {
                'L' -> this.x = x - 1
                'R' -> this.x = x + 1
                'D' -> this.y = y - 1
                'U' -> this.y = y + 1
                else -> error("Unknown direction")
            }
        }

        fun follow(position: Position) {
            if (!isAdjacent(position)) {
                val relX = x - position.x
                when {
                    relX > 0 -> move('L')
                    relX < 0 -> move('R')
                }
                val relY = y - position.y
                when {
                    relY > 0 -> move('D')
                    relY < 0 -> move('U')
                }
            }
        }

        private fun isAdjacent(position: Position) = abs(x - position.x) <= 1 && abs(y - position.y) <= 1

    }
}