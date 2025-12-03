package myUtils
import java.io.File

fun readLinesFromFile(filename: String): List<String> {
    return File(filename).readLines()
}