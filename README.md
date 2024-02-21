# Java Runner

**Java Runner** is my first attempt at game development. I am very passionate about video games
and I wanted to build a simple but entertaining game without a game engine to learn about the underlying 
logic and how game components should interact. Java Runner is a 2D platforming game similar 
to a game with Italian plumbers, and the main goal is to reach the end of the level with your
character without falling or running into hazards. The game also includes power-ups, a built-in
timer for speed-running, and the ability to save and load game states. I was able to learn about
game *threads*, *collision detection algorithms*, features of *Java Swing*, how to design *tests for
complete code coverage*, and optimal *design principles* for software construction.

## Stack and Implementation
Java Runner was built using *Java*, the Java *Swing* library for the GUI, the *JSON* library for saving
and loading game states, and the *JUnit* library for testing. I drew the sprites using Piskel. 
A default test level was implemented using a *.csv file* with different values representing different 
blocks in the game. Java was chosen since it is an object-oriented language similar to C# used in Unity.

## Usage
After cloning the repository, run MainGUI to start the game.

- At the main menu
  - Click **"New Game"** to load a new game
  - Click **"Load Game"** to load the last saved game state (starts New Game if no previous save state)
  - Click **"Upload Map .csv"** to upload a custom game map (up to 28 rows, infinite columns), 
  where values correspond to following blocks:
    - 0 = empty space
    - 1 = normal block
    - 2 = hazard
    - 3 = speed up
    - 4 = invulnerability
    - 5 = finish line
  - Click **"Reset Default Map"** to use the test map provided in the project files

- In the game:
  - Press **left arrow** to move left
  - Press **right arrow** to move right
  - Walk over "green arrows (speed)" or "shield (invulnerability)" to collect the power-up
    - 3 power-ups can be held
    - Speed doubles the character velocity for 3 seconds
    - Invulnerability makes the character pass through hazards for 3 seconds (character becomes golden)
  - Press **1, 2, or 3** to use the corresponding item at the top of the HUD
  - Press **space** to jump
    - The character can be moved left or right in the air
  - Press the **"s"** key to save the current game state 
  - Touching "spikes (hazard)" while not invulnerable or falling to the bottom boundary will result in "Game Over"
  - Touching the green "GOAL (finish line)" blocks will result in level completion 

- At the end screen:
  - Click **"Back to Menu"** to return to the main menu panel

## What's Next
TODO: music and pause menu.