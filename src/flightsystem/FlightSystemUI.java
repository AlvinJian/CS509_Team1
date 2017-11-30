package flightsystem;

import airport.Airports;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.zinternaltools.DateTimeChangeEvent;
import dao.DaoFlight;
import dao.ServerInterface;
import flight.Flight;
import flight.Flights;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.util.Collections;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * UI class that's responsible for displaying and handling button presses,
 * airport information, and flight information
 * 
 * @author dg71532
 */
public class FlightSystemUI extends javax.swing.JFrame {

    private LocalDateTime localTime = LocalDateTime.now();
    private LocalDateTime localTimeReturn = LocalDateTime.now();
    private String departureCode;
    private String arrivalCode;
    private FlightInfoController flightInfoController;
    private static final Logger flightSystemUILogger;
    static {
        flightSystemUILogger = Logger.getLogger(DaoFlight.class.getName());
        flightSystemUILogger.setLevel(Level.INFO);
    }
    /**
     * Creates new form FlightSystemUI
     */
    public FlightSystemUI() {
        //Initialize UI components
        initComponents();
        flightInfoController = new FlightInfoController();
        dateTimePickerReturn.setEnabled(false);
        arrivalTable.setEnabled(false);
        departureComboBox.removeAllItems();
        arrivalComboBox.removeAllItems();
        getAirports();
        dateTimePicker.setDateTimeStrict(LocalDateTime.of(2017, Month.DECEMBER, 6, 0, 0));
        dateTimePickerReturn.setDateTimeStrict(LocalDateTime.of(2017, Month.DECEMBER, 6, 0, 0));
        
        //DateTimePicker Listeners that set local time whenever user picks a 
        dateTimePicker.addDateTimeChangeListener((DateTimeChangeEvent dtce) -> {
            localTime = dateTimePicker.getDateTimeStrict();
            flightSystemUILogger.log(Level.INFO, "Departure Time: {0}", localTime.toString());
        });
        dateTimePickerReturn.addDateTimeChangeListener((DateTimeChangeEvent dtce) -> {
           localTimeReturn = dateTimePickerReturn.getDateTimeStrict();
           flightSystemUILogger.log(Level.INFO, "Return Time: {0}", localTimeReturn.toString());
        });
        
        //ComboBox Listeners to check when airport is selectec
        ItemListener departureListener = (ItemEvent itemEvent) -> {
            int state = itemEvent.getStateChange();
            flightSystemUILogger.log(Level.INFO, "Departure Airport {0}", (state == ItemEvent.SELECTED) ? "Selected" : "Deselected");
            if (state == ItemEvent.SELECTED) {
                Object item = departureComboBox.getSelectedItem();
                departureCode = ((ComboItem)item).getAirportCode();
                checkAirportSelection();
            }
            flightSystemUILogger.log(Level.INFO, "Departure airport: {0}", itemEvent.getItem());
            ItemSelectable is = itemEvent.getItemSelectable();
        };
        ItemListener arrivalListener = (ItemEvent itemEvent) -> {
            int state = itemEvent.getStateChange();
            flightSystemUILogger.log(Level.INFO, "Arrival Airport {0}", (state == ItemEvent.SELECTED) ? "Selected" : "Deselected");
            if (state == ItemEvent.SELECTED) {
                Object item = arrivalComboBox.getSelectedItem();
                arrivalCode = ((ComboItem)item).getAirportCode();
                checkAirportSelection();
            }
            flightSystemUILogger.log(Level.INFO, "Arival airport: {0}", itemEvent.getItem());
            ItemSelectable is = itemEvent.getItemSelectable();
        };
        departureComboBox.addItemListener(departureListener);
        arrivalComboBox.addItemListener(arrivalListener);
        searchButton.setEnabled(false);
    }
    /**
     * Check to make sure user didn't select the same airport
     * Enable search button if airports are not the same
     */
    private void checkAirportSelection()
    {
        flightSystemUILogger.log(Level.INFO,"checkAirportSelection");
        if (!departureComboBox.getSelectedItem().toString().equals(arrivalComboBox.getSelectedItem().toString()))
        {
            searchButton.setEnabled(true);
        }
        else
        {
            searchButton.setEnabled(false);
        }
    }
    /**
     * Function use to get airports from the server and update the comboBoxes
     * for arrival and departure
     */
    private void getAirports()
    {
        flightSystemUILogger.log(Level.INFO, "Getting airports");
	// Try to get a list of airports
        Airports airports = flightInfoController.syncAirports();
	Collections.sort(airports);
        airports.forEach((airport) -> {
            addAiportToComboBoxes(airport.code(), airport.name());
        });
    }
    /**
     * Add airport to the comboBox(drop down of airports)
     * @param code: 3 letter airport code
     * @param airport: full name of the airport
     */
    private void addAiportToComboBoxes( String code, String airport)
    {
       flightSystemUILogger.log(Level.FINEST, "Add: {0} to comboBox", code);
       ComboItem ci = new ComboItem(code,airport);
       departureComboBox.addItem(ci);
       arrivalComboBox.addItem(ci);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        departureComboBox = new javax.swing.JComboBox<>();
        arrivalComboBox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        searchButton = new javax.swing.JButton();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        arrivalTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        departureTable = new javax.swing.JTable();
        departureLabelOut = new javax.swing.JLabel();
        departureLabelIn = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        arrivalLabelOut = new javax.swing.JLabel();
        arrivalLableIn = new javax.swing.JLabel();
        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setAllowEmptyDates(false);
        LocalDate currentDate = LocalDate.of(2017, Month.DECEMBER, 6);
        LocalDate futureDate = LocalDate.of(2017, Month.DECEMBER, 31);
        dateTimePicker =  new DateTimePicker(dateSettings, null);
        dateSettings.setDateRangeLimits(currentDate, futureDate);
        jLabel5 = new javax.swing.JLabel();
        roundtripCheckBox = new javax.swing.JCheckBox();
        DatePickerSettings dateSettings1 = new DatePickerSettings();
        dateSettings1.setAllowEmptyDates(false);
        dateTimePickerReturn =  new DateTimePicker(dateSettings1, null);
        dateSettings1.setDateRangeLimits(currentDate, futureDate);
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        departureComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                departureComboBoxActionPerformed(evt);
            }
        });

        arrivalComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrivalComboBoxActionPerformed(evt);
            }
        });

        jLabel2.setText("Departure");

        jLabel3.setText("Arrival");

        searchButton.setText("Search For Flight");
        searchButton.setEnabled(false);
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        jInternalFrame1.setVisible(false);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        arrivalTable.setAutoCreateRowSorter(true);
        arrivalTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Flight #", "Departure", "Departure Tme", "Arrival", "Arrival Time", "Flight Time", "FirstClass $", "Coach $"
            }
        ));
        jScrollPane2.setViewportView(arrivalTable);

        departureTable.setAutoCreateRowSorter(true);
        departureTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Flight #", "Departure", "Departure Tme", "Arrival", "Arrival Time", "Flight Time", "FirstClass $", "Coach $"
            }
        ));
        jScrollPane3.setViewportView(departureTable);

        departureLabelOut.setText("Departing");

        departureLabelIn.setText("Departing");

        jLabel1.setText("->");

        jLabel4.setText("->");

        arrivalLabelOut.setText("Arrival");

        arrivalLableIn.setText("Arrival");

        jLabel5.setText("Pick Date and Time: Departure");

        roundtripCheckBox.setText("Round Trip");
        roundtripCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundtripCheckBoxActionPerformed(evt);
            }
        });

        jLabel6.setText("Pick Date and Time: Return");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(jLabel2)
                        .addGap(195, 195, 195)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(dateTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateTimePickerReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(200, 200, 200)
                                .addComponent(searchButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(107, 107, 107)
                                        .addComponent(jLabel6))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(departureComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(arrivalComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(35, 35, 35)
                                        .addComponent(roundtripCheckBox))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(814, 814, 814)
                        .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 791, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(235, 235, 235)
                        .addComponent(departureLabelOut, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(26, 26, 26)
                        .addComponent(arrivalLabelOut))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 791, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(257, 257, 257)
                        .addComponent(departureLabelIn)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(arrivalLableIn)))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(departureComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(arrivalComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(roundtripCheckBox)))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateTimePickerReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addComponent(searchButton)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(departureLabelOut, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(arrivalLabelOut))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(departureLabelIn)
                    .addComponent(jLabel1)
                    .addComponent(arrivalLableIn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(215, 215, 215))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
   
    private void departureComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_departureComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_departureComboBoxActionPerformed

    private void arrivalComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrivalComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_arrivalComboBoxActionPerformed

    /**
     * Search for flight when search button is pressed
     * @param evt 
     */
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here: 
        flightSystemUILogger.log(Level.INFO, "Search button pressed, getting flights");
        roundtripCheckBox.setEnabled(false);
        searchButton.setEnabled(false);
        //Get one way flight from departure -> arrival airport
        getFlights();
        roundtripCheckBox.setEnabled(true);
        searchButton.setEnabled(true);
        
    }//GEN-LAST:event_searchButtonActionPerformed
    private void getFlights()
    {
        //Flights depFlights = ServerInterface.INSTANCE.getFlights("CS509team1", departureCode, localTime, ServerInterface.QueryFlightType.DEPART, getFilter(arrivalCode));
        FlightInfoController.FlightsReceiver receiver = (Flights depFlights) -> {
            if (depFlights.size() > 0 )
            {
                flightSystemUILogger.log(Level.INFO, "Adding departure->arrival flights info to table");
                departureLabelOut.setText(departureCode);
                arrivalLabelOut.setText(arrivalCode);
                addFlightToTable(depFlights, departureTable);
            }
            else
            {
                JOptionPane.showMessageDialog(null,
                "No flights available for this combination",
                "Flight Availability Warning",
                JOptionPane.WARNING_MESSAGE);
            }
        };
        flightInfoController.searchDirectFlight(departureCode, localTime, arrivalCode, receiver);
        //Get return flight from arrival -> departure airport
        if (roundtripCheckBox.isSelected())
        {
            flightSystemUILogger.log(Level.INFO,"Getting return flights");
            //Flights arrivalFlights = ServerInterface.INSTANCE.getFlights("CS509team1", arrivalCode, localTimeReturn, ServerInterface.QueryFlightType.DEPART, getFilter(departureCode));
            FlightInfoController.FlightsReceiver arrivalReceiver = (Flights arrivalFlights) -> {
                if (arrivalFlights.size() > 0 )
                {
                    flightSystemUILogger.log(Level.INFO, "Adding arrival flight info to table");
                    departureLabelIn.setText(arrivalCode);
                    arrivalLableIn.setText(departureCode);
                    addFlightToTable(arrivalFlights, arrivalTable);
                }
                else
                {
                    JOptionPane.showMessageDialog(null,
                        "No return flights available for this combination",
                        "Flight Availability Warning",
                        JOptionPane.WARNING_MESSAGE);
                }
            };
            flightInfoController.searchDirectFlight(arrivalCode, localTimeReturn, departureCode, arrivalReceiver);
        }
    }
    private void addFlightToTable(Flights flights, javax.swing.JTable table)
    {
                 DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String [] {
                "Flight #", "Departure", "Departure Tme", "Arrival", "Arrival Time", "Flight Time", "FirstClass $", "Coach $"
            });
            flights.forEach((f) -> {
                model.addRow(new Object[]{ f.getmNumber(),
                    f.getmDepAirport(),
                    f.getmDepTime().toString(),
                    f.getmArrAirport(),
                    f.getmArrTime().toString(),
                    f.getmFlightTime(),
                    f.getmPriceFirst(),
                    f.getmPriceCoach()});
                });
            table.setModel(model);   
    }
    private void roundtripCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundtripCheckBoxActionPerformed
        // TODO add your handling code here:
        arrivalTable.setEnabled(roundtripCheckBox.isSelected());
        dateTimePickerReturn.setEnabled(roundtripCheckBox.isSelected());
    }//GEN-LAST:event_roundtripCheckBoxActionPerformed
   /**
    * Filter that's used to filter out arrival flights 
    * @param code
    * @return QueryFilter
    */
    private ServerInterface.QueryFlightFilter getFilter(String code)
    {
        ServerInterface.QueryFlightFilter flightFilter = (Flight f) -> {
            return f.getmArrAirport() != null && f.getmArrAirport().equals(code);
        };
        return flightFilter;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<ComboItem> arrivalComboBox;
    private javax.swing.JLabel arrivalLabelOut;
    private javax.swing.JLabel arrivalLableIn;
    private javax.swing.JTable arrivalTable;
    private com.github.lgooddatepicker.components.DateTimePicker dateTimePicker;
    private com.github.lgooddatepicker.components.DateTimePicker dateTimePickerReturn;
    private javax.swing.JComboBox<ComboItem> departureComboBox;
    private javax.swing.JLabel departureLabelIn;
    private javax.swing.JLabel departureLabelOut;
    private javax.swing.JTable departureTable;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JCheckBox roundtripCheckBox;
    private javax.swing.JButton searchButton;
    // End of variables declaration//GEN-END:variables

}
