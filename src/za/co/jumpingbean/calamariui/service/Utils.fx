/*
 * Utils.fx
 *
 * Created on 07 Apr 2010, 8:48:51 PM
 */

package za.co.jumpingbean.calamariui.service;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.sql.Timestamp;
import java.lang.StringBuilder;
import java.util.Date;

/**
 * @author mark
 */

public static function timestampFromString(date:String):Timestamp{
        var format:SimpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss");
        /*var tmpDate:String;
        if ( input.endsWith( "Z" ) ) {
            tmpDate = input.substring( 0, input.length() - 1);
            tmpDate = "{tmpDate}GMT-00:00";
        } else {
            var inset:Integer = 6;
            var string1 =input.substring( 0, input.length() - inset );
            var string2 = input.substring( input.length() - inset, input.length() );
            tmpDate = "{string1}GMT{string2}";
        }*/
        //println(date);
        var tmpDate =date;
        if (date.lastIndexOf('.')>0){
            tmpDate = date.substring(0,date.lastIndexOf('.'));
        }else if (date.lastIndexOf('+')>0){
            tmpDate = date.substring(0,date.lastIndexOf('+'));
        }
        var tmpDate2 = format.parse(tmpDate);
        return new Timestamp(tmpDate2.getTime());
}



public static function formatDate(date:GregorianCalendar):String{
   var format:SimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
   format.format(date.getTime());
}


public static function formatDatePrettyPrint(date:GregorianCalendar):String{
   var format:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
   format.format(date.getTime());
}

// Taken from jfxtra and modified.

public static function toClassCamelCase(s:String){
    if (s.length() == 0) return s;
    def chars = s.toCharArray();
    def result = StringBuilder{};
    var capNextChar = false;
    var counter:Integer=0;
    for (char in chars) {
        if (counter==0 and Character.isLetter(char)) {
           result.append(char.toLowerCase(char));
        } else if (Character.isLetter(char) and capNextChar) {
            result.append(Character.toUpperCase(char));
            capNextChar = false;
        } else if (char == 0x20 /* ' ' */ or char == 0x3A /* ':' */ or char == 0x5F /* '_' */ or char == 0x2F /* '/' */) {
            if (indexof char > 0) {
                capNextChar = true;
            }
        } else {
            result.append(char);
            capNextChar = false;
        }
        counter++;
    }
    return result.toString();
}

public static function getDateFromTimestamp(timestamp:Timestamp):Date{
    var ms:Long = timestamp.getTime() + (timestamp.getNanos() / 1000000);
    return new Date(ms);
}

//Getting tired of all this date stuff
public static function toDate(date:String):Date{
         var format:SimpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
         return format.parse(date);
}


