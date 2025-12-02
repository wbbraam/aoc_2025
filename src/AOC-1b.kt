import java.io.File

fun readLinesFromFile(filename: String): List<String> {
    return File(filename).readLines()
}

fun splitFirstCharAndIntSafe(input: String): Pair<String, Int?> {
    require(input.isNotEmpty()) { "Input string must not be empty." }
    val first = input.substring(0, 1)           // first character as String
    val rest = input.substring(1)    // everything after the first character
    val number = rest.toIntOrNull()             // null if not a valid Int
    return first to number
}

fun rotateDial(current: Int, steps: Int): Int {
    require(current in 0..99) { "Dial position must be between 0 and 99" }
    return (current + steps).mod(100)
}

fun main() {
    var dialPoint = 50
    var zeroCount = 0
    val lines = readLinesFromFile("MILO.txt")

    lines.forEach {
        val (dir, amount) = splitFirstCharAndIntSafe(it)
        val value: Int = amount ?: 0  // default to 0 if null
        println("Direction value: $dir, Amount value: $amount")
        if (dir == "R")
        {
            for (i in 1..value)
            {
                dialPoint = rotateDial(dialPoint, 1)
                if (dialPoint == 0) {
                    zeroCount += 1
                }
            }
        } else {
            for (i in 1..value)
            {
                dialPoint = rotateDial(dialPoint, -1)
                if (dialPoint == 0) {
                    zeroCount += 1
                }
            }
        }
        println(dialPoint)
    }
    println("Total of $zeroCount times at 0")
}