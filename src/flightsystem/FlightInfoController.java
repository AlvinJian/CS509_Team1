/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightsystem;

import airport.Airport;
import airport.AirportZoneMap;
import airport.Airports;
import dao.ServerInterface;
import flight.Flight;
import flight.Flights;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    
    private FlightsQueryParam prevFlightsQuery;
    private Flights directFlightsCache;
    private Airports airportsCache;
    private List<ArrayList<Flight>> stopoverFlights;
    
    public FlightInfoController() {
    }
    
    public interface FlightsReceiver {
        public void onReceived(Flights ret);
    }
    
    public Airports syncAirports() {
        synchronized (serverLck) {
            airportsCache = ServerInterface.INSTANCE.getAirports(teamName);
        }
        return airportsCache;
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
        final FlightsQueryParam queryParam = new FlightsQueryParam();
        queryParam.fromAirportCode = fromAirportCode;
        queryParam.fromTime = fromTime;
        queryParam.toAirportCode = toAirportCode;
        queryParam.layoverCount = 0;
        if (prevFlightsQuery == null || !prevFlightsQuery.equals(queryParam)) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    Flights ret;
                    synchronized (serverLck) {
                        ret = SearchFlightsImpl(fromAirportCode, fromTime, toAirportCode);
                        
                        controllerLogger.log(logLevel, "flight count={0}", ret.size());
                        prevFlightsQuery = queryParam;
                        directFlightsCache = ret;
                    }
                    Runnable _r = () -> receiver.onReceived(ret);
                    SwingUtilities.invokeLater(_r);
                }
            };
            Thread t = new Thread(r);
            t.start();
        } else {
            controllerLogger.log(logLevel, "use flights cache!");
            receiver.onReceived(directFlightsCache);
        }
        
    }
    
    public void searchStopoverFlight() {
    
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
                    return true;
                }
            }
            return false;
        };
        Flights ret = ServerInterface.INSTANCE.getFlights(teamName, ServerInterface.QueryFlightType.DEPART, fromAirport.code(), 
                gmtFromDateTime, arrivalFilter);
        return ret;
    }
    
    private void searchDFS(Flight fromAirport, int stopover) {
        
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
    
    private class FlightsQueryParam {
        String fromAirportCode;
        LocalDateTime fromTime; 
        String toAirportCode; 
        int layoverCount;

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 29 * hash + Objects.hashCode(this.fromAirportCode);
            hash = 29 * hash + Objects.hashCode(this.fromTime);
            hash = 29 * hash + Objects.hashCode(this.toAirportCode);
            hash = 29 * hash + this.layoverCount;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final FlightsQueryParam other = (FlightsQueryParam) obj;
            if (this.layoverCount != other.layoverCount) {
                return false;
            }
            if (!Objects.equals(this.fromAirportCode, other.fromAirportCode)) {
                return false;
            }
            if (!Objects.equals(this.toAirportCode, other.toAirportCode)) {
                return false;
            }
            if (!Objects.equals(this.fromTime, other.fromTime)) {
                return false;
            }
            return true;
        }   
    }
}
