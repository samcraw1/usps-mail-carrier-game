import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

public class GamePanel extends JPanel implements Runnable {

    // FIELDS
    Thread gameThread;
    Player carrier;
    KeyHandler keyH = new KeyHandler();
    TileMap tileMap = new TileMap("assets/map01.txt");
    NPC oldlady = new NPC("Mrs. Johnson", 200, 200, "Thank you for delivering on time!");
    NPC dog = new NPC("Rover", 300, 300, "Woof! Thanks for the treats!");
    String currentDialogue = "";   // holds whatever NPC is talking right now
    // CONSTRUCTOR
    public GamePanel() {
        this.setPreferredSize(new Dimension(768, 576));
        this.setBackground(Color.BLACK);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        carrier = new Player("Sam");
    }

    // METHOD - starts the game thread
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // METHOD - the game loop (runs ~60 fps)
    public void run() {
        while (gameThread != null) {
            update();
            repaint();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // METHOD - update game state — moves player IF the new position isn't a solid tile
    public void update() {
        // figure out where the player wants to move
        int newX = carrier.x;
        int newY = carrier.y;

        if (keyH.upPressed)    newY -= carrier.speed;
        if (keyH.downPressed)  newY += carrier.speed;
        if (keyH.leftPressed)  newX -= carrier.speed;
        if (keyH.rightPressed) newX += carrier.speed;

        // only actually move if the new spot isn't a wall
        if (!tileMap.isSolid(newX, newY, 48)) {
            carrier.x = newX;
            carrier.y = newY;
        }

        // try to deliver if E was pressed
        if (keyH.ePressed) {
            tryDeliver();
            keyH.ePressed = false;   // reset so it only fires once per press
        }

        // try to talk if T was pressed
        if (keyH.tPressed) {
            tryTalk();
            keyH.tPressed = false;
        }
    }

    // METHOD - check if any NPC is close, show their dialogue
    public void tryTalk() {
        // distance from player to each NPC
        if (isClose(oldlady.x, oldlady.y)) {
            currentDialogue = oldlady.name + ": " + oldlady.dialogue;
        } else if (isClose(dog.x, dog.y)) {
            currentDialogue = dog.name + ": " + dog.dialogue;
        } else {
            currentDialogue = "";   // no one nearby
        }
    }

    // helper - is the player close to this spot? (within 60 pixels)
    public boolean isClose(int npcX, int npcY) {
        int dx = Math.abs(carrier.x - npcX);
        int dy = Math.abs(carrier.y - npcY);
        return dx < 60 && dy < 60;
    }

    // METHOD - try to deliver to a nearby house
    public void tryDeliver() {
        // figure out which tile the player is on (use center of player)
        int playerCol = (carrier.x + 24) / 48;
        int playerRow = (carrier.y + 24) / 48;

        // check the 4 tiles next to the player (up, down, left, right)
        int[][] neighbors = {
            {playerRow - 1, playerCol},  // up
            {playerRow + 1, playerCol},  // down
            {playerRow, playerCol - 1},  // left
            {playerRow, playerCol + 1}   // right
        };

        for (int[] spot : neighbors) {
            int row = spot[0];
            int col = spot[1];

            // make sure this tile is actually on the map
            if (row >= 0 && row < 10 && col >= 0 && col < 10) {

                // is this neighbor an undelivered house, and do we have packages?
                if (tileMap.mapData[row][col] == 3 && carrier.packages > 0) {
                    tileMap.setTile(row, col, 4);   // mark it delivered
                    carrier.packages--;              // one less package
                    return;                          // stop after one delivery
                }
            }
        }
    }

    // METHOD - draw to the screen
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        tileMap.draw(g);
        oldlady.draw(g);
        dog.draw(g);
        carrier.draw(g);
        

        // show package count in top corner
        g.setColor(Color.WHITE);
        g.drawString("Packages left: " + carrier.packages, 600, 30);

        // show NPC dialogue at the bottom if there is one
        if (!currentDialogue.isEmpty()) {
            g.setColor(Color.BLACK);
            g.fillRect(20, 500, 728, 50);
            g.setColor(Color.WHITE);
            g.drawString(currentDialogue, 40, 530);
        }

        // win screen if all packages delivered
        if (carrier.packages == 0) {
            g.setColor(new Color(0, 0, 0, 200));   // semi-transparent black overlay
            g.fillRect(0, 0, 768, 576);
            g.setColor(Color.GREEN);
            g.drawString("ROUTE COMPLETE!", 340, 280);
            g.setColor(Color.WHITE);
            g.drawString("All packages delivered. Great job!", 290, 310);
        }
    }
}
