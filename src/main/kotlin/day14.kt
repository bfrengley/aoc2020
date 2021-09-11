package aoc2020.day14

import aoc2020.util.loadTextResource

val toBinaryString = java.lang.Long::toBinaryString

fun main(args: Array<String>) {
    val instrs = loadTextResource("/day14.txt").lines().map(Instr::parse)

    part1(instrs)
    part2(instrs)
}

fun part1(instrs: List<Instr>) {
    var mask = Bitmask.parse("")
    val mem = mutableMapOf<Long, Long>()

    for (instr in instrs) {
        when (instr) {
            is Instr.Mask -> mask = instr.mask
            is Instr.Set -> {
                mem[instr.addr] = mask(instr.value)
            }
        }
    }

    println("Part 1: ${mem.values.sum()}")
}

fun part2(instrs: List<Instr>) {
    var mask = Bitmask.parse("")
    val mem = mutableMapOf<Long, Long>()

    for (instr in instrs) {
        when (instr) {
            is Instr.Mask -> mask = instr.mask
            is Instr.Set -> {
                for (addr in mask.maskAddress(instr.addr)) {
                    mem[addr] = instr.value
                }
            }
        }
    }

    println("Part 2: ${mem.values.sum()}")
}

sealed class Instr {
    class Mask(val mask: Bitmask) : Instr()
    class Set(val addr: Long, val value: Long) : Instr()

    companion object {
        fun parse(str: String): Instr {
            val (l, r) = str.split('=')
            return if (l.startsWith("mask")) {
                Mask(Bitmask.parse(r.trim()))
            } else if (l.startsWith("mem")) {
                val addr = l.substring(l.indexOf('[') + 1, l.indexOf(']')).toLong()
                val value = r.trim().toLong()
                Set(addr, value)
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}

data class Bitmask(val setmask: Long, val clearmask: Long, val floatmask: Long) {
    private val floatmaskBits = toBinaryString(floatmask).reversed()

    operator fun invoke(n: Long) = (n or setmask) and clearmask

    fun maskAddress(addr: Long) = ((addr or setmask) and floatmask.inv()).let { baseAddr ->
        floatmaskBits.foldIndexed(listOf(baseAddr)) { i, masks, b ->
            if (b == '1') masks + masks.map { it or (1L shl i) } else masks
        }
    }

    override fun toString(): String {
        return "Bitmask(setmask=${
            toBinaryString(setmask)
        }, clearmask=${
            toBinaryString(clearmask.inv())
        }, floatmask=${
            toBinaryString(floatmask)
        })"
    }

    companion object {
        fun parse(str: String): Bitmask {
            var setmask = 0L
            var clearmask = 0L.inv()
            var floatmask = 0L

            for ((i, c) in str.reversed().withIndex()) {
                when (c) {
                    '1' -> setmask = setmask or (1L shl i)
                    '0' -> clearmask = clearmask xor (1L shl i)
                    else -> floatmask = floatmask or (1L shl i)
                }
            }

            return Bitmask(setmask, clearmask, floatmask)
        }
    }
}
