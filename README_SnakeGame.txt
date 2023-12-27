
# Snake Game Documentation

## Overview
This Java-based Snake game is a terminal-run application where the player controls a snake to eat apples. Each apple consumed increases the snake's length and the player's score. The game ends if the snake runs into the game board's edges or into itself.

## Game Components

### Classes
- **Main**: The entry point of the game. It initializes and starts the game.
- **Game**: Core of the game logic, handling the main game loop, game board, snake, and game threads.
- **Snake**: Represents the snake with properties like direction, moves, score, etc,.
- **Apple**: Manages the apple's position on the game board.
- **Rectangle**: Represents the game board, handling dimensions and elements like the apple.
- **Direction**: Enum for the snake's movement direction (Up, Down, Left, Right).
- **PositionThread**: Handles user input to control the snake's direction.
- **DeadSnakeThread**: Manages game-over scenarios and prompts for game restarts.

## Features
- **Basic Gameplay**: Navigate the snake to eat apples and grow in length.
- **Score Tracking**: Scores increase with each apple consumed.
- **Boundary Collision**: The game ends if the snake hits the board edges.
- **Self-Collision**: The game ends if the snake runs into itself.
- **Game Restart**: Option to restart the game after it ends.

## Running the Game
- Compile and run the `Main.java` file in a Java-enabled terminal.
- Use keyboard inputs to control the snake's direction: a, s, d, w, then click Enter to confirm the input.


