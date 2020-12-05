import aoc2020.util.loadTextResource

fun main(args: Array<String>) {
    val nums = loadTextResource("/day1.txt").trim().lines().map { it.toInt() }

    nums.forEachIndexed loop@{ index, i ->
        val other = nums.subList(index, nums.lastIndex).find { it + i == 2020 }
        if (other != null) {
            print(i * other)
            return@loop
        }
    }
}
