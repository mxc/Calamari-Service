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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.VPos;
import javafx.scene.chart.PieChart3D;
import javafx.geometry.HPos;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.control.ProgressIndicator;

/**
 * @author mark
 */

def service:DataService= DataService{};
var startDate:GregorianCalendar= new GregorianCalendar(2010,2,31);
var endDate:GregorianCalendar= new GregorianCalendar(2010,2,31);
var count:Integer=10;

var topSitesHits = ListWrapper{};
var topSitesBytes = ListWrapper{};
var topUsersHits = ListWrapper{};
var topUsersBytes=ListWrapper{};
var scale=0.9;

//Determines when we can stop the poller timeline object.
var pollerDone=false on replace oldvalue{
            if (pollerDone==true){
                poller.stop();
                println("stoping poller...");
            }
     };

//This code will poll the web service every 10s until a result is received
//To start the poller again the pollerDone variable must be set to false
// and the poller play() function called.

def poller = Timeline{
    repeatCount: Timeline.INDEFINITE
    keyFrames: [
      KeyFrame{
                time: 5s
                action: function(){
                   println("polling ....");
                   if (not topSitesHits.done) service.getTopSitesByHits(startDate, endDate, count, topSitesHits);
                   if (not topSitesBytes.done) service.getTopSitesBySize(startDate, endDate, count, topSitesBytes);
                   if (not topUsersBytes.done) service.getTopUsersBySize(startDate, endDate, count, topUsersBytes);
                   if (not topUsersHits.done) service.getTopUsersByHits(startDate, endDate, count, topUsersHits);
                   if (not (topSitesHits.done and topSitesBytes.done and topUsersBytes.done and topUsersHits.done)) pollerDone = true;
                }
              }
    ]
}

def progressIndicator = ProgressIndicator{progress: -1}

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

public function startPoller(){
                pollerDone=false;
                topSitesBytes.reset();
                topSitesHits.reset();
                topUsersBytes.reset();
                topUsersHits.reset();
                poller.play();
}


function run(){
poller.play();
var chartArea:HBox;

Stage {
    title: "Calamari - Yummy Squid"
    scene: Scene {
        width: 1000
        height: 1000
        content: [
         VBox{
                vpos:VPos.TOP
                hpos:HPos.CENTER
                content:[
                    CriteriaControls{
                        startDate:startDate
                        endDate:endDate
                    }
                    chartArea = HBox{
                          hpos:HPos.LEFT
                          content:[
                           progressIndicator,
                           PieChart3D{
                                    data: bind topSitesHits.list;
                                    title:"Top Sites By Hits from {Utils.formatDatePrettyPrint(startDate)} to {Utils.formatDatePrettyPrint(endDate)}";
                                    //chartBackgoundFill:gradientFill;
                                    pieLabelFill:Color.BLACK;
                                    legendVisible:true;
                                    pieLabelVisible:false;
                                    //legendSide:Side.BOTTOM
                                    scaleX: bind scale
                                    scaleY:bind scale
                                    //onMouseClicked:function(event:MouseEvent){scale=2; event.source.scaleY=2;}
                                },
                           PieChart3D{
                                    data: bind topSitesBytes.list;
                                    title:"Top Sites By MBytes from {Utils.formatDatePrettyPrint(startDate)} to {Utils.formatDatePrettyPrint(endDate)}";
                                    pieLabelFill:Color.BLACK;
                                    pieLabelVisible:false;
                                    legendVisible:true;
                                    //legendSide:Side.BOTTOM
                                    scaleX: bind scale
                                    scaleY:bind scale
                                   //onMouseClicked:function(event:MouseEvent){scale=2; event.source.scaleY=2;}
                               }
                           ]
                           },
                      HBox{
                           hpos:HPos.LEFT
                           content:[
                                PieChart3D{
                                    data: bind topUsersHits.list;
                                    //width:800;
                                    //height:800;
                                    title:"Top Users By Hits from {Utils.formatDatePrettyPrint(startDate)} to {Utils.formatDatePrettyPrint(endDate)}";
                                    //chartBackgoundFill:gradientFill;
                                    pieLabelFill:Color.BLACK;
                                    legendVisible:true;
                                    pieLabelVisible:false;
                                    //legendSide:Side.BOTTOM
                                    scaleX: bind scale
                                    scaleY:bind scale
                                    //onMouseClicked:function(event:MouseEvent){scale=2; event.source.scaleY=2;}
                                },
                               PieChart3D{
                                    data: bind topUsersBytes.list;
                                    title:"Top Users By MBytes from {Utils.formatDatePrettyPrint(startDate)} to {Utils.formatDatePrettyPrint(endDate)}";
                                    pieLabelFill:Color.BLACK;
                                    legendVisible:true;
                                    pieLabelVisible:false;
                                    //legendSide:Side.BOTTOM
                                    scaleX: bind scale
                                    scaleY:bind scale
                                   //onMouseClicked:function(event:MouseEvent){scale=2; event.source.scaleY=2;}
                               }
                           ]
                           }
                     ]
               }
            ]
         }
    }
}














