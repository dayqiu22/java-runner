# My Personal Project

## 2D Platformer Game

For my project this term, I would like to design and program a 2D platformer game.
The goal of the game would be similar to Mario, which is to make your character reach
the end of a level without hitting hazards.
The basic features of the game will include:

- Left, right, and jumping movement
- Platforms to maneuver through
- ***HAZARDS*** that players must avoid
- Unique **POWER-UPS** to pick up and use
- Saving and loading of game data

Anybody who has played games similar to Mario and anybody who wants to try a fun little 
game in general will be the primary users of this application. While I am not aiming to become
a game developer right now, I have played a lot of games during my spare time,
and I am very eager to try to develop a functional game myself. I would also like 
to make this game as pretty and as fun as possible with the time I have, as I would
like any potential player to have an enjoyable time playing the game I made.

## User Stories

- As a player, I want to be able to pick up power-ups and add them to my inventory.
- As a player, I want to be able to see the list of power-ups I currently possess.
- As a player, I want to be able to use my power-ups.
- As a player, I want to be able to move my character left, right, and jump.
- As a player, I want to be able to save my game state and quit in the options' menu.
- As a player, I want to be able to load a saved game state or start a new game in the main menu.

## Instructions for Grader

- At the main menu
  - Click "New Game" to load a new game
  - Click "Load Game" to load the last saved game state
  - Either will result in the game panel being displayed


- In the game (all visual components found here):

***NOTE: Since the game was originally implemented in Phase 1 with Lanterna for the terminal
the representation of objects in the game are single points in the **top left corner** of the sprites, 
rather than the entire 50x50 sprites displayed. Thus, the collision detection works as before
but only when the **top left corner** of objects come into contact. Please forgive any clunkiness
that may occur during the gameplay.***

  - Press left arrow to move left
  - Press right arrow to move right
  - Walk over "green arrows (speed)" or "shield (invulnerability)" to collect the item **(Action 1)**
    - At most 3 items can be held
    - Speed doubles the character velocity for 3 seconds
    - Invulnerability makes the character pass through hazards for 3 seconds (character becomes golden)
  - Press 1, 2, or 3 to use the corresponding item at the bottom of the UI **(Action 2)**
  - Press space to jump
    - Can move the character left or right midair
  - Press the "s" key to save the current game state 
  - Walk over "spikes (hazard)" while not invulnerable to end game


- At the end screen:
  - Click "Back to Menu" to return to the main menu panel