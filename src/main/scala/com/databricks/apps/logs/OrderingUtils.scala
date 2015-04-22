package com.databricks.apps.logs

import scala.math.Ordering

/**
 * Created by flavio on 4/21/15.
 */
object OrderingUtils {

  object SecondValueOrdering extends Ordering[(String, Int)] {
    def compare(a: (String, Int), b: (String, Int)) = {
      a._2 compare b._2
    }
  }

  object SecondValueLongOrdering extends Ordering[(String, Long)] {
    def compare(a: (String, Long), b: (String, Long)) = {
      a._2 compare b._2
    }
  }


}
