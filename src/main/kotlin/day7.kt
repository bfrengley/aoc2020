package aoc2020.day7

import aoc2020.util.loadTextResource

const val bagTypePattern = "([a-z]+ [a-z]+) bags?"
val outerBagRegex = Regex(bagTypePattern)
val innerBagRegex = Regex("(\\d+) $bagTypePattern")

data class Rule(val outer: String, val inners: List<Pair<String, Int>>)

fun main(args: Array<String>) {
    val rawRules = loadTextResource("/day7.txt").lines()
    val rules = rawRules.map(::parseRule)

    print("Part 1: ")
    val rules1 = bottomUpRules(rules)
    println(containingBags("shiny gold", rules1).size)

    print("Part 2: ")
    println(containedBags("shiny gold", rules.map { (outer, inner) -> Pair(outer, inner) }.toMap()))
}

fun parseRule(rule: String): Rule =
    rule.split("contain").let { (outerRaw, innersRaw) ->
        outerBagRegex.find(outerRaw)!!.groupValues[1].let { outer ->
            val inners = innerBagRegex.findAll(innersRaw).map {
                val (_, count, bagType) = it.groupValues
                Pair(bagType, count.toInt())
            }.toList()
            Rule(outer, inners)
        }
    }

fun bottomUpRules(rules: List<Rule>): Map<String, Set<String>> =
    rules
        // extract (outer, inner) pairs from the rules
        .flatMap { (outer, inners) -> inners.map { Pair(outer, it.first) } }
        // group them by the inner bag type (inner -> List<(outer, inner)>)
        .groupBy { it.second }
        // get the inners and convert them to a set
        .mapValues { (_, value) -> value.map { it.first }.toSet() }


fun containingBags(innerBag: String, rules: Map<String, Set<String>>): Set<String> =
    when (val outers = rules[innerBag]) {
        null -> setOf()
        else -> outers
            .map { containingBags(it, rules) }
            .fold(outers) { acc, allOuters -> acc.union(allOuters) }
    }

fun containedBags(outerBag: String, rules: Map<String, List<Pair<String, Int>>>): Int {
    fun containedBagsRec(outerBag_: String, rules_: Map<String, List<Pair<String, Int>>>): Int =
        1 + (rules_[outerBag_]
            ?.map { (bagType, count) -> count * containedBagsRec(bagType, rules_) }
            ?.sum()
            ?: 1)

    return containedBagsRec(outerBag, rules) - 1
}
