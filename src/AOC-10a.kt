package exercise10a

import exercise8B.Point3D
import myUtils.*
import kotlin.math.abs
import kotlin.math.sqrt
import java.util.BitSet


private val INDICATOR_RE = Regex("""\[([.#]+)]""")
private val BUTTONS_RE   = Regex("""\(([^)]*)\)""")
private val VOLTAGES_RE  = Regex("""\{([^}]*)}""")


data class Machine(
    val lights: BitSet,
    val buttons: List<BitSet>,
    val voltages: List<Int>
)



fun parseMachineBitSet(line: String): Machine {
    val indicator = INDICATOR_RE.find(line)?.groupValues?.get(1)
        ?: error("Missing indicator lights: $line")
    val n = indicator.length

    val lights = BitSet(n)
    indicator.forEachIndexed { idx, ch ->
        if (ch == '#') lights.set(idx)
        else require(ch == '.') { "Only '.' or '#' allowed in indicator: $line" }
    }

    val buttons: List<BitSet> = BUTTONS_RE.findAll(line).map { m ->
        val b = BitSet(n)
        val raw = m.groupValues[1].trim()
        if (raw.isNotEmpty()) {
            raw.split(',').forEach { token ->
                val i = token.trim().toInt()
                require(i in 0 until n) { "Button index $i out of range 0..${n - 1}" }
                b.set(i)
            }
        }
        b
    }.toList()

    val voltages: List<Int> = VOLTAGES_RE.find(line)?.groupValues?.get(1)
        ?.split(',')
        ?.mapNotNull { it.trim().takeIf(String::isNotEmpty)?.toInt() }
        ?: emptyList()

    return Machine(lights, buttons, voltages)
}



fun minPressSetBruteforce(machine: Machine): List<Int> {
    val m = machine.buttons.size
    var bestMask = -1
    var bestCount = Int.MAX_VALUE

    val total = 1 shl m
    for (mask in 0 until total) {
        val state = BitSet() // start with all lights off
        for (j in 0 until m) {
            if ((mask ushr j) and 1 == 1) {
                state.xor(machine.buttons[j])
            }
        }
        if (state == machine.lights) {
            val presses = Integer.bitCount(mask)
            if (presses < bestCount) {
                bestCount = presses
                bestMask = mask

            }
        }
    }

    if (bestMask < 0) return emptyList() // no solution found
    return (0 until m).filter { ((bestMask ushr it) and 1) == 1 }
}




fun main() {

    var answer:Int = 0
    val lines  = readLinesFromFile("AAC-10A.txt")

    lines.forEach {
        println(it)
        var machine = parseMachineBitSet(it)
        println(machine)
        val amount = minPressSetBruteforce(machine)
        println(amount.size)
        answer += amount.size
    }

    println(answer)

}
