package myUtils
import java.io.File

fun readLinesFromFile(filename: String): List<String> {
    return File(filename).readLines()
}


fun toGrid(lines: List<String>): Array<CharArray> {
    return Array(lines.size) { rowIndex ->
        lines[rowIndex].toCharArray()
    }
}



fun padGrid(grid: Array<CharArray>): Array<CharArray> {
    val maxWidth = grid.maxOf { it.size }
    return Array(grid.size) { rowIndex ->
        val row = grid[rowIndex]
        if (row.size < maxWidth) {
            // Create a new CharArray with spaces for padding
            CharArray(maxWidth) { i -> if (i < row.size) row[i] else ' ' }
        } else {
            row
        }
    }
}




fun parseToMatrix(lines: List<String>): List<List<String>> {
    return lines.map { line ->
        line.trim().split(Regex("\\s+"))
    }
}


fun <T> rotateClockwise(matrix: List<List<T>>): List<List<T>> {
    val rows = matrix.size
    val cols = matrix.first().size
    return (0 until cols).map { c ->
        (rows - 1 downTo 0).map { r ->
            matrix[r][c]
        }
    }
}
