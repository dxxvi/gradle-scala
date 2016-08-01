package home.Option_Either_Try

import org.scalatest.FlatSpec

import scala.util.{Failure, Success, Try}

/**
  * Created by ly on 7/31/16.
  * Try[T] is similar to Either. It also has 2 cases, Success[T] and Failure[T] which can only be of type Throwable. It
  * can be used instead of a try/catch block to postpone exception handling.
  */
class TryExperiment extends FlatSpec {
    "Try" should "be used like this" in {
        def parseInt(value: String): Try[Int] = Try(value.toInt)

        // apply some function to the successful result
        info(parseInt("3").map(_ * 2).getOrElse(Int.MinValue).toString)
        info(parseInt("not a number").map(_ * 2).getOrElse(Int.MinValue).toString)

        case class Person(age: Int)
        // combine with other validations, short-circuiting on the 1st failure, returning a new Try[Person]
        var p = for { age <- parseInt("3") } yield Person(age)
        p match {
            case Failure(ex) => info(s"Validation failed: $ex")
            case Success(p) => info(p.toString)
        }
        p = for { age <- parseInt("not a number") } yield Person(age)
        p match {
            case Failure(ex) => info(s"Validation failed: $ex")
            case Success(p) => info(p.toString)
        }
    }
}
