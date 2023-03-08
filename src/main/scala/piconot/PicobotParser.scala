import scala.util.parsing.combinator._

import scala.collection.mutable.ListBuffer

import picolib._
import picolib.maze._
import picolib.semantics._
import java.io.File




case class Movement(d: MoveDirection);
case class StateSwitch(state: State);
case class RuleEffect(m: Movement, s: StateSwitch)

case class WallCondition (direction: MoveDirection, desc: RelativeDescription) 

//My way of writing DSLs treats rules as a smaller part of a wider state,
//While the API treats rules as the top level and each state as basically a label.
//To solve this, create some intermediate classes: ParsedRule (with no start state),
//and ParsedState (composed of a start state and list of rules).
case class ParsedRule(conds: List[WallCondition], effect: RuleEffect)
case class ParsedState(state: State, rules: List[ParsedRule])

object PicobotParser extends JavaTokenParsers with PackratParsers {
    def direction: Parser[MoveDirection] = ("N" | "E" | "S" | "W") ^^ {
        case "N" => North
        case "E" => East
        case "S" => South
        case "W" => West
    }

    def movement: Parser[Movement] = "go"~>direction ^^ {case d => Movement(d)}
    def stateSwitch: Parser[StateSwitch] = "switch"~"to"~> stringLiteral ^^ {case s => StateSwitch(State(s))}
    def effects: Parser[RuleEffect] = (
        movement ~ "and" ~ stateSwitch ^^ {case m~"and"~s => RuleEffect(m,s)}
     |  stateSwitch ~ "and" ~ movement ^^ {case s~"and"~m => RuleEffect(m,s)}
     |  movement ^^ {case m => RuleEffect(m, StateSwitch(State("")))}
     |  stateSwitch ^^ {case s => RuleEffect(Movement(StayHere), s)}
    )

    def wallConditions: Parser[List[WallCondition]] = ( 
        wallConditions ~ "and" ~ wallConditions ^^ { case l~"and"~r => l ++ r }
     |  direction <~ "wall" ^^ {case d => List(WallCondition(d, Blocked))}
     |  direction <~ "clear" ^^ {case d => List(WallCondition(d, Open))}
    )

    def rule: Parser[ParsedRule] = ( 
        "else" ~ "if" ~> wallConditions ~ effects ^^ {case c~e => ParsedRule(c, e)}
     |  "if" ~> wallConditions ~ effects ^^ {case c~e => ParsedRule(c, e)}
     |  "else" ~> effects ^^ {case e => ParsedRule(List(), e)}
    )

    def rules: Parser[List[ParsedRule]] = rep1(rule)

    def state: Parser[ParsedState] = (
        stringLiteral ~ "(" ~ rules ~ ")" ^^ {case s~"("~r~")" => ParsedState(State(s), r)}
    )
    def states: Parser[List[ParsedState]] = rep1(state)

    def wallsToSurroundings(wcs: List[WallCondition]): Surroundings =
        var north : RelativeDescription = Anything;
        var east : RelativeDescription = Anything;
        var south : RelativeDescription = Anything;
        var west : RelativeDescription = Anything;

        var wc = WallCondition;
        for (wc <- wcs) {
            wc.direction match 
                case North => north = wc.desc 
                case East => east = wc.desc 
                case South => south = wc.desc 
                case West => west = wc.desc 
        }
        Surroundings(north, east, west, south);

    def parsedToAPI(states: List[ParsedState]): List[Rule] = 
        // Initialize a mutable linked list, that we can add to.
        var returnRules = new ListBuffer[Rule];

        var parsedState = ParsedState;
        var parsedRule = ParsedRule;
        for (parsedState <- states) {
            for (parsedRule <- parsedState.rules) {
                //If the parsed rule's final state is blank, the final state is the start state
                if(parsedRule.effect.s.state == State("")) 
                then returnRules += Rule( parsedState.state,
                                          wallsToSurroundings(parsedRule.conds),
                                          parsedRule.effect.m.d,
                                          parsedState.state);
                //Otherwise, set final state.
                else returnRules += Rule( parsedState.state,
                                          wallsToSurroundings(parsedRule.conds),
                                          parsedRule.effect.m.d,
                                          parsedRule.effect.s.state);
            }
        }

        returnRules.toList;

    def parseString(input: String) = 
        val parseResult = parseAll(states, input)
        parseResult match {
            case Success(result, next) => parsedToAPI(result)
            case _ => ()
        }   

    def main(args: Array[String]) = parseString(args(0))

}

