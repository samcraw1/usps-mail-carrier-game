import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class House {

    // FIELDS
    int x;
    int y;
    int address;
    Color houseColor;
    boolean delivered;
    String direction;
    int distance;       // tiles between this house and its nearest road

    // CONSTRUCTOR
    public House(int x, int y, int address, Color houseColor, String direction, int distance) {
        this.x = x;
        this.y = y;
        this.address = address;
        this.houseColor = houseColor;
        this.delivered = false;
        this.direction = direction;
        this.distance = distance;
    }

    public void draw(Graphics g) {

        Color pathColor = new Color(190, 170, 140);
        Color pathBorder = new Color(140, 120, 90);
        Color fenceColor = new Color(140, 100, 60);
        Color fenceDark = new Color(90, 60, 35);
        Color gateRed = new Color(200, 40, 40);          // red metal gate
        Color gateRedDark = new Color(140, 20, 20);
        Color gateRedHighlight = new Color(240, 90, 90);

        // ===== WIDE U-FENCE around a big front yard with gate at street side =====
        // Yard extends ~20px past the house tile sideways. Path is 24px thick.
        if (direction.equals("DOWN")) {
            int yardTop = y + 44;        // start right at house bottom
            int yardBottom = y + distance * 48;
            int yardLeft = x - 20;
            int yardRight = x + 68;
            int pathLeft = x + 12;
            int pathRight = x + 36;
            // CONNECTING fence from house corners out to wider perimeter
            g.setColor(fenceColor);
            g.fillRect(yardLeft, y + 42, (x + 4) - yardLeft + 2, 2);   // left connector
            g.fillRect(x + 44, y + 42, yardRight - (x + 44) + 2, 2);   // right connector

            // path through the middle
            g.setColor(pathBorder);
            g.fillRect(pathLeft - 1, yardTop, (pathRight - pathLeft) + 2, yardBottom - yardTop);
            g.setColor(pathColor);
            g.fillRect(pathLeft, yardTop, pathRight - pathLeft, yardBottom - yardTop);

            // U-FENCE on the OUTER edges of the yard
            g.setColor(fenceColor);
            g.fillRect(yardLeft, yardTop, 2, yardBottom - yardTop);                  // left
            g.fillRect(yardRight - 2, yardTop, 2, yardBottom - yardTop);             // right
            g.fillRect(yardLeft, yardBottom - 2, pathLeft - yardLeft, 2);            // bottom-left
            g.fillRect(pathRight, yardBottom - 2, yardRight - pathRight, 2);         // bottom-right
            // RED METAL gate posts at gate opening
            g.setColor(gateRed);
            g.fillRect(pathLeft - 2, yardBottom - 10, 2, 10);
            g.fillRect(pathRight, yardBottom - 10, 2, 10);
            g.setColor(gateRedDark);
            g.fillRect(pathLeft - 2, yardBottom - 10, 2, 1);
            g.fillRect(pathRight, yardBottom - 10, 2, 1);
            // RED METAL closed gate between the posts
            g.setColor(gateRed);
            g.fillRect(pathLeft, yardBottom - 8, pathRight - pathLeft, 8);
            g.setColor(gateRedHighlight);
            g.fillRect(pathLeft, yardBottom - 8, pathRight - pathLeft, 1);
            g.setColor(gateRedDark);
            for (int i = pathLeft + 4; i < pathRight; i += 4) {
                g.fillRect(i, yardBottom - 8, 1, 8);
            }
            g.drawRect(pathLeft, yardBottom - 8, pathRight - pathLeft - 1, 7);
            // mailbox on the right (bigger USPS-style)
            drawMailbox(g, yardRight - 14, yardBottom - 18);
        } else if (direction.equals("UP")) {
            int yardTop = y - (distance - 1) * 48;
            int yardBottom = y + 4;       // end right at house top
            int yardLeft = x - 20;
            int yardRight = x + 68;
            int pathLeft = x + 12;
            int pathRight = x + 36;
            // CONNECTING fence from house corners out to wider perimeter
            g.setColor(fenceColor);
            g.fillRect(yardLeft, y + 4, (x + 4) - yardLeft + 2, 2);
            g.fillRect(x + 44, y + 4, yardRight - (x + 44) + 2, 2);

            g.setColor(pathBorder);
            g.fillRect(pathLeft - 1, yardTop, (pathRight - pathLeft) + 2, yardBottom - yardTop);
            g.setColor(pathColor);
            g.fillRect(pathLeft, yardTop, pathRight - pathLeft, yardBottom - yardTop);

            g.setColor(fenceColor);
            g.fillRect(yardLeft, yardTop, 2, yardBottom - yardTop);
            g.fillRect(yardRight - 2, yardTop, 2, yardBottom - yardTop);
            g.fillRect(yardLeft, yardTop, pathLeft - yardLeft, 2);
            g.fillRect(pathRight, yardTop, yardRight - pathRight, 2);
            g.setColor(gateRed);
            g.fillRect(pathLeft - 2, yardTop, 2, 10);
            g.fillRect(pathRight, yardTop, 2, 10);
            g.fillRect(pathLeft, yardTop, pathRight - pathLeft, 8);
            g.setColor(gateRedHighlight);
            g.fillRect(pathLeft, yardTop, pathRight - pathLeft, 1);
            g.setColor(gateRedDark);
            for (int i = pathLeft + 4; i < pathRight; i += 4) {
                g.fillRect(i, yardTop, 1, 8);
            }
            g.drawRect(pathLeft, yardTop, pathRight - pathLeft - 1, 7);
            // mailbox on the right (bigger USPS-style)
            drawMailbox(g, yardRight - 14, yardTop + 8);
        } else if (direction.equals("LEFT")) {
            int yardLeft = x - (distance - 1) * 48;
            int yardRight = x + 4;        // end right at house left
            int yardTop = y - 20;
            int yardBottom = y + 68;
            int pathTop = y + 12;
            int pathBottom = y + 36;
            g.setColor(fenceColor);
            g.fillRect(x + 4, yardTop, 2, (y + 4) - yardTop + 2);
            g.fillRect(x + 4, y + 44, 2, yardBottom - (y + 44) + 2);

            g.setColor(pathBorder);
            g.fillRect(yardLeft, pathTop - 1, yardRight - yardLeft, (pathBottom - pathTop) + 2);
            g.setColor(pathColor);
            g.fillRect(yardLeft, pathTop, yardRight - yardLeft, pathBottom - pathTop);

            g.setColor(fenceColor);
            g.fillRect(yardLeft, yardTop, yardRight - yardLeft, 2);
            g.fillRect(yardLeft, yardBottom - 2, yardRight - yardLeft, 2);
            g.fillRect(yardLeft, yardTop, 2, pathTop - yardTop);
            g.fillRect(yardLeft, pathBottom, 2, yardBottom - pathBottom);
            g.setColor(gateRed);
            g.fillRect(yardLeft, pathTop - 2, 10, 2);
            g.fillRect(yardLeft, pathBottom, 10, 2);
            g.fillRect(yardLeft, pathTop, 8, pathBottom - pathTop);
            g.setColor(gateRedHighlight);
            g.fillRect(yardLeft, pathTop, 1, pathBottom - pathTop);
            g.setColor(gateRedDark);
            for (int i = pathTop + 4; i < pathBottom; i += 4) {
                g.fillRect(yardLeft, i, 8, 1);
            }
            g.drawRect(yardLeft, pathTop, 7, pathBottom - pathTop - 1);
            // mailbox on the bottom-left (bigger USPS-style)
            drawMailbox(g, yardLeft + 4, yardBottom - 18);
        } else if (direction.equals("RIGHT")) {
            int yardLeft = x + 44;        // start right at house right
            int yardRight = x + distance * 48;
            int yardTop = y - 20;
            int yardBottom = y + 68;
            int pathTop = y + 12;
            int pathBottom = y + 36;
            g.setColor(fenceColor);
            g.fillRect(x + 42, yardTop, 2, (y + 4) - yardTop + 2);
            g.fillRect(x + 42, y + 44, 2, yardBottom - (y + 44) + 2);

            g.setColor(pathBorder);
            g.fillRect(yardLeft, pathTop - 1, yardRight - yardLeft, (pathBottom - pathTop) + 2);
            g.setColor(pathColor);
            g.fillRect(yardLeft, pathTop, yardRight - yardLeft, pathBottom - pathTop);

            g.setColor(fenceColor);
            g.fillRect(yardLeft, yardTop, yardRight - yardLeft, 2);
            g.fillRect(yardLeft, yardBottom - 2, yardRight - yardLeft, 2);
            g.fillRect(yardRight - 2, yardTop, 2, pathTop - yardTop);
            g.fillRect(yardRight - 2, pathBottom, 2, yardBottom - pathBottom);
            g.setColor(gateRed);
            g.fillRect(yardRight - 10, pathTop - 2, 10, 2);
            g.fillRect(yardRight - 10, pathBottom, 10, 2);
            g.fillRect(yardRight - 8, pathTop, 8, pathBottom - pathTop);
            g.setColor(gateRedHighlight);
            g.fillRect(yardRight - 8, pathTop, 1, pathBottom - pathTop);
            g.setColor(gateRedDark);
            for (int i = pathTop + 4; i < pathBottom; i += 4) {
                g.fillRect(yardRight - 8, i, 8, 1);
            }
            g.drawRect(yardRight - 8, pathTop, 7, pathBottom - pathTop - 1);
            // mailbox on the bottom-right (bigger USPS-style)
            drawMailbox(g, yardRight - 14, yardBottom - 18);
        }

        // ===== HIPPED ROOF (4 triangular faces meeting at center) =====
        int x0 = x + 4;
        int y0 = y + 4;
        int x1 = x + 44;
        int y1 = y + 44;
        int cx = x + 24;
        int cy = y + 24;

        // top face (lightest — sun coming from above)
        g.setColor(houseColor.brighter().brighter());
        g.fillPolygon(new int[]{x0, x1, cx}, new int[]{y0, y0, cy}, 3);

        // left face (medium-light)
        g.setColor(houseColor.brighter());
        g.fillPolygon(new int[]{x0, x0, cx}, new int[]{y0, y1, cy}, 3);

        // right face (medium-dark)
        g.setColor(houseColor);
        g.fillPolygon(new int[]{x1, x1, cx}, new int[]{y0, y1, cy}, 3);

        // bottom face (darkest)
        g.setColor(houseColor.darker());
        g.fillPolygon(new int[]{x0, x1, cx}, new int[]{y1, y1, cy}, 3);

        // ridge lines (dark borders of each triangle)
        g.setColor(new Color(40, 25, 15));
        g.drawLine(x0, y0, x1, y0);     // top edge
        g.drawLine(x1, y0, x1, y1);     // right edge
        g.drawLine(x0, y1, x1, y1);     // bottom edge
        g.drawLine(x0, y0, x0, y1);     // left edge
        g.drawLine(x0, y0, cx, cy);     // diagonal to center
        g.drawLine(x1, y0, cx, cy);
        g.drawLine(x1, y1, cx, cy);
        g.drawLine(x0, y1, cx, cy);

        // chimney (small dark square sticking up from one roof corner)
        g.setColor(new Color(60, 45, 35));
        g.fillRect(x + 36, y + 8, 5, 5);
        g.setColor(new Color(40, 25, 15));
        g.drawRect(x + 36, y + 8, 5, 5);

        // ===== ADDRESS NUMBER =====
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(address), x + 5, y + 14);

        // ===== DELIVERED BADGE =====
        if (delivered) {
            g.setColor(new Color(50, 200, 80));
            g.fillOval(x + 36, y, 12, 12);
            g.setColor(Color.WHITE);
            g.fillRect(x + 40, y + 6, 2, 4);
            g.fillRect(x + 42, y + 8, 4, 2);
        }
    }

    // Reusable mailbox - USPS blue with red flag.
    // (mx, my) = top-left of the box itself.
    private void drawMailbox(Graphics g, int mx, int my) {
        // wooden post (4x18)
        g.setColor(new Color(80, 55, 30));
        g.fillRect(mx + 9, my + 16, 4, 18);
        g.setColor(new Color(50, 30, 15));            // post shadow
        g.fillRect(mx + 12, my + 16, 1, 18);

        // USPS-blue mailbox body (24x18) - bigger and more visible
        g.setColor(new Color(0, 75, 135));
        g.fillRect(mx, my, 24, 18);
        g.setColor(new Color(40, 110, 170));          // top highlight
        g.fillRect(mx, my, 24, 3);
        g.setColor(new Color(0, 50, 90));             // bottom shadow
        g.fillRect(mx, my + 15, 24, 3);

        // white USPS stripe across the front (decal)
        g.setColor(new Color(220, 220, 220));
        g.fillRect(mx + 3, my + 8, 18, 3);

        // door slot (dark line)
        g.setColor(new Color(0, 30, 60));
        g.fillRect(mx + 3, my + 6, 18, 1);

        // RED FLAG raised on the right side
        g.setColor(new Color(60, 40, 25));
        g.fillRect(mx + 24, my - 3, 1, 12);           // thin pole
        g.setColor(new Color(220, 50, 50));
        g.fillRect(mx + 25, my - 3, 7, 6);            // flag (bigger)
        g.setColor(new Color(160, 30, 30));
        g.fillRect(mx + 25, my + 1, 7, 1);            // flag shadow
    }
}
