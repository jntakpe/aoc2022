package com.github.jntakpe.aoc2022.days.day1

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputSplitOnBlank

object Day1 : Day {

    override val input: List<String> = readInputSplitOnBlank(1)

    override fun part1(): Int {
        return input.maxOf { l -> l.split("\n").sumOf { it.toInt() } }
    }

    override fun part2() : Int {
        return input.map { l -> l.split("\n").sumOf { it.toInt() } }.sortedDescending().take(3).sum()
    }
}
