package aoc2020.day12

import aoc2020.util.loadTextResource
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.roundToInt

fun main(args: Array<String>) {
    val actions = loadTextResource("/day12.txt").lines().map(Action::parse)

    print("Part 1: ")
    val ship1 = Ship.withWaypoint(null)
    println(actions
        .fold(ship1) { ship, action -> ship.move(action) }
        .pos.manhattanDist(Pos.Origin)
    )

    print("Part 2: ")
    val ship2 = Ship.withWaypoint(Pos(10, 1))
    println(actions
        .fold(ship2) { ship, action -> ship.move(action) }
        .pos.manhattanDist(Pos.Origin)
    )
}

data class Pos(val x: Int, val y: Int) {
    fun manhattanDist(pos: Pos) = abs(x - pos.x) + abs(y - pos.y)

    fun move(action: Action) = when (action) {
        is Action.North -> Pos(x, y + action.value)
        is Action.South -> Pos(x, y - action.value)
        is Action.East -> Pos(x + action.value, y)
        is Action.West -> Pos(x - action.value, y)
        is Action.Left -> Pos(
            (cos(degToRad(action.value)) * x - sin(degToRad(action.value)) * y).roundToInt(),
            (sin(degToRad(action.value)) * x + cos(degToRad(action.value)) * y).roundToInt()
        )
        is Action.Right -> Pos(
            (cos(degToRad(-action.value)) * x - sin(degToRad(-action.value)) * y).roundToInt(),
            (sin(degToRad(-action.value)) * x + cos(degToRad(-action.value)) * y).roundToInt()
        )
        else -> Pos(x, y)
    }

    companion object {
        val Origin = Pos(0, 0)
    }
}

data class Ship(val pos: Pos, val heading: Int, val waypoint: Pos?) {
    fun move(action: Action) = if (waypoint != null) {
        when (action) {
            is Action.Forward -> Ship(
                Pos(pos.x + waypoint.x * action.value, pos.y + waypoint.y * action.value),
                heading,
                waypoint
            )
            else -> Ship(pos, heading, waypoint.move(action))
        }
    } else {
        when (action) {
            is Action.Left -> Ship(pos, heading + action.value, waypoint)
            is Action.Right -> Ship(pos, heading - action.value, waypoint)
            is Action.Forward -> Ship(
                Pos(
                    pos.x + (cos(degToRad(heading)) * action.value).toInt(),
                    pos.y + (sin(degToRad(heading)) * action.value).toInt()
                ),
                heading,
                waypoint
            )
            else -> Ship(pos.move(action), heading, waypoint)
        }
    }

    companion object {
        fun withWaypoint(waypoint: Pos?) = Ship(Pos.Origin, 0, waypoint)
    }
}

fun degToRad(deg: Int) = 2 * PI * deg.toDouble() / 360.0

sealed class Action(val value: Int) {
    class North(value: Int) : Action(value)
    class South(value: Int) : Action(value)
    class East(value: Int) : Action(value)
    class West(value: Int) : Action(value)
    class Left(value: Int) : Action(value)
    class Right(value: Int) : Action(value)
    class Forward(value: Int) : Action(value)

    override fun toString(): String {
        return "${this.javaClass.simpleName}($value)"
    }

    companion object {
        private val actionRegex = Regex("([A-Z])(\\d+)")

        fun parse(actionStr: String): Action {
            val (name, valueStr) = actionRegex.matchEntire(actionStr)?.destructured
                ?: throw IllegalArgumentException()

            val value = valueStr.toInt()
            return when (name) {
                "N" -> North(value)
                "S" -> South(value)
                "E" -> East(value)
                "W" -> West(value)
                "L" -> Left(value)
                "R" -> Right(value)
                "F" -> Forward(value)
                else -> throw IllegalArgumentException()
            }
        }
    }
}
