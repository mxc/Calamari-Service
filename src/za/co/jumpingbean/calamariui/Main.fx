/*
 * Main.fx
 *  http://www.jumpingbean.co.za
 * Created on 04 Apr 2010, 12:33:49 PM
 */

package za.co.jumpingbean.calamariui;

import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.GregorianCalendar;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;
import za.co.jumpingbean.calamariui.chartDisplay.ChartDisplay;
import za.co.jumpingbean.calamariui.tabularDisplay.TabularDisplay;
import za.co.jumpingbean.calamariui.adminDisplay.AdminDisplay;
import za.co.jumpingbean.calamariui.common.DisplaySelector;
import javafx.async.RunnableFuture;
import javafx.async.JavaTaskBase;

/**
 * @author mark
 */


var startDate:GregorianCalendar= new GregorianCalendar(2010,2,31);
var endDate:GregorianCalendar= new GregorianCalendar(2010,2,31);
var scene:Scene;

static public var asyncTaksInProgress:Boolean;

//Our eye candy gradient fill
def gradientFill:LinearGradient =  LinearGradient {
            startX: 0
            startY: 0
            endX: 1
            endY: 1
            stops: [
                       Stop {
                               offset: 0.0
                               color: Color.CORNFLOWERBLUE;
                       }

                       Stop{
                               offset: 1.0
                               color: Color.ALICEBLUE;
                       }
                    ]
 }


//Build our Pie Chart Scene
def chartControl = ChartDisplay{
    startDate:startDate;
    endDate:endDate;
    width:bind scene.width
    height:bind scene.height
}

def tabularControl = TabularDisplay{
    startDate:startDate
    endDate:endDate
    width:bind scene.width
    height:bind scene.height
}

def displaySelector=DisplaySelector{default:DisplaySelector.chartDisplay}

def adminControl = AdminDisplay{
}


//Use this function to hide/show panels
public function showTabularDisplay(reportType:String,parameter:String){
    var runFlag:Boolean=false;
    if (tabularControl.reportType!=reportType) {
            tabularControl.reportType=reportType;
            runFlag=true;
    }
    if (tabularControl.reportParameter!=parameter){
           tabularControl.reportParameter=parameter;
           runFlag=true;
    }
    chartControl.removeDisplaySelector(displaySelector);
    adminControl.removeDisplaySelector(displaySelector);
    tabularControl.insertDisplaySelector(displaySelector);
    delete chartControl from scene.content;
    delete adminControl from scene.content;
    insert tabularControl into scene.content;
    if (runFlag) tabularControl.startPoller();
}

//Use this function to hide/show panels
public function showChartDisplay(){
    adminControl.removeDisplaySelector(displaySelector);
    tabularControl.removeDisplaySelector(displaySelector);
    chartControl.insertDisplaySelector(displaySelector);
    delete tabularControl from scene.content;
    delete adminControl from scene.content;
    insert chartControl into scene.content;
}

public function showAdminDisplay(){
    tabularControl.removeDisplaySelector(displaySelector);
    chartControl.removeDisplaySelector(displaySelector);
    adminControl.insertDisplaySelector(displaySelector);
    delete tabularControl from scene.content;
    delete chartControl from scene.content;
    insert adminControl into scene.content;
}


function run(){
    chartControl.insertDisplaySelector(displaySelector);//insert the display selector on startup
Stage {
    title: "Calamari - Yummy Squid"
    scene: scene = Scene {
        fill:gradientFill
        content: [
                    chartControl
                 ]
         }
    }
chartControl.startPoller();
}














