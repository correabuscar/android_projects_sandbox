package com.example.basicstatecodelab

data class WellnessTask(val id: Int, val label: String)

internal fun getWellnessTasks() = List(30) {
        i -> WellnessTask(i, "Task # $i")
}
