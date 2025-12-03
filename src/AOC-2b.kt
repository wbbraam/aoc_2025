package exercise2B

import myUtils.*
import java.io.File



fun isInvalidId2B(id: Long): Boolean {
    val s = id.toString()
    val len = s.length

    // Try all possible substring lengths
    for (subLen in 1..(len / 2)) {
        if (len % subLen == 0) { // Only consider lengths that divide the whole string
            val pattern = s.substring(0, subLen)
            val repeatCount = len / subLen

            // Build the repeated string
            val repeated = pattern.repeat(repeatCount)
            if (repeated == s) {
                return true
            }
        }
    }
    return false
}



fun main() {

    var answer:Long = 0
    val lines = readLinesFromFile("AAC-2A.txt")

    println(lines[0])
    val parts = lines[0].split(",").map { it.trim() }.toTypedArray()
    parts.forEach {

    // Split on the dash
        val bothends = it.split("-")
        val firstString  = bothends[0]
        val secondString = bothends[1]

        val firstInt     = firstString.toLong()
        val secondInt     = secondString.toLong()

        println("Evaluating pair: $firstString - $secondString")

        for (i in firstInt..secondInt) {
            println("Checking $i")
            if (isInvalidId2B(i)) {
                println("Invalid")
                answer += i
            }
        }

    }
    println("Answer is $answer")
}