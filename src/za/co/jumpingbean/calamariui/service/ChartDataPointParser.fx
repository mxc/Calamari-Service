/*
 * ChartDataPointParser.fx
 *
 * Created on 05 Apr 2010, 7:45:33 PM
 */

package za.co.jumpingbean.calamariui.service;

import javafx.data.pull.PullParser;
import java.io.InputStream;
import javafx.data.pull.Event;
import za.co.jumpingbean.calamariui.model.ChartDataPoint;


/**
 * @author mark
 */

public class ChartDataPointParser extends AbstractParser {

    public-read var list:ChartDataPoint[];
    var chartDataPoint:ChartDataPoint;

    override function onEvent(event:Event){
            if (event.level==1 and event.type==PullParser.START_ELEMENT){
                chartDataPoint = ChartDataPoint{};
            }else if (event.level==2 and event.type==PullParser.END_ELEMENT){
                if (event.qname.name=="name") {
                        chartDataPoint.name = event.text;
                } else if (event.qname.name=="bytes") {
                        chartDataPoint.bytes=Integer.parseInt(event.text);
                }else if (event.qname.name=="hits"){
                        chartDataPoint.hits=Integer.parseInt(event.text);
                }
            }else if (event.level==1 and event.type==PullParser.END_ELEMENT){
                insert chartDataPoint into list;
            }
    }

}
