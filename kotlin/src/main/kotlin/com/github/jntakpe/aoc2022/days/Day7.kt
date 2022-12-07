package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines

object Day7 : Day {

    override val input = readInputLines(7)

    override fun part1(): Any {
        val mutableListOf = mutableListOf<Long>()
        total(buildTree(parseCommands(input)), mutableListOf)
        return mutableListOf.filter { it <= 100000 }.sum()

    }

    override fun part2(): Any {
        val sizes = mutableListOf<Long>()
        total(buildTree(parseCommands(input)), sizes)
        val unused = 70000000 - sizes.max()
        val toFree = 30000000 - unused
        val filter = sizes.filter { it >= toFree }
        return filter.min()
    }

    fun total(current: Directory, dirs: MutableList<Long>) {
        dirs.add(current.totalSize())
        current.directories.values.forEach { total(it, dirs) }
    }

    data class Directory(
        val parent: Directory?,
        var directories: Map<String, Directory> = emptyMap(),
        var files: List<Long> = emptyList()
    ) {

        fun totalSize(): Long {
            return directories.values.sumOf { it.totalSize() } + files.sum()
        }

    }

    sealed interface Command {
        class CD(val path: String) : Command {

            fun navigate(current: Directory): Directory {
                return when (path) {
                    "/" -> {
                        var dir = current
                        while (dir.parent != null) {
                            dir = dir.parent!!
                        }
                        return dir
                    }

                    ".." -> current.parent!!
                    else -> current.directories.getValue(path)
                }
            }
        }

        class LS(val result: List<String>) : Command {

            fun update(current: Directory) {
                if (current.directories.isEmpty()) {
                    current.directories = result
                        .filter { it.startsWith("dir") }
                        .map { it.substring(4).trim() }
                        .associateWith { Directory(current) }
                }
                current.files = result
                    .filter { !it.startsWith("dir") }
                    .map { it.split(" ").first().toLong() }
            }
        }
    }

    fun parseCommands(lines: List<String>): List<Command> {
        return buildList {
            lines.forEachIndexed { i, s ->
                when {
                    s.startsWith("$ cd") -> add(Command.CD(s.substring(4).trim()))
                    s.startsWith("$ ls") -> add(Command.LS(lines.drop(i + 1).takeWhile { l -> l.first() != '$' }))
                }
            }
        }
    }

    fun buildTree(commands: List<Command>): Directory {
        var current = Directory(null)
        commands.forEach {
            when (it) {
                is Command.CD -> current = it.navigate(current)
                is Command.LS -> it.update(current)
            }
        }
        return Command.CD("/").navigate(current)
    }
}