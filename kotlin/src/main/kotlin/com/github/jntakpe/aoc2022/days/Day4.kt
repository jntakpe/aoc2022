package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines

object Day4 : Day {

    override val input = readInputLines(4)
        .map { it.split(",") }
        .map { (p1, p2) -> p1.toRange() to p2.toRange() }

    private fun String.toRange() = split("-").let { (p1, p2) -> p1.toInt()..p2.toInt() }

    override fun part1() = input.count { (p1, p2) -> p1.all { it in p2 } || p2.all { it in p1 } }

    override fun part2() = input.count { (p1, p2) -> p1.any { it in p2 } }
}
