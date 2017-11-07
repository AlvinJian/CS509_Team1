/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightsystem;
/**
 *
 * @author dg71532
 */
import airport.Airport;
import airport.Airports;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.optionalusertools.DateTimeChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateTimeChangeEvent;
import dao.ServerInterface;
import flight.Flight;
import flight.Flights;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Observable;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Observer;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


public class FlightSysteUI extends javax.swing.JFrame implements Observer{

    private boolean arrivalSelected = false;
    private boolean departureSelected = false;
    private LocalDateTime localTime = LocalDateTime.now();
    private LocalDateTime localTimeReturn = LocalDateTime.now();
    private String departureCode;
    private String arrivalCode;
    //private FlightInfo flightInfoUpdate ;

    /**
     * Creates new form FlightSysteUI
     */
    public FlightSysteUI() {
        initComponents();
       
        dateTimePickerReturn.setEnabled(false);
        arrivalTable.setEnabled(false);
        departureComboBox.removeAllItems();
        arrivalComboBox.removeAllItems();
        getAirports();
        dateTimePicker.setDateTimeStrict(LocalDateTime.of(2017, Month.DECEMBER, 6, 0, 0));
        dateTimePickerReturn.setDateTimeStrict(LocalDateTime.of(2017, Month.DECEMBER, 6, 0, 0));
        dateTimePicker.addDateTimeChangeListener( new DateTimeChangeListener()
        {
            @Override
            public void dateOrTimeChanged(DateTimeChangeEvent dtce) {  
                localTime = dateTimePicker.getDateTimeStrict();
                System.out.println("Time: " + localTime.toString());
            }
                    
       });
       dateTimePickerReturn.addDateTimeChangeListener( new DateTimeChangeListener()
        {
            @Override
            public void dateOrTimeChanged(DateTimeChangeEvent dtce) {  
                localTimeReturn = dateTimePickerReturn.getDateTimeStrict();
                System.out.println("Time: " + localTimeReturn.toString());
            }
                    
       });
        
        ItemListener departureListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) 
            {
                int state = itemEvent.getStateChange();
                System.out.println((state == ItemEvent.SELECTED) ? "Selected" : "Deselected");
                if ( state == ItemEvent.SELECTED)
                {
                    departureSelected = true;
                    Object item = departureComboBox.getSelectedItem();
                    departureCode = ((ComboItem)item).getKey();
                    checkIfDone();
                }
                System.out.println("Item: " + itemEvent.getItem());
                ItemSelectable is = itemEvent.getItemSelectable();
            }
        };
        ItemListener arrivalListener = new ItemListener() 
        {
            public void itemStateChanged(ItemEvent itemEvent) {
                int state = itemEvent.getStateChange();
                System.out.println((state == ItemEvent.SELECTED) ? "Selected" : "Deselected");
                if ( state == ItemEvent.SELECTED)
                {
                    arrivalSelected = true;
                    Object item = arrivalComboBox.getSelectedItem();
                    arrivalCode = ((ComboItem)item).getKey();
                    checkIfDone();
                }
                System.out.println("Item: " + itemEvent.getItem());
                ItemSelectable is = itemEvent.getItemSelectable();
            }
        };
        departureComboBox.addItemListener(departureListener);
        arrivalComboBox.addItemListener(arrivalListener);
        searchButton.setEnabled(false);
    }
    private void checkIfDone()
    {
        System.out.println("checkIfdone");
        if ((departureComboBox.getSelectedItem().toString() != arrivalComboBox.getSelectedItem().toString()))
        {
            System.out.println("done");
            searchButton.setEnabled(true);
        }
        else
        {
            searchButton.setEnabled(false);
        }
    }
    /**
     * 
     */
    private void getAirports()
    {
        System.out.println("Getting airports");
        String teamName = "CS509team1";
		// Try to get a list of airports
        Airports airports = ServerInterface.INSTANCE.getAirports(teamName);
	Collections.sort(airports);
	for (Airport airport : airports) {
            addAiport(airport.code(), airport.name());
            //System.out.println(airport.toString());
	}
                //ui.displayList();
    }
    
    @Override
    public void update(Observable observable, Object arg)
    {
         System.out.println("received FlightInfo");
        //flightInfoUpdate = (FlightInfo) observable;
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
        jScrollPane4 = new javax.swing.JScrollPane();
        departureTableAll = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();

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

        departureTableAll.setAutoCreateRowSorter(true);
        departureTableAll.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Flight #", "Departure", "Departure Tme", "Arrival", "Arrival Time", "Flight Time", "FirstClass $", "Coach $"
            }
        ));
        jScrollPane4.setViewportView(departureTableAll);

        jLabel7.setText("All Flights From Destination");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 784, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                    .addComponent(jLabel5)
                                    .addGap(148, 148, 148)
                                    .addComponent(jLabel6))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(dateTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(dateTimePickerReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(41, 41, 41)
                                    .addComponent(departureComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(arrivalComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(35, 35, 35)
                                    .addComponent(roundtripCheckBox))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(200, 200, 200)
                                    .addComponent(searchButton))))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(814, 814, 814)
                            .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(33, 33, 33)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 791, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(departureLabelOut, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel4)
                                    .addGap(26, 26, 26)
                                    .addComponent(arrivalLabelOut)
                                    .addGap(11, 11, 11))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(290, 290, 290)
                                        .addComponent(jLabel7))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 791, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(216, 216, 216)
                                        .addComponent(departureLabelIn)
                                        .addGap(25, 25, 25)
                                        .addComponent(jLabel1)
                                        .addGap(26, 26, 26)
                                        .addComponent(arrivalLableIn)))))))
                .addContainerGap(127, Short.MAX_VALUE))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateTimePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateTimePickerReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addComponent(searchButton)
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(departureLabelOut, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(arrivalLabelOut))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(departureLabelIn)
                    .addComponent(jLabel1)
                    .addComponent(arrivalLableIn))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(13, 13, 13)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public void addAiport( String code, String airport)
    {
       ComboItem ci = new ComboItem(code,airport);
       departureComboBox.addItem(ci);
       arrivalComboBox.addItem(ci);
     //jComboBox1.addItem(code);
    }
    private void departureComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_departureComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_departureComboBoxActionPerformed

    private void arrivalComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrivalComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_arrivalComboBoxActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here: 
        System.out.println("Getting flights");
        roundtripCheckBox.setEnabled(false);
        searchButton.setEnabled(false);
        Flights depFlights = ServerInterface.INSTANCE.getFlights("CS509team1", departureCode, localTime, ServerInterface.QueryFlightType.DEPART, null);
        if (depFlights.size() > 0 )
        {
            System.out.println("Adding flight info to table");
            departureLabelOut.setText(departureCode);
            arrivalLabelOut.setText(arrivalCode);
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String [] {
                "Flight #", "Departure", "Departure Tme", "Arrival", "Arrival Time", "Flight Time", "FirstClass $", "Coach $"
            });
            for (Flight f: depFlights) {
              model.addRow(new Object[]{ f.getmNumber(),
                  f.getmDepAirport().toString(),
                  f.getmDepTime().toString(),
                  f.getmArrAirport().toString(),
                  f.getmArrTime().toString(),
                  f.getmFlightTime(),
                  f.getmPriceFirst(),
                  f.getmPriceCoach()});
            }
            departureTableAll.setModel(model);
        }
        depFlights = ServerInterface.INSTANCE.getFlights("CS509team1", departureCode, localTime, ServerInterface.QueryFlightType.DEPART, getFilter(arrivalCode));
        if (depFlights.size() > 0 )
        {
            System.out.println("Adding flight info to table");
            departureLabelOut.setText(departureCode);
            arrivalLabelOut.setText(arrivalCode);
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String [] {
                "Flight #", "Departure", "Departure Tme", "Arrival", "Arrival Time", "Flight Time", "FirstClass $", "Coach $"
            });
            for (Flight f: depFlights) {
              model.addRow(new Object[]{ f.getmNumber(),
                  f.getmDepAirport().toString(),
                  f.getmDepTime().toString(),
                  f.getmArrAirport().toString(),
                  f.getmArrTime().toString(),
                  f.getmFlightTime(),
                  f.getmPriceFirst(),
                  f.getmPriceCoach()});
            }
            departureTable.setModel(model);
        }
        else
        {
            JOptionPane.showMessageDialog(null,
            "No flights available for this combination",
            "Flight Availability Warning",
                JOptionPane.WARNING_MESSAGE);
        }
        if (roundtripCheckBox.isSelected())
        {
            Flights arrivalFlights = ServerInterface.INSTANCE.getFlights("CS509team1", arrivalCode, localTimeReturn, ServerInterface.QueryFlightType.DEPART, getFilter(departureCode));
        if (arrivalFlights.size() > 0 )
        {
            System.out.println("Adding arrival flight info to table");
            departureLabelIn.setText(arrivalCode);
            arrivalLableIn.setText(departureCode);
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String [] {
                "Flight #", "Departure", "Departure Tme", "Arrival", "Arrival Time", "Flight Time", "FirstClass $", "Coach $"
            });
            for (Flight f: arrivalFlights) {
              model.addRow(new Object[]{ f.getmNumber(),
                  f.getmDepAirport().toString(),
                  f.getmDepTime().toString(),
                  f.getmArrAirport().toString(),
                  f.getmArrTime().toString(),
                  f.getmFlightTime(),
                  f.getmPriceFirst(),
                  f.getmPriceCoach()});
            }
            arrivalTable.setModel(model);
        }
        else
        {
            JOptionPane.showMessageDialog(null,
            "No return flights available for this combination",
            "Flight Availability Warning",
                JOptionPane.WARNING_MESSAGE);
        }
        }
        roundtripCheckBox.setEnabled(true);
        searchButton.setEnabled(true);
        
    }//GEN-LAST:event_searchButtonActionPerformed

    private void roundtripCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundtripCheckBoxActionPerformed
        // TODO add your handling code here:
        arrivalTable.setEnabled(roundtripCheckBox.isSelected());
        dateTimePickerReturn.setEnabled(roundtripCheckBox.isSelected());
    }//GEN-LAST:event_roundtripCheckBoxActionPerformed
    private ServerInterface.QueryFlightFilter getFilter(String code)
    {
        ServerInterface.QueryFlightFilter flightFilter = new ServerInterface.QueryFlightFilter() {
                public boolean isValid(Flight f) {
                    if (f.getmArrAirport() != null && f.getmArrAirport().equals(code)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
        return flightFilter;
    }
    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(FlightSysteUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(FlightSysteUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(FlightSysteUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(FlightSysteUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new FlightSysteUI().setVisible(true);
//            }
//        });
//		
//		String teamName = "CS509team1";
//		// Try to get a list of airports
//		Airports airports = ServerInterface.INSTANCE.getAirports(teamName);
//		Collections.sort(airports);
//		for (Airport airport : airports) {
//                        jComboBox1.addItem(airport.code());
//			System.out.println(airport.toString());
//		}
//    }

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
    private javax.swing.JTable departureTableAll;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JCheckBox roundtripCheckBox;
    private javax.swing.JButton searchButton;
    // End of variables declaration//GEN-END:variables

}
