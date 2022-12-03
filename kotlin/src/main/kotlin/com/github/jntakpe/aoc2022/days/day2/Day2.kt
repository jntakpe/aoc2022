package com.github.jntakpe.aoc2022.days.day2

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines

object Day2 : Day {

    override val input: List<Pair<Char, Char>> = readInputLines(2).map { l -> l.split("\\s".toRegex()).let { it.first().first() to it.last().first() } }

    override fun part1(): Int {
        return input
            .map { (a, b) -> Shape.fromA(a) to Shape.fromB(b) }
            .sumOf { (a, b) -> b.score(a) }
    }

    override fun part2(): Int {
        return input
            .map { (a, b) -> Shape.fromA(a) to Outcome.fromB(b) }
            .sumOf { (a, b) -> a.score(b) }
    }

    enum class Shape(private val score: Int) {
        ROCK(1),
        PAPER(2),
        SCISSORS(3);

        companion object {

            fun fromA(char: Char): Shape {
                return when (char) {
                    'A' -> ROCK
                    'B' -> PAPER
                    'C' -> SCISSORS
                    else -> throw IllegalArgumentException("Unsupported char $char for shapes")
                }
            }

            fun fromB(char: Char): Shape {
                return when (char) {
                    'X' -> ROCK
                    'Y' -> PAPER
                    'Z' -> SCISSORS
                    else -> throw IllegalArgumentException("Unsupported char $char for shapes")
                }
            }
        }

        fun score(a: Shape): Int {
            return when (a) {
                this -> Outcome.DRAW
                wins() -> Outcome.WIN
                else -> Outcome.LOSS
            }.score + score
        }

        fun score(outcome: Outcome): Int {
            val shape = when (outcome) {
                Outcome.WIN -> loss()
                Outcome.DRAW -> this
                Outcome.LOSS -> wins()
            }
            return shape.score + outcome.score
        }

        private fun wins(): Shape {
            return when (this) {
                ROCK -> SCISSORS
                SCISSORS -> PAPER
                PAPER -> ROCK
            }
        }

        private fun loss(): Shape {
            return when (this) {
                ROCK -> PAPER
                SCISSORS -> ROCK
                PAPER -> SCISSORS
            }
        }
    }

    enum class Outcome(val score: Int) {
        LOSS(0),
        DRAW(3),
        WIN(6);

        companion object {
            fun fromB(char: Char) : Outcome {
                return when (char) {
                    'X' -> LOSS
                    'Y' -> DRAW
                    'Z' -> WIN
                    else -> throw IllegalArgumentException("Unsupported char $char for shapes")
                }
            }
        }
    }
}
