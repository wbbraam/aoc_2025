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
