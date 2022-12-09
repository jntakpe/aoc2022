package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines
import kotlin.math.abs

object Day9 : Day {

    override val input = readInputLines(9)
        .map { line -> line.split(" ").map { it.trim() } }
        .map { (dir, steps) -> dir.first() to steps.toInt() }

    override fun part1(): Int {
        val visited = buildSet<Position> {
            var headPosition = Position()
            var tailPosition = Position()
            for ((dir, steps) in input) {
                repeat(steps) {
                    headPosition = headPosition.move(dir)
                    tailPosition = tailPosition.follow(headPosition)
                    add(tailPosition)
                }
            }
        }
        return visited.size
    }

    override fun part2(): Int {
        val visited = buildSet<Position> {
            var headPosition = Position()
            var tailPosition1 = Position()
            var tailPosition2 = Position()
            var tailPosition3 = Position()
            var tailPosition4 = Position()
            var tailPosition5 = Position()
            var tailPosition6 = Position()
            var tailPosition7 = Position()
            var tailPosition8 = Position()
            var tailPosition9 = Position()
            for ((dir, steps) in input) {
                repeat(steps) {
                    headPosition = headPosition.move(dir)
                    tailPosition1 = tailPosition1.follow(headPosition)
                    tailPosition2 = tailPosition2.follow(tailPosition1)
                    tailPosition3 = tailPosition3.follow(tailPosition2)
                    tailPosition4 = tailPosition4.follow(tailPosition3)
                    tailPosition5 = tailPosition5.follow(tailPosition4)
                    tailPosition6 = tailPosition6.follow(tailPosition5)
                    tailPosition7 = tailPosition7.follow(tailPosition6)
                    tailPosition8 = tailPosition8.follow(tailPosition7)
                    tailPosition9 = tailPosition9.follow(tailPosition8)
                    add(tailPosition9)
                }
            }
        }
        return visited.size
    }

    data class Position(val x: Int = 0, val y: Int = 0) {

        fun move(direction: Char): Position {
            return when (direction) {
                'L' -> copy(x = x - 1)
                'R' -> copy(x = x + 1)
                'D' -> copy(y = y - 1)
                'U' -> copy(y = y + 1)
                else -> error("Unknown direction")
            }
        }

        fun follow(position: Position): Position {
            return when {
                isAdjacent(position) -> this
                else -> {
                    val relX = x - position.x
                    val newPosition = when {
                        relX > 0 -> move('L')
                        relX < 0 -> move('R')
                        else -> this
                    }
                    val relY = y - position.y
                    when {
                        relY > 0 -> newPosition.move('D')
                        relY < 0 -> newPosition.move('U')
                        else -> newPosition
                    }
                }
            }
        }

        fun isAdjacent(position: Position) = abs(x - position.x) <= 1 && abs(y - position.y) <= 1

    }
}