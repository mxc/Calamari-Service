/*
 * DataService.fx
 *
 * Created on 05 Apr 2010, 6:44:31 PM
 */

package za.co.jumpingbean.calamariui.service;

import java.util.GregorianCalendar;
import javafx.scene.chart.PieChart;
import za.co.jumpingbean.calamariui.Main;
import za.co.jumpingbean.calamariui.tabularDisplay.TabularDisplay;
import org.jfxtras.util.XMap;
import javafx.io.http.HttpRequest;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import javafx.io.http.HttpHeader;
import javafx.io.http.URLConverter;
import javafx.data.Pair;
import java.lang.Exception;

/**
 * @author mark
 */

public class DataService {

       var main:Main;

       def baseUrl="http://192.168.1.216:9998";

        public function getTopSitesByHits(startDate:GregorianCalendar,endDate:GregorianCalendar,count:Integer,data:ChartDataListWrapper){
             getChartData(startDate,endDate,count,data,"dataservice/topsitesbyhits","hits");
        }

        function getChartData(startDate:GregorianCalendar,endDate:GregorianCalendar,count:Integer,data:ChartDataListWrapper,url:String,type:String){
            def begin = Utils.formatDate(startDate);
            def end = Utils.formatDate(endDate);
            def parser = ChartDataPointParser{};
            println("{baseUrl}/{url}/{begin}/{end}/{count}");
            var request:UIHttpRequest=UIHttpRequest{
                location: "{baseUrl}/{url}/{begin}/{end}/{count}";
                parser: parser;
                onException:function(ex:Exception){
                       data.error=true;
                       data.errorMessage=ex.getMessage();
                }
                onDone: function(){
                   if (data.error){
                        data.done=true;
                   }else if (sizeof parser.list>0){
                    for (point in parser.list){
                        var tmpData =PieChart.Data{
                            label:point.name.replace("http://","");
                        }
                        if(type=="hits") {
                             tmpData.value=point.hits;
                           }else{
                             tmpData.value=point.bytes/(1024*1024);
                        }
                        if (url.indexOf("site")!=-1) {
                                tmpData.action=function(){ main.showTabularDisplay(TabularDisplay.reportDomainDetail,tmpData.label);};
                        }
                        else {
                                tmpData.action=function(){ main.showTabularDisplay(TabularDisplay.reportUserDetail,tmpData.label);};
                        }
                        insert tmpData into data.list;
                    }
                    data.done=true;
                    data.error=false;
                  }else{
                    data.done=true;
                    data.error=false;
                  }
                }
            }
            request.start();
        }

        public function getTopSitesBySize(startDate:GregorianCalendar,endDate:GregorianCalendar,count:Integer,data:ChartDataListWrapper){
             getChartData(startDate,endDate,count,data,"dataservice/topsitesbysize","bytes");
        }

        public function getTopUsersBySize(startDate:GregorianCalendar,endDate:GregorianCalendar,count:Integer,data:ChartDataListWrapper){
             getChartData(startDate,endDate,count,data,"dataservice/topusersbysize","bytes");
        }

        public function getTopUsersByHits(startDate:GregorianCalendar,endDate:GregorianCalendar,count:Integer,data:ChartDataListWrapper){
             getChartData(startDate,endDate,count,data,"dataservice/topusersbyhits","hits");
        }

        public function getUserDetails(startDate:GregorianCalendar,endDate:GregorianCalendar,username:String,data:SquidLogRecordListWrapper){
             getSquidLogDetailData(startDate,endDate,"dataservice/userdetails",username,data);
        }

        public function getDetails(startDate:GregorianCalendar,endDate:GregorianCalendar,data:SquidLogRecordListWrapper){
             getSquidLogDetailData(startDate,endDate,"dataservice/details",null,data);
        }

