import java.awt.Color;
import java.awt.Graphics;

public class NPC {
    int x;
    int y;
    String name;
    String dialogue;
    String type;
    

    public NPC(String name, int x, int y, String dialogue, String type) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.dialogue = dialogue;
        this.type = type;

    }

    public void draw(Graphics g) {
        if (type.equals("person")) {
            drawPerson(g);
        } else if (type.equals("dog")) {
            drawDog(g);
        } else {
            g.setColor(Color.YELLOW);
            g.fillRect(x, y, 48, 48);
        }
    }

    // 48x48 pixel-art person (Mrs. Johnson) - pink dress, gray hair, peach face
    private void drawPerson(Graphics g) {
        // shadow under feet
        g.setColor(new Color(0, 0, 0, 100));
        g.fillOval(x + 8, y + 40, 32, 8);

        // hair bun on top (gray)
        g.setColor(new Color(180, 180, 180));
        g.fillOval(x + 18, y + 2, 12, 10);

        // face (peach)
        g.setColor(new Color(240, 200, 160));
        g.fillRect(x + 16, y + 10, 16, 14);
        g.setColor(new Color(200, 160, 125));   // shadow on right
        g.fillRect(x + 28, y + 10, 4, 14);

        // eyes
        g.setColor(new Color(20, 20, 20));
        g.fillRect(x + 19, y + 16, 3, 3);
        g.fillRect(x + 26, y + 16, 3, 3);

        // pink dress
        g.setColor(new Color(220, 110, 160));
        g.fillRect(x + 12, y + 24, 24, 18);
        g.setColor(new Color(245, 140, 185));   // shoulder highlight
        g.fillRect(x + 12, y + 24, 24, 2);
        g.setColor(new Color(170, 70, 120));    // bottom shadow
        g.fillRect(x + 12, y + 40, 24, 2);

        // arms peeking out
        g.setColor(new Color(240, 200, 160));
        g.fillRect(x + 10, y + 26, 4, 10);
        g.fillRect(x + 34, y + 26, 4, 10);

        // shoes
        g.setColor(new Color(40, 40, 40));
        g.fillRect(x + 16, y + 42, 6, 5);
        g.fillRect(x + 26, y + 42, 6, 5);
    }

    // 48x48 pixel-art dog (Rover) - brown body, ears, tail
    private void drawDog(Graphics g) {
        // shadow
        g.setColor(new Color(0, 0, 0, 100));
        g.fillOval(x + 6, y + 40, 36, 6);

        // body (brown oval)
        g.setColor(new Color(140, 90, 50));
        g.fillOval(x + 8, y + 22, 28, 18);
        g.setColor(new Color(170, 115, 70));    // back highlight
        g.fillOval(x + 10, y + 22, 22, 6);

        // tail wagging up to the right
        g.setColor(new Color(140, 90, 50));
        g.fillRect(x + 32, y + 16, 4, 10);
        g.fillRect(x + 34, y + 12, 4, 6);

        // head (smaller circle on the left)
        g.setColor(new Color(140, 90, 50));
        g.fillOval(x + 4, y + 14, 18, 16);
        g.setColor(new Color(170, 115, 70));
        g.fillOval(x + 6, y + 14, 12, 4);

        // ears
        g.setColor(new Color(90, 55, 30));
        g.fillRect(x + 4, y + 12, 5, 8);
        g.fillRect(x + 16, y + 12, 5, 8);

        // eyes
        g.setColor(new Color(20, 20, 20));
        g.fillRect(x + 9, y + 20, 2, 2);
        g.fillRect(x + 16, y + 20, 2, 2);

        // black nose
        g.setColor(new Color(20, 20, 20));
        g.fillOval(x + 4, y + 22, 5, 4);

        // 4 legs (small dark rectangles)
        g.setColor(new Color(90, 55, 30));
        g.fillRect(x + 10, y + 38, 4, 6);
        g.fillRect(x + 18, y + 38, 4, 6);
        g.fillRect(x + 26, y + 38, 4, 6);
        g.fillRect(x + 32, y + 38, 4, 6);
    }
}
