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
             getChartData(startDate,endDate,count,data,"dataservice/topsitesbyhits","hits");
        }

        function getChartData(startDate:GregorianCalendar,endDate:GregorianCalendar,count:Integer,data:ListWrapper,url:String,type:String){
            def begin = Utils.formatDate(startDate);
            def end = Utils.formatDate(endDate);
            def parser = ChartDataPointParser{};
            println("{baseUrl}/{url}/{begin}/{end}/{count}");
            var request=UIHttpRequest{
                location: "{baseUrl}/{url}/{begin}/{end}/{count}";
                parser: parser;
                onDone: function(){
                   if (sizeof parser.list>0){
                    for (point in parser.list){
                        var tmpData =PieChart.Data{
                            label:point.name.replace("http://","");
                        }
                        if(type=="hits") {
                             tmpData.value=point.hits;
                           }else{
                             tmpData.value=point.bytes/1024/1024;
                        }
                        tmpData.action=function(){ tmpData.explodeFactor=2 }
                        insert tmpData into data.list;
                    }
                    data.done=true;
                  }else{
                    data.done=false;
                  }
                }
            }
            request.start();
        }

        public function getTopSitesBySize(startDate:GregorianCalendar,endDate:GregorianCalendar,count:Integer,data:ListWrapper){
             getChartData(startDate,endDate,count,data,"dataservice/topsitesbysize","bytes");
        }

        public function getTopUsersBySize(startDate:GregorianCalendar,endDate:GregorianCalendar,count:Integer,data:ListWrapper){
             getChartData(startDate,endDate,count,data,"dataservice/topusersbysize","bytes");
        }

        public function getTopUsersByHits(startDate:GregorianCalendar,endDate:GregorianCalendar,count:Integer,data:ListWrapper){
             getChartData(startDate,endDate,count,data,"dataservice/topusersbyhits","hits");
        }
}
