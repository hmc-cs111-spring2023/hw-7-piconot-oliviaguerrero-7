import scala.util.parsing.combinator._

object PicobotParser extends JavaTokenParsers with PackratParsers {
    def direction: Parser[Character] = Character


    // FOR REFERENCE
    /*lazy val expr: PackratParser[Expr] =
        (
            expr ~ "+" ~ expr ^^ {case l~"+"~r => Plus(l,r)}
         |  "(" ~ expr ~ ")" ^^ {case "("~e~")" => e }
         |  wholeNumber ^^ {s => Num(s.toInt) }
        ) */
}
