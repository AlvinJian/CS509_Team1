/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightsystem;

import airplane.Airplane;
import airport.Airport;
import airport.AirportZoneMap;
import airport.Airports;
import dao.ServerInterface;
import flight.Flight;
import flight.FlightConfirmation;
import flight.Flights;
import flight.ReserveFlight;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author alvin
 */
public class FlightInfoController {    
    private static final Object serverLck = new Object();
    // TODO shouldn't be here
    private static final String teamName = "CS509team1";
    private static final Logger controllerLogger;
    
    static {
        controllerLogger = Logger.getLogger(FlightInfoController.class.getName());
        controllerLogger.setLevel(Level.FINE);
    }

    private Flights directFlightsCache;
    private Airports airportsCache;
    private List<ArrayList<Flight>> stopoverFlights = new ArrayList<>();
    
    public FlightInfoController() {
    }
    
    public interface FlightsReceiver {
        public void onReceived(Flights ret);
    }
    public interface FlightConfirmationReceiver
    {
        public void onReceived(FlightConfirmation confirm);
    }
    public Airports syncAirports() {
        synchronized (serverLck) {
            airportsCache = ServerInterface.INSTANCE.getAirports(teamName);
        }
        return airportsCache;
    }
    
    public void reserveFlight(ReserveFlight reserveFlightObj, FlightConfirmationReceiver receiver)
    {
        String xml = reserveFlightObj.getXML();
        if ( xml.isEmpty() ){
            if( receiver != null)
            {
                 receiver.onReceived(new FlightConfirmation(false, "Received an empty list of flights"));
            }
        }
        else
        {
            //TODO: add thread to loop until database is unlocked
            ServerInterface.INSTANCE.lock(teamName);
            Runnable r = new Runnable() {
            @Override
            public void run() {
                FlightConfirmation flightConfirm;
                synchronized (serverLck) {
                    
                }
                Runnable _r = () -> receiver.onReceived(flightConfirm);
                SwingUtilities.invokeLater(_r);
            }
            };
            Thread t = new Thread(r);
            t.start();
        }
        
        
    }
    public void searchDirectFlight(String fromAirportCode, LocalDateTime fromTime, 
            String toAirportCode, FlightsReceiver receiver) {
        if (airportsCache == null) {
            syncAirports();
        }
        controllerLogger.log(Level.INFO, "fromAirportCode={0}, fromTime={1}, toAirportCode={2}, receiver={3}", 
                new Object[] {fromAirportCode, fromTime, toAirportCode, receiver});
        if (fromAirportCode == null || fromTime == null || toAirportCode == null
                || receiver == null) {
            controllerLogger.log(Level.SEVERE, "searchDirectFlight args error");
            receiver.onReceived(new Flights());
            return;
        }
        final Level logLevel = Level.INFO;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Flights ret;
                synchronized (serverLck) {
                    ret = SearchFlightsImpl(fromAirportCode, fromTime, toAirportCode);
                    controllerLogger.log(logLevel, "flight count={0}", ret.size());
                    directFlightsCache = ret;
                }
                Runnable _r = () -> receiver.onReceived(ret);
                SwingUtilities.invokeLater(_r);
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
    
    private List<ArrayList<Flight>> SearchStopoverFlightsImpl(String fromAirportCode, LocalDateTime fromTime, 
            String toAirportCode, int level, int stopover) {
        final Airport fromAirport = getAirportByCode(fromAirportCode);
        
        ZoneId fromZoneId = AirportZoneMap.GetTimeZoneByAiport(fromAirport);
        ZonedDateTime zonedFromDateTime = ZonedDateTime.of(fromTime, fromZoneId);               
        LocalDateTime gmtFromDateTime = zonedFromDateTime.withZoneSameInstant(
            ZoneId.of("GMT")).toLocalDateTime();
        Flights ret = null;
        if(level == 1) {
            stopoverFlights.set(0, new ArrayList<>());
            ServerInterface.QueryFlightFilter arrivalFilter = (Flight f) -> {
                if (f == null) return false;
                if (f.getmArrAirport().equals(toAirportCode)) {
                    if (f.getmDepTime().isAfter(gmtFromDateTime)) {
                        return true;
                    }
                }
            return false;
        };
        ret = ServerInterface.INSTANCE.getFlights(teamName, ServerInterface.QueryFlightType.DEPART, fromAirport.code(), 
                gmtFromDateTime, arrivalFilter);
        DFS(ret, new ArrayList<>(), gmtFromDateTime, toAirportCode, level, stopover);
        return stopoverFlights;
        } else {
        ServerInterface.QueryFlightFilter arrivalFilter = (Flight f) -> {
            if (f == null) return false;
            if (f.getmDepTime().isAfter(fromTime)) {
                    return true;
            }
            return false;
        };
        ret = ServerInterface.INSTANCE.getFlights(teamName, ServerInterface.QueryFlightType.DEPART, fromAirport.code(), 
                gmtFromDateTime, arrivalFilter);
        DFS(ret, new ArrayList<>(), fromTime, toAirportCode, level, stopover);
        }
        return stopoverFlights;
    }
    
    private void DFS(Flights flights, List<Flight> list, LocalDateTime fromTime, String toAirportCode, int level, int stopover) {
        if(flights == null)
            return;
        
        level++;
        for(Flight flight: flights) {
            list.add(flight);
            if(level == stopover + 2) {
                if(flight.getmArrAirport().equals(toAirportCode)) {
                    stopoverFlights.add(new ArrayList<>(list));
                    return;
                }
            }
            if(flight.getmDepTime().isAfter(fromTime)) {
                SearchStopoverFlightsImpl(flight.getmDepAirport(), flight.getmDepTime(), toAirportCode, level, stopover);
            }
            list.remove(list.size() - 1);
        }
    }
    
    private Flights SearchFlightsImpl(String fromAirportCode, LocalDateTime fromTime, 
            String toAirportCode) {
        final Airport fromAirport = getAirportByCode(fromAirportCode);
        ZoneId fromZoneId = AirportZoneMap.GetTimeZoneByAiport(fromAirport);
        final ZonedDateTime zonedFromDateTime = ZonedDateTime.of(fromTime, fromZoneId);               
        final LocalDateTime gmtFromDateTime = zonedFromDateTime.withZoneSameInstant(
            ZoneId.of("GMT")).toLocalDateTime();
        final ServerInterface.QueryFlightFilter arrivalFilter = (Flight f) -> {
            if (f == null) return false;
            if (f.getmArrAirport().equals(toAirportCode)) {
                if (f.getmDepTime().isAfter(gmtFromDateTime)) {
                   
                }
            }
            return false;
        };
        Flights ret = ServerInterface.INSTANCE.getFlights(teamName, ServerInterface.QueryFlightType.DEPART, fromAirport.code(), 
                gmtFromDateTime, arrivalFilter);
        return ret;
    }
    
    private Airport getAirportByCode(String code) {
        if (airportsCache == null) return null;
        Airport ret = null;
        for (Airport a: airportsCache) {
            if (a.code().equals(code)) {
                ret = a;
                break;
            }
        }
        return ret;
    }
    
    private boolean seatMatch(Flight flight, Airplane airplane, String seattype) {
        boolean availableSeats = false;

        if (seattype.equalsIgnoreCase("coach")) {
            int mSeatsCoach = flight.getmSeatsCoach();
            int mCoachSeats = airplane.getmCoachSeats();
            int remCoachSeats = mCoachSeats - mSeatsCoach;
            if (remCoachSeats > 0) {
                availableSeats = true;
            }

        } else if (seattype.equalsIgnoreCase("firstclass")) {
            int mSeatsFirst = flight.getmSeatsFirst();
            int mFirstClassSeats = airplane.getmFirstClassSeats();
            int remFirstClassSeats = mFirstClassSeats - mSeatsFirst;
            if (remFirstClassSeats > 0) {
                availableSeats = true;
            }
        }

        return availableSeats;

    }
}


