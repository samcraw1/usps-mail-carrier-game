import java.awt.Color;
import java.awt.Graphics;

public class Player {
    int x;
    int y;
    int speed;
    String name;
    int packages;

public Player(String name) {
    this.name = name;
    this.x = 100;
    this.y = 100;
    this.speed = 4;
    this.packages = 5;
}

public void draw(Graphics g) {
    g.setColor(Color.BLUE);
    g.fillRect(x, y, 48, 48);
 }
}



    