package aoc2020.day13

import aoc2020.util.loadTextResource

fun main(args: Array<String>) {
    val (earliestLine, busLine) = loadTextResource("/day13.txt").lines()
    val earliestTime = earliestLine.toLong()
    val busList = busLine.split(',')
        .mapIndexedNotNull { i, bus ->
            bus.toLongOrNull()?.let { Pair(i, it) }
        }.toList()
    val busIds = busList.map { (_, b) -> b }

    print("Part 1: ")
    val earliestBus = busIds.minByOrNull { it - (earliestTime % it) }!!
    println(earliestBus * (earliestBus - (earliestTime % earliestBus)))

    print("Part 2: ")
    val res = gaussianCRT(busIds, busList.map { it.second - it.first })
    println(res)
}

// https://shainer.github.io/crypto/math/2017/10/22/chinese-remainder-theorem.html
fun gaussianCRT(ns: List<Long>, vs: List<Long>): Long {
    var res = 0L
    val prod = ns.fold(1L) { acc, n -> acc * n }

    for ((n, v) in ns.zip(vs)) {
        val p = prod / n
        res += v * p * multInv(p, n)
    }
    return res % prod
}

fun multInv(a: Long, b: Long): Long {
    if (b == 1L) return 1L
    var aa = a
    var bb = b
    var x0 = 0L
    var x1 = 1L
    while (aa > 1L) {
        val q = aa / bb
        var t = bb
        bb = aa % bb
        aa = t
        t = x0
        x0 = x1 - q * x0
        x1 = t
    }
    if (x1 < 0L) x1 += b
    return x1
}
