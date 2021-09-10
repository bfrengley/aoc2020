package aoc2020.day13

import aoc2020.util.loadTextResource

fun main(args: Array<String>) {
    val (earliestLine, busLine) = loadTextResource("/day13.txt").lines()
    val earliestTime = earliestLine.toLong()
    val busList = busLine.split(',')
        .mapIndexedNotNull { i, bus ->
            bus.toLongOrNull()?.let { Pair(i, it) }
        }.toList().also(::println)
    val busIds = busList.map { (_, b) -> b }

    print("Part 1: ")
    val earliestBus = busIds.minByOrNull { it - (earliestTime % it) }!!
    println(earliestBus * (earliestBus - (earliestTime % earliestBus)))

//    print("Part 2: ")
//    val idsFromMin = busIds.sorted()
//    val busTimes = sequence {
//        for (i in earliestTime..Long.MAX_VALUE) {
//            if (idsFromMin.any { i % it == 0L }) {
//                yield(i)
//            }
//        }
//    }
//    busTimes.windowed(busIds.size).find {
//        it.zip(busList).windowed(2).parallelStream().allMatch { pair ->
//            val (t1, busPair1) = pair.first()
//            val (i1, bus1) = busPair1
//            val (t2, busPair2) = pair.last()
//            val (i2, bus2) = busPair2
//            (t2 - t1) == (i2 - i1).toLong() && t1 % bus1 == 0L && t2 % bus2 == 0L
//        }
//    }?.let(::println)
}
