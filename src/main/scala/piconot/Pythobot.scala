import scala.util.parsing.combinator._

import scala.collection.mutable.ListBuffer
import io.Source

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

object Pythobot extends JavaTokenParsers with PackratParsers {
    def direction: Parser[MoveDirection] = ("N" | "E" | "S" | "W") ^^ {
        case "N" => North
        case "E" => East
        case "S" => South
        case "W" => West
    }

    def movement: Parser[Movement] = "go"~>direction ^^ {case d => Movement(d)}
    def stateSwitch: Parser[StateSwitch] = "switch"~"to"~> state ^^ {case s => StateSwitch(s)}
    def effects: Parser[RuleEffect] = (
        movement ~ "and" ~ stateSwitch ^^ {case m~"and"~s => RuleEffect(m,s)}
     |  stateSwitch ~ "and" ~ movement ^^ {case s~"and"~m => RuleEffect(m,s)}
     |  movement ^^ {case m => RuleEffect(m, StateSwitch(State("")))}
     |  stateSwitch ^^ {case s => RuleEffect(Movement(StayHere), s)}
    )

    def wallCondition: Parser[WallCondition] = (
        "and" ~> direction <~ "wall" ^^ {case d => WallCondition(d, Blocked)}
     | "and" ~> direction <~ "blocked" ^^ {case d => WallCondition(d, Blocked)}
     |  "and" ~> direction <~ "clear" ^^ {case d => WallCondition(d, Open)}
     |  "and" ~> direction <~ "free" ^^ {case d => WallCondition(d, Open)}
     |  "and" ~> direction <~ "open" ^^ {case d => WallCondition(d, Open)}
     |  direction <~ "wall" ^^ {case d => WallCondition(d, Blocked)}
     |  direction <~ "blocked" ^^ {case d => WallCondition(d, Blocked)}
     |  direction <~ "clear" ^^ {case d => WallCondition(d, Open)}
     |  direction <~ "free" ^^ {case d => WallCondition(d, Open)}
     |  direction <~ "open" ^^ {case d => WallCondition(d, Open)}
    )

    def wallConditions: Parser[List[WallCondition]] = rep(wallCondition)

    def rule: Parser[ParsedRule] = ( 
        //These three are all identical, just "if".
        "else" ~ "if" ~> wallConditions ~ effects ^^ {case c~e => ParsedRule(c, e)}
     |  "if" ~> wallConditions ~ effects ^^ {case c~e => ParsedRule(c, e)}
     |  "elif" ~> wallConditions ~ effects ^^ {case c~e => ParsedRule(c, e)}
        //for else, we need to explicitly say there are 0 conditions.
     |  "else" ~> effects ^^ {case e => ParsedRule(List(), e)}
    )

    def rules: Parser[List[ParsedRule]] = ( 
        "(" ~> rep1(rule) <~ ")"
    // | "(" ~ ")" ^^ Error("One of your states has zero rules!", ) //IDK how to access unparsed string
    // |  "(" ^^ Error("One of your states is missing a close parenthesis!")
    )

    def parsedState: Parser[ParsedState] = (
        state ~ rules ^^ {case s~r => ParsedState(s, r)}
    )
    def parsedStates: Parser[List[ParsedState]] = rep1(parsedState)

    def state: Parser[State] = (
        //regex for at least one alphanumeric character or underscore
        """[a-zA-Z0-9_]+""".r ^^ {case s => State(s)}
    )
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

    def parseString(input: String) : List[Rule] = 
        val parseResult = parseAll(parsedStates, input)
        parseResult match {
            case Success(result, next) => parsedToAPI(result)
            case Failure(msg, _) => throw new Exception(msg)
            case Error(msg, _) => throw new Exception(msg)
        }   



}

    //def main(args: Array[String]): Int = 
        // Parse entire file into string
        val file = Source.fromFile("src/main/scala/piconot/external/RightHand.bot")
        val input = try file.mkString finally file.close()
        val rules = Pythobot.parseString(input)

        val maze = Maze("resources/maze.txt")
        object EmptyText extends TextSimulation(maze, rules)

        //return 0