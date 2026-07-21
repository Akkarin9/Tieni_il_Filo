package com.tieniilfilo.app.util

private val mmToUs = mapOf(
    0.5 to "14",
    0.75 to "13",
    0.85 to "12",
    1.0 to "11",
    1.1 to "10",
    1.25 to "9",
    1.4 to "8",
    1.5 to "7",
    1.6 to "6",
    1.75 to "5",
    1.8 to "4",
    1.9 to "3",
    2.0 to "2",
    2.1 to "1",
    2.2 to "B/1",
    2.25 to "B/1",
    2.3 to "C/2",
    2.4 to "C/2",
    2.5 to "C/2",
    2.75 to "D/3",
    2.8 to "D/3",
    3.0 to "D/3",
    3.1 to "E/4",
    3.2 to "E/4",
    3.25 to "E/4",
    3.3 to "F/5",
    3.4 to "F/5",
    3.5 to "E/4",
    3.6 to "F/5",
    3.75 to "F/5",
    3.8 to "G/6",
    3.9 to "G/6",
    4.0 to "G/6",
    4.1 to "7",
    4.2 to "7",
    4.25 to "7",
    4.3 to "8",
    4.4 to "8",
    4.5 to "7",
    4.6 to "G/6",
    4.75 to "G/6",
    5.0 to "H/8",
    5.1 to "I/9",
    5.2 to "I/9",
    5.25 to "I/9",
    5.3 to "J/10",
    5.4 to "J/10",
    5.5 to "I/9",
    5.6 to "9",
    5.7 to "K/10½",
    5.75 to "K/10½",
    5.8 to "K/10½",
    5.9 to "L/11",
    6.0 to "J/10",
    6.1 to "L/11",
    6.2 to "M/13",
    6.25 to "M/13",
    6.3 to "M/13",
    6.4 to "M/13",
    6.5 to "K/10½",
    6.6 to "N/15",
    6.7 to "N/15",
    6.75 to "N/15",
    6.8 to "N/15",
    6.9 to "N/15",
    7.0 to "L/11",
    7.1 to "P/16",
    7.2 to "P/16",
    7.3 to "P/16",
    7.4 to "P/16",
    7.5 to "M/13",
    7.6 to "Q",
    7.7 to "Q",
    7.8 to "Q",
    7.9 to "Q",
    8.0 to "L/11",
    8.1 to "S",
    8.2 to "S",
    8.3 to "S",
    8.4 to "S",
    8.5 to "M",
    8.6 to "T",
    8.7 to "T",
    8.8 to "T",
    8.9 to "T",
    9.0 to "M/13",
    9.1 to "U",
    9.2 to "U",
    9.3 to "U",
    9.4 to "U",
    9.5 to "N",
    9.6 to "V",
    9.7 to "V",
    9.8 to "V",
    9.9 to "V",
    10.0 to "N/15",
    10.1 to "W",
    10.2 to "W",
    10.3 to "W",
    10.4 to "W",
    10.5 to "W",
    10.6 to "X",
    10.7 to "X",
    10.8 to "X",
    10.9 to "X",
    11.0 to "X",
    11.1 to "Y",
    11.2 to "Y",
    11.3 to "Y",
    11.4 to "Y",
    11.5 to "Y",
    11.6 to "Z",
    11.7 to "Z",
    11.8 to "Z",
    11.9 to "Z",
    12.0 to "Z",
    15.0 to "17",
    16.0 to "17",
    17.0 to "17",
    18.0 to "18",
    19.0 to "18",
    20.0 to "19",
    25.0 to "35",
    30.0 to "50",
)

private fun Double.closestKey(): Double? = mmToUs.keys.minByOrNull { kotlin.math.abs(this - it) }

fun Double.toUsHookSize(): String? {
    val key = closestKey() ?: return null
    return mmToUs[key]
}

fun Double.toUkHookSize(): String {
    val mm = this
    return when {
        mm < 1.2 -> "15"
        mm < 1.6 -> "14"
        mm < 2.0 -> "13"
        mm < 2.4 -> "12"
        mm < 2.7 -> "11"
        mm < 3.0 -> "10"
        mm < 3.2 -> "9"
        mm < 3.5 -> "8"
        mm < 3.8 -> "7"
        mm < 4.0 -> "6"
        mm < 4.2 -> "5"
        mm < 4.5 -> "4"
        mm < 4.8 -> "3"
        mm < 5.0 -> "2"
        mm < 5.3 -> "1"
        mm < 5.7 -> "0"
        mm < 6.0 -> "00"
        mm < 7.0 -> "000"
        mm < 8.0 -> "0000"
        mm < 9.0 -> "00000"
        mm < 10.0 -> "000000"
        else -> "0000000"
    }
}
