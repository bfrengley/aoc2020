package aoc2020.day8

import aoc2020.util.loadTextResource

enum class Operation {
    NOP, ACC, JMP;
}

data class Instruction(val op: Operation, val arg: Int) {
    companion object {
        fun parse(instrStr: String) = Instruction(
            Operation.valueOf(instrStr.substringBefore(' ').toUpperCase()),
            instrStr.substringAfter(' ').toInt()
        )
    }
}

class Program(private val instrs: List<Instruction>) {
    data class State(val acc: Int, val pc: Int) {
        fun next(instr: Instruction) = when (instr.op) {
            Operation.ACC -> State(acc + instr.arg, pc + 1)
            Operation.JMP -> State(acc, pc + instr.arg)
            Operation.NOP -> State(acc, pc + 1)
        }
    }

    enum class TerminationState {
        LOOP, SUCCESS, OUT_OF_BOUNDS
    }

    private fun nextState(state: State) = state.next(instrs[state.pc])

    fun runToFirstLoop(): Pair<TerminationState, State> {
        val seen = mutableSetOf<Int>()
        var state = State(0, 0)

        while (state.pc !in seen && state.pc in instrs.indices) {
            seen.add(state.pc)
            state = nextState(state)
        }

        return when (state.pc) {
            in instrs.indices -> Pair(TerminationState.LOOP, state)
            instrs.size -> Pair(TerminationState.SUCCESS, state)
            else -> Pair(TerminationState.OUT_OF_BOUNDS, state)
        }
    }
}

fun main(args: Array<String>) {
    val instrs = loadTextResource("/day8.txt").lines().map(Instruction::parse)

    println("Part 1: ${Program(instrs).runToFirstLoop()}")
    println("Part 2: ${solveLoop(instrs.toMutableList())}")
}


fun solveLoop(instrs: MutableList<Instruction>): Program.State {
    val flips = instrs.indices.filter { instrs[it].op != Operation.ACC }

    for (flipIdx in flips) {
        // flip the instruction in-place to its opposite
        flip(instrs, flipIdx)

        val (term, state) = Program(instrs).runToFirstLoop()
        if (term == Program.TerminationState.SUCCESS) {
            return state
        }

        // flip it back
        flip(instrs, flipIdx)
    }

    return Program.State(-1, 0)
}

fun flip(instrs: MutableList<Instruction>, idx: Int) {
    val (op, arg) = instrs[idx]

    if (op == Operation.JMP) {
        instrs[idx] = Instruction(Operation.NOP, arg)
    } else if (op == Operation.NOP) {
        instrs[idx] = Instruction(Operation.JMP, arg)
    }
}
