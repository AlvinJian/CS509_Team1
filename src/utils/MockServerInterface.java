/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import airplane.Airplanes;
import airport.Airport;
import airport.Airports;
import dao.ServerInterface.QueryFlightFilter;
import dao.ServerInterface.QueryFlightType;
import flight.Flights;
import flight.ReserveFlight;
import java.time.LocalDateTime;

/**
 *
 * @author dg71532
 */
public class MockServerInterface implements Server {
    private boolean lockState = false;
    private Airports airports = null;
    @Override
    public Airports getAirports (String teamName)
    {
        System.out.println("getAirports");
        Airport boston = new Airport("Boston Airport", "BOS", 0, 10);
        Airports airports = new Airports();
        airports.add(boston);
        return airports;
    }
    @Override
    public Airplanes getAirplanes(String teamName)
    {
        System.out.println(" getAirplanes");
        return new Airplanes();
    }

    public void  setAirPort(Airports ap)
    {
    	System.out.println(" setAirplanes");
        airports = ap;
    }

    @Override
    public Flights getFlights(String teamName, QueryFlightType type, String airportCode, LocalDateTime gmtDateTime, QueryFlightFilter filter)
    {
        System.out.println(" getFlights");
        return new Flights();
    }
    @Override
    public boolean lock (String teamName)
    {
    	System.out.println(" lock");
    	lockState = true;
        return lockState;
    }
    @Override
    public boolean unlock (String teamName)
    {
        System.out.println(" unlock");
        lockState = false;
        return lockState;
    }
    @Override
    public boolean reserveSeat(String teamName, ReserveFlight reserveFlightObj)
    {
        System.out.println(" reserveSeat");
        return false;
    }
}
