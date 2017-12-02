/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flight;

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
public class ReserveFlightTest {
    
    public ReserveFlightTest() {
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
     * Test of addSeat method, of class ReserveFlight.
     */
    @Test
    public void testAddSeat() {
        System.out.println("addSeat");
        String flightNumber = "";
        String seatType = "";
//        ReserveFlight instance = new ReserveFlight();
//        instance.addSeat(flightNumber, seatType);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getXML method, of class ReserveFlight.
     */
    @Test
    public void testGetXML() {
        System.out.println("getXML");
        ReserveFlight instance = new ReserveFlight();
        String expResult = "";
        String result = instance.getXML();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
     @Test
    public void testGetXMLContent() {
        System.out.println("getXMLContent");
        ReserveFlight instance = new ReserveFlight();
        instance.addSeat("123", "Coach");
        instance.addSeat("345", "Coach");

        String expResult = "";
        String result = instance.getXML();
        assert (expResult != result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
}
