import java.io.File
import myUtils.*

fun isInvalidId2A(id: Long): Boolean {
    val s = id.toString()
    if (s.length % 2 != 0) return false // must be even length

    val mid = s.length / 2
    val firstHalf = s.substring(0, mid)
    val secondHalf = s.substring(mid)

    return firstHalf == secondHalf
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

        println("Evluating pair: $firstString - $secondString")

        for (i in firstInt..secondInt) {
            println("Checking $i")
            if (isInvalidId2A(i)) {
                println("Invalid")
                answer += i
            }
        }

    }
    println("Answer is $answer")
}