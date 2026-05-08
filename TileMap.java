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

    // CONSTRUCTOR — runs when you do "new TileMap("path/to/file")"
    public TileMap(String filePath) {
        // TODO: create the empty 2D array
        mapData = new int[rows][cols];
        // TODO: call loadMap(filePath)
        loadMap(filePath);
    }

    // METHOD - read the map file and fill the 2D array
    public void loadMap(String filePath) {
        try {
            // open the file for reading, line by line
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            // outer loop: go through each row of the file (0 to 9)
            for (int row = 0; row < rows; row++) {

                // read ONE line of text from the file
                // .trim() removes extra spaces at the start/end
                String line = reader.readLine().trim();

                // split the line by spaces — turns "0 0 1 0" into ["0", "0", "1", "0"]
                String[] numbers = line.split(" ");

                // inner loop: go through each column of this row
                for (int col = 0; col < cols; col++) {

                    // convert the string "0" into the int 0 and save it to the grid
                    mapData[row][col] = Integer.parseInt(numbers[col]);
                }
            }

            // always close the file when done — frees up memory
            reader.close();

        } catch (Exception e) {
            // if anything goes wrong (file missing, bad data, etc.), print the error
            e.printStackTrace();
        }
    }

    // METHOD - check if a player at this position would hit a solid tile
    // Solid tiles = water (2) or house (3) — can't walk through them
    public boolean isSolid(int playerX, int playerY, int playerSize) {

        // we check all 4 corners of the player's body
        // because the player is bigger than one tile
        int leftCol   = playerX / tileSize;
        int rightCol  = (playerX + playerSize - 1) / tileSize;
        int topRow    = playerY / tileSize;
        int bottomRow = (playerY + playerSize - 1) / tileSize;

        // if player is going off the edge of the map, treat it as solid
        if (leftCol < 0 || rightCol >= cols || topRow < 0 || bottomRow >= rows) {
            return true;
        }

        // check each corner — if any corner is in water or a house, BLOCKED
        int topLeft     = mapData[topRow][leftCol];
        int topRight    = mapData[topRow][rightCol];
        int bottomLeft  = mapData[bottomRow][leftCol];
        int bottomRight = mapData[bottomRow][rightCol];

        // returns true if ANY corner is solid
        return isBlocked(topLeft) || isBlocked(topRight)
            || isBlocked(bottomLeft) || isBlocked(bottomRight);
    }

    // helper — is this tile type solid?
    private boolean isBlocked(int tileType) {
        return tileType == 2 || tileType == 3;
    }

    // METHOD - change the type of a tile at this row/col
    public void setTile(int row, int col, int newType) {
        mapData[row][col] = newType;
    }

    // METHOD - draw all the tiles on screen
    public void draw(Graphics g) {

        // outer loop: go through each ROW from top to bottom
        for (int row = 0; row < rows; row++) {

            // inner loop: go through each COLUMN left to right
            for (int col = 0; col < cols; col++) {

                // grab the number stored at this row/col (0, 1, 2, or 3)
                int tileType = mapData[row][col];

                // pick a color based on what number it is
                if (tileType == 0)      g.setColor(new Color(50, 150, 50));    // 0 = grass green
                else if (tileType == 1) g.setColor(new Color(150, 150, 150));  // 1 = road gray
                else if (tileType == 2) g.setColor(new Color(50, 100, 200));   // 2 = water blue
                else if (tileType == 3) g.setColor(new Color(180, 100, 50));
                else if  (tileType == 4) g.setColor(new Color(255, 200,150));  // 4 = mailbox yellow

                // draw a filled square at this spot
                // x = col * tileSize  (because col goes left/right)
                // y = row * tileSize  (because row goes up/down)
                g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
            }
        }
    }
}
