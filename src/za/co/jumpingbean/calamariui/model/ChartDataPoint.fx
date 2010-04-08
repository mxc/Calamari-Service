/*
 * ChartDataPoint.fx
 *
 * Created on 05 Apr 2010, 6:43:13 PM
 */

package za.co.jumpingbean.calamariui.model;

/**
 * @author mark
 */

public class ChartDataPoint {
        public var name:String;
        public var hits:Integer;
        public var bytes:Integer;

        override function toString():String{
            return "name:{name},hits:{hits},bytes{bytes}";
        }

}
