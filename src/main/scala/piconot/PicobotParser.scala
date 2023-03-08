import scala.util.parsing.combinator._

trait Direction;
case object North extends Direction;
case object East extends Direction;
case object South extends Direction;
case object West extends Direction;
case object NoMove extends Direction; // IDK if needed

trait WallState;
case object Blocked extends WallState;
case object NotBlocked extends WallState;
case object AnyWallState extends WallState; // IDK 

case class Movement(d: Direction);
case class StateSwitch(stateName: String);
case class RuleEffect(m: Movement, s: StateSwitch)

case class WallCondition (d: Direction, c: WallState) 

case class Rule(conds: List[WallCondition], effect: RuleEffect)
case class State(stateName: String, rules: List[Rule])

// trait WallStatus
// case object Empty extends WallStatus;
// case object Blocked extends WallStatus;

// case class StateRequrement(d: Direction, s:WallStatus);

object PicobotParser extends JavaTokenParsers with PackratParsers {
    def direction: Parser[Direction] = ("N" | "E" | "S" | "W") ^^ {
        case "N" => North
        case "E" => East
        case "S" => South
        case "W" => West
    }

    def movement: Parser[Movement] = "go"~>direction ^^ {case d => Movement(d)}
    def stateSwitch: Parser[StateSwitch] = "switch"~"to"~> stringLiteral ^^ {case s => StateSwitch(s)}
    def effects: Parser[RuleEffect] = (
        movement ~ "and" ~ stateSwitch ^^ {case m~"and"~s => RuleEffect(m,s)}
     |  stateSwitch ~ "and" ~ movement ^^ {case s~"and"~m => RuleEffect(m,s)}
     |  movement ^^ {case m => RuleEffect(m, StateSwitch(""))}
     |  stateSwitch ^^ {case s => RuleEffect(Movement(NoMove), s)}
    )

    def wallConditions: Parser[List[WallCondition]] = ( 
        wallConditions ~ "and" ~ wallConditions ^^ { case l~"and"~r => l ++ r }
     |  direction <~ "wall" ^^ {case d => List(WallCondition(d, Blocked))}
     |  direction <~ "clear" ^^ {case d => List(WallCondition(d, NotBlocked))}
    )

    def rule: Parser[Rule] = ( 
        "if" ~> wallConditions ~ effects ^^ {case c~e => Rule(c, e)}
     |  "else" ~> effects ^^ {case e => Rule(List(), e)}
    )

    def rules: Parser[List[Rule]] = rep1(rule)

    def state: Parser[State] = (
        stringLiteral ~ "(" ~ rules ~ ")" ^^ {case s~"("~r~")" => State(s, r)}
    )
    def states: Parser[List[State]] = rep1(state)

    // FOR REFERENCE
    // lazy val expr: PackratParser[Expr] =
    //     (
    //         expr ~ "+" ~ expr ^^ {case l~"+"~r => Plus(l,r)}
    //      |  "(" ~ expr ~ ")" ^^ {case "("~e~")" => e }
    //      |  wholeNumber ^^ {s => Num(s.toInt) }
    //     ) 
}
