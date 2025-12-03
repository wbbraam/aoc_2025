package exercise3A

import myUtils.*
import java.io.File


fun maxJoltage(bank: String): Int {



    var leftMaxDigit = bank[0].toString().toInt()
    var best = (bank[0].toString().toInt()) * 10 + (bank[1].toString().toInt()) // initialize with first pair

    for (j in 1 until bank.length) {
        val d = bank[j].toString().toInt()
        best = maxOf(best, leftMaxDigit * 10 + d)
        leftMaxDigit = maxOf(leftMaxDigit, d)
    }

    return best
}




fun main() {

    var answer:Long = 0
    val lines = readLinesFromFile("AAC-3A.txt")

    lines.forEach {
        println(it)
        val highest = maxJoltage(it)
        println(highest)
        answer += highest
    }


    println("Answer is $answer")
}