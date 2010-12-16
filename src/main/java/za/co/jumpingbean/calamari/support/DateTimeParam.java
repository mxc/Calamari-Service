/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.support;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * This class reads in the data from the url in the form yyyymmdd and converts it from local time to UTC
 * this is to be able to compare it to the accesslogs on squid which is stored in unix time.
 * @author mark
 */
public class DateTimeParam {

        private String date;


        public DateTimeParam(String date){
            this.date=date;
        }

        /**
         * Returns UTC date
         * @return
         */
        public DateTime getDateTime(){
               Integer year = Integer.parseInt(date.substring(0,4));
               Integer month = Integer.parseInt(date.substring(4,6));
               Integer day = Integer.parseInt(date.substring(6,8));
               //get the date in default time zone
               return  new DateTime (year,month,day,0,0,0,0);
               //convert to UTC/Unix time
               //eturn date.toDateTime(DateTimeZone.UTC);

        }
}
