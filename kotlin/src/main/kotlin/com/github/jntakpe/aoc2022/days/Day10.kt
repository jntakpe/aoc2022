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
        val cycles = mutableListOf(1)
        input.forEach {
            it.exec(cycles)
        }
        return listOf(20, 60, 100, 140, 180, 220).sumOf { it * cycles[it - 1] }
    }


    override fun part2(): List<String> {
        val cycles = mutableListOf(1)
        input.forEach {
            it.exec(cycles)
        }
        return cycles.asSequence()
            .mapIndexed { idx, x -> if (abs((idx % 40) - x) <= 1) '#' else '.' }
            .chunked(40)
            .map { it.joinToString("") }
            .onEach { println(it) }
            .take(6).toList()
    }

    sealed interface Instruction {

        fun exec(state: MutableList<Int>)

        object Noop : Instruction {

            override fun exec(state: MutableList<Int>) {
                state.add(state.last())
            }
        }

        class AddX(private val x: Int) : Instruction {

            override fun exec(state: MutableList<Int>) {
                state.add(state.last())
                state.add(state.last() + x)
            }
        }
    }
}