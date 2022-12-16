package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines

object Day16 : Day {

    override val input = Terrain.from(readInputLines(16))

    override fun part1() = dfs(30)

    override fun part2() = dfs(26, part2 = true)

    private fun dfs(
        minutes: Int,
        current: Valve = input.allValves.getValue("AA"),
        remaining: Set<Valve> = input.flowingValves,
        cache: MutableMap<State, Int> = mutableMapOf(),
        part2: Boolean = false
    ): Int {
        return minutes * current.flow + cache.getOrPut(State(current.name, minutes, remaining)) {
            maxOf(remaining
                      .filter { input.distances.getValue(current.name).getValue(it.name) < minutes }
                      .takeIf { it.isNotEmpty() }
                      ?.maxOf {
                          val time = minutes - 1 - input.distances.getValue(current.name).getValue(it.name)
                          dfs(time, it, remaining - it, cache, part2)
                      }
                      ?: 0,
                  if (part2) dfs(26, remaining = remaining) else 0)
        }
    }

    data class Terrain(val allValves: Map<String, Valve>) {

        val flowingValves = allValves.values.filter { it.flow > 0 }.toSet()
        val distances = distances()

        private fun distances(): Map<String, Map<String, Int>> {
            return allValves.keys.map { valve ->
                val distances = mutableMapOf<String, Int>().withDefault { Int.MAX_VALUE }.apply { put(valve, 0) }
                val toVisit = mutableListOf(valve)
                while (toVisit.isNotEmpty()) {
                    val current = toVisit.removeFirst()
                    allValves.getValue(current).leads.forEach { neighbour ->
                        val newDistance = distances.getValue(current) + 1
                        if (newDistance < distances.getValue(neighbour)) {
                            distances[neighbour] = newDistance
                            toVisit.add(neighbour)
                        }
                    }
                }
                distances
            }.associateBy { it.keys.first() }
        }

        companion object {

            fun from(input: List<String>) = Terrain(input.map { parse(it) }.associateBy { it.name })
            private fun parse(line: String): Valve {
                val valve = line.substring(6, 8)
                val flow = line.substringAfter('=').substringBefore(';').toInt()
                val leadsTo = line.substringAfter("to valve").substringAfter(" ").split(',').map { it.trim() }
                return Valve(valve, flow, leadsTo)
            }
        }
    }

    data class Valve(val name: String, val flow: Int, val leads: List<String>)

    data class State(val current: String, val minutes: Int, val opened: Set<Valve>)
}