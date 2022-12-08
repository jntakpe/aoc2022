package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines
import kotlin.math.abs

object Day8 : Day {

    override val input = readInputLines(8).map { c -> c.map { it.digitToInt() }.toTypedArray() }.toTypedArray()

    override fun part1(): Int {
        return input
            .flatMapIndexed { y, a -> a.mapIndexed { x, v -> isVisible(x, y, v, input) } }
            .count { it }
    }

    override fun part2(): Int {
        return input
            .flatMapIndexed { y, a -> a.mapIndexed { x, v -> scenicScore(x, y, v, input) } }
            .max()
    }

    private fun isVisible(x: Int, y: Int, size: Int, positions: Array<Array<Int>>): Boolean {
        val edge = positions.lastIndex
        if (listOf(x, y).any { it in listOf(0, edge) }) return true
        if (hasTree(0 until x, positions[y], size)) return true
        if (hasTree(x + 1..edge, positions[y], size)) return true
        val column = (0..edge).map { positions[it][x] }.toTypedArray()
        if (hasTree(0 until y, column, size)) return true
        if (hasTree(y + 1..edge, column, size)) return true
        return false
    }

    private fun scenicScore(x: Int, y: Int, size: Int, positions: Array<Array<Int>>): Int {
        val edge = positions.lastIndex
        if (listOf(x, y).any { it in listOf(0, edge) }) return 0
        var score = view(x - 1 downTo 0, positions[y], size)
        score *= view(x + 1..edge, positions[y], size)
        val column = (0..edge).map { positions[it][x] }.toTypedArray()
        score *= view(y - 1 downTo 0, column, size)
        score *= view(y + 1..edge, column, size)
        return score
    }

    private fun hasTree(range: IntRange, line: Array<Int>, size: Int) = !range.any { line[it] >= size }
    private fun view(range: IntProgression, line: Array<Int>, size: Int): Int {
        return (range.indexOfFirst { line[it] >= size }.takeIf { it >= 0 } ?: abs(range.first - range.last)) + 1
    }
}