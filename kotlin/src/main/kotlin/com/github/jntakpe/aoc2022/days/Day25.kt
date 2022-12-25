import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines

object Day25 : Day {

    override val input = readInputLines(25)
    private val codex = mapOf('0' to 0, '1' to 1, '2' to 2, '=' to -2, '-' to -1)

    override fun part1(): String {
        val (keys, values) = codex.keys.toList() to codex.values.toList()
        var decimal = input.sumOf { it.toDecimal() }
        val result = StringBuilder()
        while (decimal != 0L) {
            val codexIdx = (decimal % 5).toInt()
            result.insert(0, keys[codexIdx])
            decimal = (decimal - values[codexIdx]) / 5
        }
        return result.toString()
    }

    override fun part2() = 1

    private fun String.toDecimal() = fold(0L) { a, c -> a * 5 + codex.getValue(c) }
}