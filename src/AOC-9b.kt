package exercise9b

import exercise8B.Point3D
import myUtils.*
import kotlin.math.abs
import kotlin.math.sqrt





// Taken from old python code and rewritten


fun scanlineFill(reds: List<Pair<Long, Long>>): List<Pair<Long, List<LongRange>>> {
    require(reds.size >= 2) { "Need at least two points" }

    val minY = reds.minOf { it.second }
    val maxY = reds.maxOf { it.second }

    data class Edge(val x1: Long, val y1: Long, val x2: Long, val y2: Long)
    val edges = reds.indices.map { i ->
        val (x1, y1) = reds[i]
        val (x2, y2) = reds[(i + 1) % reds.size]
        require(x1 == x2 || y1 == y2) { "Points must align horizontally or vertically" }
        Edge(x1, y1, x2, y2)
    }

    val result = mutableListOf<Pair<Long, List<LongRange>>>()

    for (y in minY..maxY) {
        val intersections = mutableListOf<Long>()
        val boundary = mutableListOf<LongRange>()

        for (e in edges) {
            if (e.x1 == e.x2) {
                // Vertical edge: half-open in Y to avoid double counting at corners
                val yMin = minOf(e.y1, e.y2)
                val yMax = maxOf(e.y1, e.y2)
                if (y in yMin until yMax) {
                    intersections += e.x1
                }
            } else {
                // Horizontal edge: include the loop line itself on row y
                if (y == e.y1) {
                    val xMin = minOf(e.x1, e.x2)
                    val xMax = maxOf(e.x1, e.x2)
                    boundary += (xMin..xMax) // inclusive
                }
            }
        }

        intersections.sort()

        // Interior fill via even–odd rule
        val interior = mutableListOf<LongRange>()
        var i = 0
        while (i + 1 < intersections.size) {
            val a = intersections[i]
            val b = intersections[i + 1]
            interior += (minOf(a, b)..maxOf(a, b))
            i += 2
        }

        // Merge interior + boundary to get clean, left-to-right ranges
        val merged = mergeRanges(interior + boundary)
        result += (y to merged)
    }

    return result
}

private fun mergeRanges(ranges: List<LongRange>): List<LongRange> {
    if (ranges.isEmpty()) return emptyList()
    val sorted = ranges.sortedBy { it.first }
    val out = mutableListOf<LongRange>()
    var curFirst = sorted.first().first
    var curLast = sorted.first().last
    for (r in sorted.drop(1)) {
        if (r.first <= curLast + 1) {
            curLast = maxOf(curLast, r.last)
        } else {
            out += (curFirst..curLast)
            curFirst = r.first
            curLast = r.last
        }
    }
    out += (curFirst..curLast)
    return out
}




fun parseInput(lines: List<String>): List<Pair<Long, Long>> =
    lines.asSequence()
        .map(String::trim)
        .filter { it.isNotEmpty() }
        .map { line ->
            val parts = line.split(",").map(String::trim)
            require(parts.size == 2) { "Expected 'x,y' per line, got: '$line'" }
            val x = parts[0].toLong()
            val y = parts[1].toLong()
            x to y
        }
        .toList()


fun isInArea(
    p1: Pair<Long, Long>,
    p2: Pair<Long, Long>,
    areaRanges: List<Pair<Long, List<LongRange>>>
): Boolean {
    val xMin = minOf(p1.first, p2.first)
    val xMax = maxOf(p1.first, p2.first)
    val yMin = minOf(p1.second, p2.second)
    val yMax = maxOf(p1.second, p2.second)

    // Build a quick lookup: rowY -> ranges
    val rowMap = areaRanges.associate { it.first to it.second }

    for (y in yMin..yMax) {
        val ranges = rowMap[y] ?: return false // row not filled at all
        // Check if [xMin..xMax] is fully covered by at least one range
        val covered = ranges.any { r -> xMin >= r.first && xMax <= r.last }
        if (!covered) return false
    }
    return true
}


fun main() {

    var answer:Int = 0
    val lines  = readLinesFromFile("AAC-9A.txt")
    var coords = parseInput(lines)
    var maxArea:Long = 0L
    val areaRanges = scanlineFill(coords)

    for (i in coords.indices) {
        val (x1, y1) = coords[i]
        println(i)
        for (j in i + 1 until coords.size) {
            if (isInArea(coords[i], coords[j], areaRanges))
            {
                val (x2, y2) = coords[j]

                val width = kotlin.math.abs(x2 - x1) + 1
                val height = kotlin.math.abs(y2 - y1) + 1
                val area = width * height

                if (area > maxArea) {
                    maxArea = area
            }
            }
        }
    }

    println("Answer is $maxArea")
}
