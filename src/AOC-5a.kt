package exercise5A

import myUtils.*





fun extractRanges(lines: List<String>): List<String> {
    val rangeRegex = Regex("""^\s*(\d+)\s*-\s*(\d+)\s*$""")
    val result = ArrayList<String>()

    var i = 0
    while (i < lines.size) {
        val line = lines[i]
        val match = rangeRegex.matchEntire(line)
        if (match != null) {
            val groups = match.destructured
            val start = groups.component1()
            val end = groups.component2()
            result.add("$start-$end")
        }
        i += 1
    }

    return result
}


fun toLongRanges(rangeStrings: List<String>): List<LongRange> =
    rangeStrings.map {
        val (start, end) = it.split("-").map { s -> s.toLong() }
        start..end
    }


fun extractValues(lines: List<String>): List<String> {
    val valueRegex = Regex("""^\s*\d+\s*$""")
    val result = ArrayList<String>()

    var i = 0
    while (i < lines.size) {
        val line = lines[i]
        if (valueRegex.matches(line)) {
            result.add(line.trim())
        }
        i += 1
    }

    return result
}



fun main() {

    var answer:Long = 0
    val lines = readLinesFromFile("AAC-5A.txt")
    val ranges = extractRanges(lines)
    val values = extractValues(lines)
    val longRanges = toLongRanges(ranges)

    ranges.forEach { println("range: $it") }
    values.forEach { println("value: $it")}

    for (value in values) {
        val myValue = value.toLong()
        val inAnyRange = longRanges.any { myValue in it }
        println("$value -> $inAnyRange")
        if (inAnyRange) {answer+=1}
    }
    println("Answer is $answer")
}
