import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

public class LittleRockDashboard {

    public static void main(String[] args) {
        List<Station> stations = new ArrayList<>();
        stations.add(new Station("Main Office Downtown", 42, 1710, 220, 1, 36));
        stations.add(new Station("West Little Rock",     20,  840, 180, 0, 16));
        stations.add(new Station("Mabelvale",            11,  280, 390, 2,  9));
        stations.add(new Station("Sherwood",             14,  610, 140, 1, 12));
        stations.add(new Station("North Little Rock",    22,  900, 300, 1, 18));
        stations.add(new Station("Jacksonville",         10,  150, 500, 4,  8));
        stations.add(new Station("Cabot",                 9,  430, 120, 0,  8));
        stations.add(new Station("Benton",               12,  330, 340, 1, 10));
        stations.add(new Station("Bryant",                8,  250, 410, 2,  7));

        SwingUtilities.invokeLater(() ->
            new PostmasterDashboard("Little Rock", "AR", stations).setVisible(true));
    }
}
