import aoc2020.util.loadTextResource

fun main(args: Array<String>) {
    val lines = loadTextResource("/day2.txt").lines()

    // part 1
    print("Part 1: ")
    println(lines.count { validate(it) { rule, pass -> rule.checkV1(pass) } })

    // part 2
    print("Part 2: ")
    println(lines.count { validate(it) { rule, pass -> rule.checkV2(pass) } })
}

data class Rule(val letter: Char, val min: Int, val max: Int) {
    fun checkV1(pass: String) = pass.count { it == letter } in min..max
    fun checkV2(pass: String) =
        (pass[min - 1] == letter && pass[max - 1] != letter) ||
            (pass[max - 1] == letter && pass[min - 1] != letter)
}

fun validate(line: String, check: (Rule, String) -> Boolean): Boolean {
    val rule = line.substringBefore(':').trim()
    val pass = line.substringAfter(':').trim()

    return check(parseRule(rule), pass)
}

fun parseRule(rule: String): Rule {
    val dashIdx = rule.indexOf('-')
    val spaceIdx = rule.indexOf(' ')

    val letter = rule[spaceIdx + 1]
    val min = rule.substring(0 until dashIdx).toInt()
    val max = rule.substring(dashIdx + 1 until spaceIdx).toInt()

    return Rule(letter, min, max)
}
