package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInput

object Day6 : Day {

    override val input = readInput(6)

    override fun part1() = indexOfDifferentChars(input, 4)

    override fun part2() = indexOfDifferentChars(input, 14)

    fun indexOfDifferentChars(input: String, window: Int): Int {
        return input.windowed(window).indexOfFirst { it.toSet().size == it.length } + window
    }
}