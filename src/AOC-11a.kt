package exercise11a

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


fun findPaths(
    graph: Map<String, List<String>>,
    start: String,
    end: String
): List<List<String>> {
    val allPaths = mutableListOf<List<String>>()

    fun dfs(current: String, path: MutableList<String>) {
        path.add(current)

        if (current == end) {

            allPaths.add(path.toList()) // Copy current path
        } else {

            for (next in graph[current].orEmpty()) {
                if (next !in path) { // Avoid cycles
                    dfs(next, path)
                }
            }
        }

        path.removeAt(path.size - 1) // Backtrack
    }

    dfs(start, mutableListOf())
    return allPaths
}



fun main() {

    var answer:Int = 0
    val lines  = readLinesFromFile("AAC-11A.txt")

    val graph = buildGraph(lines)
    val paths = findPaths(graph, "you", "out")


    lines.forEach { println(it) }
    graph.forEach { println(it) }
    paths.forEach {
        println(it)
        answer += 1

    }

    println(answer)

}
