import aoc2020.util.loadTextResource

fun main(args: Array<String>) {
    val lines = loadTextResource("/day5.txt").lines()

    print("Part 1: ")
    val ids = lines.map(::idForSeat)
    println(ids.maxOrNull())

    print("Part 2: ")
    println(
        ids.sorted().let {
            ((it.first()..it.last()) - it).first()
        }
    )
}

fun idForSeat(dirString: String) = dirs(dirString).let { (rowDirs, colDirs) ->
    val row = search(0, 127, rowDirs)
    val col = search(0, 7, colDirs)
    row * 8 + col
}

enum class Dir {
    FRONT, BACK;
}

fun dirs(dirString: String) = Pair(
    dirString.substring(0, 7).map { if (it == 'F') Dir.FRONT else Dir.BACK },
    dirString.substring(7).map { if (it == 'L') Dir.FRONT else Dir.BACK }
)

fun search(start: Int, end: Int, dirs: List<Dir>): Int = when (dirs.firstOrNull()) {
    null -> start
    Dir.FRONT -> search(start, (start + end) / 2, dirs.drop(1))
    Dir.BACK -> search(1 + (start + end) / 2, end, dirs.drop(1))
}
