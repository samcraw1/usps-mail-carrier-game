import java.awt.Color;
import java.awt.Graphics;

public class NPC {
    int x;
    int y;
    String name;
    String dialogue;

    public NPC(String name, int x, int y, String dialogue) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.dialogue = dialogue;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 48, 48);
    }
}
