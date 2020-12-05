import aoc2020.util.loadTextResource

fun main(args: Array<String>) {
    val nums = loadTextResource("/day1.txt").lines().map { it.toInt() }

    // part 1
    print("Part 1: ")
    nums.forEachIndexed loop@{ index, i ->
        val other = nums.subList(index, nums.lastIndex).find { it + i == 2020 }
        if (other != null) {
            println(i * other)
            return@loop
        }
    }

    // part 2
    print("Part 2: ")
    nums.forEachIndexed loop@{ i, first ->
        nums.subList(i, nums.lastIndex).forEachIndexed { j, second ->
            nums.subList(j, nums.lastIndex).forEach {
                if (first + second + it == 2020) {
                    println(first * second * it)
                    return@loop
                }
            }
        }
    }
}
