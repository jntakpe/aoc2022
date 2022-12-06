package com.github.jntakpe.aoc2022.days.day6

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day6Test {

    @Test
    fun part1() {
        assertEquals(
            listOf(5, 6, 10, 11), listOf(
                "bvwbjplbgvbhsrlpgdmjqwftvncz",
                "nppdvjthqldpwncqszvftbrmjlhg",
                "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg",
                "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw",
            ).map { Day6.indexOfDifferentChars(it, 4) })
    }

    @Test
    fun part2() {
        assertEquals(
            listOf(19, 23, 23, 29, 26), listOf(
                "mjqjpqmgbljsphdztnvjfqwrcgsmlb",
                "bvwbjplbgvbhsrlpgdmjqwftvncz",
                "nppdvjthqldpwncqszvftbrmjlhg",
                "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg",
                "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw",
            ).map { Day6.indexOfDifferentChars(it, 14) })
    }
}