package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines

object Day7 : Day {

    override val input = buildTree(parseCommands(readInputLines(7)))

    override fun part1() = input.dirSizes().filter { it <= 100000 }.sum()

    override fun part2() = input.dirSizes().run { filter { it >= 30000000 - (70000000 - max()) }.min() }

    class Directory(
        val parent: Directory?,
        var directories: Map<String, Directory> = emptyMap(),
        var files: List<Long> = emptyList()
    ) {

        fun root(): Directory {
            var cwd = this
            while (cwd.parent != null) {
                cwd = cwd.parent!!
            }
            return cwd
        }

        fun dirSizes(cwd: Directory = this): List<Long> = buildList {
            add(cwd.size())
            cwd.directories.values.forEach { addAll(dirSizes(it)) }
        }

        private fun size(): Long = directories.values.sumOf { it.size() } + files.sum()
    }

    sealed interface Command {
        class CD(private val path: String) : Command {

            fun navigate(cwd: Directory): Directory {
                return when (path) {
                    "/" -> cwd.root()
                    ".." -> cwd.parent!!
                    else -> cwd.directories.getValue(path)
                }
            }
        }

        class LS(private val sdtout: List<String>) : Command {

            fun update(current: Directory) {
                current.directories = subdirectories(current)
                current.files = sizes()
            }

            private fun sizes(): List<Long> {
                return sdtout
                    .filter { !it.startsWith("dir") }
                    .map { it.split(" ").first().toLong() }
            }

            private fun subdirectories(current: Directory): Map<String, Directory> {
                return sdtout
                    .filter { it.startsWith("dir") }
                    .map { it.substring(4).trim() }
                    .associateWith { Directory(current) }
            }
        }
    }

    private fun parseCommands(lines: List<String>): List<Command> {
        return buildList {
            lines.forEachIndexed { i, s ->
                when {
                    s.startsWith("$ cd") -> add(Command.CD(s.substring(4).trim()))
                    s.startsWith("$ ls") -> add(Command.LS(lines.drop(i + 1).takeWhile { it.first() != '$' }))
                }
            }
        }
    }

    private fun buildTree(commands: List<Command>): Directory {
        var cwd = Directory(null)
        commands.forEach {
            when (it) {
                is Command.CD -> cwd = it.navigate(cwd)
                is Command.LS -> it.update(cwd)
            }
        }
        return cwd.root()
    }
}