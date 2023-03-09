# Design

## Who is the target for this design, e.g., are you assuming any knowledge on the part of the language users?

The target for this design is people who don't know much about either picobot or programming, and are taking cs5 as their first programming class and could really use a natural-seeming syntax (similar to python)

## Why did you choose this design, i.e., why did you think it would be a good idea for users to express the maze-searching computation using this syntax?

I thought it would be good to have the program require all of a state's rules be defined in one block, and for each state to assume that it will lead to another rule in its own state unless you explicitly state otherwise. This doesn't *restrict* the ways for your picobot to move at all, but makes it simpler to recognize when you're explicitly switching to another state.

## What behaviors are easier to express in your design than in Picobot’s original design?  If there are no such behaviors, why not?

It is easier to express human written rules, that often stay in the same state for a long time and only think about one requirement at a time, because it doesn't need the snytax of every other state.

## What behaviors are more difficult to express in your design than in Picobot’s original design? If there are no such behaviors, why not?

It is harder to express programs that have like 20 states, because each state has a name rather than a number. This improves readability, but also makes it more complicated when *every* rule in a state changes the state.

## On a scale of 1–10 (where 10 is “very different”), how different is your syntax from PicoBot’s original design?

4

## Is there anything you would improve about your design?

I think it would be good if there were a better way to deal with large numbers of possible states.
