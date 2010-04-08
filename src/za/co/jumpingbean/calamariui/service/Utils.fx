/*
 * Utils.fx
 *
 * Created on 07 Apr 2010, 8:48:51 PM
 */

package za.co.jumpingbean.calamariui.service;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * @author mark
 */


public static function formatDate(date:GregorianCalendar):String{
   var format:SimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
   format.format(date.getTime());
}


public static function formatDatePrettyPrint(date:GregorianCalendar):String{
   var format:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
   format.format(date.getTime());
}