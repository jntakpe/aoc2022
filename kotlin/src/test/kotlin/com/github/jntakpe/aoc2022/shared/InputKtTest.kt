package com.github.jntakpe.aoc2022.shared

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse

internal class InputKtTest {

    @Nested
    inner class ReadInputLines {

        @Test
        fun `should return lines`() {
            assertEquals(listOf(
                "forward 5",
                "down 5",
                "forward 8",
                "up 3",
                "down 8",
                "forward 2",
            ), readInputLines(28))
        }

        @Test
        fun `should ignore empty lines`() {
            val lines = readInputLines(29)
            assertEquals(12, lines.size)
            assertFalse(lines.contains(""))
        }
    }

    @Nested
    inner class ReadInputNumbers {

        @Test
        fun `should return numbers`() {
            assertEquals(listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263), readInputNumbers(27))
        }
    }

    @Nested
    inner class ReadInputSplitOnBlank {

        @Test
        fun `should split on blank`() {
            val lines = readInputSplitOnBlank(26)
            assertEquals(4, lines.size)
            assertEquals(listOf(
                """ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
byr:1937 iyr:2017 cid:147 hgt:183cm""",
                """iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
hcl:#cfa07d byr:1929""",
                """hcl:#ae17e1 iyr:2013
eyr:2024
ecl:brn pid:760753108 byr:1931
hgt:179cm""",
                """hcl:#cfa07d eyr:2025 pid:166559648
iyr:2011 ecl:brn hgt:59in"""), lines)
        }
    }

    @Nested
    inner class ReadInput {

        @Test
        fun `should read raw input`() {
            assertEquals(
                """
                forward 5
                down 5
                forward 8
                up 3
                down 8
                forward 2
                """.trimIndent(), readInput(28))
        }
    }
}
