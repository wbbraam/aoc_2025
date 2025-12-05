package exercise4B

import myUtils.*


fun countEmptyNeighbours(grid: Array<CharArray>, row: Int, col: Int): Int {
    val directions = listOf(
        -1 to -1, -1 to 0, -1 to 1,  // top-left, top, top-right
        0 to -1,          0 to 1,   // left,       right
        1 to -1,  1 to 0,  1 to 1   // bottom-left, bottom, bottom-right
    )

    var count = 0
    for ((dr, dc) in directions) {
        val r = row + dr
        val c = col + dc
        if (r in grid.indices && c in grid[0].indices && grid[r][c] == '.') {
            count++
        }
    }
    return count
}


fun countRollNeighbours(grid: Array<CharArray>, row: Int, col: Int): Int {
    val directions = listOf(
        -1 to -1, -1 to 0, -1 to 1,  // top-left, top, top-right
        0 to -1,          0 to 1,   // left,       right
        1 to -1,  1 to 0,  1 to 1   // bottom-left, bottom, bottom-right
    )

    var count = 0
    for ((dr, dc) in directions) {
        val r = row + dr
        val c = col + dc
        if (r in grid.indices && c in grid[0].indices && grid[r][c] == '@') {
            count++
        }
    }
    return count
}


fun isRoll(grid: Array<CharArray>, row: Int, col: Int): Boolean {
    return row in grid.indices &&
            col in grid[0].indices &&
            grid[row][col] == '@'
}


fun removeRoll(grid: Array<CharArray>, row: Int, col: Int): Array<CharArray> {
    val newGrid = Array(grid.size) { r -> grid[r].copyOf() }

    if (row in grid.indices && col in grid[0].indices && newGrid[row][col] == '@') {
        newGrid[row][col] = '.'
    }
    return newGrid
}



fun main() {

    var answer:Long = 0
    val lines = readLinesFromFile("AAC-4A.txt")
    var grid = toGrid(lines)

    var x = 0
    var y = 0

    var removedInRun = 0
    var keepGoing = true


    while (keepGoing) {
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                if (isRoll(grid, row, col)) {
                    val amountEmpty = countRollNeighbours(grid, row, col)
                    //println("Cell [$row][$col] = ${grid[row][col]} empty $amountEmpty")
                    if (amountEmpty < 4) {
                        answer += 1
                        removedInRun += 1
                        grid = removeRoll(grid, row, col)
                    }
                }
            }
        }
        if (removedInRun == 0) {
            keepGoing = false
        }
        println("Removed $removedInRun going again. Total so far $answer")
        removedInRun = 0
    }



    println("Answer is $answer")
}
