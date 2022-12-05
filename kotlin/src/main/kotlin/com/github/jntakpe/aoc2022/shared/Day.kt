package com.github.jntakpe.aoc2022.shared

import kotlin.system.measureTimeMillis

interface Day {

    val input: Any

    fun part1(): Any

    fun part2(): Any

    fun run() {
        measureTimeMillis {
            print("Part 1: ${part1()}")
        }.let { println(" in $it ms") }
        measureTimeMillis {
            print("Part 2: ${part2()}")
        }.let { println(" in $it ms") }
    }
}
