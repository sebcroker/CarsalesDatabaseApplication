package Presentation;

import Business.Summary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

/**
 * @author Abbey
 */
public class CarSaleSummaryPane extends JScrollPane {
    private JTable table;
    private Vector<Summary> summaryList;
    private String clickedRow = null;

    private String[] columnNames = {"Make", "Model", "Available Units", "Sold Units", "Total Sales $", "Last Purchased At"};

    /**
     * Constructs a CarSaleSummaryPane to display a summary of car sales.
     * The given summary list is displayed in a table with headings defined in columnNames.
     * The table is added to the scroll pane to allow scrolling when the content exceeds 
     * the visible area.
     * 
     * @param summaryList A Vector containing the car sale summary data to be displayed.
     */
    public CarSaleSummaryPane(Vector<Summary> summaryList){
        this.summaryList = summaryList;
        this.initList(summaryList);
    }

    public void refresh(Vector<Summary> summaryList)
    {
        this.summaryList = summaryList;
        this.clickedRow = null;
        this.initList(summaryList);
    }

    private void initList(Vector<Summary> summaryList) {

        // Convert Vector<Summary> to a 2D Object array for JTable
        Object[][] tableData = new Object[summaryList.size()][6];
        for (int i = 0; i < summaryList.size(); i++) {
            Summary s = summaryList.get(i);
            tableData[i][0] = s.getMake();
            tableData[i][1] = s.getModel();
            tableData[i][2] = s.getAvailableUnits();
            tableData[i][3] = s.getSoldUnits();
            tableData[i][4] = s.getSoldTotalPrices();
            tableData[i][5] = s.getLastPurchaseAt();
        }
        // Create a custom TableModel that makes the table uneditable
        DefaultTableModel model = new DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells are uneditable
            }
        };

        this.table = new JTable(model);
        
        // Style the table header
        this.table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        this.table.getTableHeader().setBackground(Color.LIGHT_GRAY);
        this.table.getTableHeader().setReorderingAllowed(false);

        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Check if a row is clicked (ensure it's not the header)
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    // Get the data from the clicked row
                    String model = (String) table.getValueAt(row, 1); // Get value from column "Model"
                    clickedRow = model;
                }
            }
        });

        setViewportView(this.table);
    }

    /**
     * Retrieves the model of the row that was clicked in the table.
     */ 
    public String getClickedRow() {
        return clickedRow;
    }
}
