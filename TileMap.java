import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;

public class TileMap {

    // FIELDS
    int[][] mapData;
    int tileSize = 48;
    int rows = 12;
    int cols = 16;

    // CONSTRUCTOR
    public TileMap(String filePath) {
        mapData = new int[rows][cols];
        loadMap(filePath);
    }

    // METHOD - read the map file and fill the 2D array
    public void loadMap(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            for (int row = 0; row < rows; row++) {
                String line = reader.readLine().trim();
                String[] numbers = line.split(" ");

                for (int col = 0; col < cols; col++) {
                    mapData[row][col] = Integer.parseInt(numbers[col]);
                }
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // METHOD - check if a player at this position would hit a solid tile
    public boolean isSolid(int playerX, int playerY, int playerSize) {
        int leftCol   = playerX / tileSize;
        int rightCol  = (playerX + playerSize - 1) / tileSize;
        int topRow    = playerY / tileSize;
        int bottomRow = (playerY + playerSize - 1) / tileSize;

        if (leftCol < 0 || rightCol >= cols || topRow < 0 || bottomRow >= rows) {
            return true;
        }

        int topLeft     = mapData[topRow][leftCol];
        int topRight    = mapData[topRow][rightCol];
        int bottomLeft  = mapData[bottomRow][leftCol];
        int bottomRight = mapData[bottomRow][rightCol];

        return isBlocked(topLeft) || isBlocked(topRight)
            || isBlocked(bottomLeft) || isBlocked(bottomRight);
    }

    private boolean isBlocked(int tileType) {
        return tileType == 2 || tileType == 3;
    }

    public void setTile(int row, int col, int newType) {
        mapData[row][col] = newType;
    }

    // METHOD - draw all the tiles on screen
    public void draw(Graphics g) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int tileType = mapData[row][col];
                int x = col * tileSize;
                int y = row * tileSize;

                if (tileType == 0)      drawGrass(g, x, y);
                else if (tileType == 1) { drawRoad(g, x, y); drawCurbs(g, row, col, x, y); }
                else if (tileType == 2) drawWater(g, x, y);
                else if (tileType == 3) drawHouse(g, x, y);
                else if (tileType == 4) drawDelivered(g, x, y);
                else if (tileType == 5) { drawCrosswalk(g, x, y); drawCurbs(g, row, col, x, y); }
                else if (tileType == 6) drawTree(g, x, y);
            }
        }
    }

    // ============================================================
    // TILE DRAWING METHODS — each one fills a full 48x48 square
    // ============================================================

    void drawGrass(Graphics g, int x, int y) {
        // base green
        g.setColor(new Color(76, 153, 76));
        g.fillRect(x, y, tileSize, tileSize);
        // darker grass blades for texture
        g.setColor(new Color(56, 122, 56));
        g.fillRect(x + 6,  y + 8,  3, 6);
        g.fillRect(x + 18, y + 22, 3, 6);
        g.fillRect(x + 32, y + 14, 3, 6);
        g.fillRect(x + 40, y + 36, 3, 6);
        g.fillRect(x + 12, y + 38, 3, 6);
    }

    // draw light gray curb strips on road edges that touch grass/non-road tiles
    void drawCurbs(Graphics g, int row, int col, int x, int y) {
        Color curb = new Color(200, 200, 200);
        // check each neighbor — if it's NOT a road or crosswalk, draw a curb on that side
        if (row > 0 && !isRoad(mapData[row - 1][col])) {
            g.setColor(curb);
            g.fillRect(x, y, tileSize, 3);                       // top curb
        }
        if (row < rows - 1 && !isRoad(mapData[row + 1][col])) {
            g.setColor(curb);
            g.fillRect(x, y + tileSize - 3, tileSize, 3);        // bottom curb
        }
        if (col > 0 && !isRoad(mapData[row][col - 1])) {
            g.setColor(curb);
            g.fillRect(x, y, 3, tileSize);                       // left curb
        }
        if (col < cols - 1 && !isRoad(mapData[row][col + 1])) {
            g.setColor(curb);
            g.fillRect(x + tileSize - 3, y, 3, tileSize);        // right curb
        }
    }

    private boolean isRoad(int t) {
        return t == 1 || t == 5;
    }

    void drawRoad(Graphics g, int x, int y) {
        // dark gray asphalt
        g.setColor(new Color(70, 70, 70));
        g.fillRect(x, y, tileSize, tileSize);
        // yellow lane stripe down the middle (vertical)
        g.setColor(new Color(230, 200, 60));
        g.fillRect(x + tileSize/2 - 2, y + 8, 4, 14);
        g.fillRect(x + tileSize/2 - 2, y + 26, 4, 14);
    }

    void drawWater(Graphics g, int x, int y) {
        // blue water
        g.setColor(new Color(50, 110, 200));
        g.fillRect(x, y, tileSize, tileSize);
        // light wave lines
        g.setColor(new Color(140, 200, 255));
        g.fillRect(x + 4,  y + 12, 12, 2);
        g.fillRect(x + 20, y + 24, 12, 2);
        g.fillRect(x + 8,  y + 36, 12, 2);
    }

    // House class draws the actual house, so this just draws grass under it
    void drawHouse(Graphics g, int x, int y) {
        drawGrass(g, x, y);
    }

    void drawDelivered(Graphics g, int x, int y) {
        drawGrass(g, x, y);
    }

    // type 5 — road with crosswalk stripes
    void drawCrosswalk(Graphics g, int x, int y) {
        // base asphalt
        g.setColor(new Color(70, 70, 70));
        g.fillRect(x, y, tileSize, tileSize);
        // 4 white horizontal stripes (the crosswalk)
        g.setColor(new Color(230, 230, 230));
        g.fillRect(x + 4, y + 6,  40, 5);
        g.fillRect(x + 4, y + 16, 40, 5);
        g.fillRect(x + 4, y + 26, 40, 5);
        g.fillRect(x + 4, y + 36, 40, 5);
    }

    // type 6 — tree (sits on grass)
    void drawTree(Graphics g, int x, int y) {
        // grass background
        drawGrass(g, x, y);
        // soft shadow under tree
        g.setColor(new Color(0, 0, 0, 80));
        g.fillOval(x + 8, y + 36, 32, 8);
        // dark green tree canopy (round)
        g.setColor(new Color(40, 110, 50));
        g.fillOval(x + 6, y + 6, 36, 36);
        // lighter highlight on top-left
        g.setColor(new Color(80, 160, 80));
        g.fillOval(x + 10, y + 10, 14, 14);
    }
}
