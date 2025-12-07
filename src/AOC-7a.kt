package exercise7A

import myUtils.*


fun findStartInRow(row: CharArray): Int {
    return row.indexOf('S')
}


fun isSplitter(row: CharArray, col: Int): Boolean {
    return col in row.indices && row[col] == '^'
}


fun main() {

    var answer:Long = 0
    val lines = readLinesFromFile("AAC-7A.txt")
    val grid = toGrid(lines)

    val start = findStartInRow(grid[0])

    var currentSet = mutableSetOf<Int>()
    var nextSet    = mutableSetOf<Int>()
    val splitters  = mutableSetOf<Int>()

    currentSet.add(start)

    grid.drop(1).forEach {
        nextSet = mutableSetOf<Int>()
        val row = it
        currentSet.forEach {
            if (isSplitter(row, it)){
                answer += 1
                nextSet.add(it-1)
                nextSet.add(it+1)
            } else {
                nextSet.add(it)
            }
        }
        println(nextSet)

        currentSet = nextSet.toMutableSet()

    }


    println("Answer is $answer")
}
