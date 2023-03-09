# Evaluation: running commentary

## External DSL

_Describe each change from your ideal syntax to the syntax you implemented, and
describe_ why _you made the change._

- I ended up adding statements for "else if" and "elif" in defining a new rule, since I ended up finding that thinking about things as if-elif-else came more naturally.
- For similar reasons, I added somae aliases for Wall (Blocked) and Clear (Free, Open) since I was forgetting which one was the correct syntax.
- Technically, you can start out a set of wall conditions with "and" even if there is no item before it, due to specifics of the implementation.

**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**

- a 2. I basically kept the syntax identical to my original plans, with the only difference being a few more minor degrees of freedom I thought to add later.

**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**

- a 7. All of the stateegies i used to do what I wanted were *pretty* intuitive, but the end result is a messy labyrinth of parser combinators designed in the *perfect* way to shut up every bug the machine threw at me. (Partly because my original design didn't *strictly* require left recursion, and so I simply avoided ever needing it rather than worrying about using it.)