import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

public class FortWorthDashboard {
    public static void main(String[] args) {
        List<Station> stations = new ArrayList<>();
        stations.add(new Station("Main Office",     45, 1820, 180, 1, 38));
        stations.add(new Station("Riverside",       18,  640, 260, 0, 14));
        stations.add(new Station("Polytechnic",     14,  310, 410, 2, 11));
        stations.add(new Station("Northside",       16,  720, 150, 1, 13));
        stations.add(new Station("Eastside",        12,  180, 520, 4,  9));
        stations.add(new Station("Richland Hills",  10,  410, 130, 0,  8));
        stations.add(new Station("Haltom City",     11,  290, 370, 1,  9));
        stations.add(new Station("Keller",           9,  520, 100, 0,  8));
        stations.add(new Station("Southside",       13,  240, 430, 2, 10));

        SwingUtilities.invokeLater(() ->
            new PostmasterDashboard("Fort Worth", "TX", stations).setVisible(true));
    }
}
