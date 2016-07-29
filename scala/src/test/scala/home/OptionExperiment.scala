package home

import org.scalatest.FlatSpec

/**
  * Created by ly on 7/29/16.
  */
class OptionExperiment extends FlatSpec {
    "Option" should "be used like this" in {  // in IDEA, right click the editor to run a test
        def validateName(s: String): Option[String] = if (s.isEmpty) None else Some(s)

        val s1 = "Emmy"

        val stream = new java.io.ByteArrayOutputStream()
        info(validateName(s1).getOrElse("Ana"))  // println will not work
    }
}
