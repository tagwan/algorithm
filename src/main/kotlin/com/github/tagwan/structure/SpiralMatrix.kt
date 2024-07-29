package com.github.tagwan.structure

fun spiral(a: Int) {
    var minColumn = 0
    var minRow = 0
    var maxRow = a - 1
    var maxColumn = a - 1
    var count = 1
    val arr = Array(a) { IntArray(a) }
    while (count <= a * a) {
        for (i in minColumn..maxColumn) {
            arr[i][minRow] = count
            count++
        }
        for (j in minRow + 1..maxRow) {
            arr[maxColumn][j] = count
            count++
        }
        for (i in maxColumn - 1 downTo minColumn) {
            arr[i][maxRow] = count
            count++
        }
        for (j in maxRow - 1 downTo minRow + 1) {
            arr[minColumn][j] = count
            count++
        }
        minColumn++
        minRow++
        maxColumn--
        maxRow--
    }
    for (j in arr.indices) {
        for (i in arr.indices) {
            print(arr[i][j].toString() + ",")
        }
        print("\n")
    }
}