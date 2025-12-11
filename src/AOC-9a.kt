package exercise9a

import exercise8B.Point3D
import myUtils.*
import kotlin.math.sqrt


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


fun main() {

    var answer:Int = 0
    val lines  = readLinesFromFile("AAC-9A.txt")
    var coords = parseInput(lines)
    var maxArea:Long = 0L

    for (i in coords.indices) {
        val (x1, y1) = coords[i]
        for (j in i + 1 until coords.size) {
            val (x2, y2) = coords[j]

            val width = kotlin.math.abs(x2 - x1) + 1
            val height = kotlin.math.abs(y2 - y1) + 1
            val area = width * height

            if (area > maxArea) {
                maxArea = area
            }
        }
    }

    println("Answer is $maxArea")
}
