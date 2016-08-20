package home.Option_Either_Try

import org.scalatest.FlatSpec

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
  * Created by ly on 7/29/16.
  */
class OptionExperiment extends FlatSpec {
    "Option" should "be used like this" in {     // in IDEA, right click the editor to run a test
        def validateName(s: String): Option[String] = if (s.isEmpty) None else Some(s)

        def validateAge(i: Int): Option[Int] = {
            info("validateAge is called")
            if (i <= 0) None else Some(i)
        }

        var s1: java.lang.String = "Emmy"
        info(validateName(s1).getOrElse("Ana"))  // println will not work; print Emmy

        s1 = ""
        info(validateName(s1).getOrElse("Ana"))  // print Ana

        // like Java 8 Optional, it has a map method which returns another Option
        info(validateName(s1).map(_.toUpperCase).getOrElse("DEFAULT VALUE IN UPPERCASE"))

        // combine with other validation, short-circuiting on the 1st error, returning a new Option[Person]
        case class Person(name: String, age: Integer)
        info("---------------------------------------------------------------------------------")
        var person = for {
            name <- validateName("Tom")
            age <- validateAge(23)
        } yield Person(name, age)
        person match {
            case Some(p) => info(p.toString)
            case None => info("None")
        }
        info("---------------------------------------------------------------------------------")
        person = for {
            name <- validateName("")
            age <- validateAge(1)
        } yield Person(name, age)
        person match {
            case Some(p) => info(p.toString)
            case None => info("None")
        }
    }

    "This test" should "be equivalent to the java test in home.Optional.OptionalExperiment" in {
        val y = Try(Source.fromFile("/dev/shm/test.txt1")) match {
            case Failure(_) => Array[Int]()
            case Success(source) =>    // this needs 2 transformations, so we need a stream
                source.getLines.toStream.map(s => Try(s.toInt)).collect({ case Success(i) => i }).toArray
        }

        Try(Source.fromFile("/dev/shm/test.txt1")) match {
            case Failure(_) => Array[Int]()
            case Success(source) => source.getLines.flatMap(s => Try(s.toInt) match {
                case Success(i) => Seq(i)
                case Failure(_) => Seq()
            }).toArray                 // just 1 transformation in this case
        }

        Try(Source.fromFile("/dev/shm/test.txt")) match {
            case Failure(_)      => Array[Int]()
            case Success(source) => source.getLines.flatMap(s => Try(s.toInt).toOption.toSeq).toArray
        }                              // exactly like the pattern matching above but shorter
    }
}
