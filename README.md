# Cli_Chess

A two-player command-line chess game written in Java.

## Demo



https://github.com/user-attachments/assets/8fe366d3-316e-4878-9c60-659ec6ae2f95


## Features

- **Full chess rules**
  - Legal-move validation for every piece type (pawn, knight, bishop, rook, queen, king)
  - Check detection — moves that would leave your own king in check are rejected
  - Checkmate ends the game with the opposing player declared winner
  - Stalemate ends the game in a draw
- **Special moves**
  - Kingside and queenside castling (with all the standard preconditions: king and rook unmoved, path clear, king not in or passing through check)
  - En passant pawn captures
  - Pawn promotion to queen, rook, bishop, or knight
  - Two-square pawn opening move
- **Takeback** — undo any move from the in-game move history
- **Board flip** — swap player perspective at the press of a key
- **Draw offers and resignation** — built-in commands to end the game without checkmate
- **Coordinate-notation input** — type the from-square and to-square (e.g. `e2 e4`)


## Requirements

- JDK 11 or later
- A terminal that supports ANSI escape codes (Terminal.app, iTerm2, the VS Code integrated terminal, etc.)
- The bundled `lib/lanterna-3.1.3.jar` — already in the repo, no download needed

## Build

From the project root:

```
javac -cp "lib/lanterna-3.1.3.jar" -d bin $(find src -name "*.java")
```

## Run

```
java -cp "bin:lib/lanterna-3.1.3.jar" chess.Chess
```

If your terminal can't host the TUI, the program falls back automatically. To force the plain console UI:

```
java -cp "bin:lib/lanterna-3.1.3.jar" chess.Chess --console
```

## How to play

Moves are entered in coordinate notation — two squares separated by a space, e.g. `e2 e4`. For pawn promotion, append `Q`, `R`, `B`, or `N` (Queen by default if omitted): `e7 e8 Q`.

## Project layout

```
src/chess/        Java sources
  Chess.java      entry point
  model/          Board, Piece, Move, individual piece logic
  ui/             ConsoleUI (plain console)
  ui/tui/         Lanterna TUI components
lib/              third-party jars (Lanterna)
```
