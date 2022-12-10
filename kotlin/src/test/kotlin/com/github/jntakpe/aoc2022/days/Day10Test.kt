package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.DayTest

internal class Day10Test : DayTest<Day10>() {

    override val day = Day10
    override val expectPart1 = 13140
    override val expectPart2 = listOf(
        "##..##..##..##..##..##..##..##..##..##..",
        "###...###...###...###...###...###...###.",
        "####....####....####....####....####....",
        "#####.....#####.....#####.....#####.....",
        "######......######......######......####",
        "#######.......#######.......#######.....",
    )
}