        public function getDomainDetails(startDate:GregorianCalendar,endDate:GregorianCalendar,domain:String,data:SquidLogRecordListWrapper){
             getSquidLogDetailData(startDate,endDate,"dataservice/domaindetails",domain,data);
        }

        public function getContentTypeDetails(startDate:GregorianCalendar,endDate:GregorianCalendar,contentType:String,data:SquidLogRecordListWrapper){
             getSquidLogDetailData(startDate,endDate,"dataservice/contenttypedetails",contentType,data);
        }


        function getSquidLogDetailData(startDate:GregorianCalendar,endDate:GregorianCalendar,url:String,param:String,data:SquidLogRecordListWrapper){
            def begin = Utils.formatDate(startDate);
            def end = Utils.formatDate(endDate);
            def parser = SquidLogRecordParser{};
            var tmpUrl = "{baseUrl}/{url}/{begin}/{end}";
            if (param!=null and param!="") tmpUrl="{tmpUrl}/{param}";
            println("{tmpUrl}");
            var request:UIHttpRequest=UIHttpRequest{
                location: tmpUrl;
                parser: parser;
                onException:function(ex:Exception){
                       data.error=true;
                       data.errorMessage=ex.getMessage();
                }
                onDone: function(){
                   if ( data.error){
                        data.done=true;
                   }else if (sizeof parser.list>0){
                    data.list=parser.list;
                    data.done=true;
                    data.error=false;
                  }else{
                    data.done=true;
                    data.error=false;
                  }
                }
            }
            request.start();
        }

        public function saveAdminData(location:String,result:StringResultWrapper){
            def urlConverter = URLConverter{};
            def encodedMessage = urlConverter.encodeParameters(Pair{name:"path" value:location});
            //def pair = Pair{name:"path" value:location}
            //def encodedMessageSize: Integer = encodedMessage.getBytes().length;
            var tmpUrl = "{baseUrl}/admin/settings/squidlogfolder?{encodedMessage}";
            def request:HttpRequest=HttpRequest{
                location: tmpUrl
                method:HttpRequest.POST
                headers: [
                HttpHeader {
                name: HttpHeader.CONTENT_TYPE;
                value: "text/plain";
                },
                //HttpHeader {
                //name: HttpHeader.CONTENT_LENGTH;
                //value: "{encodedMessageSize}";
                //}
                ];
                onOutput:function(os:OutputStream){
                     //println("writing...{pair.toString()}");
                     //def string="posting...";
                     //os.write(string.getBytes());
                     os.close();
                }
                onException:function(ex:Exception){
                    result.result=ex.getMessage();
                    result.errorMessage=ex.getMessage();
                }
                onInput:function(is:InputStream){
                    result.result=readInputBuffer(is);
                    println("{tmpUrl}--{result.result}");
               }
                onDone: function(){
                   println("done...");
                   if (request.error!=null){
                       println("error---{result.result}");
                       result.error=true;
                       result.done=true;
                       //result.result=readInputBuffer(request.error);
                    }else{
                        result.result="success";
                        result.done=true;
                    }
                    println("result---{result.result}");
                }
            }
            request.start();
        }


        public function getAdminData(url:String,param:String,data:StringResultWrapper){
            var result:String;
            var tmpUrl = "{baseUrl}/{url}";
            if (param!=null) tmpUrl="{tmpUrl}/{param}";
            var request:HttpRequest=HttpRequest{
                location: tmpUrl
                onInput:function (is:InputStream){
                    result=readInputBuffer(is);
                    data.result=result;
                    data.done=true;
                }
                onDone: function(){
                   if (request.error!=null){
                       result=readInputBuffer(request.error);
                       data.result=result;
                       data.error=true;
                    }
                }
            }
            request.start();
        }

        function readInputBuffer(is:InputStream){
            var byte:Integer;
            var byteArray = new ByteArrayOutputStream();
            while ((byte= is.read())!=-1){
                byteArray.write(byte);
            }
            is.close();
            return byteArray.toString();
        }


}
