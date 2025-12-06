package exercise6B

import myUtils.*


fun findOperatorRanges(grid: Array<CharArray>): List<IntRange> {
    val bottomRow = grid.last()
    val ranges = mutableListOf<IntRange>()

    var start = -1
    for ((col, ch) in bottomRow.withIndex()) {
        if (ch == '+' || ch == '*') {
            if (start != -1) {
                ranges += start..(col - 1)
            }
            start = col
        }
    }
    // Add the last operator range (to the end of the row)
    if (start != -1) {
        ranges += start..(bottomRow.size - 1)
    }

    return ranges
}


fun extractNumber(grid: Array<CharArray>, col: Int): Long {
    val lastRow = grid.lastIndex
    val sb = StringBuilder()

    for (row in 0 until lastRow) { // skip bottom row
        val ch = grid[row][col]
        if (ch in '0'..'9') {
            sb.append(ch)
        }
    }

    return if (sb.isNotEmpty()) sb.toString().toLong() else 0L
}


fun main() {

    var answer:Long = 0
    val lines = readLinesFromFile("AAC-6A.txt")

    val grid = padGrid(toGrid(lines))
    val myRanges = findOperatorRanges(grid)

    myRanges.forEach {
        val operator = grid.last()[it.first]
        var subtotal:Long = 0
        var numbers = mutableListOf<Long>()

        println("Operator: $operator")
        for (i in it.last downTo it.first) {
            numbers.add(extractNumber(grid,i))
        }

        if (operator.equals('+')) {
            val total = numbers.sum()
            println("Total: $total")
            answer += total
        } else {
            val total = numbers.filter{it != 0L}.fold(1L){acc, value -> acc * value}
            println("Total: $total")
            answer += total
        }

        println(numbers)

    }

    println(myRanges)
    println("Answer is $answer")
}
