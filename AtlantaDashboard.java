import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

public class AtlantaDashboard {

    public static void main(String[] args) {
        List<Station> stations = new ArrayList<>();
        stations.add(new Station("Main Office",   50, 2000, 100, 0, 40));
        stations.add(new Station("Midtown",       30, 1200, 300, 1, 25));
        stations.add(new Station("Buckhead",      25,  900, 100, 2, 20));
        stations.add(new Station("East Atlanta",  20,  600, 400, 3, 15));
        stations.add(new Station("West End",      15,  400, 200, 0, 10));
        stations.add(new Station("College Park",  10,  300, 100, 1,  8));
        stations.add(new Station("Hapeville",      8,  200,  50, 0,  5));
        stations.add(new Station("Airport",       12,  500, 150, 2, 10));
        stations.add(new Station("Southside",     18,  700, 300, 1, 12));

        SwingUtilities.invokeLater(() ->
            new PostmasterDashboard("Atlanta", "GA", stations).setVisible(true));
    }
}
