package aoc2020.day11

import aoc2020.util.*

enum class Seat {
    EMPTY, FLOOR, OCCUPIED;

    companion object {
        fun parse(chr: Char): Seat = when (chr) {
            '#' -> OCCUPIED
            '.' -> FLOOR
            'L' -> EMPTY
            else -> throw IllegalArgumentException("Invalid value: $chr") // whatever
        }
    }
}

data class SeatState(
    val seats: List<List<Seat>>,
    val threshold: Int,
    val useExpandedSearch: Boolean
) {
    fun nextState() = seats.indices.map { x ->
        seats[x].indices.map { y ->
            when (val seat = seats[x][y]) {
                Seat.FLOOR -> seat
                else -> {
                    val occupied = occupiedNeighbours(x, y)
                    if (seat == Seat.EMPTY && occupied == 0) {
                        Seat.OCCUPIED
                    } else if (seat == Seat.OCCUPIED && occupied >= threshold) {
                        Seat.EMPTY
                    } else {
                        seat
                    }
                }
            }
        }
    }.let { SeatState(it, threshold, useExpandedSearch) }

    private fun occupiedNeighbours(x: Int, y: Int): Int =
        listOf(
            Pair(-1, -1),
            Pair(-1, 0),
            Pair(-1, 1),
            Pair(0, -1),
            Pair(0, 1),
            Pair(1, -1),
            Pair(1, 0),
            Pair(1, 1)
        )
            .map { (xOff, yOff) ->
                if (useExpandedSearch) {
                    val xs = generateSequence(x + xOff) { it + xOff }
                    val ys = generateSequence(y + yOff) { it + yOff }
                    xs.zip(ys)
                        .map { (x_, y_) ->
                            seats.getOrNull(x_)?.getOrNull(y_)
                        }
                        .firstOrNull { it != Seat.FLOOR }
                } else {
                    seats.getOrNull(x + xOff)?.getOrNull(y + yOff)
                }
            }.count {
                it == Seat.OCCUPIED
            }

    fun runToSteadyState(): SeatState {
        var state = this
        while (true) {
            val next = state.nextState()
            if (next == state) {
                break
            }
            state = next
        }
        return state
    }
}

fun main(args: Array<String>) {
    val seats = loadTextResource("/day11.txt").lines().map { line -> line.map(Seat::parse) }

    print("Part 1: ")
    println(
        SeatState(seats, 4, false)
            .runToSteadyState()
            .seats
            .map { it.count { it == Seat.OCCUPIED } }
            .sum()
    )

    print("Part 1: ")
    println(
        SeatState(seats, 5, true)
            .runToSteadyState()
            .seats
            .map { it.count { it == Seat.OCCUPIED } }
            .sum()
    )
}
