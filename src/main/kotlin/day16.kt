package aoc2020.day16

import aoc2020.util.loadTextResource

fun main(args: Array<String>) {
    val lines = loadTextResource("/day16.txt").lines()

    val fields = lines.takeWhile { it.isNotBlank() }.toList()
    val ticket = lines.drop(fields.size + 2).first().split(',').map { it.toInt() }
    val nearTickets = lines.drop(fields.size + 5).map { ticketStr ->
        ticketStr.split(',').map { it.toInt() }
    }

    val ticketProps = TicketProps(fields)

    val (validTickets, invalidTickets) =
        nearTickets.partition { ticketProps.findInvalidField(it) == null }

    println("Part 1: ${
        invalidTickets.mapNotNull { ticketProps.findInvalidField(it) }.sum()
    }")

    part2(ticketProps, ticket, validTickets)
}

fun part2(props: TicketProps, myTicket: List<Int>, nearTickets: List<List<Int>>) {
    val assignments = nearTickets.map { props.assignments(it) }
    val candidates = assignments.reduce { assgns, ticket ->
        assgns.zip(ticket).map { (a1, a2) -> a1.intersect(a2) }
    }

    val mutableCandidates = candidates.toMutableList()
    val propsByCount = props.props.sortedBy { prop -> candidates.count { prop in it } }
    for (prop in propsByCount) {
        reduceCandidates(prop, mutableCandidates)
    }

    assert(mutableCandidates.all { it.size == 1 })

    val departureProduct = myTicket
        .zip(mutableCandidates.map { it.first() })
        .fold(1L) { acc, (value, cand) ->
            when (cand.name.startsWith("departure")) {
                true -> acc * value.toLong()
                false -> acc
            }
        }
    println("Part 3: $departureProduct")
}

fun reduceCandidates(prop: TicketProps.Prop, candidates: MutableList<Set<TicketProps.Prop>>) {
    val candIdx = candidates.indexOfFirst { prop in it }
    candidates[candIdx] = setOf(prop)
}

class TicketProps(fields: List<String>) {
    data class Prop(val name: String, val ranges: List<IntRange>) {
        fun contains(value: Int) = ranges.any { value in it }

        override fun equals(other: Any?) = other is Prop && name == other.name
        override fun hashCode() = name.hashCode()
    }

    val props: List<Prop> = fields.map { propStr ->
        val (name, rangeStrs) = propStr.split(":")
        val ranges = rangeStrs.split("or").map { range ->
            val (start, end) = range.trim().split('-')
            (start.toInt()..end.toInt())
        }
        Prop(name, ranges)
    }

    fun findInvalidField(fieldVals: List<Int>) = fieldVals.find { value ->
        props.all { !it.contains(value) }
    }

    fun assignments(fieldVals: List<Int>) = fieldVals.map { value ->
        props.filter { it.contains(value) }.toSet()
    }
}
