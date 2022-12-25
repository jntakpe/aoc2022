package com.github.jntakpe.aoc2022

import com.github.jntakpe.aoc2022.days.*

fun main(args: Array<String>) {
    when (val day = args[0].toInt()) {
        1 -> Day1.run()
        2 -> Day2.run()
        3 -> Day3.run()
        4 -> Day4.run()
        5 -> Day5.run()
        6 -> Day6.run()
        7 -> Day7.run()
        8 -> Day8.run()
        9 -> Day9.run()
        10 -> Day10.run()
        11 -> Day11.run()
        12 -> Day12.run()
        13 -> Day13.run()
        14 -> Day14.run()
        15 -> Day15.run()
        16 -> Day16.run()
        17 -> Day17.run()
        18 -> Day18.run()
        19 -> Day19.run()
        20 -> Day20.run()
        21 -> Day21.run()
        22 -> Day22.run()
        23 -> Day23.run()
        24 -> Day24.run()
        25 -> Day25.run()
        else -> error("Day $day not implemented yet")
    }
}
