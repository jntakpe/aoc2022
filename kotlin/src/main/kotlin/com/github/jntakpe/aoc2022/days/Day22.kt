package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines


object Day22 : Day {

    override val input = readInputLines(22).let { it.dropLast(2) to instructions(it.last()) }

    operator fun List<String>.get(pos: Position): Char = getOrNull(pos.y)?.getOrNull(pos.x) ?: ' '

    private fun limits(grid: List<String>): List<State> {
        val start = Position(grid[0].indexOf('.'), 0)
        return buildList {
            var pos = start
            var dir = 0
            do {
                add(State(dir, pos))
                val forward = pos.move(dir)
                if (grid[forward] == ' ') {
                    dir = (dir + 1).mod(4)
                } else {
                    val left = forward.move(dir - 1)
                    if (grid[left] == ' ') {
                        pos = forward
                    } else {
                        pos = left
                        dir = (dir - 1).mod(4)
                    }
                }
            } while (pos != start || dir != 0)
        }
    }

    private fun walkTree(nextMap: Map<State, State>): Int {
        return input.second.fold(State.START) { a, c ->
            when (c) {
                "L" -> a.copy(direction = (a.direction - 1).mod(4))
                "R" -> a.copy(direction = (a.direction + 1).mod(4))
                else -> forward(c.toInt(), a, nextMap)
            }
        }.run { 1000 * (position.y + 1) + 4 * (position.x + 1) + direction }
    }

    private fun forward(iterations: Int, start: State, nextMap: Map<State, State>): State {
        return (0 until iterations)
            .fold(start) { a, _ ->
                (nextMap[a] ?: a.copy(position = a.position.move(a.direction)))
                    .takeIf { input.first[it.position] != '#' }
                    ?: return a
            }
    }

    private fun instructions(input: String): List<String> {
        val forward = input.split('R', 'L')
        val turns = input.filter { it.isLetter() }.map { it.toString() }
        return forward.zip(turns).flatMap { (f, t) -> listOf(f, t) } + forward.last()
    }

    override fun part1(): Int {
        val (grid) = input
        val nextMap = limits(grid)
            .map { it.copy(direction = (it.direction - 1).mod(4)) }
            .associateWith { (dir, pos) ->
                val next = when (dir) {
                    0 -> pos.copy(x = grid[pos.y].indexOfFirst { it != ' ' })
                    1 -> pos.copy(y = grid.indexOfFirst { pos.x in it.indices && it[pos.x] != ' ' })
                    2 -> pos.copy(x = grid[pos.y].indexOfLast { it != ' ' })
                    3 -> pos.copy(y = grid.indexOfLast { pos.x in it.indices && it[pos.x] != ' ' })
                    else -> error("")
                }
                State(dir, next)
            }
        return walkTree(nextMap)
    }

    override fun part2(): Int {
        val sideLength = if (input.second.size == 13) 4 else 50
        val edges = limits(input.first).chunked(sideLength).map { it[0].direction to it }.toMutableList()
        val nextMap = buildMap {
            while (edges.isNotEmpty()) {
                var i = 0
                while (i < edges.lastIndex) {
                    val a = edges[i]
                    val b = edges[i + 1]
                    if ((a.first - b.first).mod(4) == 1) {
                        edges.subList(i, i + 2).clear()
                        for (j in i..edges.lastIndex) {
                            val edge = edges[j]
                            edges[j] = (edge.first - 1).mod(4) to edge.second
                        }
                        for (j in 0 until sideLength) {
                            val (dir1, pos1) = a.second[j]
                            val (dir2, pos2) = b.second[sideLength - j - 1]
                            put(State((dir1 - 1).mod(4), pos1), State((dir2 + 1).mod(4), pos2))
                            put(State((dir2 - 1).mod(4), pos2), State((dir1 + 1).mod(4), pos1))
                        }
                    } else {
                        i++
                    }
                }
            }
        }
        return walkTree(nextMap)
    }

    data class State(val direction: Int, val position: Position) {
        companion object {

            val START = State(0, Position(input.first.first().indexOf('.'), 0))
        }

    }

    data class Position(val x: Int, val y: Int) {

        fun move(direction: Int) = when (direction.mod(4)) {
            0 -> copy(x = x + 1)
            1 -> copy(y = y + 1)
            2 -> copy(x = x - 1)
            3 -> copy(y = y - 1)
            else -> error("Unknown direction")
        }
    }
}