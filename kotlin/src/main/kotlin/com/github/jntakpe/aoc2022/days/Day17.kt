package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInput

object Day17 : Day {

    override val input = readInput(17).map { Jet.from(it) }

    override fun part1() = solve(2022)

    override fun part2() = solve(1000000000000)

    private fun solve(rounds: Long): Long {
        var height = 0
        var moves = 0
        val blocked = buildSet { (0..7).map { add(Position(it, 0)) } }.toMutableSet()
        val cycleCache = mutableMapOf<Cycle, Int>()
        val heights = mutableListOf<Int>()
        val shapes = Shape.values()
        repeat(minOf(Int.MAX_VALUE.toLong(), rounds).toInt()) { i ->
            val key = Cycle.from(height, blocked, moves, i)
            val initial = Position(3, height + 4)
            val (rock, idx) = rockFall(Rock(shapes[(i % shapes.size)], initial), moves, blocked)
            moves = idx
            blocked += rock.positions
            height = maxOf(height, rock.positions.maxOf { it.y })
            heights += height
            cycleCache[key]?.also { return repeatCycles(it, i, height, heights, rounds) }
            cycleCache[key] = i
        }
        return height.toLong()
    }

    private fun repeatCycles(previousIdx: Int, i: Int, height: Int, heights: List<Int>, rounds: Long): Long {
        val diff = (heights[i] - heights[previousIdx]).toLong()
        val cycles = (rounds - (i + 1)) / (i - previousIdx)
        val rem = ((rounds - (i + 1)) % (i - previousIdx)).toInt()
        return height.toLong() + cycles * diff + heights[previousIdx + rem] - heights[previousIdx]
    }

    private tailrec fun rockFall(rock: Rock, idx: Int, blocked: Set<Position>): Pair<Rock, Int> {
        val jet = input[idx % input.size]
        val pushed = rock.push(jet, blocked)
        val down = pushed.down(blocked)
        if (down.position == pushed.position) {
            return down to idx + 1
        }
        return rockFall(down, idx + 1, blocked)
    }

    data class Cycle(val rocks: List<Boolean>, val shape: Int, val jet: Int) {
        companion object {

            fun from(height: Int, blocked: Set<Position>, moves: Int, i: Int): Cycle {
                val floor = (1..7)
                    .map { (0..15).map { h -> Position(it, height - h) in blocked } }
                    .flatten()
                return Cycle(floor, i % 5, moves % input.size)
            }
        }
    }

    enum class Jet {
        LEFT,
        RIGHT;

        companion object {

            fun from(char: Char) = when (char) {
                '<' -> LEFT
                '>' -> RIGHT
                else -> error("Unknown char $char")
            }
        }
    }

    class Rock(private val shape: Shape, val position: Position) {

        val positions = shape.positions.map { it + position }

        fun push(jet: Jet, blockedPositions: Iterable<Position>): Rock {
            return when (jet) {
                Jet.RIGHT -> moveTowards(Position(1, 0))
                Jet.LEFT -> moveTowards(Position(-1, 0))
            }.takeIf { it.isValid(blockedPositions) } ?: this
        }

        fun down(blockedPositions: Iterable<Position>): Rock {
            return moveTowards(Position(0, -1))
                .takeIf { it.isValid(blockedPositions) }
                ?: this
        }

        private fun isValid(blockedPositions: Iterable<Position>): Boolean {
            val withinWalls = positions.all { it.x in 1..7 }
            val aboveFloor = positions.all { it.y > 0 }
            val notOverlappingRocks = positions.none { it in blockedPositions }
            return withinWalls && aboveFloor && notOverlappingRocks
        }

        private fun moveTowards(next: Position) = Rock(shape, position + next)
    }

    data class Position(val x: Int, val y: Int) {

        operator fun plus(other: Position) = Position(x + other.x, y + other.y)
    }

    enum class Shape(val positions: List<Position>) {
        MINUS((0..3).map { Position(it, 0) }),
        PLUS(buildList {
            add(Position(1, 0))
            addAll((0..2).map { Position(it, 1) })
            add(Position(1, 2))
        }),
        L((0..2).map { Position(it, 0) } + (1..2).map { Position(2, it) }),
        BAR((0..3).map { Position(0, it) }),
        SQUARE((0..1).flatMap { y -> (0..1).map { Position(it, y) } });
    }
}