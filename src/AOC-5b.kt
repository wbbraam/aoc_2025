package exercise5B

import myUtils.*



fun mergeRanges(ranges: List<LongRange>): List<LongRange> {

    val sorted = ranges.sortedBy { it.first }
    val merged = mutableListOf<LongRange>()
    var current = sorted.first()

    for (next in sorted.drop(1)) {
        if (next.first <= current.last + 1) {
            // Overlaps or touches
            current = current.first..maxOf(current.last, next.last)
        } else {
            // No overlap
            merged.add(current)
            current = next
        }
    }
    merged.add(current)
    return merged
}


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

    //ranges.forEach { println("range: $it") }
    //values.forEach { println("value: $it")}

    val merged = mergeRanges(longRanges)
    var total:Long = 0

    var i = 0
    while (i < merged.size) {
        val range = merged[i]
        total += (range.last - range.first + 1)
        i += 1
    }

    println("Answer is $total")
}
