package com.rk.mipt.npm

fun main() {
    try {
        val studentA = Student("Tom", 18, 101)
    } catch (e: IllegalArgumentException) {
        println("学生建档失败，错误：数据异常，message: ${e.message}")
    }
}

data class Student(
    val name: String,
    val age: Int,
    val score: Int
) {
    init {
        require(name.isNotBlank()) { "学生姓名不能为空" }
        require(age in 0..150) { "年龄必须在合理范围内" }
        require(score in 0..100) { "分数必须在 0 到 100 之间" }
    }
}

data class StudentFile(
    val students: List<Student>
) {
    init {
        require(students.isNotEmpty()) { "学生列表不能为空" }
    }

    fun calculateAverageScore(): Double {
        return students.averageOf { it.score.toDouble() }
    }
}

private fun <T> Iterable<T>.averageOf(selector: (T) -> Double): Double {
    var sum = 0.0
    var count = 0

    for (element in this) {
        sum += selector(element)
        count++
    }

    require(count > 0) { "集合不能为空" }
    return sum / count
}