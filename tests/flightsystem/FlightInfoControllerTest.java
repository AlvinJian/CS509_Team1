/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightsystem;

import airport.Airport;
import airport.Airports;
import flight.FlightConfirmation;
import flight.ReserveFlight;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import flight.Flight;
import flight.ReserveFlight;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author yuey
 */
public class FlightInfoControllerTest {
    
    public FlightInfoControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of syncAirports method, of class FlightInfoController.
     */
    @Ignore
    @Test
    public void testSyncAirports() {
        System.out.println("syncAirports");
        FlightInfoController instance = new FlightInfoController();
        Airports expResult = null;
        Airports result = instance.getAirports();
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of searchDirectFlight method, of class FlightInfoController.
     */
    @Ignore
    @Test
    public void testSearchDirectFlight() {
        System.out.println("searchDirectFlight");
        String fromAirportCode = "";
        LocalDateTime fromTime = null;
        String toAirportCode = "";
        FlightInfoController.FlightsReceiver receiver = null;
        FlightInfoController instance = new FlightInfoController();
        //instance.searchDirectFlight(fromAirportCode, fromTime, toAirportCode, receiver);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of reserveFlight method, of class FlightInfoController.
     */
//    @Test
//    public void testReserveFlight() {
//        System.out.println("reserveFlight");
//        ReserveFlight reserveFlightObj = null;
//        FlightInfoController.FlightConfirmationReceiver receiver = null;
//        FlightInfoController instance = new FlightInfoController();
//        instance.reserveFlight(reserveFlightObj, receiver);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of SearchStopoverFlights method, of class FlightInfoController.
     */
    @Test
    public void testSearchStopoverFlights() {
        System.out.println("SearchStopoverFlights");
        String depAirportCode = "";
        LocalDateTime depTime = null;
        String arrAirportCode = "";
        List<String> seatTypes = null;
        int stopover = 0;
        FlightInfoController.StopoverFlightsReceiver receiver = null;
        FlightInfoController instance = new FlightInfoController();
        //instance.SearchStopoverFlights(depAirportCode, depTime, arrAirportCode, seatTypes, stopover, receiver);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of syncAirplanes method, of class FlightInfoController.
     */
    @Test
    public void testSyncAirplanes() {
        System.out.println("syncAirplanes");
        FlightInfoController instance = new FlightInfoController();
        instance.getAirplanes();
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of convertToAirportTime method, of class FlightInfoController.
     */
    @Test
    public void testConvertToAirportTime() {
        System.out.println("convertToAirportTime");
//        List<Flight> flights = null;
        List<Flight> flights = new ArrayList<Flight>();
        String[] codeAirport = {"BOS", "PHL", "JFK", "SFO", "SEA", "BOS"};
        for (int i = 0; i < 5; i++){
            Flight f = new Flight();
            f.setmDepAirport(codeAirport[i+1]);
            f.setmDepTime(LocalDateTime.now().plusDays(i));
            f.setmArrAirport(codeAirport[i]);
            f.setmArrTime(LocalDateTime.now().plusHours(i));
            flights.add(f);
        }
        
        for (int i = 0; i < 5; i ++) {
            System.out.println("-----------------------------");
            System.out.println(flights.get(i).getmDepAirport());
            System.out.println(flights.get(i).getmDepTime());
            System.out.println(flights.get(i).getmArrAirport());
            System.out.println(flights.get(i).getmArrTime());
        }
        FlightInfoController instance = new FlightInfoController();
        System.out.println(instance);
        Airports airports = instance.syncAirports();
        System.out.println("airports size="+airports.size());
            for (Airport a: airports) {
                System.out.println(a.toString());
            }
        instance.convertToAirportTime(flights);
        
        for (int i = 0; i < 5; i ++) {
            System.out.println("-----------------------------");
            System.out.println(flights.get(i).getmDepAirport());
            System.out.println(flights.get(i).getmDepTime());
            System.out.println(flights.get(i).getmArrAirport());
            System.out.println(flights.get(i).getmArrTime());
        }
        
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
//    @Test
//    public void testReserveFlight() {
//        System.out.println("searchDirectFlight");
//        String fromAirportCode = "";
//        LocalDateTime fromTime = null;
//        String toAirportCode = "";
//        ReserveFlight reserveFlightObj = new ReserveFlight();
//        reserveFlightObj.addSeat("4853", "COACH");
//        AtomicBoolean magic = new AtomicBoolean();
//        magic.set(false);
//        FlightInfoController.FlightConfirmationReceiver receiver = new FlightInfoController.FlightConfirmationReceiver() {
//            @Override
//            public void onReceived(FlightConfirmation confirm) {
//                System.out.println("receive");
//                magic.set(true);
//            }
//        };
//        FlightInfoController instance = new FlightInfoController();
//        instance.reserveFlight(reserveFlightObj, receiver);
//        
//        fail("The test case is a prototype.");
//    }
}
