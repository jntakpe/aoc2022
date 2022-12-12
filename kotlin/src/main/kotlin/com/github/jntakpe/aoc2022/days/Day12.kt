package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines

object Day12 : Day {

    override val input = readInputLines(12).map { it.toCharArray() }.toTypedArray()

    override fun part1(): Int {
        return solve(false)

    }

    override fun part2(): Int {
        return solve(true)
    }

    private fun solve(isPart2: Boolean): Int {
        val deque = ArrayDeque<Pair<Node, Int>>()
        val visited = mutableSetOf<Node>()
        deque.add(Node.START to 0)
        if (isPart2) {
            deque.addAll(findNode('a').map { it to 0 })
        }
        while (deque.isNotEmpty()) {
            val (node, dist) = deque.removeFirst()
            if (node in visited) {
                continue;
            }
            visited.add(node)
            if (node == Node.END) {
                return dist
            }
            val adjacent = node.adjacent()
            val sortedBy = buildList {
                for (adj in adjacent) {
                    if (node.distance(adj) < 2) {
                        add(adj to dist + 1)
                    }
                }
            }.sortedBy { it.first.char - node.char }
            deque.addAll(sortedBy)
        }
        return -1
    }

    data class Node(val char: Char, val x: Int, val y: Int) {

        companion object {

            val START = findNode('S').single()
            val END = findNode('E').single()
        }

        fun adjacent(): List<Node> {
            return listOf(x - 1 to y, x + 1 to y, x to y - 1, x to y + 1)
                .mapNotNull { (x, y) -> input.getOrNull(y)?.getOrNull(x)?.let { Node(it, x, y) } }
        }

        fun distance(other: Node) = other.height() - height()

        private fun height() = when (char) {
            START.char -> 'a'
            END.char -> 'z'
            else -> char
        }.code
    }

    private fun findNode(char: Char): List<Node> {
        return input
            .flatMapIndexed { y, a -> a.toList().mapIndexedNotNull { x, c -> if (c == char) y to x else null } }
            .map { (y, x) -> Node(char, x, y) }
    }
}