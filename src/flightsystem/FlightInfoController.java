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
import flight.FlightConfirmation;
import flight.Flights;
import flight.ReserveFlight;
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
import java.util.Timer;
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

    public FlightInfoController() {
        syncAirplanes();
    }

    public interface FlightsReceiver {
        public void onReceived(Flights ret);
    }
    public interface FlightConfirmationReceiver
    {
        public void onReceived(FlightConfirmation confirm);
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
            final FlightConfirmation flightConfirm = new FlightConfirmation(false, "timeout");
            Timer timer = new Timer();
            timer.schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    Runnable _r = () -> receiver.onReceived(flightConfirm);
                    SwingUtilities.invokeLater(_r);
//                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

            }, 30000);
            //TODO: add thread to loop until database is unlocked
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    synchronized (serverLck) {
                        boolean isGetLock = ServerInterface.INSTANCE.lock(teamName);
                        while (!isGetLock) {
                            isGetLock = ServerInterface.INSTANCE.lock(teamName);
                        }
                        timer.cancel();
                        ServerInterface.INSTANCE.reserveSeat(teamName, reserveFlightObj);
                        ServerInterface.INSTANCE.unlock(teamName);
                    }
                    final FlightConfirmation flightConfirm = new FlightConfirmation(true, "");
                    Runnable _r = () -> receiver.onReceived(flightConfirm);
                    SwingUtilities.invokeLater(_r);
                }
            };
            Thread t = new Thread(r);
            t.start();
        }
        
        
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
                    ret = searchFlightsImpl(fromAirportCode, fromTime, toAirportCode, seatTypes);
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
    
    public void searchStopoverFlights(String depAirportCode, LocalDateTime depTime, 
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
                ret = searchStopoverFlightsImpl(depAirportCode, depTime, 
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
        Level level = Level.FINE;
        controllerLogger.log(level, "===START===");
        StringBuilder builder = new StringBuilder();
        for (Flight f : list) {
            String depCode = f.getmDepAirport();
            LocalDateTime depDateTime = f.getmDepTime();
            String arrCode = f.getmArrAirport();
            LocalDateTime arrDateTime = f.getmArrTime();
            String msg = String.format("depCode=%s depDateTime=%s arrCode=%s arrDateTime=%s",
                    depCode, depDateTime.toString(), arrCode, arrDateTime.toString());
            controllerLogger.log(level, msg);
            builder.append(f.getmNumber());
            builder.append(", ");
            if (flight.getmArrAirport().equals(f.getmDepAirport())) {
                return true;
            }
        }
        controllerLogger.log(level, builder.toString());
        controllerLogger.log(level, "===END===");
        return false;
    }

    // t2 - t1
    private static long DurationMins(LocalDateTime t1, LocalDateTime t2) {
        return Duration.between(t1, t2).toMinutes();
    }
    
    private List<List<Flight>> _stopoverFlights;
    
    private boolean isAnySeatTypeAvailable(Flight f, List<String> seatTypes) {
        boolean avail = false;
        for (String seatType : seatTypes) {
            if (isSeatAvailable(f, airplaneCache.get(f.getmAirplane()), seatType)) {
                avail = true;
                break;
            }
        }
        return avail;
    }
    
    private List<List<Flight>> searchStopoverFlightsImpl(String depAirportCode, 
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
                if (!isAnySeatTypeAvailable(f, seatTypes)) {
                    return false;

                }
                if (f.getmDepTime().isAfter(gmtDepDateTime)) {
                    f.setmSeatTypeAvailable(seatTypes);
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
            searchStopoverFlightsInner(history, nextDepAirportCode, nextDepDateTime, 
                    arrAirportCode, seatTypes, level + 1, stopover);
        }

        return _stopoverFlights;
    }
    
    private void searchStopoverFlightsInner(List<Flight> history, 
            String depAirportCode, LocalDateTime depTime, String arrAirportCode, 
            List<String> seatTypes, int level, int stopover) {
        ServerInterface.QueryFlightFilter highLvFilter = new ServerInterface.QueryFlightFilter() {
            @Override
            public boolean isValid(Flight f) {
                if (f == null) return false;
                if (!isAnySeatTypeAvailable(f, seatTypes)) {
                    return false;

                }
                long diff = DurationMins(depTime, f.getmDepTime());
                if ((diff >= 30 && diff <= 240) && !isInDfsHistory(history, f)) {
                    f.setmSeatTypeAvailable(seatTypes);
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
            if (newHistory.size() == stopover+1) {
                if (f.getmArrAirport().equals(arrAirportCode)) {
                    convertToAirportTime(newHistory);
                    List<List<Flight>> ret = new ArrayList<>();
                    copyFlightList(ret, newHistory, newHistory, stopover, 0);
                    for (List<Flight> r: ret) {
                        copyFlightListTest(r);
                        _stopoverFlights.add(r);
                    }
//                    _stopoverFlights.add(newHistory);
                }
            } else {
                String nextDepAirportCode = f.getmArrAirport();
                LocalDateTime nextDepDateTime = f.getmArrTime();
                searchStopoverFlightsInner(newHistory, nextDepAirportCode, nextDepDateTime, 
                    arrAirportCode, seatTypes, level + 1, stopover);
            }
        }
    }

    private Flights searchFlightsImpl(String fromAirportCode, LocalDateTime fromTime,
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
            if (!isAnySeatTypeAvailable(f, seatTypes)) {
                return false;
            }
            if (f.getmArrAirport().equals(toAirportCode)) {
                if (f.getmDepTime().isAfter(gmtFromDateTime)) {
                    f.setmSeatTypeAvailable(seatTypes);
                    return true;
                }
            }
            return false;
        };
        Flights ret = ServerInterface.INSTANCE.getFlights(teamName, ServerInterface.QueryFlightType.DEPART, fromAirport.code(),
                gmtFromDateTime, arrivalFilter);
        return ret;
    }
    
    private void copyFlightListTest(List<Flight> fs) {
        Level level = Level.INFO;
        StringBuilder builder = new StringBuilder();
        for (Flight f: fs) {
            builder.append(String.format("departCode=%s ", f.getmDepAirport()));
            builder.append(String.format("departTime=%s ", f.getmDepTime().toString()));
            builder.append(String.format("arrCode=%s ", f.getmArrAirport()));
            builder.append(String.format("arrTime=%s ", f.getmArrTime().toString()));
            builder.append(String.format("seat_type=%s", 
                    f.getmSeatTypeAvailable().toString()));
        }
        controllerLogger.log(level, builder.toString());
    }

    private void copyFlightList(List<List<Flight>> result, List<Flight> flights, List<Flight> original, int stopover, int index) {
        if(flights.size() == 0 || flights == null || index < stopover + 1) {
            result.add(new ArrayList<>(flights));
            return;
        }
            
        List<String> seatTypes;
        Flight f = original.get(index);
        index++;
        if(f.getmSeatTypeAvailable().size() <= 1) {
            copyFlightList(result, flights, original, stopover, index);
        } else {
            List<Flight> copy = new ArrayList<>(flights);
            List<String> coach = new ArrayList<>();
            coach.add(Airplane.COACH);
            copy.get(index).replacemSeatTypeAvailable(coach);
            copyFlightList(result, copy, original, stopover, index);
            List<String> first = new ArrayList<>();
            coach.add(Airplane.FIRST);
            flights.get(index).replacemSeatTypeAvailable(first);
            copyFlightList(result, flights, original, stopover, index);
        }
        
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
    
     public void convertToAirportTime(List<Flight> flights) {
            String mDepAirport;
            LocalDateTime mDepTime;
            String mArrAirport;
            LocalDateTime mArrTime;
            
            for (Flight flight : flights) {
             mDepAirport = flight.getmDepAirport();
             mDepTime = flight.getmDepTime();
             mArrAirport = flight.getmArrAirport();
             mArrTime = flight.getmArrTime();

             //Airportcode -> Airport
             Airport depAirport = getAirportByCode(mDepAirport);
             Airport arrAirport = getAirportByCode(mArrAirport);

             ZoneId zoneDep = AirportZoneMap.GetTimeZoneByAiport(depAirport);
             ZoneId zoneArr = AirportZoneMap.GetTimeZoneByAiport(arrAirport);
             ZoneId gmtZone = ZoneId.of("GMT");

             ZonedDateTime gmtDepTime = ZonedDateTime.of(mDepTime, gmtZone);
             flight.setmDepTime(gmtDepTime.withZoneSameInstant(zoneDep).toLocalDateTime());

             ZonedDateTime gmtArrTime = ZonedDateTime.of(mArrTime, gmtZone);
             flight.setmArrTime(gmtArrTime.withZoneSameInstant(zoneArr).toLocalDateTime());

            }
            
            
        }
}


