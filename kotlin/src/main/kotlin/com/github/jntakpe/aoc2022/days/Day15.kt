package com.github.jntakpe.aoc2022.days

import com.github.jntakpe.aoc2022.shared.Day
import com.github.jntakpe.aoc2022.shared.readInputLines
import kotlin.math.abs

object Day15 : Day {

    override val input = readInputLines(15).map { SensorBeacon.from(it) }

    override fun part1(): Int {
        val y = 10L
        val possible = mutableSetOf<Long>()
        input.forEach {
            val x = it.distance - abs(it.sensor.y - y)
            possible += it.sensor.x - x..it.sensor.x + x
        }
        val beacons = input.map { it.beacon }
        return possible.count { Position(it, y) !in beacons }
    }

    override fun part2(): Long {
        val max = if (isTest()) 20 else 4000000
        input.forEach { (sensor, _, distance) ->
            for (distanceX in 0..(distance + 1)) {
                val distanceY = distance + 1 - distanceX
                for ((dirX, dirY) in listOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1)) {
                    val (bX, bY) = sensor.x + distanceX * dirX to sensor.y + distanceY * dirY
                    if (bX !in 0..max || bY !in 0..max)
                        continue
                    if (input.none { abs(it.sensor.y - bY) + abs(it.sensor.x - bX) <= it.distance })
                        return bX * 4000000 + bY
                }
            }
        }
        return -1
    }

    private fun isTest() = input.maxOf { it.sensor.x } <= 100

    data class Position(val x: Long, val y: Long)

    data class SensorBeacon(val sensor: Position, val beacon: Position, val distance: Long) {
        companion object {

            fun from(line: String): SensorBeacon {
                val sensorX = line.substringAfter("x=").substringBefore(",").toLong()
                val beaconX = line.substringAfterLast("x=").substringBeforeLast(",").toLong()
                val sensorY = line.substringAfter("y=").substringBefore(":").toLong()
                val beaconY = line.substringAfterLast("y=").trim().toLong()
                return SensorBeacon(
                    Position(sensorX, sensorY),
                    Position(beaconX, beaconY),
                    abs(sensorX - beaconX) + abs(sensorY - beaconY)
                )
            }
        }
    }
}