package home.Option_Either_Try

import org.scalatest.FlatSpec

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
}
