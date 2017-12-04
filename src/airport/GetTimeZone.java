/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airport;
import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author yuey
 */
public class GetTimeZone {
   
      public static String getHTML(String urlToRead) throws MalformedURLException, InterruptedException {
      StringBuilder result = new StringBuilder();
      URL url = new URL(urlToRead);
          for (int i = 0; i < 10 ; i++) {
              try{
              HttpURLConnection conn = (HttpURLConnection) url.openConnection();

              conn.setRequestMethod("GET");

              BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          
          
              String line;
              while ((line = rd.readLine()) != null) {
                  result.append(line);
              }
              rd.close();
              break;
              }
              catch(IOException e){
                  TimeUnit.SECONDS.sleep(1);
                  
              }
          }
      
      return result.toString();
   }
//         public static void main(String[] args) throws Exception
//   {
//     System.out.println(getHTML(args[0]));
//   }
}
