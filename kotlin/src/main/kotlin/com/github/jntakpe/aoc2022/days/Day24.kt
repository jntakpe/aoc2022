import Day24.Direction.*
import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines
import kotlin.math.abs

object Day24 : Day {

    override val input = readInputLines(24)

    override fun part1(): Int {
        return Terrain.from(input).run { optimalPath(this, State(start, 0, listOf(end))).last().time }
    }

    override fun part2(): Int {
        return if (isTest()) TODO("understand why StackOverflow")
        else {
            Terrain.from(input)
                .run { optimalPath(this, State(start, 0, listOf(end, start, end))).last().time }
        }
    }

    private fun timeToPath(
        terrainMap: MutableMap<Int, Terrain>,
        visitedStates: MutableSet<State>,
        states: List<State>,
        minTime: Int
    ): List<State>? {
        fun next(time: Int): Terrain = terrainMap.computeIfAbsent(time) { next(time - 1).next() }
        val lastState = states.last()
        val targets = lastState.destinations
        if (targets.isEmpty()) return states
        val next = next(lastState.time + 1)
        val nextStates = movePriority(lastState.position, targets.first())
            .map {
                val position = lastState.position + it.vector
                State(
                    position, lastState.time + 1, if (position == targets.first()) {
                        targets.drop(1)
                    } else targets
                )
            }
            .plus(State(lastState.position, lastState.time + 1, lastState.destinations))
            .filter {
                val notVisited = !visitedStates.contains(it)
                val betterPath = optimisticTime(it, targets) < minTime
                val isStartOrEnd = it.position == next.end || it.position == next.start
                val isValid = next.isValid(it.position) && !next.blizzards.containsKey(it.position)
                notVisited && betterPath && (isStartOrEnd || isValid)
            }
        visitedStates += nextStates
        var currentTime = minTime
        return nextStates
            .asSequence()
            .mapNotNull { timeToPath(terrainMap, visitedStates, states + it, currentTime) }
            .onEach { currentTime = it.lastIndex }
            .minByOrNull { it.lastIndex }
    }

    private fun movePriority(position: Position, target: Position): List<Direction> {
        return values().sortedByDescending { d -> ((target - position) * d.vector).let { it.x + it.y } }
    }

    private fun optimisticTime(state: State, targets: List<Position>): Int {
        return targets.fold(state.position to state.time) { (last, total), next ->
            next to total + (next - last).let { abs(it.x) + abs(it.y) }
        }.second
    }

    private fun optimalPath(map: Terrain, state: State): List<State> {
        return timeToPath(mutableMapOf(0 to map), mutableSetOf(state), listOf(state), Int.MAX_VALUE).orEmpty()
    }

    private fun isTest() = input.size <= 7

    data class State(val position: Position, val time: Int, val destinations: List<Position>)
    data class Terrain(
        val start: Position,
        val end: Position,
        val max: Position,
        val blizzards: Map<Position, List<Direction>>,
    ) {

        companion object {

            fun from(input: List<String>): Terrain {
                return Terrain(
                    Position(1, 0),
                    Position(input.first().lastIndex - 1, input.lastIndex),
                    Position(input.first().lastIndex, input.lastIndex),
                    input.flatMapIndexed { y, line ->
                        line.mapIndexedNotNull { x, char ->
                            val location = Position(x, y)
                            when (char) {
                                '<' -> location to listOf(LEFT)
                                '>' -> location to listOf(RIGHT)
                                '^' -> location to listOf(UP)
                                'v' -> location to listOf(DOWN)
                                else -> null
                            }
                        }
                    }.toMap()
                )
            }
        }

        fun next(): Terrain = Terrain(start, end, max, blizzards.flatMap { (location, blizzards) ->
            blizzards.map {
                val candidate = location + it.vector
                if (!isValid(candidate)) {
                    when (it) {
                        LEFT -> Position(max.x - 1, candidate.y)
                        RIGHT -> Position(1, candidate.y)
                        UP -> Position(candidate.x, max.y - 1)
                        DOWN -> Position(candidate.x, 1)
                    } to it
                } else {
                    candidate to it
                }
            }
        }.groupBy({ it.first }) { it.second })

        fun isValid(position: Position) = (position.x > 0 && position.y > 0 && position.x < max.x && position.y < max.y)
    }

    enum class Direction(val vector: Position) {
        RIGHT(Position(x = 1)),
        DOWN(Position(y = 1)),
        LEFT(Position(x = -1)),
        UP(Position(y = -1));
    }

    data class Position(val x: Int = 0, val y: Int = 0) {

        operator fun plus(other: Position) = Position(x + other.x, y + other.y)
        operator fun minus(other: Position) = Position(x - other.x, y - other.y)
        operator fun times(other: Position) = Position(x * other.x, y * other.y)
        operator fun rangeTo(other: Position): Sequence<Sequence<Position>> =
            (minOf(y, other.y)..maxOf(y, other.y)).asSequence().map { y ->
                (minOf(x, other.x)..maxOf(x, other.x)).asSequence().map { x ->
                    Position(x, y)
                }
            }
    }
}