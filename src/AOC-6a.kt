package exercise6A

import myUtils.*



fun main() {

    var answer:Long = 0
    val lines = readLinesFromFile("AAC-6A.txt")
    val mymatrix = parseToMatrix(lines)

    println(mymatrix)

    val myRotated = rotateClockwise(mymatrix)

    println(myRotated)

    myRotated.forEach {
        println(it)
        var total:Long = 0
        if (it[0] == "*") {

            total = it.drop(1).map{it.toLong()}.reduce{acc, value -> acc * value}
            println("Product: $total")

        } else {
            total = it.drop(1).sumOf{it.toLong()}
            println("Sum: $total")
        }
        answer += total
    }

    println("Answer is $answer")
}
