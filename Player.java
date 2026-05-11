import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Player {

    // FIELDS — every player has these things
    int x;          // horizontal position
    int y;          // vertical position
    int speed;      // how fast they move (pixels per frame)
    String name;
    int packages;   // how many packages they're carrying
    BufferedImage sprite;
    boolean inVehicle = false;  // whether the player is in a vehicle



    // CONSTRUCTOR — runs when you do "new Player("Sam")"
    public Player(String name) {
        this.name = name;
        this.x = 100;             // start in the middle area
        this.y = 100;
        this.speed = 4;
        this.packages = 5;        // start with 5 packages to deliver
        try {
            sprite = ImageIO.read(new File("assets/sprites/carrier.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // METHOD — draws the player on screen
    // To switch between PNG and code-drawn versions, change which line is commented out
    public void draw(Graphics g) {
        if (inVehicle) {
            drawTruck(g);
        } else {
            // drop shadow oval under the carrier (semi-transparent black)
            g.setColor(new Color(0, 0, 0, 100));
            g.fillOval(x + 6, y + 38, 36, 10);

            // V1: ChatGPT PNG sprite
            // g.drawImage(sprite, x - 4, y - 4, 56, 56, null);

            // V2: code-drawn pixel art mail carrier
            drawCodeCarrier(g);
        }
    }

    // V2 — 64x64 pixel art mail carrier with shading
    public void drawCodeCarrier(Graphics g) {
        int px = x - 8;   // center 64x64 sprite over 48x48 hitbox
        int py = y - 8;

        // ---- MAIL BAG ----
        g.setColor(new Color(110, 70, 40));
        g.fillRect(px + 44, py + 30, 16, 24);
        g.setColor(new Color(140, 95, 55));         // top highlight
        g.fillRect(px + 44, py + 30, 16, 2);
        g.setColor(new Color(80, 50, 30));          // bottom shadow
        g.fillRect(px + 44, py + 52, 16, 2);
        g.fillRect(px + 44, py + 36, 16, 2);        // strap line

        // ---- HAT ----
        g.setColor(new Color(20, 40, 90));
        g.fillRect(px + 16, py + 4, 32, 14);
        g.setColor(new Color(45, 75, 140));         // top highlight
        g.fillRect(px + 18, py + 4, 28, 2);
        g.setColor(new Color(10, 25, 60));          // base shadow
        g.fillRect(px + 16, py + 16, 32, 2);
        // hat brim
        g.setColor(new Color(20, 40, 90));
        g.fillRect(px + 14, py + 16, 36, 5);
        g.setColor(new Color(10, 25, 60));          // shadow under brim
        g.fillRect(px + 14, py + 20, 36, 1);
        // USPS logo
        g.setColor(new Color(220, 220, 220));
        g.fillRect(px + 28, py + 9, 8, 4);

        // ---- FACE ----
        g.setColor(new Color(220, 180, 140));
        g.fillRect(px + 20, py + 21, 24, 12);
        g.setColor(new Color(180, 140, 105));       // shadow on right side
        g.fillRect(px + 37, py + 21, 7, 12);
        g.setColor(new Color(240, 200, 160));       // cheek highlight (left)
        g.fillRect(px + 20, py + 21, 5, 7);
        // eyes
        g.setColor(new Color(20, 20, 20));
        g.fillRect(px + 25, py + 26, 4, 4);
        g.fillRect(px + 35, py + 26, 4, 4);

        // ---- BODY (blue uniform) ----
        g.setColor(new Color(35, 60, 130));
        g.fillRect(px + 16, py + 32, 32, 18);
        g.setColor(new Color(60, 90, 170));         // shoulder highlight
        g.fillRect(px + 16, py + 32, 32, 2);
        g.setColor(new Color(20, 35, 90));          // bottom body shadow
        g.fillRect(px + 16, py + 48, 32, 2);
        // belt
        g.setColor(new Color(15, 20, 45));
        g.fillRect(px + 16, py + 44, 32, 3);

        // ---- PANTS ----
        g.setColor(new Color(20, 30, 60));
        g.fillRect(px + 20, py + 50, 9, 10);
        g.fillRect(px + 35, py + 50, 9, 10);
        g.setColor(new Color(40, 55, 95));          // left edge highlight
        g.fillRect(px + 20, py + 50, 2, 10);
        g.fillRect(px + 35, py + 50, 2, 10);

        // ---- SHOES ----
        g.setColor(new Color(10, 10, 10));
        g.fillRect(px + 20, py + 58, 9, 5);
        g.fillRect(px + 35, py + 58, 9, 5);
        g.setColor(new Color(50, 50, 50));          // shoe top highlight
        g.fillRect(px + 20, py + 58, 9, 1);
        g.fillRect(px + 35, py + 58, 9, 1);
    }

    // Top-down USPS LLV mail truck - drawn over the 48x48 player hitbox.
    public void drawTruck(Graphics g) {
        int tx = x - 8;
        int ty = y - 8;

        // shadow under the truck
        g.setColor(new Color(0, 0, 0, 100));
        g.fillOval(tx + 4, ty + 56, 56, 8);

        // wheels (4 black rectangles at the corners)
        g.setColor(new Color(15, 15, 15));
        g.fillRect(tx + 4,  ty + 12, 6, 12);   // front-left
        g.fillRect(tx + 54, ty + 12, 6, 12);   // front-right
        g.fillRect(tx + 4,  ty + 40, 6, 12);   // rear-left
        g.fillRect(tx + 54, ty + 40, 6, 12);   // rear-right

        // main body (white box)
        g.setColor(new Color(245, 245, 245));
        g.fillRect(tx + 8, ty + 8, 48, 48);

        // roof shadow on right edge for depth
        g.setColor(new Color(210, 210, 210));
        g.fillRect(tx + 50, ty + 8, 6, 48);

        // top highlight along the front
        g.setColor(new Color(255, 255, 255));
        g.fillRect(tx + 8, ty + 8, 48, 3);

        // USPS blue stripe along the side (the iconic LLV stripe)
        g.setColor(new Color(0, 75, 135));
        g.fillRect(tx + 8, ty + 30, 48, 6);

        // red stripe under the blue
        g.setColor(new Color(200, 40, 40));
        g.fillRect(tx + 8, ty + 36, 48, 2);

        // windshield at the front (top of vehicle)
        g.setColor(new Color(40, 70, 100));
        g.fillRect(tx + 14, ty + 12, 36, 10);
        g.setColor(new Color(80, 130, 170));        // glass highlight
        g.fillRect(tx + 14, ty + 12, 36, 2);

        // headlights (small yellow rectangles at front corners)
        g.setColor(new Color(250, 220, 100));
        g.fillRect(tx + 10, ty + 9, 5, 3);
        g.fillRect(tx + 49, ty + 9, 5, 3);

        // USPS eagle decal area (small white box on the side stripe)
        g.setColor(new Color(245, 245, 245));
        g.fillRect(tx + 26, ty + 31, 12, 4);
        g.setColor(new Color(0, 75, 135));          // dark blue text
        g.drawString("USPS", tx + 26, ty + 35);
    }
}
