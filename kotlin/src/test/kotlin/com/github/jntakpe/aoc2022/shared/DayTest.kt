package com.github.jntakpe.aoc2022.shared

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

abstract class DayTest<T : Day> {

    abstract val day: T
    abstract val expectPart1: Any
    abstract val expectPart2: Any

    @Test
    fun `part 1 sample is ok`() {
        assertEquals(expectPart1, day.part1())
    }

    @Test
    fun `part 2 sample is ok`() {
        assertEquals(expectPart2, day.part2())
    }
}
