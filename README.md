# USPS Mail Carrier Game

A 2D top-down delivery game built in pure Java with Swing — no game engine.

You play as a USPS mail carrier walking a route, delivering packages to houses, and chatting with neighborhood NPCs.

## Features

- **Tile-based world** loaded from a text file
- **WASD movement** with collision detection (can't walk through water or houses)
- **Delivery system** — press `E` next to a house to deliver
- **NPCs with dialogue** — press `T` near an NPC to talk
- **Win condition** — "ROUTE COMPLETE!" when all packages delivered
- **60fps game loop** using `Thread` and `Runnable`
- **2D arrays + file I/O** for the map system

## Tech Stack

- Java 25 (works on any modern JDK)
- Java Swing (`JFrame`, `JPanel`, `Graphics`)
- AWT (`KeyListener`, `Color`, `BufferedReader`)
- No external dependencies

## Controls

| Key | Action |
|-----|--------|
| W / A / S / D | Move |
| E | Deliver package to nearby house |
| T | Talk to nearby NPC |

## Run It

```bash
javac *.java
java MailCarrierGame
```

## Project Structure

```
mail-carrier-game/
├── MailCarrierGame.java   # main entry point
├── GamePanel.java         # canvas + game loop
├── Player.java            # player class
├── NPC.java               # non-playable characters
├── TileMap.java           # tile grid + collision
├── KeyHandler.java        # keyboard input
└── assets/
    └── map01.txt          # the level data
```

## How It Works

The map is just a text file — each number is a different tile type:

```
0 = grass
1 = road
2 = water (blocks player)
3 = house (blocks player, can deliver to)
4 = delivered house
```

`TileMap` reads the file into a 2D array. `GamePanel` runs a game loop 60 times per second, updating the player position and redrawing the screen.

Built as a Java learning project to reinforce OOP, classes, inheritance, 2D arrays, file I/O, and event handling.
