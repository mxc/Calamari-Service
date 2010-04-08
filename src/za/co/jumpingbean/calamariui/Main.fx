/*
 * Main.fx
 *  http://www.jumpingbean.co.za
 * Created on 04 Apr 2010, 12:33:49 PM
 */

package za.co.jumpingbean.calamariui;

import javafx.stage.Stage;
import javafx.scene.Scene;
import za.co.jumpingbean.calamariui.service.ListWrapper;
import za.co.jumpingbean.calamariui.service.DataService;
import java.util.GregorianCalendar;
import javafx.scene.chart.PieChart;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;
import javafx.scene.chart.part.Side;
import za.co.jumpingbean.calamariui.service.Utils;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author mark
 */

def service:DataService= DataService{};
var startDate:GregorianCalendar= new GregorianCalendar(2010,2,31);
var endDate:GregorianCalendar= new GregorianCalendar(2010,2,31);
var count:Integer=10;

//var list:PieChart.Data[];
var topSitesHits = ListWrapper{};
var topSitesBytes = ListWrapper{};
var topUsersHits = ListWrapper{};
var topUsersBytes=ListWrapper{};


def gradientFill:LinearGradient =  LinearGradient {
            startX: 0
            startY: 0
            endX: 1
            endY: 1
            stops: [
                       Stop {
                               offset: 0.0
                               color: Color.ALICEBLUE;
                       }

                       Stop{
                               offset: 1.0
                               color: Color.ANTIQUEWHITE;
                       }
                    ]
 }

//var scale=1;

Stage {
    title: "Calamari - Yummy Squid"
    scene: Scene {
        width: 1000
        height: 1000
        content: [
         VBox{
             content:[
                      HBox{
                          content:[
                           PieChart{
                                    data: bind topSitesHits.list;
                                    //width:800;
                                    //height:800;
                                    title:"Top Sites By Hits from {Utils.formatDatePrettyPrint(startDate)} to {Utils.formatDatePrettyPrint(endDate)}";
                                    //chartBackgoundFill:gradientFill;
                                    pieLabelFill:Color.BLACK;
                                    legendVisible:true;
                                    legendSide:Side.BOTTOM
                                    //scaleX: bind scale
                                    //scaleY:bind scale
                                    //onMouseClicked:function(event:MouseEvent){scale=2; event.source.scaleY=2;}
                                },
                           PieChart{
                                    data: bind topSitesBytes.list;
                                    title:"Top Sites By MBytes from {Utils.formatDatePrettyPrint(startDate)} to {Utils.formatDatePrettyPrint(endDate)}";
                                    pieLabelFill:Color.BLACK;
                                    legendVisible:true;
                                    legendSide:Side.BOTTOM
                                    //scaleX: bind scale
                                    //scaleY:bind scale
                                   //onMouseClicked:function(event:MouseEvent){scale=2; event.source.scaleY=2;}
                               }
                           ]
                           },
                      HBox{
                           content:[
                                PieChart{
                                    data: bind topUsersHits.list;
                                    //width:800;
                                    //height:800;
                                    title:"Top Users By Hits from {Utils.formatDatePrettyPrint(startDate)} to {Utils.formatDatePrettyPrint(endDate)}";
                                    //chartBackgoundFill:gradientFill;
                                    pieLabelFill:Color.BLACK;
                                    legendVisible:true;
                                    legendSide:Side.BOTTOM
                                   // scaleX: bind scale
                                   // scaleY:bind scale
                                    //onMouseClicked:function(event:MouseEvent){scale=2; event.source.scaleY=2;}
                                },
                               PieChart{
                                    data: bind topUsersBytes.list;
                                    title:"Top Users By MBytes from {Utils.formatDatePrettyPrint(startDate)} to {Utils.formatDatePrettyPrint(endDate)}";
                                    pieLabelFill:Color.BLACK;
                                    legendVisible:true;
                                    legendSide:Side.BOTTOM
                                   // scaleX: bind scale
                                   // scaleY:bind scale
                                   //onMouseClicked:function(event:MouseEvent){scale=2; event.source.scaleY=2;}
                               }
                           ]
                           }
                     ]
               }
            ]
         }
    }


//getData()

//function getData(startDate:GregorianCalendar,endDate:GregorianCalendar,count:Integer){
    service.getTopSitesByHits(startDate, endDate, count, topSitesHits);
    service.getTopSitesBySize(startDate, endDate, count, topSitesBytes);
    service.getTopUsersBySize(startDate, endDate, count, topUsersBytes);
    service.getTopUsersByHits(startDate, endDate, count, topSitesHits);


//}












