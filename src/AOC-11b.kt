package exercise11b

import exercise8B.Point3D
import myUtils.*
import kotlin.math.abs
import kotlin.math.sqrt
import java.util.BitSet


fun buildGraph(lines: List<String>): MutableMap<String, MutableList<String>> {
    val graph = mutableMapOf<String, MutableList<String>>()

    for (line in lines) {

        val parts = line.split(":")
        val device = parts[0].trim()
        val outputs = if (parts.size > 1) {
            parts[1].trim().split(" ").filter { it.isNotEmpty() }
        } else {
            emptyList()
        }

        graph[device] = outputs.toMutableList()
    }

    return graph
}




fun countPaths(
    graph: Map<String, List<String>>,
    start: String,
    end: String,
    required: Set<String> = setOf("dac", "fft")
): Long {
    val memo = mutableMapOf<Pair<String, Set<String>>, Long>()

    fun dfs(node: String, seen: Set<String>): Long {
        val key = node to seen
        memo[key]?.let { return it }

        if (node == end) {
            return if (required.all { it in seen }) 1L else 0L
        }

        val newSeen = if (node in required) seen + node else seen
        val total = graph[node].orEmpty().sumOf { next -> dfs(next, newSeen) }

        memo[key] = total
        return total
    }

    return dfs(start, emptySet())
}





fun main() {

    var answer:Long = 0L
    val lines  = readLinesFromFile("AAC-11A.txt")

    val graph = buildGraph(lines)


    println("Graph size: ${graph.size}")
    println("Start exists: ${graph.containsKey("svr")}")
    println("End exists: ${graph.containsKey("out")}")


    val paths = countPaths(graph, "svr", "out")
    answer = paths


    lines.forEach { println(it) }
    graph.forEach { println(it) }


    println(answer)

}
