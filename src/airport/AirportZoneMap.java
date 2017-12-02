/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airport;

import java.time.ZoneId;
import org.json.*;


/**
 *
 * @author alvin
 */
// TODO need 3rd APIs to do mapping here!
public class AirportZoneMap {
public static ZoneId GetTimeZoneByAiport(Airport airport) {
        double lat = airport.latitude();
        double lon = airport.longitude();
        
        String htmlstr = "https://maps.googleapis.com/maps/api/timezone/"
                + "json?location="
                + lat + "," + lon + "&timestamp=1458000000&key="
                + "AIzaSyAG80PBlcPx5O2xnSyQuxIO4mey1Rcgrcw";

        try {
//            System.out.println(GetTimeZone.getHTML(htmlstr));
            JSONObject timezoneobj = new JSONObject(GetTimeZone.getHTML(htmlstr));
            
            String outID = timezoneobj.getString("timeZoneId");
            ZoneId ret = ZoneId.of(outID);
            System.out.println(ret);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return ZoneId.systemDefault();
        }    
        // TODO this is dummy.

    }
}
