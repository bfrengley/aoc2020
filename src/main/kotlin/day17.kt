package aoc2020.day17

import aoc2020.util.loadTextResource
import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    val initialState = loadTextResource("/day17.txt").lines().flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, cell ->
            when (cell) {
                '#' -> Point(x, y, 0)
                else -> null
            }
        }
    }.toSet().let(State::new)

    println("Part 1: ${initialState.successors().drop(6).first().countActive()}")
}

val OFFSETS = sequence {
    for (xOff in -1..1)
    for (yOff in -1..1)
    for (zOff in -1..1)

    if (!(xOff == 0 && yOff == 0 && zOff == 0)) {
        yield(Point(xOff, yOff, zOff))
    }
}.toList()

data class Point(val x: Int, val y: Int, val z: Int) {
    operator fun plus(p: Point) = Point(x + p.x, y + p.y, z + p.z)
    fun neighbours() = OFFSETS.map(this::plus)
}

data class State(val world: Set<Point>, val dims: Pair<Point, Point>) {
    fun countActive() = world.size

    private fun countLivingNeighbours(p: Point) = p.neighbours().count { it in world }

    fun tick(): State {
        val (low, high) = dims
        val nextState = mutableSetOf<Point>()

        for (x in (low.x - 1)..(high.x + 1))
        for (y in (low.y - 1)..(high.y + 1))
        for (z in (low.z - 1)..(high.z + 1)) {
            val p = Point(x, y, z)
            val livingNeighbours = countLivingNeighbours(p)

            if (p in world && livingNeighbours in 2..3 || p !in world && livingNeighbours == 3) {
                nextState.add(p)
            }
        }

        return State(nextState, dims(nextState))
    }

    companion object {
        fun dims(world: Set<Point>): Pair<Point, Point> {
            if (world.isEmpty()) {
                throw IllegalArgumentException("world")
            }

            var minX = Int.MAX_VALUE
            var minY = Int.MAX_VALUE
            var minZ = Int.MAX_VALUE
            var maxX = Int.MIN_VALUE
            var maxY = Int.MIN_VALUE
            var maxZ = Int.MIN_VALUE

            for (p in world) {
                minX = min(p.x, minX)
                minY = min(p.y, minY)
                minZ = min(p.z, minZ)
                maxX = max(p.x, maxX)
                maxY = max(p.y, maxY)
                maxZ = max(p.z, maxZ)
            }

            return Pair(Point(minX, minY, minZ), Point(maxX, maxY, maxZ))
        }

        fun new(world: Set<Point>) = State(world, dims(world))
    }

    fun successors() = this.let {
        sequence {
            var s = it
            while (true) {
                yield(s)
                s = s.tick()
            }
        }
    }
}
