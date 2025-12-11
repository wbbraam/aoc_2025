package exercise8B

import myUtils.*
import kotlin.math.sqrt

data class Point3D(val x: Int, val y: Int, val z: Int)


fun parseInput(lines: List<String>): List<Point3D> =
    lines.map { line ->
        val (x, y, z) = line.split(",").map { it.toInt() }
        Point3D(x, y, z)
    }


fun distance(a: Point3D, b: Point3D): Double {
    val dx = (a.x - b.x).toDouble()
    val dy = (a.y - b.y).toDouble()
    val dz = (a.z - b.z).toDouble()
    return sqrt(dx * dx + dy * dy + dz * dz)
}


fun generatePairs(points: List<Point3D>): List<Triple<Int, Int, Double>> {
    val pairs = mutableListOf<Triple<Int, Int, Double>>()
    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            pairs.add(Triple(i, j, distance(points[i], points[j])))
        }
    }
    return pairs.sortedBy { it.third }
}





class UnionFind(size: Int) {
    private val parent = IntArray(size) { it }
    private val rank = IntArray(size)
    private val componentSize = IntArray(size) { 1 }

    fun find(x: Int): Int {
        if (parent[x] != x) parent[x] = find(parent[x]) // Path compression
        return parent[x]
    }

    fun union(a: Int, b: Int) {
        val rootA = find(a)
        val rootB = find(b)
        if (rootA == rootB) return

        if (rank[rootA] < rank[rootB]) {
            parent[rootA] = rootB
            componentSize[rootB] += componentSize[rootA]
        } else {
            parent[rootB] = rootA
            componentSize[rootA] += componentSize[rootB]
            if (rank[rootA] == rank[rootB]) rank[rootA]++
        }
    }

    fun size(x: Int): Int = componentSize[find(x)]
}


fun findNetworks(connections: List<Triple<Int, Int, Double>>, nodeCount: Int): Map<Int, Int> {
    val uf = UnionFind(nodeCount)

    // Merge nodes based on connections
    for ((a, b, _) in connections) {
        uf.union(a, b)
    }

    // Count sizes per root
    val networkSizes = mutableMapOf<Int, Int>()
    for (node in 0 until nodeCount) {
        val root = uf.find(node)
        networkSizes[root] = (networkSizes[root] ?: 0) + 1
    }

    return networkSizes // root -> size
}



fun main() {

    var answer:Int = 0
    val lines  = readLinesFromFile("AAC-8A.txt")
    var coords = parseInput(lines)
    var pairs  = generatePairs(coords)


    var amountOfPairs:Int = 1000
    var keepGoing = true


    while (keepGoing) {
        var top10 = pairs.take(amountOfPairs)
        val networks = findNetworks(top10, 1000)

        val sortedNetworks = networks.toList().sortedByDescending { it.second }.toMap()

        val top3 = sortedNetworks.values.take(1)
        val longestSize = top3.reduce{acc, v -> acc * v}

        println("Amount of pairs: $amountOfPairs size: $longestSize")
        if (longestSize == 1000)
        {
            val last = top10.last()
            println(last)
            println(coords[last.first])
            println(coords[last.second])
            keepGoing = false
        } else {
            amountOfPairs += 1
        }
    }

    println("Answer is $answer")
}
