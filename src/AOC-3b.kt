package exercise3B

import myUtils.*
import java.io.File
import java.math.BigInteger


fun maxJoltage(bank: String, pick: Int): String {


    val removalsAllowed = bank.length - pick
    val stack = IntArray(bank.length)
    var top = 0
    var removalsLeft = removalsAllowed

    for (ch in bank) {
        val d = ch.toString().toInt()
        while (top > 0 && removalsLeft > 0 && stack[top - 1] < d) {
            top--
            removalsLeft--
        }
        stack[top++] = d
    }

    var result = ""
    val keep = pick
    for (i in 0 until keep) {
        result += stack[i].toString()
    }


    return result
}






fun main() {
    val start = System.currentTimeMillis()
    var answer:Long = 0
    val lines = readLinesFromFile("AAC-3A.txt")

    lines.forEach {
        //println(it)
        val highest = maxJoltage(it, 12)
        //println(highest)
        answer += highest.toLong()
    }


    println("Answer is $answer")
    val end = System.currentTimeMillis()
    println("Time taken: ${end - start} ms")
}