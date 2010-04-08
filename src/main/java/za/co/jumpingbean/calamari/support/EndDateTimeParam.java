/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.jumpingbean.calamari.support;

import org.joda.time.DateTime;

/**
 *
 * @author mark
 */
public class EndDateTimeParam {

        private String date;


        public EndDateTimeParam(String date){
            this.date=date;
        }

        public DateTime getDateTime(){
               Integer year = Integer.parseInt(date.substring(0,4));
               Integer month = Integer.parseInt(date.substring(4,6));
               Integer day = Integer.parseInt(date.substring(6,8));
               return new DateTime (year,month,day,23,59,59,999);
        }
}
