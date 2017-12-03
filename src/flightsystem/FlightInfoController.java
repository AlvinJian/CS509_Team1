/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightsystem;

import airplane.Airplane;
import airplane.Airplanes;
import airport.Airport;
import airport.AirportZoneMap;
import airport.Airports;
import airport.TimeConverter;
import dao.ServerInterface;
import flight.Flight;
import flight.Flights;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private Map<String, Airplane> airplaneCache;
    private List<ArrayList<Flight>> stopoverFlights = new ArrayList<>();

    public FlightInfoController() {
        syncAirplanes();
    }

    public interface FlightsReceiver {
        public void onReceived(Flights ret);
    }
    
    public interface StopoverFlightsReceiver {
        public void onReceived(List<List<Flight>> ret);
    }

    public Airports syncAirports() {
        synchronized (serverLck) {
            airportsCache = ServerInterface.INSTANCE.getAirports(teamName);
        }
        return airportsCache;
    }

    public void searchDirectFlight(String fromAirportCode, LocalDateTime fromTime,
            String toAirportCode, List<String> seatTypes, FlightsReceiver receiver) {
        if (airportsCache == null) {
            syncAirports();
        }
//        controllerLogger.log(Level.INFO, "fromAirportCode={0}, fromTime={1}, toAirportCode={2}, receiver={3}",
//                new Object[]{fromAirportCode, fromTime, toAirportCode, receiver});
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
                    ret = SearchFlightsImpl(fromAirportCode, fromTime, toAirportCode, seatTypes);
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
    
    public void SearchStopoverFlights(String depAirportCode, LocalDateTime depTime, 
            String arrAirportCode, List<String> seatTypes, int stopover, 
            StopoverFlightsReceiver receiver) {
        if (depAirportCode == null || depTime == null || arrAirportCode == null ||
                seatTypes == null) {
            controllerLogger.log(Level.SEVERE, "SearchStopoverFlights args error");
            List<List<Flight>> ret = new ArrayList<>();
            ret.add(new ArrayList<>());
            receiver.onReceived(ret);
            return;
        }
        final Level logLevel = Level.INFO;
        Runnable r = () -> {
            List<List<Flight>> ret;
            synchronized (serverLck) {
                ret = _SearchStopoverFlightsImpl(depAirportCode, depTime, 
                        arrAirportCode, seatTypes, stopover);
            }
            controllerLogger.log(logLevel, "flight count={0}", ret.size());
            Runnable _r = () -> receiver.onReceived(ret);
            SwingUtilities.invokeLater(_r);
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void stopOverSearchTest(List<List<Flight>> trial) {
        Level level = Level.INFO;
        controllerLogger.log(level, "trialSize={0}", trial.size());
        for (List<Flight> sublist : trial) {
            StringBuilder builder = new StringBuilder();
            for (Flight f : sublist) {
                String depCode = f.getmDepAirport();
                LocalDateTime depDateTime = f.getmDepTime();
                String arrCode = f.getmArrAirport();
                LocalDateTime arrDateTime = f.getmArrTime();
                String msg = String.format("depCode=%s depDateTime=%s arrCode=%s arrDateTime=%s",
                        depCode, depDateTime.toString(), arrCode, arrDateTime.toString());
                controllerLogger.log(level, msg);
                builder.append(f.getmNumber());
                builder.append(", ");
            }
            controllerLogger.log(level, builder.toString());
        }
    }

    private boolean isInDfsHistory(List<Flight> list, Flight flight) {
        controllerLogger.log(Level.INFO, "===START===");
        StringBuilder builder = new StringBuilder();
        for (Flight f : list) {
            String depCode = f.getmDepAirport();
            LocalDateTime depDateTime = f.getmDepTime();
            String arrCode = f.getmArrAirport();
            LocalDateTime arrDateTime = f.getmArrTime();
            String msg = String.format("depCode=%s depDateTime=%s arrCode=%s arrDateTime=%s",
                    depCode, depDateTime.toString(), arrCode, arrDateTime.toString());
            controllerLogger.log(Level.INFO, msg);
            builder.append(f.getmNumber());
            builder.append(", ");
            if (flight.getmArrAirport().equals(f.getmDepAirport())) {
                return true;
            }
        }
        controllerLogger.log(Level.INFO, builder.toString());
        controllerLogger.log(Level.INFO, "===END===");
        return false;
    }

    // t2 - t1
    private static long hourSubstract(LocalDateTime t1, LocalDateTime t2) {
        int h2 = t2.getHour();
        int h1 = t1.getHour();
        LocalDate d2 = t2.toLocalDate();
        LocalDate d1 = t1.toLocalDate();
        if ( t1.getDayOfMonth() != t2.getDayOfMonth())
        {
            controllerLogger.log(Level.INFO, "FOUND ROLLOVER DATE ***");
            controllerLogger.log(Level.INFO, "*********" + Duration.between(t1, t2).toMinutes());
        }

        return Duration.between(t1, t2).toMinutes();
    }
    
    private List<List<Flight>> _stopoverFlights;
    
    private List<List<Flight>> _SearchStopoverFlightsImpl(String depAirportCode, 
            LocalDateTime depTime, String arrAirportCode, List<String> seatTypes, int stopover) {
        Airport depAirport = getAirportByCode(depAirportCode);
        ZoneId depZoneId = AirportZoneMap.GetTimeZoneByAiport(depAirport);
        ZonedDateTime zonedDepDateTime = ZonedDateTime.of(depTime, depZoneId);
        LocalDateTime gmtDepDateTime = zonedDepDateTime.withZoneSameInstant(
                    ZoneId.of("GMT")).toLocalDateTime();
        ServerInterface.QueryFlightFilter lv1Filter = new ServerInterface.QueryFlightFilter() {
            @Override
            public boolean isValid(Flight f) {
                if (f == null) return false;
                for (String seatType: seatTypes) {
                    if (!isSeatAvailable(f, airplaneCache.get(f.getmAirplane()), seatType)) {
                        return false;
                    }
                }
                if (f.getmDepTime().isAfter(gmtDepDateTime)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        _stopoverFlights = new ArrayList<>();
        Flights flights = ServerInterface.INSTANCE.getFlights(teamName, ServerInterface.QueryFlightType.DEPART, 
                depAirportCode, gmtDepDateTime, lv1Filter);
        int level = 1;
        for (Flight f: flights) {
            String nextDepAirportCode = f.getmArrAirport();
            LocalDateTime nextDepDateTime = f.getmArrTime();
            List<Flight> history = new ArrayList<>();
            history.add(f);
            _SearchStopoverFlightsInnr(history, nextDepAirportCode, nextDepDateTime, 
                    arrAirportCode, seatTypes, level + 1, stopover);
        }

        return _stopoverFlights;
    }
    
    private void _SearchStopoverFlightsInnr(List<Flight> history, 
            String depAirportCode, LocalDateTime depTime, String arrAirportCode, 
            List<String> seatTypes, int level, int stopover) {
        ServerInterface.QueryFlightFilter highLvFilter = new ServerInterface.QueryFlightFilter() {
            @Override
            public boolean isValid(Flight f) {
                if (f == null) return false;
                for (String seatType: seatTypes) {
                    if (!isSeatAvailable(f, airplaneCache.get(f.getmAirplane()), seatType)) {
                        return false;
                    }
                }
                long diff = hourSubstract(depTime, f.getmDepTime());
                if ((diff >= 30 && diff <= 240) && !isInDfsHistory(history, f)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        Flights flights = ServerInterface.INSTANCE.getFlights(teamName, ServerInterface.QueryFlightType.DEPART, 
                depAirportCode, depTime, highLvFilter);
        for (Flight f: flights) {
            List<Flight> newHistory = new ArrayList<>(history);
            newHistory.add(f);
            if (newHistory.size() == stopover+2) {
                if (f.getmArrAirport().equals(arrAirportCode)) {
                    _stopoverFlights.add(newHistory);
                }
            } else {
                String nextDepAirportCode = f.getmArrAirport();
                LocalDateTime nextDepDateTime = f.getmArrTime();
                _SearchStopoverFlightsInnr(newHistory, nextDepAirportCode, nextDepDateTime, 
                    arrAirportCode, seatTypes, level + 1, stopover);
            }
        }
    }

    private Flights SearchFlightsImpl(String fromAirportCode, LocalDateTime fromTime,
            String toAirportCode, List<String> seatTypes) {
        final Airport fromAirport = getAirportByCode(fromAirportCode);
        ZoneId fromZoneId = AirportZoneMap.GetTimeZoneByAiport(fromAirport);
        final ZonedDateTime zonedFromDateTime = ZonedDateTime.of(fromTime, fromZoneId);
        final LocalDateTime gmtFromDateTime = zonedFromDateTime.withZoneSameInstant(
                ZoneId.of("GMT")).toLocalDateTime();
        final ServerInterface.QueryFlightFilter arrivalFilter = (Flight f) -> {
            if (f == null) {
                return false;
            }
            for (String seatType : seatTypes) {
                if (!isSeatAvailable(f, airplaneCache.get(f.getmAirplane()), seatType)) {
                    return false;
                }
            }
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

    private Airport getAirportByCode(String code) {
        if (airportsCache == null) {
            return null;
        }
        Airport ret = null;
        for (Airport a : airportsCache) {
            if (a.code().equals(code)) {
                ret = a;
                break;
            }
        }
        return ret;
    }
    
    private boolean isSeatAvailable(Flight flight, Airplane airplane, String seattype) {
        boolean availableSeats = false;

        if (seattype.equalsIgnoreCase(Airplane.COACH)) {
            int mSeatsCoach = flight.getmSeatsCoach();
            int mCoachSeats = airplane.getmCoachSeats();
            int remCoachSeats = mCoachSeats - mSeatsCoach;
            if (remCoachSeats > 0) {
                availableSeats = true;
            }

        } else if (seattype.equalsIgnoreCase(Airplane.FIRST)) {
            int mSeatsFirst = flight.getmSeatsFirst();
            int mFirstClassSeats = airplane.getmFirstClassSeats();
            int remFirstClassSeats = mFirstClassSeats - mSeatsFirst;
            if (remFirstClassSeats > 0) {
                availableSeats = true;
            }
        }

        return availableSeats;

    }
    
    public void syncAirplanes() {
        Airplanes allPlanes = ServerInterface.INSTANCE.getAirplanes(teamName);
        airplaneCache = new HashMap<>();
        for (Airplane a: allPlanes) {
            String model = a.getmModel();
            airplaneCache.put(model, a);
        }
    }
}


