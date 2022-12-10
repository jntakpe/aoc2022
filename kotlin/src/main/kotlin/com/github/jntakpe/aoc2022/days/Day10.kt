package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines
import kotlin.math.abs

object Day10 : Day {

    override val input: List<Instruction> = readInputLines(10)
        .map { it.split(" ") }
        .map {
            when (it[0].trim()) {
                "addx" -> Instruction.AddX(it[1].trim().toInt())
                else -> Instruction.Noop
            }
        }

    override fun part1(): Int {
        val cycles = cycles()
        return listOf(20, 60, 100, 140, 180, 220).sumOf { it * cycles[it - 1] }
    }

    override fun part2(): List<String> {
        val chunk = 40
        return cycles()
            .mapIndexed { idx, x -> if (abs((idx % chunk) - x) <= 1) '#' else '.' }
            .chunked(chunk)
            .map { it.joinToString("") }
    }

    private fun cycles() = input.fold(mutableListOf(1)) { acc, cmd -> acc.also { it.addAll(cmd.exec(it)) } }.take(240)

    sealed interface Instruction {

        fun exec(state: List<Int>): List<Int>

        object Noop : Instruction {

            override fun exec(state: List<Int>) = listOf(state.last())
        }

        class AddX(private val x: Int) : Instruction {

            override fun exec(state: List<Int>) = state.last().let { listOf(it, it + x) }
        }
    }
}