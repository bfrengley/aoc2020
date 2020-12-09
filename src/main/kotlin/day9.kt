package aoc2020.day9

import aoc2020.util.*

fun main(args: Array<String>) {
    val vals = loadTextResource("/day9.txt").lines().map { it.toLong() }

    print("Part 1: ")
    println(XmasSequence(25, vals).validate())

    print("Part 2: ")
    println(XmasSequence(25, vals).findWeakness())
}

data class XmasSequence(val preambleSize: Int, val numbers: List<Long>) {
    fun validate(): Result<Unit, Pair<Int, Long>> {
        val failed = numbers.indices.windowed(preambleSize + 1).find {
            val sum = numbers[it.last()]
            val source = numbers.subList(it.first(), it.last())
            source.pairs().none { (a, b) -> a + b == sum }
        }

        return when (failed) {
            null -> Ok(Unit)
            else -> {
                val idx = failed.last()
                Err(Pair(idx, numbers[idx]))
            }
        }
    }

    fun findWeakness(): Long? {
        val (idx, wrongNum) = validate().err() ?: return null

        var start = 0
        var end = 1
        // keep a running sum to avoid recalculating the sum of intermediate values every time we
        // move an end
        var sum = numbers[start] + numbers[end]

        while (end < idx) {
            if (sum == wrongNum) {
                val vals = numbers.subList(start, end + 1)
                return vals.minOrNull()!! + vals.maxOrNull()!!
            }

            if (sum < wrongNum) {
                // this sequence is too small, so extend it
                end += 1
                sum += numbers[end]
            } else {
                // this sequence is too large, so drop the first number
                sum -= numbers[start]
                start += 1

                // a sequence must be at least two values, so we have to extend it if start == end
                if (start == end) {
                    end += 1
                    sum += numbers[end]
                }
            }
        }

        return null
    }
}
