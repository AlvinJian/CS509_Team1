/**
 * 
 */
package utils;

/**
 * @author blake
 * @version 1.2
 *
 */
public class QueryFactory {
	
	/**
	 * Return a query string that can be passed to HTTP URL to request list of airports
	 * 
	 * @param teamName is the name of the team to specify the data copy on server
	 * @return the query String which can be appended to URL to form HTTP GET request
	 */
	public static String getAirports(String teamName) {
		return "?team=" + teamName + "&action=list&list_type=airports";
	}
        
        // TODO replace team name
        // ?team=TeamXX&action=list&list_type=departing&airport=code&day=yyyy_mm_dd
        public static String getFlights(String teamName, String flightQueryTypeStr
                , String code, String formattedDateStr) {
            return String.format("?team=%s&action=list&list_type=%s&airport=%s&day=%s"
                    , teamName, flightQueryTypeStr, code, formattedDateStr);
        }
	
	/**
	 * Lock the server database so updates can be written
	 * 
	 * @param teamName is the name of the team to acquire the lock
	 * @return the String written to HTTP POST to lock server database 
	 */
	public static String lock (String teamName) {
		return "team=" + teamName + "&action=lockDB";
	}
	
	/**
	 * Unlock the server database after updates are written
	 * 
	 * @param teamName is the name of the team holding the lock
	 * @return the String written to the HTTP POST to unlock server database
	 */
	public static String unlock (String teamName) {
		return "team=" + teamName + "&action=unlockDB";
	}
	

}
