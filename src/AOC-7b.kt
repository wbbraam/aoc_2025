package exercise7B

import myUtils.*


fun findStartInRow(row: CharArray): Int {
    return row.indexOf('S')
}


fun isSplitter(row: CharArray, col: Int): Boolean {
    return col in row.indices && row[col] == '^'
}


fun main() {
    // Well that was an easy one... just go lanternfish from now on,

    var answer:Long = 0
    val lines = readLinesFromFile("AAC-7A.txt")
    val grid = toGrid(lines)

    val start = findStartInRow(grid[0])
    val cols = grid[0].size

    var current = LongArray(cols)
    current[start] = 1L

    for (rowIndex in 1 until grid.size) {
        val row = grid[rowIndex]
        val next = LongArray(cols)

        for (c in 0 until cols) {
            val count = current[c]
            if (count == 0L) continue

            if (isSplitter(row, c)) {
                if (c - 1 >= 0) next[c - 1] += count
                if (c + 1 < cols) next[c + 1] += count
            } else {
                next[c] += count
            }
        }

        current = next
    }

    answer = current.sum()

    println("Answer is $answer")
}
