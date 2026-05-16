import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PostmasterDashboard extends JFrame {

    private static final String[] COLUMN_NAMES = {
        "Station", "Carriers on Route", "Packages Delivered",
        "Packages Remaining", "Grievances Filed", "LLVs Active", "Status"
    };

    private final String cityName;
    private final String stateAbbreviation;
    private final List<Station> stations;

    private DefaultTableModel tableModel;
    private JTable stationTable;
    private JLabel summaryLabel;

    public PostmasterDashboard(String cityName, String stateAbbreviation,
                               List<Station> seedStations) {
        super("Postmaster Dashboard — " + cityName + ", " + stateAbbreviation);
        this.cityName = cityName;
        this.stateAbbreviation = stateAbbreviation;
        this.stations = seedStations;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildSummaryBar(), BorderLayout.NORTH);
        add(buildTablePane(), BorderLayout.CENTER);

        refreshTable();
        refreshSummary();
    }

    private JPanel buildSummaryBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        bar.setBackground(new Color(20, 30, 50));
        bar.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        summaryLabel = new JLabel("Loading " + cityName + " totals...");
        summaryLabel.setForeground(Color.WHITE);
        summaryLabel.setFont(new Font("Arial", Font.BOLD, 14));

        bar.add(summaryLabel);
        return bar;
    }

    private JScrollPane buildTablePane() {
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        stationTable = new JTable(tableModel);
        stationTable.setRowHeight(28);
        stationTable.setFont(new Font("Arial", Font.PLAIN, 13));
        stationTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        stationTable.setGridColor(new Color(60, 70, 90));
        stationTable.setDefaultRenderer(Object.class, new StatusRowRenderer());

        return new JScrollPane(stationTable);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Station s : stations) {
            Object[] rowData = {
                s.name,
                s.carriersOnRoute,
                s.packagesDelivered,
                s.packagesRemaining,
                s.grievancesFiled,
                s.llvsActive,
                s.getStatus()
            };
            tableModel.addRow(rowData);
        }
    }

    private void refreshSummary() {
        int totalCarriers = 0;
        int totalDelivered = 0;
        int totalRemaining = 0;
        int totalGrievances = 0;
        int totalLLVs = 0;

        for (Station s : stations) {
            totalCarriers += s.carriersOnRoute;
            totalDelivered += s.packagesDelivered;
            totalRemaining += s.packagesRemaining;
            totalGrievances += s.grievancesFiled;
            totalLLVs += s.llvsActive;
        }

        int totalPackages = totalDelivered + totalRemaining;
        int percent = (totalPackages == 0)
            ? 100
            : (int) Math.round((double) totalDelivered / totalPackages * 100);

        summaryLabel.setText(String.format(
            "%s TOTALS  —  Carriers: %d   |   Delivered: %d   |   " +
            "Remaining: %d   |   Grievances: %d   |   LLVs Active: %d   |   " +
            "City Delivery Rate: %d%%",
            cityName.toUpperCase(),
            totalCarriers, totalDelivered, totalRemaining,
            totalGrievances, totalLLVs, percent
        ));
    }
}
