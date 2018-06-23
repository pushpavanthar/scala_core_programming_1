package com.ora.scalaprogrammingfundamentals

import org.scalatest.{FunSuite, Matchers}

class TraitsSpec extends FunSuite with Matchers {
  test(
    """A trait is analogous to an interface in Java. Classes and
      |  objects can extend traits but traits cannot be instantiated
      |  and therefore have no parameters""".stripMargin) {

    pending
  }

  test(
    """Just like Java 8 interfaces, you can have concrete
      |  methods (known as default methods in Java)""".stripMargin) {
    pending
  }

  test("Traits are used for mixing in functionality, this is called a mixin") {
    pending
  }

  test(
    """You can extends from a trait that was not built in to begin with, be
      |  careful that the trait is instantiated first, and may still not
      |  have a dedsired effect.""".stripMargin) {

    pending
  }


  test("Xia's question on a global counter") {

    trait Counter {
      var counter = 0
      def incrementCounter = counter = counter + 1
    }

    class Country(name:String) {
      def currentCountOfAll:Int = Country.counter

    }
    object Country extends Counter


    val country1 = new Country("Germany")
    val country2 = new Country("Zaire")
    val country3 = new Country("China")
    val country4 = new Country("India")

    Country.incrementCounter
    Country.incrementCounter
    Country.incrementCounter
    Country.incrementCounter

    country4.currentCountOfAll should be (4)
  }

  test(
    """The confusing thing about traits is that if you
      |  extends from a class then extend with extends, and then use with
      |  to list all the traits you wish to inherit, if you do extends from
      |  a superclass then you will extends with one trait and with the
      |  remaining traits""".stripMargin) {

    pending
  }

  test(
    """Avoiding the diamond of death.
      |  https://en.wikipedia.org/wiki/Multiple_inheritance#The_diamond_problem.
      |  In Scala, since traits can inherit as a diamond shape,
      |  there has to be a strategy. Instantiation goes from left to right
      |  (used to be right to left),
      |  and instantiation is marked for reuse.""".stripMargin) {

    var list = List[String]()

    trait T1 {
      list = list :+ "Instantiated T1"
    }

    trait T2 extends T1 {
      list = list :+ "Instantiated T2"
    }

    trait T3 extends T1 {
      list = list :+ "Instantiated T3"
    }

    trait T4 extends T1 {
      list = list :+ "Instantiated T4"
    }

    class C1 extends T2 with T3 with T4 {
      list = list :+ "Instantiated C1"
    }

    list = list :+ "Creating C1"
    new C1
    list = list :+ "Created C1"

    list.mkString(", ") should be ("Creating C1, Instantiated T1, Instantiated T2, Instantiated T3, Instantiated T4, Instantiated C1, Created C1")
  }

  test(
    """Stackable traits are traits stacked one atop another,
      |  make sure that all overrides
      |  are labelled, abstract override.  The order of the mixins are important.
      |  Traits on the right take effect first""".stripMargin) {

    abstract class IntQueue {
      def get(): Int

      def put(x: Int)
    }

    import scala.collection.mutable.ArrayBuffer

    class BasicIntQueue extends IntQueue {
      private val buf = new ArrayBuffer[Int]

      def get() = buf.remove(0)

      def put(x: Int) {
        buf += x
      }
    }

    trait Doubling extends IntQueue {
      abstract override def put(x: Int) {
        super.put(2 * x)
      } //abstract override is necessary to stack traits
    }

    trait Incrementing extends IntQueue {
      abstract override def put(x: Int) {
        super.put(x + 1)
      }
    }

    trait Filtering extends IntQueue {
      abstract override def put(x: Int) {
        if (x >= 0) super.put(x)
      }
    }

    val myQueue = new BasicIntQueue with Doubling with Incrementing

    myQueue.put(4)
    myQueue.get() should be (10)
  }

  test(
    """Self types declares that a trait must be mixed into another trait.
      |  The relationship is the following:
      |
      |     * B extends A, then B is an A.
      |     * When you use self-types, B requires an A
      |
      |  This is used for a pattern called the cake pattern, but is also a
      |  way for the class to define the non-inheritance behaviors""".stripMargin) {
    trait Moveable {
      def increaseSpeed(ms: Int): Moveable
      def decreaseSpeed(ms: Int): Moveable
    }

    trait Vehicle {self: Moveable =>
      def make: String
      def model: String
    }

    class Car(val make: String, val model: String, val currentSpeed: Int) extends Vehicle with Moveable {
      override def increaseSpeed(ms: Int) = new Car(make, model,
        currentSpeed + ms)

      override def decreaseSpeed(ms: Int) = new Car(make, model,
        currentSpeed - ms)
    }

    val ford = new Car("Ford", "Fiesta", 110).decreaseSpeed(20)
    ford.make should be ("Ford")
    ford.currentSpeed should be (90)
  }
}
