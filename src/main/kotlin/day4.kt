import aoc2020.util.asBlocks
import aoc2020.util.loadTextResource

data class Passport(
    val byr: String,
    val iyr: String,
    val eyr: String,
    val hgt: String,
    val hcl: String,
    val ecl: String,
    val pid: String,
    val cid: String?,
) {
    companion object {
        // is this too :galaxybrain:
        // idk if this duplication is actually worth it but hey, it handles existence checks
        private fun from(map: Map<String, String>) = object {
            val byr by map
            val iyr by map
            val eyr by map
            val hgt by map
            val hcl by map
            val ecl by map
            val pid by map
            val cid = map["cid"]

            val data = Passport(byr, iyr, eyr, hgt, hcl, ecl, pid, cid)
        }.data

        fun tryFrom(map: Map<String, String>) = try {
            from(map)
        } catch (_: NoSuchElementException) {
            null
        }
    }

    fun validate(): Boolean {
        try {
            val datesValid = this.byr.length == 4 && this.byr.toInt() in 1920..2002 &&
                    this.iyr.length == 4 && this.iyr.toInt() in 2010..2020 &&
                    this.eyr.length == 4 && this.eyr.toInt() in 2020..2030

            val height = this.hgt.takeWhile { it.isDigit() }.toInt()
            val heightType = this.hgt.dropWhile { it.isDigit() }
            val heightValid = when (heightType) {
                "cm" -> height in 150..193
                "in" -> height in 59..76
                else -> false
            }

            val hairValid = this.hcl[0] == '#' &&
                    this.hcl.substring(1).all { it in '0'..'9' || it in 'a'..'f' }

            val eyesValid = this.ecl in setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
            val pidValid = this.pid.length == 9 && this.pid.all { it.isDigit() }

            return datesValid && heightValid && hairValid && eyesValid && pidValid
        } catch (_: NumberFormatException) {
            return false
        }
    }
}

fun main(args: Array<String>) {
    val lines = loadTextResource("/day4.txt").lines()

    print("Part 1: ")
    val passports = loadPassports(lines)
    println(passports.size)

    print("Part 2: ")
    println(passports.filter { it.validate() }.size)
}

fun loadPassports(lines: List<String>) =
    lines.asBlocks().mapNotNull { block -> parsePassport(block.flatMap { it.split(' ') }) }

fun parsePassport(fields: List<String>) = Passport.tryFrom(
    fields
        .map { it.split(':') }
        .associate { (k, v) -> Pair(k, v) }
)
