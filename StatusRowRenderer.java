import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;

public class StatusRowRenderer extends DefaultTableCellRenderer {

    public static final Color GREEN  = new Color(46, 160, 67);
    public static final Color YELLOW = new Color(210, 170, 40);
    public static final Color RED    = new Color(200, 60, 60);

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);

        int statusCol = table.getColumnCount() - 1;
        Object statusValue = table.getModel().getValueAt(row, statusCol);
        String status = (statusValue == null) ? "" : statusValue.toString();

        Color bg;
        switch (status) {
            case "ON TRACK": bg = GREEN;  break;
            case "BEHIND":   bg = YELLOW; break;
            case "CRITICAL": bg = RED;    break;
            default:         bg = Color.LIGHT_GRAY;
        }

        c.setBackground(bg);
        c.setForeground(Color.WHITE);
        if (isSelected) c.setBackground(bg.darker());
        return c;
    }
}
