/*
 * DataService.fx
 *
 * Created on 05 Apr 2010, 6:44:31 PM
 */

package za.co.jumpingbean.calamariui.service;

import java.util.GregorianCalendar;
import javafx.scene.chart.PieChart;

/**
 * @author mark
 */

public class DataService {

       def baseUrl="http://127.0.0.1:9998";

        public function getTopSitesByHits(startDate:GregorianCalendar,endDate:GregorianCalendar,count:Integer,data:ListWrapper){
             getChartData(startDate,endDate,count,data,"dataservice/topsitesbyhits");
        }

        function getChartData(startDate:GregorianCalendar,endDate:GregorianCalendar,count:Integer,data:ListWrapper,url:String){
            def begin = Utils.formatDate(startDate);
            def end = Utils.formatDate(endDate);
            def parser = ChartDataPointParser{};
            println("{baseUrl}/{url}/{begin}/{end}/{count}");
            var request=UIHttpRequest{
                location: "{baseUrl}/{url}/{begin}/{end}/{count}";
                parser: parser;
                onDone: function(){
                    for (point in parser.list){
                        var tmpData =PieChart.Data{
                            label:point.name.replace("http://","");
                            value:point.hits/1024;
                        }
                        insert tmpData into data.list;
                    }
                }
            }
            request.start();
        }

        public function getTopSitesBySize(startDate:GregorianCalendar,endDate:GregorianCalendar,count:Integer,data:ListWrapper){
             getChartData(startDate,endDate,count,data,"dataservice/topsitesbysize");
        }

        public function getTopUsersBySize(startDate:GregorianCalendar,endDate:GregorianCalendar,count:Integer,data:ListWrapper){
             getChartData(startDate,endDate,count,data,"dataservice/topusersbysize");
        }

        public function getTopUsersByHits(startDate:GregorianCalendar,endDate:GregorianCalendar,count:Integer,data:ListWrapper){
             getChartData(startDate,endDate,count,data,"dataservice/topusersbyhits");
        }
}
