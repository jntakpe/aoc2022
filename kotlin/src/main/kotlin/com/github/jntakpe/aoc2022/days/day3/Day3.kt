package com.github.jntakpe.aoc2022.days.day2

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines

object Day3 : Day {

    private val priorities = listOf('a'..'z', 'A'..'Z').flatten()
    override val input: List<String> = readInputLines(3)

    override fun part1(): Int {
        return input
            .map { it.chunked(it.length / 2) }
            .map { (a, b) -> a.first { b.contains(it) } }
            .sumOf { priorities.indexOf(it) + 1 }
    }

    override fun part2(): Int {
        return input
            .chunked(3)
            .map { (a, b, c) ->
                a.toHashSet()
                    .apply { retainAll(b.toSet()) }
                    .apply { retainAll(c.toSet()) }
                    .single()
            }
            .sumOf { priorities.indexOf(it) + 1 }
    }
}
