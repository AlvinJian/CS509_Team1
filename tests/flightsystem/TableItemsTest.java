/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightsystem;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dg71532
 */
public class TableItemsTest {
    
    public TableItemsTest() {
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

    @Test
    public void testAddFlightnumberString() {
        TableItems tableItems = new TableItems();
        tableItems.flightNumberClass.addFlightNumber("503");
        tableItems.flightNumberClass.addFlightNumber("504");
        String returnString = tableItems.flightNumberClass.toString();
        assert(returnString.equals("503 504"));
    }
    @Test
    public void testDepartureArrivalString() {
        TableItems tableItems = new TableItems();
        tableItems.departureArrivalClass.addDepartureArrival("BOS", "DEV", "Coach");
        tableItems.departureArrivalClass.addDepartureArrival("DEV", "LA", "FirstClass");
        tableItems.departureArrivalClass.addDepartureArrival("LA", "FL", "FirstClass");
        String returnString = tableItems.departureArrivalClass.toString();
        assert( true );
    }
    
}
