package exercise9c

import exercise8B.Point3D
import myUtils.*
import kotlin.math.abs
import kotlin.math.sqrt
import java.util.BitSet
import kotlin.system.exitProcess



fun coordsToBitMap(coords: List<Pair<Long, Long>>): Map<Int, BitSet> {
    val map = mutableMapOf<Int, BitSet>()
    for ((x, y) in coords) {
        val row = y.toInt()
        val col = x.toInt()
        val bitset = map.getOrPut(row) { BitSet() }
        bitset.set(col)
    }
    return map
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


fun rangesFromBitSets(
    flaggedByRow: Map<Int, BitSet>,
    yMin: Int,
    yMax: Int
): List<Pair<Long, List<LongRange>>> {
    require(yMin <= yMax) { "yMin must be <= yMax" }

    var out = mutableListOf<Pair<Long, List<LongRange>>>()
    var lastRanges: List<LongRange> = emptyList()

    for (y in yMin..yMax) {
        val bitset = flaggedByRow[y]
        val ranges: List<LongRange> = if (bitset == null || bitset.isEmpty) {
            // Reuse previous line's ranges if no flagged bits
            lastRanges
        } else {
            val merged = mutableListOf<LongRange>()
            var start = -1
            var prev = -1
            var x = bitset.nextSetBit(0)
            while (x >= 0) {
                if (start == -1) {
                    start = x
                    prev = x
                } else if (x == prev + 1) {
                    prev = x
                } else {
                    merged += (start.toLong()..prev.toLong())
                    start = x
                    prev = x
                }
                x = bitset.nextSetBit(x + 1)
            }
            if (start != -1) merged += (start.toLong()..prev.toLong())
            merged
        }
        out += (y.toLong() to ranges)
        lastRanges = ranges
    }
    return out
}


fun main() {

    var answer:Int = 0
    val lines  = readLinesFromFile("AAC-9A.txt")
    var coords = parseInput(lines)
    var maxArea:Long = 0L
    println("Coords read: ${coords.size}")
    val bitMap = coordsToBitMap(coords)
    println("Bitmap generated: $bitMap.size")
    val areaRanges = rangesFromBitSets(bitMap, 0, bitMap.size)
    println("Area ranges generated: $areaRanges.size")
    //exitProcess(0)

    for (i in coords.indices) {
        val (x1, y1) = coords[i]
        println(i)
        for (j in i + 1 until coords.size) {
            if (isInArea(coords[i], coords[j], areaRanges))
            {
                val (x2, y2) = coords[j]

                val width = abs(x2 - x1) + 1
                val height = abs(y2 - y1) + 1
                val area = width * height

                if (area > maxArea) {
                    maxArea = area
            }
            }
        }
    }

    println("Answer is $maxArea")
}
