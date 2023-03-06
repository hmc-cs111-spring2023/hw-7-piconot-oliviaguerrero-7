[picobot]: https://www.cs.hmc.edu/twiki/bin/view/CS5/PicobotProgrammingGold
[api]: https://hmc-cs111-spring2023.github.io/docs/picolib/
[javafx]: https://gluonhq.com/products/javafx/
[emptyfile]: https://d1b10bmlvqabco.cloudfront.net/attach/ijjfckl6old4zz/hku6eqiwn313j8/il5k6nm8w0yk/empty.txt
[parsercombinatorresource]: http://bitwalker.org/posts/2013-08-10-learn-by-example-scala-parser-combinators/
[parsercombinatorapi]: https://www.scala-lang.org/api/2.12.8/scala-parser-combinators/scala/util/parsing/combinator/Parsers.html

# Piconot

## Overview

This assignment asks you to re-design the syntax of an existing language
(Picobot) and to implement your syntax as either an internal and external DSL.

_I very highly recommend working with a partner on this assignment! It's good to have someone
to talk with about language design and implementation issues._

## Checklist

- [ ] Design a new syntax for Picobot
  - [ ] Describe your design in `design.md`
  - [ ] Before you implement the syntax, write the "empty room" program in
        `example-ideal.txt`
- [ ] Choose whether you will implement your syntax as an internal or external DSL,
      then complete one of the following:
  - [ ] Implement your new syntax as an internal DSL
    - [ ] Add files, as needed, to implement your syntax
    - Include two example programs
      - [ ] `src/main/scala/piconot/internal/Empty.scala`
      - [ ] `src/main/scala/piconot/internal/RightHand.scala`
    - [ ] Describe your implementation process in `evaluation.md`
  - [ ] Implement your new syntax as an external DSL, using parser combinators
    - [ ] Add files, as needed, to implement your syntax
    - Include at least two example programs
      - [ ] `src/main/scala/piconot/external/Empty.bot`
      - [ ] `src/main/scala/piconot/external/RightHand.bot`
    - [ ] Provide instructions for how to run piconot in `build.md`
    - [ ] Describe your implementation process in `evaluation.md`
- [ ] Give feedback on another design and implementation

## Warm-up: Picobot

You should (re-)familiarize yourself with
[Picobot, as it's defined in CS 5][picobot]. Make sure you understand the
"empty room" and "maze" Picobot programs (posted on Discord).

## Syntax design

Design your own syntax for Picobot. Try to come up with a design that is
faithful to the domain (of maze-navigation), that does not add any new features
(e.g., non-determinism), but that is as different as possible from the current
syntax. The more novel your syntax, the better, assuming that it is faithful to
the domain. **Design your syntax as an ideal, i.e., don't worry at all about how
you will implement it.**

Re-write the empty room program in your new syntax, in the file
`example-ideal.txt`.

Describe and justify your design, in the file `design.md` (see that file for
instructions on what to write).

## Picobot semantics

I have provided a library that implements the semantics of Picobot (i.e., what
happens when a program runs). You will need to transform statements from your
syntax into calls to the provided library.

You will need to understand the interface for the Picobot library (but not its
implementation!). You should take a look at the file
`src/main/scala/piconot/EmptyAPI.scala` for an example use of the library. You
can also look at the library's [auto-generated documentation][api]. Thanks to sbt, you
shouldn't need to do anything special with the library to build and run your code.

## Building and running the code

In `src/main/scala/piconot/EmptyAPI.scala`, you will find a program that uses the Picobot
library to create rules that solve the "empty room" problem. You can run this program
by running `sbt run` in the root directory of the project. A text-based simulation will
run in the terminal.

## Syntax implementation: internal DSL

If you choose to implement your syntax as an internal DSL, place your code for the
internal implementation in the `piconot.internal` package. You can (and probably should)
add new files to the implementation.

### A running diary

As you work, comment on your experience in the file `evaluation.md`. In particular, if you
change your ideal syntax, you should describe what changed and why you made the change
(e.g., your original idea was too hard to implement or it didn't match well with the
library calls) You should also answer the following questions: On a scale of 1–10 (where
10 is "a lot"), how much did you have to change your syntax? On a scale of 1–10 (where 10
is "very difficult"), how difficult was it to map your syntax to the provided API?

### Sample programs

Include at least two sample programs.

For the internal version, include the files:

1. `src/main/scala/piconot/internal/Empty.scala`: a program in your internal
   DSL that can solve the empty room. A file that describes an empty room is in
   `resources/empty.txt`.

2. `src/main/scala/piconot/internal/RightHand.scala`: a program in your
   internal DSL that uses the right-hand rule to solve the maze in
   `resources/maze.txt`.

### Provide instructions for building and running your internal DSL

In the `build.md` file provide instructions for how to run your internal DSL. (For
example, `sbt run-main <classname>`) Ask for help if you're not sure how do so!

**Make sure the build process works before your final submission!**

## Syntax implementation: external DSL

If you choose to implement your syntax as an internal DSL, use [parser
combinators][parsercombinatorapi] and case classes to implement the parser. Here's a [good
article][parsercombinatorresource] for learning a bit more about parser combinators; there
are many other ones on the web.

