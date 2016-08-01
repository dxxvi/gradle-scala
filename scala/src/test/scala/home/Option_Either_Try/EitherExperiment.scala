package home.Option_Either_Try

import org.scalatest.FlatSpec

/**
  * Created by ly on 7/30/16.
  * Option is nice to indicate failure, but it doesn't tell anything about the failure. In that case,
  * Either[Left, Right] can be used.
  */
class EitherExperiment extends FlatSpec {
    "Either" should "be used like this" in {
        def validateName(name: String): Either[String, String] =
            if (name.isEmpty) Left("name cannot be empty") else Right(name)

        info("---------------------------------------------------------------------------------")
        var s = "Mirza"
        info(validateName(s).right.map(_.toUpperCase).right.getOrElse("DEFAULT VALUE FOR THE UPPERCASE OF THE RIGHT"))
        info(validateName(s).left.getOrElse("default value for the left"))

        info("---------------------------------------------------------------------------------")
        s = ""
        info(validateName(s).right.map(_.toUpperCase).right.getOrElse("DEFAULT VALUE FOR THE UPPERCASE OF THE RIGHT"))

        // combine with other validation, short-circuiting on the 1st error, returning a new Either[??, Person]
        case class Person(name: String)
        info("---------------------------------------------------------------------------------")
        var eitherPerson  = for { name <- validateName("Tom").right } yield Person(name)
        eitherPerson match {
            case Right(p) => info(p.toString)
            case Left(s1) => info(s1 + ": this is a string because the left of validateName is a string")
        }
        info("---------------------------------------------------------------------------------")
        eitherPerson  = for { name <- validateName("").right } yield Person(name)
        eitherPerson match {
            case Right(p) => info(p.toString)
            case Left(s1) => info(s1 + ": this is the left of validateName which is a string")
        }
    }
}
