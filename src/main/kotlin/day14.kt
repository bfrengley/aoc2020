package aoc2020.day14

import aoc2020.util.loadTextResource

fun main(args: Array<String>) {
    val lines = loadTextResource("/day14.txt").lines()


}

fun part1(lines: Iterator<String>) {
    var mask = Bitmask.parse("")
    val mem = mutableListOf<Long>()

    for (line in lines) {
        when (val instr = Instr.parse(line)) {
            is Instr.Mask -> mask = instr.mask
            is Instr.Set -> {
                if (mem.size < instr.addr) {

                }
            }
        }
    }
}

sealed class Instr {
    class Mask(mask: Bitmask) : Instr()
    class Set(addr: Int, value: Long) : Instr()

    companion object {
        fun parse(str: String): Instr {
            val (l, r) = str.split('=')
            return if (l.startsWith("mask")) {
                Mask(Bitmask.parse(r.trim()))
            } else if (l.startsWith("mem")) {
                val addr = l.substring(l.indexOf('['), l.indexOf(']')).toInt()
                val value = r.trim().toLong()
                Set(addr, value)
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}

data class Bitmask(val setmask: Long, val clearmask: Long) {
    operator fun invoke(n: Long) = (n or setmask) and clearmask

    companion object {
        fun parse(str: String): Bitmask {
            var setmask = 0L
            var clearmask = 0L.inv()

            for ((i, c) in str.reversed().withIndex()) {
                when (c) {
                    '1' -> setmask = setmask or (1L shl i)
                    '0' -> clearmask = clearmask xor (1L shl i)
                    else -> {}
                }
            }

            return Bitmask(setmask, clearmask)
        }
    }
}
