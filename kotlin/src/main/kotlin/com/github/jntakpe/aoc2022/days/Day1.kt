package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputSplitOnBlank

object Day1 : Day {

    override val input: List<String> = readInputSplitOnBlank(1)

    override fun part1() = elvesCalories().max()

    override fun part2() = elvesCalories().sortedDescending().take(3).sum()

    private fun elvesCalories() = input.map { l -> l.split("\n").sumOf { it.toInt() } }
}