**Note:** Your intermediate representation might be data structures from the
`picolib` library, or it might be data structures of your own design, or a
combination of both. In some cases, you might not need to create _all_ the
packages described above.

## A running diary

As you work, comment on your experience in the file `evaluation.md`. In
particular, each time you change your ideal syntax, you should describe what
changed and why you made the change (e.g., your original idea required an
operator that you couldn't implement in Scala or integrate with your other ides
). You should also answer the following questions: On a scale of 1–10 (where 10
is "a lot"), how much did you have to change your syntax? On a scale of 1–10
(where 10 is "very difficult"), how difficult was it to map your syntax to the
provided API?

### Sample programs

Include at least two sample programs.

For the external version, include the files:

1. `src/main/scala/piconot/external/Empty.bot`: a program in your external
   DSL that
   can solve the empty room. A file that describes an empty room is in
   `resources/empty.txt`.

2. `src/main/scala/piconot/external/RightHand.scala`: a program in your external DSL
   that uses the right-hand rule to solve the maze in `resources/maze.txt`.

### Provide instructions for building and running your external DSL

In the `build.md` file provide instructions for how to build your external DSL and how to
run it on a piconot program. Note that for your users to run your language this way, you
will have to design your solution so that the `main` function takes and processes an
argument that contains the filename of the maze file and the filename of the picobot
program. Ask for help if you're not sure how do so!

**Make sure the build process works before your final submission!**

## Grading

A "three" is work that completes all the parts and which the syntax is different
from the original Picobot syntax in a way that is justified in `design.md`.

A "four" furthermore gracefully handles errors and / or is exceptionally well-documented.
Errors might include syntax errors, programs that reference undefined rules, etc. If you
are implementing an external DSL, know that giving good error messages for parsers is
notoriously difficult. The `failure`, `positioned`, `phrase`, and `log` combinators
(defined in the [scala.util.parsing.combinators.Parsers
trait][parsercombinatorresource] may be helpful.

## Peer-review

Comment on another design and implementation. You should look through
their grammars, pay special attention to their `design.md` and `evaluation.md`
files, their internal DSL and their parser. You might consider the following
questions:

- What good insights about implementation did the team in `design.md`? Did
  you have any experiences that were similar to the team?
- How nice is their implementation? Is it understandable? Clean? Modular?
  Extensible? What do you like about their code? Are there any particularly
  elegant ways they overcame implementation challenges?
- Download their code and run their example programs. How easy was it to run?
  Did the instructions work?
- Modify the example programs to introduce errors. How robust is their
  implementation? What do you like about their error-handling? What can be improved?
- Are there any implementation tricks that you can suggest to the team?
  Anything you see that might make the implementation easier or more like the
  original design?

## Advice

### Time

**Give yourself a time budget.** This project can easily eat up hours of time,
as you try to get your language _just right_. If you've got that kind of time --
go for it! But if your time is limited, a budget would be good. Here's a
suggestion, though feel free to alter it:

- 30 minutes reviewing Picobot
- 1 hour coming up with a new syntax design
- 30 minutes writing the example(s) in your ideal syntax
- 30 minutes skimming the provided example program and API
- 2-3 hours implementing your design
- 1 hour answering the questions in `design.md` and `evaluation.md`
- 30 minutes - 1 hour: critique / peer review

**Start early!** Note that this budget doesn't leave _that_ much time for implementing
your design. You might have to make hard choices and radically change your syntax to get
things working. That's okay! You can always come back to it when you have more time.

### Implementation techniques

Look back at all our class materials and previous assignments to see tips
internal and external implementation techniques. If you get stuck trying to
implement something, ask for help!
