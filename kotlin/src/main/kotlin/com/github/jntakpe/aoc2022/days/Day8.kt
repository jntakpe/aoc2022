package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines
import kotlin.math.abs

object Day8 : Day {

    override val input = readInputLines(8).map { c -> c.map { it.digitToInt() }.toTypedArray() }.toTypedArray()

    override fun part1(): Any {
        return input
            .flatMapIndexed { y, a -> a.mapIndexed { x, v -> isVisible(x to y, v, input) } }
            .count { it }
    }

    override fun part2(): Any {
        return input
            .flatMapIndexed { y, a -> a.mapIndexed { x, v -> distance(x to y, v, input) } }
            .max()
    }

    fun isVisible(position: Pair<Int, Int>, size: Int, allPositions: Array<Array<Int>>): Boolean {
        val columnEnd = allPositions.lastIndex
        val rowEnd = allPositions[0].lastIndex
        val edges = setOf(0, columnEnd, rowEnd)
        if (position.first in edges || position.second in edges) return true
        ///
        var x = position.first
        var visible = true
        while (x > 0 && visible) {
            x--
            visible = allPositions[position.second][x] < size
        }
        if (visible) {
            return visible
        }
        visible = true
        x = position.first
        while (x < columnEnd && visible) {
            x++
            visible = allPositions[position.second][x] < size
        }
        if (visible) {
            return visible
        }
        visible = true
        var y = position.second
        while (y > 0 && visible) {
            y--
            visible = allPositions[y][position.first] < size
        }
        if (visible) {
            return visible
        }
        visible = true
        y = position.second
        while (y < rowEnd && visible) {
            y++
            visible = allPositions[y][position.first] < size
        }
        return visible
    }

    fun distance(position: Pair<Int, Int>, size: Int, allPositions: Array<Array<Int>>): Int {
        val columnEnd = allPositions.lastIndex
        val rowEnd = allPositions[0].lastIndex
        val edges = setOf(0, columnEnd, rowEnd)
        var x = position.first
        var visible = true
        while (x > 0 && visible) {
            x--
            visible = allPositions[position.second][x] < size
        }
        var score = abs(x - position.first)
        visible = true
        x = position.first
        while (x < columnEnd && visible) {
            x++
            visible = allPositions[position.second][x] < size
        }
        score *= abs(x - position.first)
        visible = true
        var y = position.second
        while (y > 0 && visible) {
            y--
            visible = allPositions[y][position.first] < size
        }
        score *= abs(y - position.second)
        visible = true
        y = position.second
        while (y < rowEnd && visible) {
            y++
            visible = allPositions[y][position.first] < size
        }
        score *= abs(y - position.second)
        return score
    }
}