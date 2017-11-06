/**
 * 
 */
package driver;

import java.util.Collections;

import airport.Airport;
import airport.Airports;
import dao.ServerInterface;
import flight.Flight;
import flight.Flights;
import flightsystem.FlightSysteUI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author blake
 *
 */
public class Driver {
    static private Logger driverLogger;
    static {
        driverLogger = Logger.getLogger(Driver.class.getName());
        driverLogger.setLevel(Level.INFO);
    }

	/**
	 * Entry point for CS509 sample code driver
	 * 
	 * This driver will retrieve the list of airports from the CS509 server and print the list 
	 * to the console sorted by 3 character airport code
	 * 
	 * @param args is the arguments passed to java vm in format of "CS509.sample teamName" where teamName is a valid team
	 */
	public static void main(String[] args) {
            String teamName = "CS509team1";
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(FlightSysteUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(FlightSysteUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(FlightSysteUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(FlightSysteUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        //</editor-fold>

        /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
            }
            });
        
            FlightSysteUI uiObj = new FlightSysteUI();
            //FlightInfo flightInfoObj = new FlightInfo();
            //flightInfoObj.addObserver( uiObj);
            uiObj.setVisible(true);
		
		// Try to get a list of airports
//		Airports airports = ServerInterface.INSTANCE.getAirports(teamName);
//		Collections.sort(airports);
//		for (Airport airport : airports) {
//			System.out.println(airport.toString());
//		}

            /* 
             * TODO following codes is an example of query flights.
             * can be comment out when not needed.
            */
//            final String dateTimePttrn = "yyyy MMM dd HH:mm zzz";
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePttrn);
//            LocalDateTime dateTime = LocalDateTime.parse("2017 Dec 10 00:12 GMT", formatter);
//            ServerInterface.QueryFlightType type = ServerInterface.QueryFlightType.ARRIVAL;
//            String code = "BOS";
//            ServerInterface.QueryFlightFilter departFilter = new ServerInterface.QueryFlightFilter() {
//                public boolean isValid(Flight f) {
//                    if (f.getmDepAirport() != null && f.getmDepAirport().equals("PHL")) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//            };
//            Flights retFlights = ServerInterface.INSTANCE.getFlights(teamName, code, dateTime, type, departFilter);
//            driverLogger.log(Level.INFO, "search result={0}", retFlights.size());
//            for (Flight f: retFlights) {
//                driverLogger.log(Level.INFO, "depart={0} arrival={1}", 
//                        new Object[]{f.getmDepAirport(), f.getmArrAirport()});
//            }
	}
}
