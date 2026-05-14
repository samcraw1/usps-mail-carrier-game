import java.awt.Color;

public class Outfit {

    private String hatColor;
    private String shirtColor;

    public Outfit(String hatColor, String shirtColor) {
        this.hatColor = hatColor;
        this.shirtColor = shirtColor;
    }

    public String getHatColor() {
        return hatColor;
    }

    public String getShirtColor() {
        return shirtColor;
    }

    public void setHatColor(String hatColor) {
        this.hatColor = hatColor;
    }

    public void setShirtColor(String shirtColor) {
        this.shirtColor = shirtColor;
    }
    
    public static Color hexToColor(String hex) {
        return Color.decode(hex);
    }
}


