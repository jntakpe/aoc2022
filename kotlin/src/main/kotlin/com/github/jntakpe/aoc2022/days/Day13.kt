package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputSplitOnBlank

object Day13 : Day {

    override val input = parse()

    override fun part1(): Int {
        return input.mapIndexedNotNull { index, (a, b) -> index.takeIf { a <= b } }.sumOf { it + 1 }
    }

    override fun part2(): Int {
        val a = Packets(listOf(2))
        val b = Packets(listOf(6))
        val packets = (input.flatMap { (a, b) -> listOf(a, b) } + a + b).sorted()
        return (packets.indexOf(a) + 1) * (packets.indexOf(b) + 1)
    }

    @JvmInline
    value class Packets(val value: Any) : Comparable<Packets> {

        override fun compareTo(other: Packets): Int {
            return when {
                value is Int && other.value is Int -> value - other.value
                value is Int -> Packets(listOf(value)).compareTo(other)
                other.value is Int -> compareTo(Packets(listOf(other.value)))
                else -> {
                    value as List<*>
                    other.value as List<*>
                    for (i in 0 until minOf(value.size, other.value.size)) {
                        val a = value[i]!!
                        val b = other.value[i]!!
                        Packets(a).compareTo(Packets(b)).let { if (it != 0) return it }
                    }
                    value.size.compareTo(other.value.size)
                }
            }
        }
    }

    private fun parse(): List<Pair<Packets, Packets>> {
        return readInputSplitOnBlank(13)
            .map { it.lines() }
            .map { (l1, l2) -> parsePair(l1) to parsePair(l2) }
    }

    private fun parsePair(input: String): Packets {
        val raw = input.drop(1).dropLast(1)
        if (raw.isEmpty()) return Packets(emptyList<Any>())
        val list = buildList {
            var inside = ""
            var brackets = 0
            fun parseInside(): Any {
                return if (inside.toIntOrNull() != null) inside.toInt() else parsePair(inside).value
            }
            for (c in raw) {
                if (c == '[') brackets++
                if (c == ']') brackets--
                if (c == ',' && brackets == 0) {
                    add(parseInside())
                    inside = ""
                } else {
                    inside += c
                }
            }
            add(parseInside())
        }
        return Packets(list)
    }
}