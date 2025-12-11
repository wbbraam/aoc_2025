package exercise10b

import exercise8B.Point3D
import myUtils.*
import kotlin.math.abs
import kotlin.math.sqrt
import java.util.BitSet
import kotlin.math.ceil
import java.util.PriorityQueue


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


fun minPressesVoltages(machine: Machine): Int {
    val target = machine.voltages.toIntArray()
    val m = target.size

    // Build button effect vectors
    val effects = machine.buttons.map { btn ->
        IntArray(m) { i -> if (btn.get(i)) 1 else 0 }
    }

  //  // Quick feasibility check
  //  val touched = BooleanArray(m)
  //  for (eff in effects) for (i in eff.indices) if (eff[i] == 1) touched[i] = true
  //  for (i in 0 until m) if (target[i] > 0 && !touched[i]) return -1

    val maxCov = effects.maxOf { it.sum() }

    data class Node(val g: Int, val h: Int, val state: IntArray) {
        val f: Int get() = g + h
    }

    fun heuristic(state: IntArray): Int {
        var sumR = 0
        var maxR = 0
        for (i in state.indices) {
            val r = target[i] - state[i]
            sumR += r
            if (r > maxR) maxR = r
        }
        val lb2 = (sumR + maxCov - 1) / maxCov // ceil
        return maxOf(maxR, lb2)
    }

    fun isGoal(state: IntArray): Boolean {
        for (i in state.indices) if (state[i] != target[i]) return false
        return true
    }

    val pq = PriorityQueue<Node>(compareBy { it.f })
    val start = IntArray(m) { 0 }
    pq.add(Node(0, heuristic(start), start))
    val best = HashMap<String, Int>()
    best[start.joinToString(",")] = 0

    while (pq.isNotEmpty()) {
        val cur = pq.poll()
        if (isGoal(cur.state)) return cur.g

        for (eff in effects) {
            val next = cur.state.copyOf()
            var ok = true
            for (i in 0 until m) {
                val v = next[i] + eff[i]
                if (v > target[i]) { ok = false; break }
                next[i] = v
            }
            if (!ok) continue
            val key = next.joinToString(",")
            val gNext = cur.g + 1
            if (gNext < best.getOrDefault(key, Int.MAX_VALUE)) {
                best[key] = gNext
                pq.add(Node(gNext, heuristic(next), next))
            }
        }
    }
    return -1 // unreachable
}






fun main() {

    var answer:Int = 0
    val lines  = readLinesFromFile("AAC-10A.txt")

    lines.forEach {
        println(it)
        var machine = parseMachineBitSet(it)
        println(machine)
        val amount = minPressesVoltages(machine) ?: 0
        println(amount)
        answer += amount
    }

    println(answer)

}
