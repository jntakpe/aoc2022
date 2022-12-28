package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.days.Day19.Resource.*
import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines
import java.util.*

object Day19 : Day {

    override val input = readInputLines(19).map { Blueprint.from(it) }

    override fun part1() = input.sumOf { dfs(it, 24) * it.index }

    override fun part2() =
        if (isTest()) TODO("Understand why it takes so long") else input.take(3).map { dfs(it, 32) }.reduce(Int::times)

    private fun dfs(blueprint: Blueprint, time: Int): Int {
        var result = 0
        val initialState = State(Resources(), Resources(ore = 1), time)
        val queue = LinkedList<State>()
        queue.add(initialState)
        val cache = mutableSetOf<State>()
        while (queue.isNotEmpty()) {
            var state = queue.poll()
            val remainingTime = state.time - 1
            result = maxOf(result, state.resources.geode)
            if (state.time == 0) continue
            val bots = state.robots.copy(
                ore = minOf(state.robots.ore, blueprint.recipes.maxSpend.ore),
                clay = minOf(state.robots.clay, blueprint.recipes.maxSpend.clay),
                obsidian = minOf(state.robots.obsidian, blueprint.recipes.maxSpend.obsidian)
            )
            val res = state.resources.copy(
                ore = minOf(
                    state.resources.ore,
                    blueprint.recipes.maxSpend.ore * state.time - bots.ore * remainingTime
                ),
                clay = minOf(
                    state.resources.clay,
                    blueprint.recipes.obsidian.cost.clay * state.time - bots.clay * remainingTime
                ),
                obsidian = minOf(
                    state.resources.obsidian,
                    blueprint.recipes.geode.cost.obsidian * state.time - bots.obsidian * remainingTime
                )
            )
            state = State(res, bots, state.time)
            if (state in cache) continue
            cache += state
            val allResources = res + bots
            queue.add(State(allResources, bots, remainingTime))
            val nextSteps = blueprint.recipes.asList
                .filter { res >= it.cost }
                .map { State(allResources - it.cost, bots + it.gain, remainingTime) }
            queue.addAll(nextSteps)
        }
        return result
    }

    //TODO understand why it so slow with test data
    private fun isTest() = input.size <= 2

    data class State(val resources: Resources, val robots: Resources, val time: Int)

    data class Blueprint(val index: Int, val recipes: Recipes) {

        companion object {

            fun from(input: String): Blueprint {
                return Blueprint(
                    index(input),
                    recipes(input)
                )
            }

            private fun index(input: String) = input.lines().first().substringAfter(' ').substringBefore(':').toInt()

            private fun recipes(input: String): Recipes {
                return input.split('.')
                    .filter { it.isNotEmpty() }
                    .associate { resource(it) to costs(it) }
                    .let {
                        Recipes(
                            Recipe(it.getValue(ORE), Resources(ore = 1)),
                            Recipe(it.getValue(CLAY), Resources(clay = 1)),
                            Recipe(it.getValue(OBSIDIAN), Resources(obsidian = 1)),
                            Recipe(it.getValue(GEODE), Resources(geode = 1)),
                        )
                    }
            }

            private fun resource(input: String): Resource {
                return Resource.valueOf(input.substringAfter("Each").substringBefore("robot").trim().uppercase())
            }

            private fun costs(input: String): Resources {
                return input.substringAfter("costs")
                    .substringBefore('.')
                    .split("and")
                    .associate {
                        val (cost, resource) = it.trim().split(' ')
                        Resource.valueOf(resource.uppercase()) to cost.toInt()
                    }.let { Resources(it[ORE] ?: 0, it[CLAY] ?: 0, it[OBSIDIAN] ?: 0, it[GEODE] ?: 0) }
            }
        }
    }

    enum class Resource {
        ORE,
        CLAY,
        OBSIDIAN,
        GEODE
    }

    data class Recipe(val cost: Resources, val gain: Resources)

    data class Recipes(val ore: Recipe, val clay: Recipe, val obsidian: Recipe, val geode: Recipe) {

        val asList = listOf(ore, clay, obsidian, geode)

        val maxSpend = asList
            .map { it.cost }
            .run { Resources(maxOf { it.ore }, maxOf { it.clay }, maxOf { it.obsidian }, maxOf { it.geode }) }
    }

    data class Resources(
        val ore: Int = 0,
        val clay: Int = 0,
        val obsidian: Int = 0,
        val geode: Int = 0
    ) : Comparable<Resources> {

        private val asList = listOf(ore, clay, obsidian, geode)

        operator fun plus(other: Resources): Resources {
            return Resources(ore + other.ore, clay + other.clay, obsidian + other.obsidian, geode + other.geode)
        }

        operator fun minus(other: Resources): Resources {
            return Resources(ore - other.ore, clay - other.clay, obsidian - other.obsidian, geode - other.geode)
        }

        override fun compareTo(other: Resources): Int {
            val zip = asList.zip(other.asList)
            return when {
                zip.any { (a, b) -> a < b } -> -1
                zip.all { (a, b) -> a == b } -> 0
                else -> 1
            }
        }
    }
}
