package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines
import java.util.*

object Day12 : Day {

    override val input = readInputLines(12).map { it.toCharArray() }.toTypedArray()

    override fun part1() = solve(listOf(NodeDistance(Node.START, 0)))

    override fun part2() = solve((findNode('a') + Node.START).map { NodeDistance(it, 0) })

    private fun solve(startingNodes: List<NodeDistance>): Int {
        val queue = PriorityQueue(startingNodes)
        val visited = mutableSetOf<Node>()
        while (queue.isNotEmpty()) {
            val (node, dist) = queue.poll()
            if (node in visited) continue;
            visited.add(node)
            if (node == Node.END) return dist
            queue.addAll(node.adjacent().filter { node.distance(it) < 2 }.map { NodeDistance(it, dist + 1) })
        }
        error("Unable to find a path")
    }

    data class NodeDistance(val node: Node, val distance: Int) : Comparable<NodeDistance> {

        override fun compareTo(other: NodeDistance) = distance.compareTo(other.distance)
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