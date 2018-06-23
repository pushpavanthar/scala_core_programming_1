package com.ora.scalaprogrammingfundamentals

import java.time.LocalDate

/**
  * Created by purushotham.p1 on 22/06/18.
  */
class Stamp private(val name:String, val year:Int, currentYear:() => Int) {
  def age:Int = currentYear() - year
}

object Stamp {
  //static factories
  def apply(name: String, year: Short): Stamp = {
    require(name.nonEmpty, "Name cannot be empty")
    require(year >= 1840, "Stamps were not available until 1840")
    new Stamp(name, year, () => LocalDate.now.getYear.toShort)
  }
}
