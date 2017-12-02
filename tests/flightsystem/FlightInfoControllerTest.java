/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightsystem;

import airport.Airports;
import java.time.LocalDateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
    @Test
    public void testSyncAirports() {
        System.out.println("syncAirports");
        FlightInfoController instance = new FlightInfoController();
        Airports expResult = null;
        Airports result = instance.syncAirports();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchDirectFlight method, of class FlightInfoController.
     */
    @Test
    public void testSearchDirectFlight() {
        System.out.println("searchDirectFlight");
        String fromAirportCode = "";
        LocalDateTime fromTime = null;
        String toAirportCode = "";
        FlightInfoController.FlightsReceiver receiver = null;
        FlightInfoController instance = new FlightInfoController();
        instance.searchDirectFlight(fromAirportCode, fromTime, toAirportCode, receiver);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
