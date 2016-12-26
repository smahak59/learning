package com.learning.sorting.mutable

/**
  * - Stable
  * - Time complexity: O(n^^2)
  * - Space complexity: O(1)
  */
class InsertionSorter extends Sorter {
  override def sort[T](xs: Array[T])(implicit o: Ordering[T]): Unit = {
    for { i <- Range(1, xs.length) } yield {
      var j = i
      while (j > 0 && o.lt(xs(j), xs(j - 1))) {
        swap(xs, j, j - 1)
        j = j - 1
      }
    }
  }
}
