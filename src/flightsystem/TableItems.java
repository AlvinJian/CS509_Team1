/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author dg71532
 */
public class TableItems {

    FlightNumber flightNumberClass = new FlightNumber();
    DestinationAirports destinationAirportsClass = new DestinationAirports();
    DepartureArrival departureArrivalClass = new DepartureArrival();
    
    
    class FlightNumber {
        List<String> flightNumbers;
        HashMap<String,String> flightAndSeatType;
        public FlightNumber() {
            flightNumbers = new ArrayList<>();
            flightAndSeatType = new HashMap<>();
        }
        public void addFlightNumber( String flight)
        {
            flightNumbers.add(flight);
        }
        public void addFlightNumberAndSeatType( String flight, String seatType)
        {
            flightAndSeatType.put(flight, seatType);
        }
        public HashMap<String,String> getFlightNumberAndSeatType()
        {
            return flightAndSeatType;
        }
        @Override
        public String toString()
        {
            String output = "";
            for(String flightNumber : flightNumbers)
            {
                if( output.isEmpty())
                {
                    output += flightNumber;
                }
                else
                {
                    output += "->"+flightNumber;
                }
            }
            //output = flightNumbers.stream().map((flight) -> flight+"->").reduce(output, String::concat);
            return output;
        }
    }
    class DestinationAirports
    {
        List<String> destinationAirports;
        public DestinationAirports() 
        {
            destinationAirports = new ArrayList<>();
        }
        public void addDestinationAirports( String flight)
        {
            destinationAirports.add(flight);
        }
        @Override
        public String toString()
        {
            String output = "";
            output = destinationAirports.stream().map((airport) -> airport+"\n").reduce(output, String::concat);
            return output;
        }
        
    }
    class DepartureArrival
    {
        List<HashMap<String, String>> departureArrivalList ;
        public DepartureArrival() {
            departureArrivalList = new ArrayList<>();
        }
        public void addDepartureArrival( String departure, String arrival, String seatType)
        {
            HashMap<String, String> infoMap = new HashMap<>();
            infoMap.put("departure",departure);
            infoMap.put("arrival", arrival);
            infoMap.put("seatType", seatType);
            departureArrivalList.add(infoMap);
        }
        @Override
        public String toString()
        {
            String output = "";
            HashMap<String, String> previousMap = new HashMap<>();
            for (HashMap<String, String> infoMap:departureArrivalList)
            {
                if(previousMap.isEmpty())
                {
                     previousMap = infoMap; 
                     output += infoMap.get("departure");
                }
                output += "-["+infoMap.get("seatType")+"]->"+infoMap.get("arrival");
            }
            return output;
        }
        
    }
    
}
