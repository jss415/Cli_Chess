# Cli_Chess

A two-player command-line chess game written in Java. Renders a Lanterna-based TUI by default, with a plain console fallback for terminals that can't host the TUI.

## Demo

https://github.com/jss415/Cli_Chess/raw/main/demo.mp4

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
