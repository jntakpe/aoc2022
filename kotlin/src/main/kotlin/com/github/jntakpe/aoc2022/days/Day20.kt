package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines
import java.util.*

object Day20 : Day {

    override val input = readInputLines(20).map { it.toLong() }.withIndex()
    override fun part1() = solve(input.toCollection(LinkedList()), 1)

    override fun part2() = solve(input.map { it.copy(value = it.value * 811589153) }.toCollection(LinkedList()), 10)

    private fun solve(numbers: LinkedList<IndexedValue<Long>>, mix: Int): Long {
        repeat(mix) { _ ->
            for (initialIndex in numbers.indices) {
                val currentIndex = numbers.indexOfFirst { it.index == initialIndex }
                val node = numbers.removeAt(currentIndex)
                numbers.add((node.value + currentIndex).mod(numbers.size), node)
            }
        }
        val zIndex = numbers.indexOfFirst { (_, v) -> v == 0L }
        return (1..3).map { (zIndex + it * 1000) % numbers.size }.sumOf { numbers[it].value }
    }
}